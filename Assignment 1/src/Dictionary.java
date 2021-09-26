//Name: clee887
//Date: 02/15/2021
//Class: CS 2210B
//Description: Programming Assignment 1

//Class that represents a Dictionary
public class Dictionary implements DictionaryADT {

	//Constant DictEntry representing a deleted spot
	private final DictEntry DELETED = new DictEntry("", -1);

	//Fields of the class
	private DictEntry[] table; //hash table for entries
	private int numElements; //real size of table

	//Constructor of the Dictionary. Receives the max size of table as parameter
	public Dictionary(int size) {
		//Initialize table array with given size
		table = new DictEntry[size];
	}

	//Inserts a new DictEntry into the dictionary. Returns 1 if a collision, 0 otherwise. Throws an Exception if the pair's key already exists or if the dictionary is full
	public int insert(DictEntry pair) throws DictionaryException {
		//First, if the dictionary is full, we throw an Exception
		if(numElements == table.length) {
			throw new DictionaryException("The dictionary is full.");
		}
		//Then, if the pair's key exists already, we throw an Exception
		if(find(pair.getKey()) != null) {
			throw new DictionaryException("The pair's key already exists.");
		}
		//If not full and key does not exist, we need to insert it into the table (if it does not exist already). We start getting the first hash value and checking if it's occupied
		int index1 = hash1(pair.getKey());
		//Now, we check if we would have a collision or not
		int collision = 0;
		if(table[index1] != null) {
			//If index exists, we now loop to find the right place for it (an empty spot or a spot flagged as DELETED). Also, we set collision as 1
			collision = 1;
			int index2 = hash2(pair.getKey());
			int i = 1;
			while(true) {
				int index = (index1 + i*index2) % table.length;
				if(table[index] == null || (table[index] != null && table[index] == DELETED)) {
					//This means the index is available to take
					index1 = index;
					break;
				}
				i++; //increase i for next loop, so next index
			}
		}
		//Once the right index was found, we insert the entry into it, increase size of table and return the collision int
		table[index1] = pair;
		numElements++;
		return collision;
	}

	//Removes the DictEntry with given key. Throws an Exception if the key does not exist
	public void remove(String key) throws DictionaryException {
		//We loop through the table, using both hash functions, looking for the possible DictEntry with the given key
		int index1 = hash1(key);
		int index2 = hash2(key);
		int i = 0;
		boolean found = false;
		while(!found) {
			//Get next index and check if it has the key needed
			int index = (index1 + i*index2) % table.length;
			if(table[index] != null && table[index].getKey().equals(key)) {
				//We found it and we set DELETED into this index and we decrease the number of elements
				table[index] = DELETED;
				numElements--;
				found = true;
			} else if(table[index] == null) {
				//We haven't found it and we ran out of elements to check, so it does not exist
				break;
			}
			i++; //To check for next possible entry
		}
		//Once the loop ends, if not found, we throw an Exception
		if(!found) {
			throw new DictionaryException("The key does not exist in the dictionary.");
		}
	}

	//Looks for the DictEntry object with given key. Returns null if it does not exist
	public DictEntry find(String key) {
		//We loop through the table, using both hash functions, looking for the possible DictEntry with the given key
		int index1 = hash1(key);
		int index2 = hash2(key);
		int i = 0;
		while(true) {
			//Get next index and check if it has the key needed
			int index = (index1 + i*index2) % table.length;
			if(table[index] != null && table[index].getKey().equals(key)) {
				//We found it, so we return the whole object
				return table[index];
			} else if(table[index] == null) {
				//This means we haven't found it and we ran out of elements to check, so it does not exist
				break;
			}
			i++; //To check for next possible entry
		}
		//If the loop breaks with no returns, it means the key does not exist
		return null;
	}

	//Returns the num of elements within the dictionary
	public int numElements() {
		//Returns the actual size of the table
		return numElements;
	}

	//Helper function that returns the first hash value of the given key
	private int hash1(String key) {
		//We get the hash value of the String by adjusting the hash value according to each of the chars
		int hash = 7;
		for(int i = 0; i < key.length(); i++) {
		    hash = hash*13 + key.charAt(i);
		}
		return Math.abs(hash) % table.length;
	}

	//Helper function that returns the second hash value of the given key
	private int hash2(String key) {
		//We get the hash value of the String by using a custom prime number in our calculation
		int prime = 7;
		return prime - (key.length() % prime);
	}
}