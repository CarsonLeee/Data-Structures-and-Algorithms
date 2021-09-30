//Class that represents an Icon Pixel
public class Pixel {
		
	//Fields of the class
	private Position pos;
	private int color;
	
	//Constructor of the class
	public Pixel(Position p, int color) {
		this.pos = p;
		this.color = color;
	}
	
	//Getter for position
	public Position getPosition() {
		return pos;
	}
	
	//Getter for color
	public int getColor() {
		return color;
	}
}
