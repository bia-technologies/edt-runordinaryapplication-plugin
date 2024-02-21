/*
 * Copyright 2022 BIA-Technologies Limited Liability Company
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.biatech.edt.ordinaryapp.core;

import com._1c.g5.v8.dt.platform.services.core.runtimes.RuntimeInstallations;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.IRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.AbstractFileRuntimeComponentResolver;
import com._1c.g5.v8.dt.platform.services.model.RuntimeInstallation;
import org.eclipse.core.runtime.CoreException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

public class OrdinaryAppComponentResolver extends AbstractFileRuntimeComponentResolver {

    public static final String ORDINARY_APP_COMPONENT_TYPE = "ru.biatech.edt.ordinaryapp.OrdinaryClient";
    public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
    public static final String ORDINARY_APP_NAME = IS_WINDOWS ? "1cv8.exe": "1cv8";
    
    private Logger logger;
    
    public OrdinaryAppComponentResolver() {
    	logger = new Logger();
    }
    
    public Collection<IRuntimeComponent> resolveComponents(RuntimeInstallation installation) throws CoreException {
        Collection<IRuntimeComponent> result = new ArrayList<>();
        URI location = installation.getLocation();
        
        logger.debug("Analyze location" + location);
        
        if (location != null && RuntimeInstallations.isFile(location)) {
            ILaunchableRuntimeComponent thickClient = this.resolveLaunchable(installation, ORDINARY_APP_NAME, ORDINARY_APP_COMPONENT_TYPE);
            if (thickClient != null) {
                result.add(thickClient);
            }
        }
        
        return result;
    }
}
