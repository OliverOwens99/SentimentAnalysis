# SentimentAnalysis

So in this programme I can prompt the user for a response, and then based on that response prompt them to enter the paths of various files and or lexicons. It then parses the lexicon and checks it against the file entered and come up with a total after it has compared the words in the file against said lexicon. There is then a console output displaying time waiting. The console will then show a list of all the words in the lexicon and there value ofr error tracking aswell as displaying a number to the console if an output-file has been entered then it will also output to the file. It uses virtual threads and structured concurrency to achieve going over the lexicon and then going over the file. There is also a progress tracker which sort of half works. There is some error handling throughout.
