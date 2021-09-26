import java.io.*;
import java.util.*;


public class UI {

    private static final int TEXT = 1, SOUND = 2, IMAGE = 3, COMMAND = 4, HTML = 5;
    private static final int num_types = 5;
    private static final int[] types={TEXT,IMAGE,HTML,SOUND,COMMAND};

  /* ------------------------------------------- */
  public static void main(String[] args) {
  /* ------------------------------------------- */
     StringReader sr;
     OrderedDictionary dict;

     if (args.length != 1) {
       System.out.println("Usage: java Query fileName");
       System.exit(0);
     }
     try {
       sr = new StringReader();
       BufferedReader in = new BufferedReader(new FileReader(args[0]));
       String word = in.readLine();
       String definition;

       dict = new OrderedDictionary();

       // Insert the words from the input file into the dictionary
       while (word != null) {
         try {
	   word = word.toLowerCase();
           definition = in.readLine();

	   if (definition.length() > 3){
               if ((definition.charAt(definition.length()-4) == '.') &&
		   (definition.charAt(definition.length()-3) == 'w'))
		   dict.insert(new Record(new Key(word,2),definition));
               else if ((definition.charAt(definition.length()-4) == '.') &&
		   (definition.charAt(definition.length()-3) == 'm'))
	          dict.insert(new Record(new Key(word,2),definition));
		else if (definition.charAt(definition.length()-4) == '.' &&
		   (definition.charAt(definition.length()-3) == 'j'))
	          dict.insert(new Record(new Key(word,3),definition));
		else if (definition.charAt(definition.length()-4) == '.' &&
		   (definition.charAt(definition.length()-3) == 'g'))
	          dict.insert(new Record(new Key(word,3),definition));
		else if (definition.contains(".exe"))
	          dict.insert(new Record(new Key(word,COMMAND),definition));
		else if (definition.contains(".html") || definition.contains("HTML"))
	          dict.insert(new Record(new Key(word,HTML),definition));
		  else dict.insert(new Record(new Key(word,1),definition));
           }
           else {
			dict.insert(new Record(new Key(word,1),definition));
	   }
         }
         catch (DictionaryException e) {
           System.out.println("Key "+word+" already in dictionary");
         }
         word = in.readLine();
       }

       boolean terminate = false;
       while (!terminate) {
          try {
             terminate = processCommand(sr,dict);
          }
	  catch (DictionaryException e) {
            System.out.println(e.getMessage());
	  }
	  catch (MultimediaException ed) {
            System.out.println(ed.getMessage());
	  }
          catch (Exception ex) {
            System.out.println("Invalid command");
          }
       }
     }
     catch (IOException e) {
       System.out.println("Could not open file");
     }
  }

 /* -------------------------------------------------------------------------- */
  private static boolean processCommand(StringReader sr, 
                                        OrderedDictionary dict) throws 
                                        DictionaryException,
                                        MultimediaException {
 /* -------------------------------------------------------------------------- */
    Record res, succ;
    int i;
    SoundPlayer sp = new SoundPlayer();
    PictureViewer ip = new PictureViewer();
    RunCommand c = new RunCommand();
	ShowHTML h = new ShowHTML();

    Key k;
    int type;
    String word, data, line, command;
    int num_null = 0;

    line = sr.read("Enter command: ");
    StringTokenizer st = new StringTokenizer(line);
    command = st.nextToken();
    command = command.toLowerCase();

    if (st.hasMoreTokens()) {
	word = st.nextToken();
	word = word.toLowerCase();
    }
    else word = "";

    if (st.hasMoreTokens()) type = Integer.parseInt(st.nextToken());
    else type = 0;

    if (command.compareTo("end") == 0) return true;

    if (command.compareTo("search") == 0) {
	for (i = 0; i < num_types; ++i) {
	    k = new Key(word,types[i]);
	    res = dict.find(k);
	    if (res == null) ++num_null;
	    else {
		if (types[i] == TEXT)
		    System.out.println(res.getData());
		else if (types[i] == SOUND) sp.play(res.getData());
		else if (types[i] == IMAGE) ip.show(res.getData());
		else if (types[i] == COMMAND) c.run(res.getData());
		else if (types[i] == HTML) h.show(res.getData());
	    }
	}
	if (num_null == num_types) System.out.println("Key not in dictionary");
    }
    else if (command.compareTo("remove") == 0) 
	if (type == 0) System.out.println("Invalid command");
	else dict.remove(new Key(word,type));

    else if (command.compareTo("insert") == 0) {
	String def = "";
	int numTokens = 0;
	while (st.hasMoreTokens()) {
	    def = def + st.nextToken();
	    if (st.hasMoreTokens()) def = def + " ";
	    ++numTokens;
	}

	boolean notInserted = true;
	if (numTokens == 1) 
	    if (def.charAt(def.length()-4) == '.')
		if (def.charAt(def.length()-3) == 'w' || def.charAt(def.length()-3) == 'm') {
		    dict.insert(new Record(new Key(word,2),def));
		    notInserted = false;
		}
		else if (def.charAt(def.length()-3) == 'j' || def.charAt(def.length()-3) == 'g') {
		    dict.insert(new Record(new Key(word,3),def));
		    notInserted = false;
		}
	if (notInserted) dict.insert(new Record(new Key(word,1),def));
    }
    else if (command.compareTo("next") == 0) {
	if (type == 0) System.out.println("Invalid command");
	else {
	    res = dict.successor(new Key(word,type));
	    if (res == null) System.out.println("Key has no successor");
	    else System.out.println("("+res.getKey().getWord()+","+res.getKey().getType()+")");
	}
    }
    else if (command.compareTo("prev") == 0) {
	if (type == 0) System.out.println("Invalid command");
	else {
	    res = dict.predecessor(new Key(word,type)) ;
	    if (res == null) System.out.println("Key has no predecessor");
	    else System.out.println("("+res.getKey().getWord()+","+res.getKey().getType()+")");
	}
    }
    else if (command.compareTo("first") == 0) {
	res = dict.smallest();
	if (res == null) System.out.println("Dictionary is empty");
	else System.out.println("("+res.getKey().getWord()+","+res.getKey().getType()+")");
    }
    else if (command.compareTo("last") == 0){
	res = dict.largest();
	if (res == null) System.out.println("Dictionary is empty");
	else System.out.println("("+res.getKey().getWord()+","+res.getKey().getType()+")");
    }

    else System.out.println("Invalid command");

    return false;
  }
}
