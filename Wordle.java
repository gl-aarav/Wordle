
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * Wordle.java
 *
 * This class implements the popular word-guessing game Wordle. Players attempt
 * to guess a five-letter word within six tries. The game provides feedback on
 * each guess, indicating which letters are correct and in the right position,
 * correct but in the wrong position, or not in the word at all.
 *
 * @author Aarav Goyal
 * @version 1.0
 * @since 10/10/2025
 */
public class Wordle {

    /**
     * This section declares all the field variables used in the Wordle game.
     * These variables store the game state, user input, and configuration.
     */
    /**
     * The secret five-letter word that the player is trying to guess. This word
     * is chosen randomly from `words5.txt` or set by a test word.
     */
    private String word;

    /**
     * An array of strings to store the player's six guesses. Each element
     * `wordGuess[i]` holds a five-letter guess.
     */
    private String[] wordGuess;

    /**
     * A string that accumulates the letters currently being typed by the user
     * for their active guess. Its length can range from 0 to 5 characters.
     */
    private String letters;

    /**
     * The filename for the dictionary of 5-letter words that can be chosen as
     * the secret word.
     */
    private final String WORDS5 = "words5.txt";

    /**
     * The filename for the larger dictionary of 5-letter words that are allowed
     * as valid user guesses.
     */
    private final String WORDS5_ALLOWED = "words5allowed.txt";

    /**
     * A collection of boolean flags to control various aspects of the game's
     * behavior and input handling: - `show`: If true, the secret word is
     * printed to the terminal for debugging. - `readyForKeyInput`: If true, the
     * game is currently accepting keyboard input. - `readyForMouseInput`: If
     * true, the game is currently accepting mouse input. - `activeGame`: If
     * true, the game is in progress; if false, only the RESET button is active.
     */
    private boolean show, readyForKeyInput, readyForMouseInput, activeGame;

    /**
     * An array of integers representing the color status of each key on the
     * virtual keyboard. This determines how each key is rendered: - 0: Not
     * checked yet (default light gray) - 1: No match (dark gray) - letter is
     * not in the word - 2: Partial match (yellow) - letter is in the word but
     * wrong position - 3: Exact match (green) - letter is in the word and
     * correct position
     */
    private int[] keyBoardColors;

    /**
     * Constructs a new Wordle game instance. Initializes game parameters based
     * on command-line arguments.
     *
     * @param showIt A string argument. If "show" (case-insensitive), the secret
     * word will be displayed in the console.
     * @param testWord A string argument. If this word is valid (exists in
     * `words5allowed.txt`), it will be used as the secret word instead of a
     * random word.
     */
    public Wordle(String showIt, String testWord) {
        show = false;
        if (showIt.equalsIgnoreCase("show")) {
            show = true;
        }

        initAll(testWord);
    }

    /**
     * Initializes or resets all game fields to their starting values. This
     * method is called at the beginning of a new game or when the RESET button
     * is pressed. It selects the secret word and sets up the initial state of
     * the keyboard colors.
     *
     * @param testWord If provided and valid, this word will be used as the
     * secret word for the game. Otherwise, a random word is chosen.
     */
    public void initAll(String testWord) {
        // Initialize the array to store guesses, clearing any previous guesses.
        wordGuess = new String[6];
        for (int i = 0; i < wordGuess.length; i++) {
            wordGuess[i] = new String("");
        }
        // Clear the current letters being typed.
        letters = "";
        // Set input flags and game activity status.
        readyForKeyInput = activeGame = true;
        readyForMouseInput = false;
        // Initialize keyboard colors to default (not checked yet).
        keyBoardColors = new int[29];
        // Choose the secret word for the game.
        word = openFileAndChooseWord(WORDS5, testWord);
    }

