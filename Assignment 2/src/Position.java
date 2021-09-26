//Name: clee887
//Date: March 24th, 2021
//Description: Programming Assignment 2

//This class represents the position (x,y) of a pixel.
public class Position {
	
	//Fields of the class
	private int x, y;
	
	//Constructor of the class
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//Getter for x coordinate
	public int xCoord() {
		return x;
	}
	
	//Getter for y coordinate
	public int yCoord() {
		return y;
	}
	
	//Compares two Position objects using row order
	public int compareTo(Position p) {
		//Check y positions
		if(this.y != p.y) {
			//If different, we check which one is smaller
			return this.y < p.y ? -1 : 1;
		} else {
			//If equal, we check which x is smaller or if equal
			return this.x == p.x ? 0 : (this.x < p.x ? -1 : 1);
		}
	}
}