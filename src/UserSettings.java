import java.util.prefs.Preferences;

/**
 * The {@code UserSettings} class provides a simple interface for managing user settings
 * using the Java Preferences API. It encapsulates methods for saving, retrieving, and
 * clearing key-value settings in the user's preferences.
 *
 * @author Your Name
 * @version 1.0
 */
public class UserSettings {
    // Fields
    /**
     * The {@code Preferences} object used for storing user settings.
     */
    private Preferences prefs;

    // Constructors
    /**
     * Constructs a new {@code UserSettings} instance with the specified {@code Preferences}.
     *
     * @param prefs The {@code Preferences} object used for storing user settings.
     */
    public UserSettings(Preferences prefs) {
        this.prefs = prefs;
    }

    // Public Methods
    /**
     * Saves a key-value setting in the user's preferences.
     *
     * @param key   The key for the setting.
     * @param value The value to be saved.
     */
    public void saveSetting(String key, String value) {
        prefs.put(key, value);
    }

    /**
     * Retrieves the value of a setting from the user's preferences.
     *
     * @param key The key for the setting.
     * @return The value of the setting, or an empty string if the key is not found.
     */
    public String getSetting(String key) {
        return prefs.get(key, "");
    }

    /**
     * Clears a setting from the user's preferences.
     *
     * @param key The key for the setting to be cleared.
     */
    public void clearSetting(String key) {
        prefs.remove(key);
    }
}
