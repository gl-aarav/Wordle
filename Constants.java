
import java.awt.event.KeyEvent;

/**
 * Constants.java
 *
 * This class defines various constant values used throughout the Wordle game,
 * including screen dimensions, drawing delays, keyboard layout, and key
 * placements.
 *
 * @author Aarav Goyal
 * @version 1.0
 * @since 10/10/2025
 */
public class Constants {

    /**
     * The width of the game screen in pixels.
     */
    public static final int SCREEN_WIDTH = 700;
    /**
     * The height of the game screen in pixels.
     */
    public static final int SCREEN_HEIGHT = 750;
    /**
     * The delay in milliseconds between drawing frames, used for animation
     * smoothness.
     */
    public static final int DRAW_DELAY = 20;

    /**
     * An array of strings representing the text labels for each key on the
     * virtual keyboard. This includes letters, "ENTER", "BACK", and "RESET".
     */
    public static final String[] KEYBOARD = {
        "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", // Top row of letters
        "A", "S", "D", "F", "G", "H", "J", "K", "L", // Middle row of letters
        "ENTER", "Z", "X", "C", "V", "B", "N", "M", "BACK", // Bottom row with special keys
        "RESET" // The reset button
    };

    /**
     * An array of integer constants from `java.awt.event.KeyEvent`
     * corresponding to the physical keyboard keys. These are used to detect
     * keyboard input.
     */
    public static final int[] KEYS = {
        KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U,
        KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P,
        KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_J,
        KeyEvent.VK_K, KeyEvent.VK_L,
        KeyEvent.VK_ENTER, KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N,
        KeyEvent.VK_M, KeyEvent.VK_BACK_SPACE,};

    /**
     * A 2D array defining the (x, y) pixel coordinates for the center of each
     * key on the virtual keyboard. These coordinates are used for drawing the
     * keys and detecting mouse clicks. Each inner array `[x, y]` corresponds to
     * a key in the `KEYBOARD` array.
     */
    public static final int[][] KEYPLACEMENT = {
        {125, 234}, {174, 234}, {223, 234}, {272, 234}, {321, 234}, {370, 234}, {419, 234},
        {468, 234}, {517, 234}, {566, 234}, // QWERTYUIOP row
        {149, 168}, {198, 168}, {247, 168}, {296, 168}, {345, 168}, {394, 168}, {443, 168},
        {492, 168}, {541, 168}, // ASDFGHJKL row
        {139, 102}, {198, 102}, {247, 102}, {296, 102}, {345, 102}, {394, 102}, {443, 102},
        {492, 102}, {551, 102}, // ENTER ZXCVBNM BACK row
        {345, 36} // RESET button
    };
}
