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
package co.senn.common.preference

import co.senn.common.preference.exception.PreferenceValidationException
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences as JavaPreferences

class Preferences private constructor(private val preferences : JavaPreferences) {
	
	private val uncommitted = HashMap<String, String>()
	
	fun <T> get(preference : Preference<T>) : T {
		return preference.decoder.invoke(preferences.get(preference.name, preference.encodedDefault))
	}
	
	fun <T> set(preference : Preference<T>, value : T) {
		synchronized(preferences) {
			if (preference.validator.invoke(value)) {
				preferences.put(preference.name, preference.encoder.invoke(value))
			} else {
				throw PreferenceValidationException(preference, value)
			}
		}
	}
	
	@Throws(BackingStoreException::class)
	fun flush() {
		preferences.flush()
	}
	
	fun tryFlush() = try {
		preferences.flush()
		true
	} catch (e : BackingStoreException) {
		false
	}
	
	private fun rollback() {
		uncommitted.clear()
	}
	
	@Throws(BackingStoreException::class)
	private fun commit() {
		uncommitted.forEach(preferences::put)
		uncommitted.clear()
		preferences.flush()
	}
	
	@Throws(BackingStoreException::class)
	fun clear() {
		preferences.clear()
	}
	
	@Throws(BackingStoreException::class)
	fun transaction(block : () -> Unit) {
		synchronized(preferences) {
			val stash = HashMap<String, String>(uncommitted)
			rollback()
			try {
				block()
				commit()
			} catch (t : Throwable) {
				rollback()
				throw t
			}
			uncommitted.putAll(stash)
		}
	}
	
	companion object {
		fun systemNodeForPackage(clazz : Class<*>) : Preferences {
			return Preferences(JavaPreferences.systemNodeForPackage(clazz))
		}
		
		fun systemRoot() : Preferences {
			return Preferences(JavaPreferences.systemRoot())
		}
		
		fun userNodeForPackage(clazz : Class<*>) : Preferences {
			return Preferences(JavaPreferences.userNodeForPackage(clazz))
		}
		
		fun userRoot() : Preferences {
			return Preferences(JavaPreferences.userRoot())
		}
	}
	
}