package ie.atu.sw;



// Import necessary classes
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class fileLoader {
    
    // Class-level variable to hold the total sentiment value
    private static double sentimentValue;
 
    // Method to parse a file and calculate the total sentiment value
    public double parseFile(String file, ConcurrentSkipListMap<String, Double> lexicon) throws Exception {

        // Initialize sentimentValue to 0
        sentimentValue = 0;
        
        // Create an ExecutorService to manage threads
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            // Read the file line by line, submit a task for each line to calculate its sentiment value,
            // wait for all tasks to complete and sum up the results
            sentimentValue = Files.lines(Paths.get(file))
                .map(text -> pool.submit(() -> process(text, lexicon)))
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
    
    // Method to calculate the sentiment value of a line
    public double process(String text, ConcurrentSkipListMap<String, Double> lexicon) {
        // Initialize the sentiment value of the line to 0
        double lineSentimentValue = 0;
        // Split the line into words
        String [] parts = text.split("\\s+");
        // For each word in the line
        for (String string : parts) {
            // Remove all non-alphabetic characters and convert to lower case
            string = string.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (lexicon.containsKey(string)) {
                lineSentimentValue += lexicon.get(string);
                System.out.println("Word: " + string + " Sentiment Value: " + lexicon.get(string));
            }
        }
        return lineSentimentValue;
	}
	
}
