//Name: clee887
//Date: March 24th, 2021
//Description: Programming Assignment 2

//Class representing a BST. Implements the BinarySearchTreeADT interface
public class BinarySearchTree implements BinarySearchTreeADT {

	//Fields of the class
	public BinaryNode root;

	//Constructor of the class
	public BinarySearchTree() {
		//Set root as a leaf node
		root = new BinaryNode();
	}

	//Returns the root of the BST
	public BinaryNode getRoot() {
		return root;
	}

	//Returns the Pixel with given position starting at given tree root, or null if it does not exist
	public Pixel get(BinaryNode r, Position key) {
		//We check if the given root is a leaf
		if(r.isLeaf()) {
			//If a leaf, it means the key does not exist
			return null;
		} else {
			//If not a leaf, we check if it contains the pixel with position requested
			if(r.getData().getPosition().compareTo(key) == 0) {
				//If so, we return its pixel!
				return r.getData();
			} else {
				//If not, we continue the traversal on the respective child
				if(r.getData().getPosition().compareTo(key) < 0) {
					//Continue search on right child
					return get(r.getRight(), key);
				} else {
					//Continue search on left child
					return get(r.getLeft(), key);
				}
			}
		}
	}

	//Adds a new pixel into the tree given. Throws an Exception if data's key already exists
	public void put(BinaryNode r, Pixel data) throws DuplicatedKeyException {
		//We first check if the given root is a leaf
		if(r.isLeaf()) {
			//If so, we store the data into it and assign it leaf children
			r.setData(data);
			r.setLeft(new BinaryNode());
			r.setRight(new BinaryNode());
			r.getLeft().setParent(r);
			r.getRight().setParent(r);
		} else {
			//If not a leaf, we check if the data's key is equal to given root data's key 
			if(r.getData().getPosition().compareTo(data.getPosition()) == 0) {
				//If equal, we throw an exception because we can't add a duplicate key
				throw new DuplicatedKeyException("The pixel's position already exists");
			}
			//If not equal, we now put the new pixel either to the left or right children, according to its key
			if(r.getData().getPosition().compareTo(data.getPosition()) < 0) {
				//Add children to right child
				put(r.getRight(), data);
			} else {
				//Add children to left child
				put(r.getLeft(), data);
			}
		}
	}

	//Removes the given key from the tree given. Throws an Exception if key does not exist
	public void remove(BinaryNode r, Position key) throws InexistentKeyException {
		//First, if current node is leaf, it means the key does not exist
		if (r.isLeaf()) {
			throw new InexistentKeyException("Key does not exist");
		}
		//Now, we compare the root's key against the key we want to remove
		if (r.getData().getPosition().compareTo(key) < 0) {
			//This means the data to remove should be on the right subtree
			remove(r.getRight(), key);
		} else if (r.getData().getPosition().compareTo(key) > 0) {
			//This means the data to remove should be on the left subtree
			remove(r.getLeft(), key);
		} else {
			//This means the current root has the key we want to remove. We have 5 cases for the removal process
			if(r.getParent() == null && r.getLeft().isLeaf() && r.getRight().isLeaf()) {
				//This means we're removing the only node in the tree, so we set it as a leaf node
				r.setData(null);
				r.setParent(null);
				r.setLeft(null);
				r.setRight(null);
			} else if ((!r.getLeft().isLeaf() && !r.getRight().isLeaf()) || (!r.getLeft().isLeaf() && r.getParent() == null)) {
				//This means the node has both children or it has no parent and has a left child.
				//we replace its pixel data with the max value on left subtree and then we recursively remove
				//it from left subtree
				r.setData(maxValue(r.getLeft()));
				remove(r.getLeft(), r.getData().getPosition());
			} else if (!r.getRight().isLeaf() && r.getParent() == null) {
				//This means has no parent and has a right only child. In this case, we replace its pixel data 
				//with the min value on right subtree and then we recursively remove it from right subtree
				r.setData(minValue(r.getRight()));
				remove(r.getRight(), r.getData().getPosition());
			} else if (r.getParent().getLeft() == r) {
				//This means this node is the left child of its parent and it has at most one child
				//We replace the left of its parent with its child
				r.getParent().setLeft(r.getLeft().isLeaf() ? r.getRight() : r.getLeft());
				//Also, we adjust the parent pointer of new left
				if(r.getParent().getLeft() != null) {
					r.getParent().getLeft().setParent(r.getParent());
				}
			} else {
				//This means this node is the right child of its parent and it has at most one child.
				//We replace the right of its parent with its child
				r.getParent().setRight(r.getLeft().isLeaf() ? r.getRight() : r.getLeft());
				//Also, we adjust the parent pointer of new right
				if(r.getParent().getRight() != null) {
					r.getParent().getRight().setParent(r.getParent());
				}
			}
		}
	}

