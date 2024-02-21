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
package ru.biatech.edt.ordinaryapp.ui;

import com._1c.g5.v8.dt.internal.launching.ui.launchconfigurations.shortcuts.AbstractRuntimeClientLaunchShortcut;

public class OrdinaryAppShortcut extends AbstractRuntimeClientLaunchShortcut {

	public static final String ORDINARY_APP_COMPONENT_TYPE = "ru.biatech.edt.ordinaryapp.OrdinaryClient";
   
	public OrdinaryAppShortcut() {
    }

    protected String getRuntimeComponentTypeId() {
        return ORDINARY_APP_COMPONENT_TYPE;
    }

    protected String getLaunchConfigurationSelectionTitle() {
        return Messages.OrdinaryAppShortcut_ShortCutTitle;
    }

    protected String getNameSuffix() {
        return Messages.OrdinaryAppShortcut_ShortCutName;
    }
}