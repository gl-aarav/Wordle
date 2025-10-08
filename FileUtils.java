
/**
 * File Utilities for reading and writing files
 * 
 * @author Aarav Goyal
 * @since Semptember 3, 2025
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils {
	/**
	 * opens a file to reac using the Scanner Class.
	 * 
	 * @param fileName name of the file to open
	 * @return the Scanner object to the file
	 */
	public static java.util.Scanner openToRead(String fileName) {
		java.util.Scanner input = null;

		try {
			input = new java.util.Scanner(new java.io.File(fileName));
		} catch (java.io.FileNotFoundException e) {
			System.err.println("ERROR: Cannot open " + fileName +
					"for reading.");
			System.exit(1);
		}
		return input;
	}

	/**
	 * Opens a file to write using the PrintWriter class
	 * 
	 * @param fileName Name of the file to open
	 * @return the PrintWrite object to the file
	 */
	public static PrintWriter openToWrite(String fileName) {
		PrintWriter output = null;
		try {
			output = new PrintWriter(new File(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("ERORR: Cannot open " + fileName +
					"for writing.");
			System.exit(2);
		}
		return output;
	}

	/**
	 * Reads all words from a file and returns them as a list
	 * 
	 * @param fileName name of the file to read
	 * @return List of words from the file
	 */
	public static List<String> readWordsFromFile(String fileName) {
		List<String> words = new ArrayList<>();
		Scanner input = openToRead(fileName);
		
		while (input.hasNext()) {
			words.add(input.next().toUpperCase());
		}
		
		input.close();
		return words;
	}

	/**
	 * Checks if a word exists in the given list (case-insensitive)
	 * 
	 * @param word the word to search for
	 * @param wordList the list of words to search in
	 * @return true if the word exists in the list, false otherwise
	 */
	public static boolean wordExistsInList(String word, List<String> wordList) {
		String upperWord = word.toUpperCase();
		for (String w : wordList) {
			if (w.equals(upperWord)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Chooses a random word from the given list
	 * 
	 * @param wordList the list of words to choose from
	 * @return a random word from the list
	 */
	public static String chooseRandomWord(List<String> wordList) {
		if (wordList == null || wordList.isEmpty()) {
			return "";
		}
		int randomIndex = (int) (Math.random() * wordList.size());
		return wordList.get(randomIndex).toUpperCase();
	}
}
