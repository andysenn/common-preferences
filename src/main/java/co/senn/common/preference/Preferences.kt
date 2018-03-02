/*-
 * #%L
 * common-preferences
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
package co.senn.common.preference

import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences as JPreferences

class Preferences private constructor(private val preferences : JPreferences) {
	
	fun <T> get(preference : Preference<T>) : T {
		return preference.decoder.invoke(preferences.get(preference.name, preference.encodedDefault))
	}
	
	fun <T> set(preference : Preference<T>, value : T) {
		preferences.put(preference.name, preference.encoder.invoke(value))
	}
	
	@Throws(BackingStoreException::class)
	fun flush() {
		preferences.flush()
	}
	
	@Throws(BackingStoreException::class)
	fun clear() {
		preferences.clear()
	}
	
	companion object {
		fun systemNodeForPackage(clazz : Class<*>) : Preferences {
			return Preferences(java.util.prefs.Preferences.systemNodeForPackage(clazz))
		}
		
		fun systemRoot() : Preferences {
			return Preferences(java.util.prefs.Preferences.systemRoot())
		}
		
		fun userNodeForPackage(clazz : Class<*>) : Preferences {
			return Preferences(java.util.prefs.Preferences.userNodeForPackage(clazz))
		}
		
		fun userRoot() : Preferences {
			return Preferences(java.util.prefs.Preferences.userRoot())
		}
	}
	
}
