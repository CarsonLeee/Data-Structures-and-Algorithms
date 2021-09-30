import java.util.ArrayList;
import java.util.Iterator;

//Class that represents a Graph
public class Graph implements GraphADT {

	//Fields of the graph
	private ArrayList<Node> nodes;
	private Edge[][] adjMatrix;

	//Constructor of the graph
	public Graph(int n) {
		//We init both nodes list and the adjacency matrix with n nodes
		nodes = new ArrayList<Node>();
		adjMatrix = new Edge[n][n];
		//We create one node for every 0,1,...,n-1
		for(int i = 0; i < n; i++) {
			nodes.add(new Node(i));
		}
	}

	//Adds an edge connecting the nodes given
	public void addEdge(Node nodeu, Node nodev, String busLine) throws GraphException {
		//We first check if any of the nodes does not exist or if an edge already exists for them
		if(nodeu.getName() < 0 || nodeu.getName() >= nodes.size() || nodev.getName() < 0 
				|| nodev.getName() >= nodes.size() || adjMatrix[nodeu.getName()][nodev.getName()] != null) {
			//If so, we throw an exception
			throw new GraphException("At least one of the nodes does not exist or it exists and "
					+ "edge already for them.");
		}
		//If both exist and no edge for them, we add it
		Edge edge = new Edge(nodeu, nodev, busLine);
		adjMatrix[nodeu.getName()][nodev.getName()] = edge;
		adjMatrix[nodev.getName()][nodeu.getName()] = edge;
	}

	//Returns the Node object with given name
	public Node getNode(int name) throws GraphException {
		//First, we check if the node does not exist
		if(name < 0 || name >= nodes.size()) {
			//If it doesn't exist, we throw an exception
			throw new GraphException("Node does not exist");
		}
		//If valid, we return the node with given name
		return nodes.get(name);
	}

	//Returns an iterator for the incident edges
	public Iterator<Edge> incidentEdges(Node u) throws GraphException {
		//First, we check if the node does not exist
		if(u.getName() < 0 || u.getName() >= nodes.size()) {
			//If it doesn't exist, we throw an exception
			throw new GraphException("Node does not exist");
		}
		//If it exists, we loop through all the row representing its edges and we create an ArrayList to store them
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int name = u.getName();
		for(int i = 0; i < adjMatrix.length; i++) {
			//The edge exists if it's not null
			if(adjMatrix[name][i] != null) {
				edges.add(adjMatrix[name][i]);
			}
		}
		//Finally, we return the iterator of the ArrayList if it has at least one edge, or null if it does not
		if(edges.size() > 0) {
			return edges.iterator();
		}
		return null;
	}

	//Returns the edge connecting both nodes
	public Edge getEdge(Node u, Node v) throws GraphException {
		//We first check if any of the nodes does not exist or if an edge does not exist for them
		if(u.getName() < 0 || u.getName() >= nodes.size() || v.getName() < 0 
				|| v.getName() >= nodes.size() || adjMatrix[u.getName()][v.getName()] == null) {
			//If so, we throw an exception
			throw new GraphException("At least one of the nodes does not exist or and"
					+ " edge betweem them does not exist");
		}
		//If both exist and there's a node in between them, we return it
		return adjMatrix[u.getName()][v.getName()];
	}

	//Checks if two nodes are adjacent, meaning there exists and edge between them
	public boolean areAdjacent(Node u, Node v) throws GraphException {
		//We first check if any of the nodes does not exist
		if(u.getName() < 0 || u.getName() >= nodes.size() || v.getName() < 0 || v.getName() >= nodes.size()) {
			//If so, we throw an exception
			throw new GraphException("At least one of the nodes does not exist");
		}
		//If valid, we check if there's a edge for them
		return adjMatrix[u.getName()][v.getName()] != null;
	}
}
