package ie.atu.sw;
public interface IFileProcessor {
    double parseFile(String file) throws Exception;
    double process(String text);
}