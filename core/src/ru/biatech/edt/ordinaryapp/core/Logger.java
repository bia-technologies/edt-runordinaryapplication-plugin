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

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;

public class Logger {
	private static String PLUGIN_ID = "ru.biatech.edt.ordinaryapp.core";
	ILog log = Platform.getLog(Platform.getBundle(PLUGIN_ID));

	public void debug(String message) {
		log.info(message);
	}
}
