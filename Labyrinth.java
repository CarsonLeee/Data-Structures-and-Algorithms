//Name: clee887
//Date: December 7th, 2020
//Class: CS 2210
//Professor: Prof.Solis-Oba
//Description: Assignment 5

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Labyrinth {

	//Declare variables
	private Graph graph;
	private Node entrance;
	private Node exit;
	private int width;
	private int length;
	private int[] keys;

	//Constructor of the class
	public Labyrinth(String inputFile) throws LabyrinthException {
		//try-catch that handles any possible error on the file
		try {
			//Use a Scanner to open and read the file
			Scanner scan = new Scanner(new File(inputFile));
			//The first three lines are scale factor, width and length
			scan.nextInt();
			width = scan.nextInt();
			length = scan.nextInt();
			//The next line (10 integers) are the number of keys per type
			keys = new int[10];
			for(int i = 0; i < 10; i++) {
				keys[i] = scan.nextInt();
			}
			scan.nextLine();
			//Initialize the graph with the total number of rooms we have (width times length)
			graph = new Graph(width*length);
			//Main grid distribution of the labyrinth reads by pairs of lines in order to connect the nodes of one row with the next row
			for(int i = 0; i < length; i++) {
				String rooms = scan.nextLine();
				//If on any row except the last, try to connect it with next row
				if(i < length-1) {
					String bottomEdges = scan.nextLine();
					//To connect it with bottom rows, read each of the chars, with jumps of 2, and checking if an edge exists or not
					int rowRum = 0;
					for(int j = 0; j < bottomEdges.length(); j += 2) {
						char c = bottomEdges.charAt(j);
						//Check for c to not be a wall
						if(c != 'w') {
							//If not a wall, have an edge being either a corridor or a door
							Node u = graph.getNode(i*width+rowRum);
							Node v = graph.getNode((i+1)*width+rowRum);
							if(c == 'c') {
								//Have a corridor (type not needed, can be set as -1, and default label is corridor)
								graph.insertEdge(u, v, -1);
							} else {
								//Set the type as the value of the integer on the char
								graph.insertEdge(u, v, Character.getNumericValue(c), "door");
							}
						}
						//Increase rowRum by 1
						rowRum++;
					}
				}
				//Connect the rooms with the rooms after them, except the last one that does not have a door in front.
				int rowRum = 0;
				for(int j = 0; j < rooms.length(); j += 2) { //The loop starts at 1 and increases by 2 to check all possible edges
					char r = rooms.charAt(j);
					//Check if room has a room in front
					if(j < rooms.length()-1) {
						//Check for char to not be a wall
						char c = rooms.charAt(j+1);
						if(c != 'w') {
							//If not a wall, we have an edge being either a corridor or a door between nodes with 
							//names i*width+rowRum and i*width+(rowRum+1)
							Node u = graph.getNode(i*width+rowRum);
							Node v = graph.getNode(i*width+rowRum+1);
							if(c == 'c') {
								//We have a corridor (type not needed, can be set as -1, and default label is corridor)
								graph.insertEdge(u, v, -1);
							} else {
								//We have a door that we set the type as the value of the integer on the char
								graph.insertEdge(u, v, Character.getNumericValue(c), "door");
							}
						}
					}
					//Update entrance or exit node (if needed)
					Node node = graph.getNode(i*width+rowRum);
					if(r == 's') {
						entrance = node;
					} else if (r == 'x') {
						exit = node;
					}
					//Increase rowRum by 1
					rowRum++;
				}
			}
		} catch (FileNotFoundException e) {
			//Error for inexistent file
			graph = null;
			throw new LabyrinthException("Input file does not exist");
		} catch(Exception e) {
			//For any other Exception, we should have an error on the format
			graph = null;
			throw new LabyrinthException("Invalid format for input file");
		}
	}

	//Getter for the graph
	public Graph getGraph() {
		return graph;
	}

	//Method that solves the Labyrinth and returns an Iterator containing the path to traverse
	public Iterator solve() throws GraphException {
		//In order to solve the labyrinth, we can perform a DFS, starting at the entrance, traversing all connected edges 
		ArrayList<Node> path = new ArrayList<Node>(); //Define arraylist that ill store the path taken. 
		HashMap<Node, Integer> keysUsed = new HashMap<Node, Integer>(); //We can use hashmap pto map node integer pairs athw ill represent the key preceding a room
		//We start setting the entrance node as marked (visited) and adding it to path
		entrance.setMark(true);
		path.add(entrance);
		//We now call for the solve recursive method to check if a path can be found and we return its result
		return solve(entrance, path, keysUsed);
	}

	//Recursive method that performs a DFS starting at given node and checking if a path can be found to the exit
	//Returns an Iterator if the path was found, null otherwise
	private Iterator solve(Node node, ArrayList<Node> path, HashMap<Node, Integer> keysTaken) throws GraphException {
		//First check if the node is the exit already
		if(node == exit) {
			//If so, we found the path. Then, we return the iterator of the path ArrayList
			return path.iterator();
		} else {
			//If not the exit yet, we check for all the neighbors of current node
			Iterator<Edge> neighborRooms = graph.incidentEdges(node);
			//Loop through them in order to check for unmarked rooms that can be visited
			while(neighborRooms.hasNext()) {
				Edge edge = neighborRooms.next();
				//For current edge, first get the next room's node
				Node next = edge.firstEndpoint() == node ? edge.secondEndpoint() : edge.firstEndpoint();
				//Now, try to visit the next room if it is not visited yet
				if(!next.getMark()) {
					//Check the type of the edge to check if we can visit the next room
					boolean possible = false;
					if(edge.getLabel().equals("corridor")) {
						//If corridor, we can go there
						possible = true;
					} else {
						//If door, we check if we have a valid key to open it, loop through the list of available keys in order to check the smallest one that can open the door
						int type = edge.getType();
						for(int i = type; i < 10; i++) {
							if(keys[i] > 0) {
								//We found the smallest that can open the door. We add it to keysTaken, remove it from available keys and we set possible as true
								keysTaken.put(next, i);
								keys[i]--;
								possible = true;
								break;
							}
						}
					}
					//Then, check if we can go to next room
					if(possible) {
						//This means we can visit the next room. We set it as marked (visited), we add it to path and we call recursively on it to check if a path can be found for that path
						path.add(next);
						next.setMark(true);
						Iterator<Node> solution = solve(next, path, keysTaken);
						if(solution != null) {
							//If the exit was found on that path, we return the result
							return solution;
						} else {
							//If not found, we remove the node from the path, we set it as unmarked and we release its key taken (if any)
							path.remove(path.size()-1);
							next.setMark(false);
							if(keysTaken.containsKey(next)) {
								int key = keysTaken.remove(next);
								keys[key]++;
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
