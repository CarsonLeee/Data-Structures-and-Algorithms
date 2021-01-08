//Name: clee887
//Date: December 7th, 2020
//Class: CS 2210
//Description: Assignment 5

//Class that represents an Edge
public class Edge {
	
	//Variables
	private Node u, v;
	private int type;
	private String label;
	
	//Constructor that receives the endpoints and the type
	public Edge(Node u, Node v, int type) {
		this.u = u;
		this.v = v;
		this.type = type;
		this.label = "corridor"; //corridor as default
	}
	
	//Constructor that receives the endpoints, type and label
	public Edge(Node u, Node v, int type, String label) {
		this.u = u;
		this.v = v;
		this.type = type;
		this.label = label;
	}
	
	//Getters for endpoints
	public Node firstEndpoint() {
		return u;
	}
	
	//Getters for endpoints
	public Node secondEndpoint() {
		return v;
	}
	
	//Getter for type
	public int getType() {
		return type;
	}
	
	//Setter for type
	public void setType(int type) {
		this.type = type;
	}
	
	//Getter for label
	public String getLabel() {
		return label;
	}
	
	//Setter for label
	public void setLabel(String label) {
		this.label = label;
	}
}
