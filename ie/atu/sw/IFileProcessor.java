package ie.atu.sw;

/**
 * This interface defines the methods that a file processor should implement.
 * A file processor is responsible for parsing a file and processing a line of text.
 */
public interface IFileProcessor {

    /**
     * Parses a file and returns a double value.
     *
     * @param file The file to parse.
     * @return A double value resulting from the file parsing.
     * @throws Exception If an error occurs while parsing the file.
     */
    double parseFile(String file) throws Exception;

    /**
     * Processes a line of text and returns a double value.
     *
     * @param text The line of text to process.
     * @return A double value resulting from the text processing.
     */
    double process(String text);
}