    /**
     * The entry point for the Wordle application. This method parses
     * command-line arguments, creates a Wordle instance, sets up the graphical
     * canvas, and starts the game loop.
     *
     * @param args Command-line arguments: - `args[0]`: Can be "show" to display
     * the secret word, or a test word to be used as the secret word. -
     * `args[1]`: If `args[0]` is "show", `args[1]` can be a test word.
     */
    public static void main(String[] args) {
        String testWord = "";
        String showIt = "";

        // Parse command-line arguments to determine if the word should be shown
        // or if a specific test word should be used.
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("show")) {
                showIt = "show";
                if (args.length > 1) {
                    testWord = args[1];
                }
            } else {
                testWord = args[0];
            }
        }

        // Create a new Wordle game instance.
        Wordle run = new Wordle(showIt, testWord);
        // Set up the graphical canvas for drawing the game.
        run.setUpCanvas();
        // Start the main game loop.
        run.playGame();
    }

    /**
     * Configures the drawing canvas for the game. Sets the canvas size,
     * coordinate scales, and enables double buffering for smooth animations and
     * drawing.
     */
    public void setUpCanvas() {
        // Set the canvas dimensions based on constants.
        StdDraw.setCanvasSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        // Set the X-axis scale from 0 to screen width.
        StdDraw.setXscale(0, Constants.SCREEN_WIDTH);
        // Set the Y-axis scale from 0 to screen height.
        StdDraw.setYscale(0, Constants.SCREEN_HEIGHT);

        // Enable double buffering to prevent flickering during drawing updates.
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Contains the main game loop. This method continuously updates the game
     * state and redraws the panel as long as the `keepGoing` flag is true.
     */
    public void playGame() {
        boolean keepGoing = true; // Flag to keep the game loop running.
        while (keepGoing) {
            // Only draw the panel if the game is active.
            if (activeGame) {
                drawPanel();
            }
            // Update game logic and handle input.
            update();
        }
    }

    /**
     * Selects the secret word for the game. If a valid `testWord` is provided,
     * it is used. Otherwise, a random word is chosen from `inFileName`. The
     * chosen word can optionally be printed to the console if the `show` flag
     * is true.
     *
     * @param inFileName The path to the file containing potential secret words.
     * @param testWord An optional word to use as the secret word if it's valid.
     * @return The chosen secret word for the current game.
     */
    public String openFileAndChooseWord(String inFileName, String testWord) {
        // Read the list of possible goal words from the specified file.
        List<String> goalWords = FileUtils.readWordsFromFile(inFileName);
        // Read the list of all allowed words for user guesses.
        List<String> allowedWords = FileUtils.readWordsFromFile(WORDS5_ALLOWED);
        String chosenWord;

        // If a test word is provided and is valid, use it as the chosen word.
        if (!testWord.isEmpty() && FileUtils.wordExistsInList(testWord, allowedWords)) {
            chosenWord = testWord.toUpperCase();
        } else {
            // Otherwise, choose a random word from the goal words list.
            chosenWord = FileUtils.chooseRandomWord(goalWords);
        }

        // If the 'show' flag is true, print the chosen word to the console.
        if (show) {
            System.out.println("The goal word is: " + chosenWord);
        }
        return chosenWord;
    }

    /**
     * Checks if a given word is present in the `words5allowed.txt` dictionary.
     * This is used to validate user guesses.
     *
     * @param possibleWord The word to check for existence in the allowed words
     * list.
     * @return `true` if the word is found in the allowed words file, `false`
     * otherwise.
     */
    public boolean inAllowedWordFile(String possibleWord) {
        // Read the list of allowed words from the file.
        List<String> allowedWords = FileUtils.readWordsFromFile(WORDS5_ALLOWED);
        // Check if the possibleWord exists in the list.
        return FileUtils.wordExistsInList(possibleWord, allowedWords);
    }

    /**
     * Processes the user's current guess (`letters`). This method is called
     * when the user presses ENTER and has typed a 5-letter word. It validates
     * the guess against the allowed words list. If valid, the guess is added to
     * `wordGuess`; otherwise, an error dialog is displayed.
     */
    public void processGuess() {
        letters = letters.toUpperCase(); // Convert the current guess to uppercase for consistency.

        // Check if the guessed word is in the list of allowed words.
        if (inAllowedWordFile(letters)) {
            // If the guess is valid, find the next available slot in `wordGuess` array.
            int guessNumber = 0;
            for (int i = 0; i < wordGuess.length; i++) {
                if (wordGuess[i].length() == 5) {
                    guessNumber = i + 1; // Increment guess number if a slot is filled.
                }
            }
            // Store the valid guess and clear the `letters` buffer for the next input.
            wordGuess[guessNumber] = letters;
            letters = "";
        } else {
            // If the guess is not in the allowed word list, display an error message.
            JOptionPane pane = new JOptionPane("Not in word list");
            JDialog d = pane.createDialog(null, "Invalid Guess");
            d.setLocation(365, 250); // Position the dialog box.
            d.setVisible(true);
            letters = ""; // Clear the invalid guess so the user can re-enter.
        }
    }

    /**
     * Updates the color status of a specific key on the virtual keyboard. The
     * color status indicates whether the letter has been guessed, and if so,
     * whether it's an exact match, partial match, or no match.
     *
     * @param letter The character representing the key to update (e.g., 'A',
     * 'B').
     * @param colorStatus The new color status to apply: - 1: No match (dark
     * gray) - 2: Partial match (yellow) - 3: Exact match (green)
     */
    private void updateKeyboardColor(char letter, int colorStatus) {
        // Iterate through the keyboard layout to find the matching letter.
        for (int i = 0; i < Constants.KEYBOARD.length; i++) {
            if (Constants.KEYBOARD[i].charAt(0) == letter) {
                // Only update the color if the new status is "better" than the current one.
                // (Green (3) > Yellow (2) > Dark Gray (1) > Not checked (0))
                if (colorStatus > keyBoardColors[i]) {
                    keyBoardColors[i] = colorStatus;
                }
                return; // Once updated, exit the method.
            }
        }
    }

    /**
     * Renders the entire Wordle game panel, including: - The grid of guessed
     * words with appropriate letter colors. - The current word being typed by
     * the user. - The virtual keyboard at the bottom, with keys colored
     * according to their match status (green, yellow, dark gray, or default).
     * This method also checks for win/loss conditions after drawing.
     */
    public void drawPanel() {
        StdDraw.clear(StdDraw.WHITE); // Clear the canvas with a white background.

        // Loop through each guess row to draw letter backgrounds and update keyboard colors.
        for (int row = 0; row < 6; row++) {
            if (wordGuess[row].length() == 5) { // If a full 5-letter word has been guessed in this row.
                String currentGuess = wordGuess[row];
                // Create a temporary copy of the secret word to track used letters
                // and correctly handle duplicate letters in the guess.
                String tempWord = word;

                // First pass: Identify and draw exact matches (green).
                for (int col = 0; col < 5; col++) {
                    if (currentGuess.charAt(col) == tempWord.charAt(col)) {
                        // Draw green background for exact match.
                        StdDraw.picture(209 + col * 68, 650 - row * 68, "letterFrameGreen.png");
                        // Update keyboard color to green (3).
                        updateKeyboardColor(currentGuess.charAt(col), 3);
                        // Mark the letter as used in tempWord to prevent it from being
                        // counted again as a partial match.
                        tempWord = tempWord.substring(0, col) + ' ' + tempWord.substring(col + 1);
                    }
                }

                // Second pass: Identify and draw partial matches (yellow) and no matches (dark gray).
                for (int col = 0; col < 5; col++) {
                    char guessChar = currentGuess.charAt(col);
                    // Skip letters already handled as exact matches in the first pass.
                    if (currentGuess.charAt(col) == word.charAt(col)) {
                        continue;
                    } else if (tempWord.indexOf(guessChar) != -1) {
                        // Draw yellow background for partial match.
                        StdDraw.picture(209 + col * 68, 650 - row * 68, "letterFrameYellow.png");
                        // Update keyboard color to yellow (2).
                        updateKeyboardColor(guessChar, 2);
                        // Mark the letter as used in tempWord.
                        tempWord = tempWord.substring(0, tempWord.indexOf(guessChar)) + ' '
                                + tempWord.substring(tempWord.indexOf(guessChar) + 1);
                    } else {
                        // Draw dark gray background for no match.
                        StdDraw.picture(209 + col * 68, 650 - row * 68, "letterFrameDarkGray.png");
                        // Update keyboard color to dark gray (1).
                        updateKeyboardColor(guessChar, 1);
                    }
                }
            } else {
                // If the row is not a complete guess, draw default empty letter frames.
                for (int col = 0; col < 5; col++) {
                    StdDraw.picture(209 + col * 68, 650 - row * 68, "letterFrame.png");
                }
            }
        }

        // Draw the Wordle title image at the top of the screen.
        Font font = new Font("Arial", Font.BOLD, 12);
        StdDraw.setFont(font);
        StdDraw.picture(Constants.SCREEN_WIDTH / 2, Constants.SCREEN_HEIGHT - 30, "wordle.png");

        // Draw the virtual keyboard at the bottom of the screen.
        int place = 0; // Index for iterating through keyboard keys.
        for (int[] pair : Constants.KEYPLACEMENT) {
            String keyImage = "keyBackground.png"; // Default key background.
            // Use a larger background image for special keys like ENTER, BACKSPACE, RESET.
            if (place == 19 || place == 27 || place == 28) {
                keyImage = "keyBackgroundBig.png";
            } else {
                // Determine the key background color based on its `keyBoardColors` status.
                switch (keyBoardColors[place]) {
                    case 1: // No match
                        keyImage = "keyBackgroundDarkGray.png";
                        break;
                    case 2: // Partial match
                        keyImage = "keyBackgroundYellow.png";
                        break;
                    case 3: // Exact match
                        keyImage = "keyBackgroundGreen.png";
                        break;
                    default: // Not checked yet (light gray)
                        keyImage = "keyBackground.png";
                        break;
                }
            }
            // Draw the key background image.
            StdDraw.picture(pair[0], pair[1], keyImage);
            // Set pen color to black for drawing key text.
            StdDraw.setPenColor(StdDraw.BLACK);
            // Draw the letter/text for the key.
            StdDraw.text(pair[0], pair[1], Constants.KEYBOARD[place]);
            place++;
        }

        // Draw all the letters that have been guessed or are currently being typed.
        drawAllLettersGuessed();

        StdDraw.show(); // Display the offscreen buffer to the screen.
        StdDraw.pause(Constants.DRAW_DELAY); // Pause for a short duration.

        // After drawing, check if the game has been won or lost.
        checkIfWonOrLost();
    }

    /**
     * Draws all the letters from previous guesses and the current active guess
     * onto the game board. This method is called by `drawPanel`.
     */
    public void drawAllLettersGuessed() {
        // Set font for drawing letters.
        Font font = new Font("Arial", Font.BOLD, 34);
        StdDraw.setFont(font);
        int guessNumber = 0; // Tracks the current guess row for active typing.

        // Draw letters from all completed guesses.
        for (int i = 0; i < wordGuess.length; i++) {
            if (wordGuess[i].length() > 0) {
                for (int j = 0; j < wordGuess[i].length(); j++) {
                    StdDraw.text(209 + j * 68, 644 - i * 68, "" + wordGuess[i].charAt(j));
                }
            }
            // Update `guessNumber` to point to the next empty row for the current guess.
            if (wordGuess[i].length() == 5) {
                guessNumber = i + 1;
            }
        }
        // Draw letters for the current, incomplete guess being typed by the user.
        for (int i = 0; i < letters.length(); i++) {
            StdDraw.text(209 + i * 68, 644 - guessNumber * 68, "" + letters.substring(i, i + 1));
        }
    }

    /**
     * Determines if the game has ended in a win or a loss, and displays an
     * appropriate message to the user. A win occurs if the last guess matches
     * the secret word. A loss occurs if all six guesses are used without
     * guessing the correct word.
     */
    public void checkIfWonOrLost() {
        String lastWord = "";
        // Find the last completed guess.
        for (int i = 0; i < wordGuess.length; i++) {
            if (wordGuess[i].length() == 5) {
                lastWord = wordGuess[i];
            }
        }

        // Check for a win condition: if the last guess matches the secret word.
        if (lastWord.equals(word)) {
            activeGame = false; // Deactivate the game, only RESET button will work.
            JOptionPane pane = new JOptionPane(lastWord + " is the word!  Press RESET to begin again");
            JDialog d = pane.createDialog(null, "CONGRATULATIONS!");
            d.setLocation(365, 250);
            d.setVisible(true);
        }

        // Check for a loss condition: if all six guesses are filled and the word was not guessed.
        int filledGuesses = 0;
        for (String guess : wordGuess) {
            if (guess.length() == 5) {
                filledGuesses++;
            }
        }

        if (filledGuesses == 6 && !lastWord.equals(word)) {
            activeGame = false; // Deactivate the game.
            JOptionPane pane = new JOptionPane("The word was " + word + ". Press RESET to begin again");
            JDialog d = pane.createDialog(null, "YOU LOST!");
            d.setLocation(365, 250);
            d.setVisible(true);
        }
    }

    /**
     * The main update method for the game, called continuously within the game
     * loop. It checks for and responds to both keyboard and mouse input, but
     * only if the game is currently active.
     */
    public void update() {
        if (activeGame) {
            respondToKeys(); // Handle keyboard input if the game is active.
        }
        respondToMouse(); // Always respond to mouse input (e.g., for RESET button).
    }

    /**
     * Handles keyboard input from the user. Processes backspace, enter, and
     * letter key presses to build the current guess or submit it.
     */
    public void respondToKeys() {
        // Handle BACKSPACE key: remove the last letter from the current guess.
        if (readyForKeyInput && StdDraw.hasNextKeyTyped()
                && StdDraw.isKeyPressed(KeyEvent.VK_BACK_SPACE) && letters.length() > 0) {
            letters = letters.substring(0, letters.length() - 1);
            readyForKeyInput = false; // Prevent rapid multiple deletions.
        } // Handle ENTER key: process the guess if it's a complete 5-letter word.
        else if (readyForKeyInput && StdDraw.hasNextKeyTyped()
                && StdDraw.isKeyPressed(KeyEvent.VK_ENTER) && letters.length() == 5) {
            processGuess();
            readyForKeyInput = false; // Prevent multiple submissions.
        } // Handle letter keys: add the typed letter to the current guess.
        else if (readyForKeyInput && StdDraw.hasNextKeyTyped() && letters.length() < 5) {
            String letter = "" + StdDraw.nextKeyTyped();
            letter = letter.toUpperCase(); // Convert to uppercase.
            // Only append if the character is an alphabet letter.
            if (letter.charAt(0) >= 'A' && letter.charAt(0) <= 'Z') {
                letters += letter;
            }
            readyForKeyInput = false; // Prevent rapid multiple letter inputs.
        } // Clear any remaining key presses and reset `readyForKeyInput` when no keys are pressed.
        else {
            while (StdDraw.hasNextKeyTyped()) {
                StdDraw.nextKeyTyped();
            }
            if (!StdDraw.hasNextKeyTyped()) {
                readyForKeyInput = true;
            }
        }
    }

    /**
     * Handles mouse input, specifically clicks on the virtual keyboard.
     * Simulates key presses for letters, backspace, enter, and the reset
     * button.
     */
    public void respondToMouse() {
        // Check if the mouse is pressed and ready for input.
        if (readyForMouseInput && StdDraw.isMousePressed()) {
            // Iterate through all keyboard key placements.
            for (int i = 0; i < Constants.KEYPLACEMENT.length; i++) {
                // Check if the mouse click is within the bounds of the current key.
                if (StdDraw.mouseX() > Constants.KEYPLACEMENT[i][0] - 22
                        && StdDraw.mouseX() < Constants.KEYPLACEMENT[i][0] + 22
                        && StdDraw.mouseY() > Constants.KEYPLACEMENT[i][1] - 29
                        && StdDraw.mouseY() < Constants.KEYPLACEMENT[i][1] + 29) {

                    // Handle RESET button (index 28).
                    if (i == 28) {
                        initAll(""); // Reset the game.
                        activeGame = true; // Reactivate the game.
                    } // Handle BACKSPACE button (index 27) if the game is active and there are letters to delete.
                    else if (activeGame && i == 27 && letters.length() > 0) {
                        letters = letters.substring(0, letters.length() - 1);
                    } // Handle ENTER button (index 19) if the game is active and a 5-letter word is typed.
                    else if (activeGame && i == 19 && letters.length() == 5) {
                        processGuess();
                    } // Handle regular letter keys (not ENTER, BACKSPACE, RESET) if the game is active
                    // and the current guess is less than 5 letters long.
                    else if (activeGame && i != 19 && i != 27 && i != 28 && letters.length() < 5) {
                        String letter = Constants.KEYBOARD[i].toUpperCase();
                        letters += letter; // Append the clicked letter to the current guess.
                    }
                }
            }
            readyForMouseInput = false; // Prevent multiple rapid mouse clicks.
        } // Reset `readyForMouseInput` when the mouse button is released.
        else if (!StdDraw.isMousePressed()) {
            readyForMouseInput = true;
        }
    }
}
