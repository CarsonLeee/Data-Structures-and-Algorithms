import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//Main class to compress files
public class Compress {

	//Main method that starts the app
	public static void main(String[] args) throws DictionaryException, IOException {		
		//Initialize Dictionary with size 7621
		DictionaryADT dict = new Dictionary(7621);
		//Put 255 different bytes into the dictionary
		for(int i = 0; i < 255; i++) {
			String key = Character.toString((char)i);
			int code = i;
			dict.insert(new DictEntry(key, code));
		}

		//Create object to read the input file (by bytes) and to write the output file
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(args[0]));
		//Remove extension (if any) and add .zzz for outfile
		int index = args[0].indexOf(".");
		index = index == -1 ? args[0].length() : index;
		String outfile = args[0].substring(0, index)+".zzz";
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outfile));
		//Read first byte and init String s
		int n = 256;
		int nextByte = in.read();
		String s = Character.toString((char)nextByte);
		//Loop as long as the file has bytes to read
		boolean completed = false;
		while(!completed) {
			//Loop to read next bytes until end of file or a String is reached that does not exist on dictionary
			while(dict.find(s) != null) {
				//Get next byte
				nextByte = in.read();
				//If byte is -1, we break
				if(nextByte == -1) {
					break;
				}
				//If valid byte, cast it to char and append it to s
				s += (char)nextByte;
			}
			//Once the loop ends, get s2. If no DictEntry object in dictionary has key s, remove last char, otherwise, make it equal to s
			String s2 = s;
			if(dict.find(s2) == null) {
				s2 = s2.substring(0, s2.length()-1);
			}
			//Get DictEntry object with given key s
			DictEntry entry = dict.find(s2);
			//Get its code and write it to file
			int k = entry.getCode();
			MyOutput.output(k, out);
			//Check if there's a next byte to consider
			if(nextByte == -1) {
				//If the file was read completely, stop the loop
				completed = true;
			} else {
				//Otherwise, check if num of elements is less than 4096
				if(dict.numElements() < 4096) {
					//If so, create new DictEntry with key = s and code = n, then insert it into dictionary
					DictEntry newEntry = new DictEntry(s, n);
					dict.insert(newEntry);
				}
				//Delete all chars from s except the last one and increase n
				s = s.substring(s.length()-1);
				n++;
			}
		}
		//Once the file is read completely, we flush any remaining bits into the file
		MyOutput.flush(out);
		out.close();
		in.close();
	}
}
