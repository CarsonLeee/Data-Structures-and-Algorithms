//Class that represents a Dictionary Entry
public class DictEntry {
	
	//Fields of the class
	private String key;
	private int code;
	
	//Constructor of the class
	public DictEntry(String theKey, int theCode) {
		this.key = theKey;
		this.code = theCode;
	}
	
	//Getter for key
	public String getKey() {
		return key;
	}
	
	//Getter for code
	public int getCode() {
		return code;
	}
	
	//Checks if two DictEntry objects are equal, same key and same code
	public boolean isEqual(DictEntry secondObject) {
		return this.key.equals(secondObject.key) && this.code == secondObject.code;
	}
}