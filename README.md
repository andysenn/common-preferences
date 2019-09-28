# co.senn.common:preferences

An extensible wrapper of the Java Preferences API

The purpose of this project is to extend the functionality of the existing Java Preferences API to reduce boilerplate
code necessary to deal with non-```String``` preferences.

## Preference Types

The following preference types are supported out-of-the-box:

- ```Boolean```
- ```Byte```
- ```Double```
- ```Float```
- ```Integer```
- ```List<T>```
- ```Long```
- ```Short```
- ```String```

## Examples

### Basic Usage

```java
import co.senn.common.preference.impl.*;
import co.senn.common.preference.Preferences;

public class PreferencesExample {
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(PreferencesExample.class);
	private static final BooleanPreference MY_BOOLEAN_PREFERENCE = new BooleanPreference("myBooleanPref", false);
	
	public boolean getMyBooleanPreference() {
		return PREFERENCES.get(MY_BOOLEAN_PREFERENCE);
	}
	
	public void setMyBooleanPreference(boolean value) {
		PREFERENCES.set(MY_BOOLEAN_PREFERENCE, value);
		PREFERENCES.tryFlush();
	}
	
}
```

### Transactions

```java
import co.senn.common.preference.impl.*;
import co.senn.common.preference.Preferences;

public class PreferencesExample {
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(PreferencesExample.class);
	private static final BooleanPreference MY_BOOLEAN_PREFERENCE = new BooleanPreference("myBooleanPref", false);
	
	public boolean getMyBooleanPreference() {
		return PREFERENCES.get(MY_BOOLEAN_PREFERENCE);
	}
	
	public void setMyBooleanPreference(boolean value) {
		PREFERENCES.transaction(() -> {
			PREFERENCES.set(MY_BOOLEAN_PREFERENCE, value);
		});
	}
	
}