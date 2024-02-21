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

import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.ILaunchableRuntimeComponent;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.RuntimeExecutionArguments;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.RuntimeExecutionException;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.RuntimeExecutionCommandBuilder;
import com._1c.g5.v8.dt.platform.services.core.runtimes.execution.impl.ThickClientLauncher;
import com._1c.g5.v8.dt.platform.services.model.ClientType;
import com._1c.g5.v8.dt.platform.services.model.InfobaseReference;
import com._1c.g5.v8.dt.platform.services.model.RuntimeInstallation;
import com._1c.g5.v8.dt.platform.services.model.VersionWithBuild;
import com.google.common.base.Preconditions;

import java.io.File;
import java.io.IOException;

public class OrdinaryAppLauncher extends ThickClientLauncher {
    @Override
    public Process runClient(ILaunchableRuntimeComponent thickClient, InfobaseReference infobase, RuntimeExecutionArguments arguments) throws RuntimeExecutionException {
        Preconditions.checkNotNull(arguments);
        File launchFile = thickClient.getFile();
        RuntimeExecutionCommandBuilder command = new RuntimeExecutionCommandBuilder(launchFile, RuntimeExecutionCommandBuilder.ThickClientMode.ENTERPRISE) {
            @Override
            public RuntimeExecutionCommandBuilder clientType(ClientType clientType) {
                if (clientType == ClientType.THICK_CLIENT) {
                    this.appendOption("RunModeOrdinaryApplication");
                }
                return this;
            }
        };
        command.forInfobase(infobase, this.splitInfobaseConnection()).clientType(ClientType.THICK_CLIENT).enableCheckModal();
        this.appendInfobaseAccess(command, arguments);
        this.appendClientArguments(command, arguments);
        this.appendRuntimeClientArguments(command, arguments);

        try {
            return command.start();
        } catch (IOException var9) {
            RuntimeInstallation installation = thickClient.getInstallation();
            String versionWithBuild = VersionWithBuild.toVersionWithBuild(installation.getVersion(), installation.getBuild());
            throw new RuntimeExecutionException(var9, versionWithBuild);
        }
    }
}
