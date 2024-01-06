package ie.atu.sw;

/* This is essentially the same as file laoder but features and in */


// Import necessary classes
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class sentimentFileLoader implements IFileProcessor {
    private double sentimentValue;
    private ConcurrentSkipListMap<String, Double> lexicon;

    // this is the constructor for the class to take in the lexicon so the lexicon can be used in the process method
    public sentimentFileLoader(ConcurrentSkipListMap<String, Double> lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public double parseFile(String file) throws Exception {
        // Initialize sentimentValue to 0
        sentimentValue = 0;

        // Create an ExecutorService to manage threads
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            // Read the file line by line, submit a task for each line to calculate its
            // sentiment value,
            // wait for all tasks to complete and sum up the results
            sentimentValue = Files.lines(Paths.get(file))
                    .map(text -> pool.submit(() -> process(text)))
                    .mapToDouble(future -> {
                        try {
                            // Retrieve the result of the task
                            return future.get();
                        } catch (Exception e) {
                            // If an exception occurs, wrap it in a RuntimeException and throw it
                            throw new RuntimeException(e);
                        }
                    })
                    .sum();
        }
        // Return the total sentiment value
        return sentimentValue;
    }

    @Override
    public double process(String text) {
        // Initialize the sentiment value of the line to 0
        double lineSentimentValue = 0;
        // Split the line into words
        String[] parts = text.split("\\s+");
        // For each word in the line
        for (String string : parts) {
            // Remove all non-alphabetic characters and convert to lower case
            string = string.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (lexicon.containsKey(string)) {
                // If the word is in the lexicon, add its sentiment value to the line sentiment
                // value
                lineSentimentValue += lexicon.get(string);
                System.out.println("Word: " + string + " Sentiment Value: " + lexicon.get(string));
            }
        }
        return lineSentimentValue;
    }

}
