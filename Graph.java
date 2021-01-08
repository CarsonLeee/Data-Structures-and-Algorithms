//Name: clee887
//Date: December 7th, 2020
//Class: CS 2210
//Professor: Prof.Solis-Oba
//Description: Assignment 5

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph implements GraphADT {

	//Fields of the class
	private Edge[][] adjMatrix;
	private HashMap<Integer, Node> nodes;

	//Constructor of the class
	public Graph(int n) {
		//Initialize the adjacency matrix with length n x n and we create a Node object for 0,1, ..., n-1
		adjMatrix = new Edge[n][n];
		nodes = new HashMap<Integer, Node>(); //The HashMap is used to map every name to its respective Node object
		for(int i = 0; i < n; i++) {
			//Create node with name "i" and add it to nodes map
			Node node = new Node(i);
			nodes.put(i, node);
		}
	}

	public void insertEdge(Node u, Node v, int type, String label) throws GraphException {
		//Check that both nodes exist
		if(u.getName() < 0 || u.getName() >= adjMatrix.length || v.getName() < 0 || v.getName() >= adjMatrix.length) {
			throw new GraphException("There is at least one invalid node");
		}
		//Now, we check that an edge does not exist already for the nodes
		if(adjMatrix[u.getName()][v.getName()] != null) {
			throw new GraphException("An edge for the nodes already exist");
		}
		//If all valid, we create an Edge for the nodes and we add it to the adjacency matrix
		Edge edge = new Edge(u, v, type, label);
		adjMatrix[u.getName()][v.getName()] = edge;
		adjMatrix[v.getName()][u.getName()] = edge;
	}

	public void insertEdge(Node u, Node v, int type) throws GraphException {
		//First, we check that both nodes exist
		if(u.getName() < 0 || u.getName() >= adjMatrix.length || v.getName() < 0 || v.getName() >= adjMatrix.length) {
			throw new GraphException("There is at least one invalid node");
		}
		//Check that an edge does not exist already for the nodes
		if(adjMatrix[u.getName()][v.getName()] != null) {
			throw new GraphException("An edge for the nodes already exist");
		}
		//If true, create an Edge for the nodes and set it in the adjacency matrix at respective indices
		Edge edge = new Edge(u, v, type);
		adjMatrix[u.getName()][v.getName()] = edge;
		adjMatrix[v.getName()][u.getName()] = edge;
	}

	public Node getNode(int u) throws GraphException {
		//Check that the node exists on the graph
		if(u < 0 || u >= adjMatrix.length) {
			//If it does not exist, we throw an exception
			throw new GraphException("Node does not exist");
		}
		//If it exists, use the HashMap to return the Node object for it
		return nodes.get(u);
	}

	public Iterator<Edge> incidentEdges(Node u) throws GraphException {
		//First check that the node exists on the graph
		if(u.getName() < 0 || u.getName() >= adjMatrix.length) {
			//If it does not exist, throw an exception
			throw new GraphException("Node does not exist");
		}
		//If it exists, loop through all the row representing its edges and create an ArrayList to store them
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int name = u.getName();
		for(int i = 0; i < adjMatrix.length; i++) {
			//The edge exists if it's not null
			if(adjMatrix[name][i] != null) {
				edges.add(adjMatrix[name][i]);
			}
		}
		//Finally, return the iterator of the ArrayList if it has at least one edge, or null if it does not
		if(edges.size() > 0) {
			return edges.iterator();
		}
		return null;
	}

	public Edge getEdge(Node u, Node v) throws GraphException {
		//First, check that both nodes exist
		if(u.getName() < 0 || u.getName() >= adjMatrix.length || v.getName() < 0 || v.getName() >= adjMatrix.length) {
			//This means there's at least one invalid node
			throw new GraphException("There is at least one invalid node");
		}
		//Now, check if an edge exists for both nodes
		if(adjMatrix[u.getName()][v.getName()] == null) {
			//This means an edge does not exists
			throw new GraphException("There is no edge for the nodes");
		}
		//If valid nodes and an edge exists, return it
		return adjMatrix[u.getName()][v.getName()];
	}

	public boolean areAdjacent(Node u, Node v) throws GraphException {
		//First, check that both nodes exist
		if(u.getName() < 0 || u.getName() >= adjMatrix.length || v.getName() < 0 || v.getName() >= adjMatrix.length) {
			//This means there's at least one invalid node
			throw new GraphException("There is at least one invalid node");
		}
		//If both exist, just check if an edge exists between them or not
		return adjMatrix[u.getName()][v.getName()] != null;
	}
}
