//Name: clee887
//Date: 04/05/2021
//Class: CS 2210B
//Description: Programming Assignment 3

//Class that represents a Node of a Graph
public class Node {
	
	//Fields of the class
	private int name;
	private boolean mark;
	
	//Constructor of the node
	public Node(int name) {
		this.name = name;
		this.mark = false;
	}
	
	//Setter for mark
	public void setMark(boolean newMark) {
		this.mark = newMark;
	}
	
	//Getter for mark
	public boolean getMark() {
		return mark;
	}
	
	//Getter for name
	public int getName() {
		return name;
	}
	
	//Checks if this node and parameter node are equal
	public boolean equals(Node otherNode) {
		return this.name == otherNode.name;
	}
}
