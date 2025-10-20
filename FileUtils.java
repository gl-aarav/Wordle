
/**
 * FileUtils.java
 *
 * Provides utility methods for file operations, specifically for reading
 * and writing text files, and for processing lists of words.
 *
 * @author Aarav Goyal
 * @since September 3, 2025
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner; // Import Random for choosing a random word

public class FileUtils {

    /**
     * Opens a specified file for reading using the `Scanner` class. If the file
     * is not found, an error message is printed to `System.err`, and the
     * program exits.
     *
     * @param fileName The name or path of the file to be opened for reading.
     * @return A `Scanner` object configured to read from the specified file.
     */
    public static java.util.Scanner openToRead(String fileName) {
        java.util.Scanner input = null;

        try {
            // Attempt to create a Scanner for the given file.
            input = new java.util.Scanner(new java.io.File(fileName));
        } catch (java.io.FileNotFoundException e) {
            // If the file is not found, print an error and exit the program.
            System.err.println("ERROR: Cannot open " + fileName
                    + "for reading.");
            System.exit(1);
        }
        return input; // Return the Scanner object.
    }

    /**
     * Opens a specified file for writing using the `PrintWriter` class. If the
     * file cannot be created or opened for writing, an error message is printed
     * to `System.err`, and the program exits.
     *
     * @param fileName The name or path of the file to be opened for writing.
     * @return A `PrintWriter` object configured to write to the specified file.
     */
    public static PrintWriter openToWrite(String fileName) {
        PrintWriter output = null;
        try {
            // Attempt to create a PrintWriter for the given file.
            output = new PrintWriter(new File(fileName));
        } catch (FileNotFoundException e) {
            // If the file cannot be opened for writing, print an error and exit.
            System.err.println("ERORR: Cannot open " + fileName
                    + "for writing.");
            System.exit(2);
        }
        return output; // Return the PrintWriter object.
    }

    /**
     * Reads all words from a specified text file, converts them to uppercase,
     * and returns them as a `List` of `String` objects. Each word is assumed to
     * be separated by whitespace.
     *
     * @param fileName The name or path of the file from which to read words.
     * @return A `List<String>` containing all words from the file, in
     * uppercase.
     */
    public static List<String> readWordsFromFile(String fileName) {
        List<String> words = new ArrayList<>(); // Initialize an empty list to store words.
        Scanner input = openToRead(fileName); // Open the file for reading.

        // Read words one by one until the end of the file.
        while (input.hasNext()) {
            words.add(input.next().toUpperCase()); // Add each word (converted to uppercase) to the list.
        }

        input.close(); // Close the scanner to release file resources.
        return words; // Return the list of words.
    }

    /**
     * Checks if a given `word` exists within a `wordList` (case-insensitive).
     * The `word` is converted to uppercase before comparison.
     *
     * @param word The word to search for in the list.
     * @param wordList The list of words to search within.
     * @return `true` if the word is found in the list, `false` otherwise.
     */
    public static boolean wordExistsInList(String word, List<String> wordList) {
        String upperWord = word.toUpperCase(); // Convert the search word to uppercase.
        // Iterate through each word in the list for comparison.
        for (String w : wordList) {
            if (w.equals(upperWord)) { // Perform a case-sensitive comparison with the uppercase word.
                return true; // Word found.
            }
        }
        return false; // Word not found after checking all elements.
    }

    /**
     * Selects and returns a random word from the provided `wordList`. If the
     * list is null or empty, an empty string is returned. The chosen word is
     * converted to uppercase.
     *
     * @param wordList The list of words from which to choose a random word.
     * @return A randomly selected word from the list, or an empty string if the
     * list is invalid.
     */
    public static String chooseRandomWord(List<String> wordList) {
        // Check for null or empty list to prevent errors.
        if (wordList == null || wordList.isEmpty()) {
            return ""; // Return an empty string if no words are available.
        }
        // Generate a random index within the bounds of the word list.
        Random rand = new Random(); // Create a Random object
        int randomIndex = rand.nextInt(wordList.size());
        // Retrieve the word at the random index and convert it to uppercase.
        return wordList.get(randomIndex).toUpperCase();
    }
}
