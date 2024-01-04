package ie.atu.sw;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;

public class LexiconLoader  {




ConcurrentSkipListMap<String, Double> lexicon = new ConcurrentSkipListMap<>();
 
    public ConcurrentSkipListMap<String, Double> parseLexicon(String file) throws Exception {

        
        
        try (var pool = Executors.newVirtualThreadPerTaskExecutor()){
			Files.lines(Paths.get(file)).forEach(text -> pool.execute(() -> process(text)));
		}
		return lexicon;
	}
	
	public void process(String text) {
        String [] parts = text.split(",");
        lexicon.put(parts[0], Double.parseDouble(parts[1]));

	}
	

        
        
    
}
