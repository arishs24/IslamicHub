/**
 * The {@code Date} class represents a specific date along with an array of prayer times.
 * It encapsulates the date and associated prayer times for a particular instance.
 *
 * @author Arish Shahab
 * @version 1.0
 */
public class Date {
    // Private Instance Variables
    /**
     * The date for which the prayer times are associated.
     */
    private String date;

    /**
     * An array containing the prayer times associated with the date.
     */
    private String[] prayers;

    // Constructors
    /**
     * Constructs a new {@code Date} instance with the specified date and array of prayer times.
     *
     * @param date    The date for which the prayer times are associated.
     * @param prayers An array containing the prayer times associated with the date.
     */
    public Date(String date, String[] prayers) {
        this.date = date;
        this.prayers = prayers;
    }

    // Public Methods
    /**
     * Gets the date associated with this instance.
     *
     * @return The date for which the prayer times are associated.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the array of prayer times associated with this instance.
     *
     * @return An array containing the prayer times associated with the date.
     */
    public String[] getPrayers() {
        return prayers;
    }
}
