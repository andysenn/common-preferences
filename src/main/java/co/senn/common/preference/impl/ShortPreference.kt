/*-
 * #%L
 * Senn Common: Preferences
 * %%
 * Copyright (C) 2018 Andy Senn
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package co.senn.common.preference.impl

import co.senn.common.preference.Preference

class ShortPreference(name : String, def : Short, validator : (Short) -> Boolean) : Preference<Short>(name, def, { it.toString() }, { it.toShort() }, validator) {
	constructor(name : String, def : Short) : this(name, def, validAlways)
}