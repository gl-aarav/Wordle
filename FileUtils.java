import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileUtils {

    public static List<String> readWordsFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim().toUpperCase());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename + " - " + e.getMessage());
        }
        return words;
    }

    public static String chooseRandomWord(List<String> words) {
        if (words.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public static boolean wordExistsInList(String word, List<String> wordList) {
        return wordList.contains(word.toUpperCase());
    }
}
