import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

//Class that represents a Route Planner
public class Planner {

	//Fields of the class
	private Graph graph;
	private Node start;
	private Node destination;
	private int width;
	private int length;
	private HashSet<String> B;

	//Constructor of the class
	public Planner(String inputFile) throws MapException {
		//use a try-catch that can handle any possible error on the file
		try {
			//We use a Scanner to open and read the file
			Scanner scan = new Scanner(new File(inputFile));
			//The first three lines are unused, width and length
			scan.nextInt();
			width = scan.nextInt();
			length = scan.nextInt();
			scan.nextLine();
			//The next line contains the bus lines within the B set
			String line = scan.nextLine();
			String[] parts = line.split(" ");
			B = new HashSet<>();
			for(String p : parts) {
				B.add(p);
			}
			//initialize the graph with the total number of intersections and dead-ends we have
			graph = new Graph(width*length);
			//Start reading by pairs of lines in order to connect the nodes (intersections) of one row with the next row
			for(int i = 0; i < length; i++) {
				String streets = scan.nextLine();
				//If we're on any row except the last, we can try to connect it with next row
				if(i < length-1) {
					String bottomEdges = scan.nextLine();
					//Read each of the chars, with jumps of 2, and checking if an edge exists (and what bus line) or not
					int rowRum = 0;
					for(int j = 0; j < bottomEdges.length(); j += 2) {
						//We check for char to be a street with bus lines
						char c = bottomEdges.charAt(j);
						if(Character.isLetter(c)) {
							//If a valid bus line, we have an edge with names i*width+rowRum and (i+1)*width+rowRum
							Node u = graph.getNode(i*width+rowRum);
							Node v = graph.getNode((i+1)*width+rowRum);
							graph.addEdge(u, v, c+"");
						}
						//Increase rowRum by 1
						rowRum++;
					}
				}
				//connect the intersections with the intersections after them (if possible), except the last one that does not have an intersection in front. The loop starts at 1 and increases by 2 to check all possible edges
				int rowRum = 0;
				for(int j = 0; j < streets.length(); j += 2) {
					char r = streets.charAt(j);
					Node u = graph.getNode(i*width+rowRum);
					//Check if intersection has an intersection in front
					if(j < streets.length()-1) {
						//We check for char to be a street with bus lines
						char c = streets.charAt(j+1);
						if(Character.isLetter(c)) {
							//If a valid bus line, we have an edge with names i*width+rowRum and i*width+(rowRum+1)
							Node v = graph.getNode(i*width+rowRum+1);
							graph.addEdge(u, v, c+"");
						}
					}
					//Update starting point or destination (if needed)
					if(r == 'S') {
						start = u;
					} else if (r == 'D') {
						destination = u;
					}
					//Increase rowRum by 1
					rowRum++;
				}
			}
		} catch (FileNotFoundException e) {
			//The file does not exist
			throw new MapException("Input file does not exist");
		} catch(Exception e) {
			//For any other Exception, we should have an error on the format
			graph = null;
			throw new MapException("Invalid format for input file");
		}
	}

	//Getter for the graph
	public Graph getGraph() {
		return graph;
	}

	//Traverses the city map and returns an iterator containing the nodes along the path from starting point to destination. It uses at most one bus line from set B. Returns null if the path does not exist
	public Iterator planTrip() throws GraphException {
		//perform a DFS, starting at the starting point, traversing all connected edges (the valid bus lines) until we reach the destination (if possible). Define an ArrayList that will store the path taken so far. 
		ArrayList<Node> path = new ArrayList<Node>();
		String usedFromB = "";
		int count = 0;
		//We start setting the starting node as marked (visited) and adding it to path
		start.setMark(true);
		path.add(start);
		//We now call for the planTrip recursive method to check if a path can be found and we return its result
		return planTripRecur(start, path, usedFromB, count);
	}

	//Recursive method that performs a DFS starting at given node and checking if a path can be found to the destination. Returns an Iterator if the path was found, null otherwise
	private Iterator<Node> planTripRecur(Node node, ArrayList<Node> path, String usedFromB, int count) throws GraphException {
		//We first check if the node is the destination already
		if(node == destination) {
			//If so, we're done, we found the path. Then, we return the iterator of the path ArrayList
			return path.iterator();
		} else {
			//If not the exit yet, we check for all the neighbors of current node
			Iterator<Edge> neighborIntersections = graph.incidentEdges(node);
			//We loop through them in order to check for unvisited (unmarked) intersections that can be visited
			while(neighborIntersections.hasNext()) {
				Edge edge = neighborIntersections.next();
				//For current edge, we first get the next intersection's node
				Node next = edge.firstEndpoint() == node ? edge.secondEndpoint() : edge.firstEndpoint();
				//Now, we try to visit the next intersection if it is not visited yet
				if(!next.getMark()) {
					//check the type of the edge (bus line) to check if we can visit the next intersection, we check if the bus line belongs to B set or not.
					if(!B.contains(edge.getBusLine()) || usedFromB.equals("") || usedFromB.equals(edge.getBusLine()))  {
						//we can visit the next intersection. First, we set usedFromB and increase count if the bus line belongs to the B set
						if(B.contains(edge.getBusLine())) {
							usedFromB = edge.getBusLine();
							count++;
						}
						//Now, we set next as marked (visited), we add it to path and we call recursively on it 
						//to check if a path can be found for that path
						path.add(next);
						next.setMark(true);
						Iterator<Node> solution = planTripRecur(next, path, usedFromB, count);
						if(solution != null) {
							//If the exit was found on that path, we return the result
							return solution;
						} else {
							//If not found, we remove the intersection's node from the path, we set it as unmarked
							//and we decrease count (if needed)
							path.remove(path.size()-1);
							next.setMark(false);
							if(B.contains(edge.getBusLine())) {
								count--;
								//Here, if count reaches 0 again, we release the current usedFromB
								if(count == 0) {
									usedFromB = "";
								}
							}
						}
					}
				}
			}
			//If no solutions were found for current path, we return null
			return null;
		}
	}
}
