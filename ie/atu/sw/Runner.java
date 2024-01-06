package ie.atu.sw;

/**
 * This class contains the main method to run the sentiment analysis application.
 */
public class Runner {

    /**
     * The main method of the sentiment analysis application.
     * It creates a new menu and displays it to the user.
     *
     * @param args The command-line arguments.
     * @throws Exception If an error occurs while displaying the menu.
     */
    public static void main(String[] args) throws Exception {
        menu menu = new menu();
        menu.displayMenu();
    }
}