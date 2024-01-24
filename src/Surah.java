/**
 * The {@code Surah} class represents a chapter (Surah) in the Quran.
 * Each Surah is identified by a unique number and has a corresponding name.
 * This class provides methods to retrieve the number and name of a Surah.
 * Additionally, the {@code toString} method is overridden to return the name of the Surah.
 *
 * @author Arish Shahab
 * @version 1.0
 */
public class Surah {
    // Fields
    /**
     * The unique number identifying the Surah.
     */
    private final int number;

    /**
     * The name of the Surah.
     */
    private final String name;

    // Constructors
    /**
     * Constructs a new Surah with the specified number and name.
     *
     * @param number The unique number identifying the Surah.
     * @param name   The name of the Surah.
     */
    public Surah(int number, String name) {
        this.number = number;
        this.name = name;
    }

    // Getter Methods
    /**
     * Gets the number of the Surah.
     *
     * @return The unique number identifying the Surah.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the name of the Surah.
     *
     * @return The name of the Surah.
     */
    public String getName() {
        return name;
    }

    // Overridden Methods
    /**
     * Returns the name of the Surah.
     *
     * @return The name of the Surah.
     */
    @Override
    public String toString() {
        return name;
    }
}
