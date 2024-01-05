package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class fileLoader {
    
    private static double sentimentValue;
 
    public double parseFile(String file, ConcurrentSkipListMap<String, Double> lexicon) throws Exception {

        sentimentValue = 0;
        
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            sentimentValue = Files.lines(Paths.get(file))
                .map(text -> pool.submit(() -> process(text, lexicon)))
                .mapToDouble(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .sum();
        }
        return sentimentValue;
    }
	
	public double process(String text, ConcurrentSkipListMap<String, Double> lexicon) {
        double lineSentimentValue = 0;
        String [] parts = text.split("\\s+");
        for (String string : parts) {
            string = string.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (lexicon.containsKey(string)) {
                lineSentimentValue += lexicon.get(string);
                System.out.println("Word: " + string + " Sentiment Value: " + lexicon.get(string));
            }
        }
        return lineSentimentValue;
	}
	
}
