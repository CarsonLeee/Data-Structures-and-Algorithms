//Class that represents an Edge
public class Graph {
	
	//Fields of the class
	private Node first, second;
	private String busLine;
	
	//Constructor of the class
	public Edge(Node u, Node v, String busLine) {
		this.first = u;
		this.second = v;
		this.busLine = busLine;
	}
	
	//Getter for first endpoint
	public Node firstEndpoint() {
		return first;
	}
	
	//Getter for second endpoint
	public Node secondEndpoint() {
		return second;
	}
	
	//Getter for bus line
	public String getBusLine() {
		return busLine;
	}
}