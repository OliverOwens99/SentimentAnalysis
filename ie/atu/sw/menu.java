package ie.atu.sw;

import java.io.File;
import java.util.Scanner;

public class menu {
    private LexiconLoader lexiconLoader;
    private fileLoader fileLoader;
    private String lexiconFile;
    private String textFile;
    private String outputFile;
    private Scanner scanner = new Scanner(System.in);
    private double sentiment;
    public menu() {
        lexiconLoader = new LexiconLoader();
        fileLoader = new fileLoader();
        lexiconFile = "";
        textFile = "";
        outputFile = "";
    }

    public void displayMenu() {
        int option = 0;
        while (option != -1) {
            System.out.println(ConsoleColour.WHITE);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*             Virtual Threaded Sentiment Analyser          *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            System.out.println("(1) Specify a Text File");
            System.out.println("(2) Specify an Output File (default: ./out.txt)");
            System.out.println("(3) Configure Lexicons");
            System.out.println("(4) Execute, Analyse and Report");
            System.out.println("(?) Optional Extras...");
            System.out.println("(-1) Quit");

            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Select Option [1-4]>");
            System.out.println();

            Scanner scanner = new Scanner(System.in);
            option = scanner.nextInt();

            handleOption(option, scanner);
            
        }

    }

    private void handleOption(int option, Scanner scanner) {
        if (option == 1) {
            specifyTextFile(scanner);
        } else if (option == 2) {
            specifyOutputFile(scanner);
        } else if (option == 3) {
            configureLexicons(scanner);
        } else if (option == 4) {
            executeAnalyseAndReport();
        }
    }

    private void specifyTextFile(Scanner scanner) {
        System.out.println("Specify a Text File");
        textFile = scanner.next();

    }

    private void specifyOutputFile(Scanner scanner) {
        System.out.println("Specify an Output File (default: ./out.txt)");
        outputFile = scanner.next();
    }

    private void configureLexicons(Scanner scanner) {
        System.out.println("Configure Lexicons");
        System.out.println("Specify a Lexicon File");
        lexiconFile = scanner.next();
    }

    private void executeAnalyseAndReport() {
        if (lexiconFile.isEmpty()) {
            System.out.println("Error: Please specify a lexicon file before executing this option.");
            return;
        }
        System.out.println("Execute, Analyse and Report");
        try {
            System.out.println(new File(".").getAbsolutePath());
            System.out.println("textFile: " + textFile + " lexiconFile: " + lexiconFile);
            sentiment = fileLoader.parseFile(textFile, lexiconLoader.parseLexicon(lexiconFile));
            
        if (sentiment >= 1) {
            System.out.println("Positive Sentiment :) for the text file: " + textFile );
            System.out.println("Sentiment Value: " + sentiment);
        } else if (sentiment <=-1) {
            System.out.println("Negative Sentiment  :( for the text file: " + textFile);    
            System.out.println("Sentiment Value: " + sentiment);
        } else {
            System.out.println("Neutral Sentiment :| for the text file: " + textFile);
            System.out.println("Sentiment Value: " + sentiment);
            
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            displayProgress(10, sentiment);
        } catch (InterruptedException e) {
            
            e.printStackTrace();
        }
    }

    public void displayProgress(int progress, double sentiment2) throws InterruptedException {
        System.out.print(ConsoleColour.YELLOW);
        for (int i = 0; i < progress; i++) {
            printProgress(i + 1, sentiment2);
            Thread.sleep(10);
        }
    }

    private void printProgress(int index, double total) {
        if (index > total)
            return; // Out of range
        int size = 50; // Must be less than console width
        char done = '█'; // Change to whatever you like.
        char todo = '░'; // Change to whatever you like.

        // Compute basic metrics for the meter
        double complete = (100 * index) / total;
        double completeLen = size * complete / 100;

        /*
         * A StringBuilder should be used for string concatenation inside a
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append((i < completeLen) ? done : todo);
        }

        /*
         * The line feed escape character "\r" returns the cursor to the
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        System.out.print("\r" + sb + "] " + complete + "%");

        // Once the meter reaches its max, move to a new line.
        if (done == total)
            System.out.println("\n");
    } // Implement your progress printing logic here

}