	//Returns the Pixel with the smallest key larger than the given one starting at given root
	public Pixel successor(BinaryNode r, Position key) {
		//First, if the given root is a leaf, it means the key has no successor
		if(r.isLeaf()) {
			return null;
		}
		//We start looping through the tree looking for the key (if it exists)
		BinaryNode current = r;
		BinaryNode successor = new BinaryNode();
		boolean found = false;
		while(!found) {
			if(current.getData().getPosition().compareTo(key) == 0) {
				//The node with key was found
				found = true;
			} else if(current.getData().getPosition().compareTo(key) < 0) {
				//The key should be on the right subtree
				if(current.getRight().isLeaf()) {
					//If no right, this is the value that should go just before it
					found = true;
				} else {
					current = current.getRight();
				}
			} else {
				//The key should be on the left subtree
				if(current.getLeft().isLeaf()) {
					//If no left, this is the value that should go just after it
					//In other words, this is the successor
					successor = current;
					found = true;
				} else {
					successor = current;
					current = current.getLeft();
				}
			}
		}
		//Now, we find the successor of given key node (if not found yet)
		if(!current.getRight().isLeaf() && (successor.isLeaf()|| (!successor.isLeaf() && successor != current))) {
			//This means the successor does not exists yet or, if we found one possible successor, it is not the result
			//because the node has a right child. This means the successor is the node with min data on its right subtree
			successor = new BinaryNode(minValue(current.getRight()), null, null, null);
		}
		//Finally, we return the data of the successor (either the Pixel or null if not successor)
		return successor.getData();
	}

	//Returns the Pixel with the largest key smaller than the given one starting at given root
	public Pixel predecessor(BinaryNode r, Position key) {
		//First, if the given root is a leaf, it means the key has no predecessor
		if(r.isLeaf()) {
			return null;
		}
		//We start looping through the tree looking for the key (if it exists)
		BinaryNode current = root;
		BinaryNode predecessor = new BinaryNode();
		boolean found = false;
		while(!found) {
			if(current.getData().getPosition().compareTo(key) == 0) {
				//The node with key was found
				found = true;
			} else if(current.getData().getPosition().compareTo(key) < 0) {
				//The key should be on the right subtree
				if(current.getRight().isLeaf()) {
					//If no right, this is the value that should go just before it
					//In other words, this is the predecessor
					predecessor = current;
					found = true;
				} else {
					predecessor = current;
					current = current.getRight();
				}
			} else {
				//The key should be on the left subtree
				if(current.getLeft().isLeaf()) {
					//If no left, this is the value that should go just after it
					found = true;
				} else {
					current = current.getLeft();
				}
			}
		}
		//Now, we find the predecessor of given key node (if not found yet)
		if(!current.getLeft().isLeaf() && (predecessor.isLeaf() || (!predecessor.isLeaf() && predecessor != current))) {
			//This means the predecessor does not exists or, if we found one possible predecessor, it is not the result
			//because the node has a left child. This means the predecessor is the node with max data on its left subtree
			predecessor = new BinaryNode(maxValue(current.getLeft()), null, null, null);
		}
		//Finally, we return the data of the predecessor (either the Pixel or null if not predecessor)
		return predecessor.getData();
	}

	//Returns the Pixel with smallest key starting at given root. Throws an Exception is tree is empty
	public Pixel smallest(BinaryNode r) throws EmptyTreeException {
		//If the given root is a leaf, we throw an Exception
		if(r.isLeaf()) {
			throw new EmptyTreeException("Tree is empty");
		}
		//If not empty, we return the min value starting at the root
		return minValue(r);
	}

	//Returns the Pixel with largest key starting at given root. Throws an Exception is tree is empty
	public Pixel largest(BinaryNode r) throws EmptyTreeException {
		///If the given root is a leaf, we throw an Exception
		if(r.isLeaf()) {
			throw new EmptyTreeException("Tree is empty");
		}
		//If not empty, we return the max value starting at the root
		return maxValue(r);
	}

	//Helper method that returns the Pixel with largest key value of given tree root
	private Pixel maxValue(BinaryNode r) {
		if (r.getRight().isLeaf()) {
			//If no right (so right is leaf), this is the largest already
			return r.getData();
		}
		//If right, return the largest on right
		return maxValue(r.getRight());
	}

	//Helper method that returns the Pixel with smallest key value of given tree root
	private Pixel minValue(BinaryNode r) {
		if (r.getLeft().isLeaf()) {
			//If no left (so left is leaf), this is the smallest already
			return r.getData();
		}
		//If left, return the smallest on left
		return minValue(r.getLeft());
	}
}