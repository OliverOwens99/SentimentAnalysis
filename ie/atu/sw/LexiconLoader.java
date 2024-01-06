package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;

/**
 * This class is responsible for loading and parsing a lexicon file.
 * The lexicon file is expected to be a txt file where each line contains a word and its sentiment value.
 */
public class LexiconLoader {

    /**
     * A map that stores the words of the lexicon and their sentiment values.
     */
    ConcurrentSkipListMap<String, Double> lexicon = new ConcurrentSkipListMap<>();

    /**
     * Parses a lexicon file and returns a map of words and their sentiment values.
     *
     * @param file The lexicon file to parse.
     * @return A map of words and their sentiment values.
     * @throws Exception If an error occurs while parsing the lexicon file.
     */
    public ConcurrentSkipListMap<String, Double> parseLexicon(String file) throws Exception {
        try (var pool = Executors.newVirtualThreadPerTaskExecutor()){
            Files.lines(Paths.get(file)).forEach(text -> pool.execute(() -> process(text)));
        }
        return lexicon;
    }

    /**
     * Processes a line of text from the lexicon file.
     * The line is expected to contain a word and its sentiment value, separated by a comma.
     *
     * @param text The line of text to process.
     */
    public void process(String text) {
        String [] parts = text.split(",");
        lexicon.put(parts[0], Double.parseDouble(parts[1]));
    }
}