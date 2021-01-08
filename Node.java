//Name: clee887
//Date: December 7th, 2020
//Description: Assignment 5

//Class that represents a Node
public class Node {
	
	//Variables
	private int name;
	private boolean mark;
	
	//Constructor of the class
	public Node(int name) {
		this.name = name;
		this.mark = false;
	}
	
	//Setter for mark
	public void setMark(boolean mark) {
		this.mark = mark;
	}
	
	//Getter for mark
	public boolean getMark() {
		return mark;
	}
	
	//Getter for name
	public int getName() {
		return name;
	}
}
