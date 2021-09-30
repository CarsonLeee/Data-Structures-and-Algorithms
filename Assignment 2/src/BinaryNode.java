//Class that represents a Binary Node for BST
public class BinaryNode {

	//Fields of the class
	private Pixel value;
	private BinaryNode left, right, parent;
	
	//Constructor of the class. Receives the pixel value and references of nodes as parameters
	public BinaryNode(Pixel value, BinaryNode left, BinaryNode right, BinaryNode parent) {
		this.value = value;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}
	
	//No-arg constructor.
	public BinaryNode() {
		this.value = null;
		this.left = this.right = this.parent = null;
	}
	
	//Getter for parent node
	public BinaryNode getParent() {
		return parent;
	}
	
	//Getter for left child
	public BinaryNode getLeft() {
		return left;
	}
	
	//Getter for right child
	public BinaryNode getRight() {
		return right;
	}
	
	//Getter for pixel data
	public Pixel getData() {
		return value;
	}
	
	//Setter for parent node
	public void setParent(BinaryNode parent) {
		this.parent = parent;
	}
	
	//Setter for left child
	public void setLeft(BinaryNode left) {
		this.left = left;
	}
	
	//Setter for right child
	public void setRight(BinaryNode right) {
		this.right = right;
	}
	
	//Setter for pixel data
	public void setData(Pixel value) {
		this.value = value;
	}
	
	//Checks if node is leaf or not
	public boolean isLeaf() {
		return value == null;
	}
}
