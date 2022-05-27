import org.apache.tools.ant.taskdefs.condition.Os
import java.util.*

plugins {
    `java-library`
    id("com.github.hierynomus.license") version "0.16.1"
    id("it.filippor.p2") version "0.0.10"
}

group = "ru.biatech.edt.ordinaryapp"
version = "0.1.0"
val vendor = "BIA-Technologies Limited Liability Company"
val createProjectYear = 2022

val licenseYear = if (Calendar.getInstance().get(Calendar.YEAR) == createProjectYear) "$createProjectYear"
    else "$createProjectYear-${Calendar.getInstance().get(Calendar.YEAR)}"
val edtLocation = findProperty("edtLocation") ?: ""
val pluginBuildPath = layout.buildDirectory.dir("buildPlugin").get().asFile
val publishTo = (findProperty("publishTo") ?: "").toString()

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDirs("src/main/java")
        resources.srcDirs("src/main/resources")
        resources.srcDirs("META-INF")
    }
    test {
        java.srcDirs("src/test/java")
        resources.srcDirs("src/test/resources")
    }
}

dependencies {
    implementation(fileTree(edtLocation) { include("*.jar") })
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Copy>() {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

license {
    header = rootProject.file("templates/HEADER.txt")
    ext["year"] = licenseYear
    ext["owner"] = vendor
    useDefaultMappings = false
    includes(listOf("**/*.java", "**/*.properties", "**/*.gradle.kts"))
    strictCheck = true
    mapping("java", "SLASHSTAR_STYLE")
}

tasks.register<com.hierynomus.gradle.license.tasks.LicenseFormat>("licenseEclipseProject") {
    header = rootProject.file("templates/HEADER_FOR_PLUGIN_TEMPLATE.txt")
    ext["year"] = licenseYear
    ext["owner"] = vendor
    source = fileTree("eclipse_project")
    useDefaultMappings = true
    strictCheck = true
    setIncludes(listOf("**/*.properties", "**/*.xml"))
    group = "license"
}

tasks.named("licenseFormat") {
    dependsOn(tasks.named("licenseEclipseProject"))
}

tasks.register<Copy>("buildPlugin-copyFiles") {
    from("eclipse_project") {
        filter { line -> line.replace("{version}", version.toString()) }
    }

    into(pluginBuildPath)

    from(layout.projectDirectory.dir("src/main/java")) {
        into("bundles/ru.biatech.edt.ordinaryapp/src")
    }
    from(layout.projectDirectory.dir("src/main/resources")) {
        into("bundles/ru.biatech.edt.ordinaryapp/resources")
    }
    from(layout.projectDirectory.dir("META-INF")) {
        into("bundles/ru.biatech.edt.ordinaryapp/META-INF")
        filter { line -> line.replace(Regex("Bundle-Version.*"), "Bundle-Version: $version.qualifier") }
    }
    from(layout.projectDirectory.dir("plugin.xml")) {
        into("bundles/ru.biatech.edt.ordinaryapp")
    }
    group = "build"
}

tasks.register<Exec>("buildPlugin") {
    isIgnoreExitValue = true
    workingDir = pluginBuildPath
    standardOutput = System.out

    environment("MAVEN_OPTS", "-Dhttps.protocols=TLSv1.2")

    if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        commandLine("mvn.cmd", "package")
    } else {
        commandLine("mvn", "package")
    }

    dependsOn(tasks.named("buildPlugin-copyFiles"))
    group = "build"
}

tasks.register<Copy>("publishToPath") {
    doFirst {
        if (publishTo == "") {
            throw GradleException("You must specify a property 'publishTo' for the publish task is 'gradle.properties'")
        }
    }
    from("$pluginBuildPath/repositories/ru.biatech.edt.ordinaryapp.repository/target/repository") {
        into("$version")
        into("latest")
    }
    from("$pluginBuildPath/repositories/ru.biatech.edt.ordinaryapp.repository/target/ru.biatech.edt.ordinaryapp.repository.zip") {
        into("$version")
        into("latest")
    }
    into(publishTo)
    group = "publish"
    dependsOn(tasks.named("buildPlugin"))

    doLast {
        print("Published to: $publishTo")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "7.1.1"
}