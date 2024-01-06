package ie.atu.sw;

// Import necessary classes
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ConcurrentSkipListMap;
import java.io.PrintWriter;
import java.io.IOException;

public class menu {
    // Initialize variables
    private LexiconLoader lexiconLoader;
    private String lexiconFile;
    private String textFile;
    private String outputFile;
    private Scanner scanner = new Scanner(System.in);
    private double sentiment;
    private IFileProcessor fileProcessor;
    private static final int PROGRESS_MAX = 10;
    private static final int Happy = 1;
    private static final int Sad = -1;
    // Constructor for the class
    public menu() {
        lexiconLoader = new LexiconLoader();
        lexiconFile = "";
        textFile = "";
        outputFile = "";
    }
    // Method to display the menu
    public void displayMenu() {
        int option = 0;
        while (option != -1) {
            try {
                System.out.println(ConsoleColour.BLACK_BOLD_BRIGHT);
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
                System.out.println(" ");

                option = scanner.nextInt();

                handleOption(option, scanner);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // discard the invalid input
            }

            
        }

    }
    // Method to handle the menu options
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
    // Method to specify a text file
    private void specifyTextFile(Scanner scanner) {
        System.out.println("Specify a Text File");
        textFile = scanner.next();

    }
    // Method to specify an output file
    private void specifyOutputFile(Scanner scanner) {
        System.out.println("Specify an Output File (default: ./out.txt)");
        outputFile = scanner.next();
    }
    // Method to configure lexicons
    private void configureLexicons(Scanner scanner) {
        System.out.println("Configure Lexicons");
        System.out.println("Specify a Lexicon File");
        lexiconFile = scanner.next();
        ConcurrentSkipListMap<String, Double> lexicon = null; // Initialize lexicon variable
        try {
            lexicon = lexiconLoader.parseLexicon(lexiconFile);
        } catch (Exception e) {
            System.out.println("Error: Unable to load lexicon: " + e.getMessage());
            e.printStackTrace();
        }
        fileProcessor = new sentimentFileLoader(lexicon);
    }
    // Method to execute the sentiment analysis and report the results
    private void executeAnalyseAndReport() {
        if (textFile == null || textFile.isEmpty()) {
            System.out.println("No text file provided for analysis.");
            return;
        }
        try {
            double sentiment = fileProcessor.parseFile(textFile);
            displaySentiment(sentiment);
            displayProgress(PROGRESS_MAX, sentiment);
            writeSentimentToFile(sentiment);
        } catch (Exception e) {
            System.out.println("Error: Unable to analyze sentiment: " + e.getMessage());
        }
    }

    // Method to display the sentiment value of a text file
    private void displaySentiment(double sentiment) {
        String sentimentText;
        if (sentiment >= Happy) {
            sentimentText = "Positive Sentiment :) for the text file: ";
        } else if (sentiment <= Sad) {
            sentimentText = "Negative Sentiment :( for the text file: ";
        } else {
            sentimentText = "Neutral Sentiment :| for the text file: ";
        }
        System.out.println(sentimentText + textFile);
        System.out.println("Sentiment Value: " + sentiment);
    }
    // Method to display the progress of the sentiment analysis
    public void displayProgress(int progress, double sentimentValue) throws InterruptedException {
        System.out.print(ConsoleColour.YELLOW);
        for (int i = 0; i < progress; i++) {
            printProgress(i + 1, sentimentValue);
            Thread.sleep(10);
        }
    }
    // Method to print the progress of the sentiment analysis
    public void printProgress(int progress, double sentimentValue) {
        System.out.print("\r");
        System.out.print(ConsoleColour.YELLOW);
        System.out.print("Analyzing Sentiment: ");
        System.out.print(ConsoleColour.GREEN);
        System.out.print("[");
        for (int i = 0; i < progress; i++) {
            System.out.print("=");
        }
        for (int i = progress; i < PROGRESS_MAX; i++) {
            System.out.print(" ");
        }
        System.out.print("]");
        System.out.print(ConsoleColour.WHITE);
        System.out.print(" " + (progress * 10) + "%");
        System.out.print(ConsoleColour.WHITE);
        System.out.print(" Sentiment Value: " + sentimentValue);
    }
    // Method to write the sentiment value to an output file
    public void writeSentimentToFile(double sentimentValue) throws IOException {
        String output = "Sentiment Value: " + sentiment;

        try (PrintWriter writer = new PrintWriter(outputFile, "UTF-8")) {
            writer.println(output);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}