//Name: clee887
//Date: March 24th, 2021
//Description: Programming Assignment 2

//Class that represents an Icon
public class Icon implements IconADT {

	//Fields of the class
	private int id, width, height;
	private String type;
	private Position pos;
	private BinarySearchTreeADT pixels;

	//Constructor of the Icon
	public Icon(int id, int width, int height, String type, Position pos) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.type = type;
		this.pos = pos;
		pixels = new BinarySearchTree(); //tree to store pixels
	}

	//Getter for width
	public int getWidth() {
		return width;
	}

	//Getter for height
	public int getHeight() {
		return height;
	}

	//Getter for type
	public String getType() {
		return type;
	}

	//Getter for id
	public int getId() {
		return id;
	}

	//Getter for offset (position)
	public Position getOffset() {
		return pos;
	}

	//Setter for offset (position)
	public void setOffset(Position value) {
		this.pos = value;
	}

	//Setter for type
	public void setType(String t) {
		this.type = t;
	}

	//Adds a new Pixel into the current Icon
	public void addPixel(Pixel pix) throws DuplicatedKeyException {
		//Put new pixel into pixels BST
		pixels.put(pixels.getRoot(), pix);
	}

	//Checks if the input Icon intersects the current Icon
	public boolean intersects(Icon fig) {
		//We use a try-catch in case smallest throws an exception, which means the tree is empty
		try {
			//To check if there's an overlap, we loop through the BST of pixels, starting at the smallest and
			//going to the successors until a null element is reached
			Pixel current = pixels.smallest(pixels.getRoot());
			while(current != null) {
				//We get the x' and y' values where a pixel cannot be found and we create a new Position for them
				int xPrime = current.getPosition().xCoord() + pos.xCoord() - fig.pos.xCoord();
				int yPrime = current.getPosition().yCoord() + pos.yCoord() - fig.pos.yCoord();
				Position p = new Position(xPrime, yPrime);
				//Now, we check if calculated position exists within pixels tree of input fig
				if(fig.pixels.get(fig.pixels.getRoot(), p) != null) {
					//If it exists, it means we have an overlap
					return true;
				}
				//If no overlap on current pixel, we go to next pixel
				current = pixels.successor(pixels.getRoot(), current.getPosition());
			}
			//If the loop ends with no returns, then the two icons do not intersect
			return false;
		} catch (EmptyTreeException e) {
			//No pixels to check, so no overlap
			return false;
		}
	}
}
