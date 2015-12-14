package data_structures;

import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 *
 * RBTree
 *
 * An implementation of a Red Black Tree with
 * non-negative, distinct integer keys and values
 *
 */

public class RBTree {
	
	private RBNode root = null;
	private int size = 0;
	private final RBNode dummyNode = new RBNode(-1 ,"" , false);
	
	public int minKey()
	   {
			if(this.empty())
			{
				return -2;
			}

			RBNode currentNode = root;

			while(-1 != currentNode.left.key)
			{
				currentNode = currentNode.left;
			}
		   return currentNode.key;
		}
	
		public int maxKey()
		{
			if(this.empty())
			{
				return -2;
			}
			RBNode currentNode = root;
			while(-1 != currentNode.right.key)
			{
				currentNode = currentNode.right;
			}
			return currentNode.key;
		}
	
	public RBTree()
	{
		return;
	}
	
	public RBTree(Iterable<Map.Entry<Integer, String>> items) {
        this();
        insertItems(items);
    }
	
	public RBTree(Map<Integer, String> map) {
        this();
        insertItems(map);
    }
	
	public void insertItems(Map<Integer, String> map) {
        insertItems(map.entrySet());
    }
	
	public void insertItems(Iterable<Map.Entry<Integer, String>> items) {
        for (Map.Entry<Integer, String> item : items) {
            insert(item.getKey(), item.getValue());
        }
    }
	
	void printTree() {
        printTree(System.out);
    }
	
	void printTree(PrintStream stream) {
        root.printTree(stream);
    }
	
	void checkTreeInvariants() {
		if(empty()){
			return;
		}
        try {
            checkTreeInvariants_();
        } catch (Throwable throwable) {
            printTree();
            throw throwable;
        }
	}
	
	private void checkTreeInvariants_() {
        assert dummyNode.getRight() == null : "rootDummy has a right child";
        assert dummyNode.getLeft() == null : "rootDummy has a right child";
        assert !dummyNode.isRed() : "Invalid color for rootDummy";
        assert dummyNode.parent == null : "Invalid parent for rootDummy";
        assert dummyNode.key == -1 : "Invalid key nil";
        assert dummyNode.value == "" : "Invalid item for nil";
        
        checkSubtreeInvariants(getRoot());
        
        TreeMap<Integer, String> map = toTreeMap();
        assert map.size() == size() : "Incorrect size";
    }
	
	public TreeMap<Integer, String> toTreeMap() {
        TreeMap<Integer, String> map = new TreeMap<>();
        if(!empty()){
        	toMap(map);
        }
        //map.put(-1, "-1");
        return map;
    }
	
	public void toMap(Map<Integer, String> map) {
        walkPreOrder(getRoot(), (node) -> map.put(node.key, node.value));
    }
	
	static Consumer<RBNode> dummyConsumer = (a) -> {
    };
	
	private void walkPreOrder(RBNode node, Consumer<RBNode> consumer) {
        walk(node, consumer, dummyConsumer, dummyConsumer);
    }
	
	private void walk(RBNode node, Consumer<RBNode> consumerPre, Consumer<RBNode> consumerIn, Consumer<RBNode> consumerPost) {
        if (node == dummyNode) {
            return;
        }
        consumerPre.accept(node);
        walk(node.left, consumerPre, consumerIn, consumerPost);
        consumerIn.accept(node);
        walk(node.right, consumerPre, consumerIn, consumerPost);
        consumerPost.accept(node);
    }
	
	private int checkSubtreeInvariants(RBNode node) {
        assert node != null : "Invalid node (null)";
        if (node == dummyNode) {
            return 1;
        }
        
        assert (node.isRed() && node.parent.isRed()) : "Red rule violated";
        
        int black_length = 0;
        if (!node.isRed()) {
            black_length += 1;
        }
        
        
        if (node.getLeft() != dummyNode) {
            assert node.left.key < node.key : "Left child key not lower than node key";
        }
        if (node.getRight() != dummyNode) {
            assert node.right.key > node.key : "Right child key not higher then node key";
        }
        
        int left_black_length = checkSubtreeInvariants(node.left);
        int right_black_length = checkSubtreeInvariants(node.right);
        assert left_black_length == right_black_length : "Black rule violated";
        black_length += left_black_length;
        
        return black_length;
    }

/**
   * public class RBNode
   */
  public class RBNode{
	  	private String value;
	  	private int key;
	  	private RBNode left;
	  	private RBNode right;
	  	private RBNode parent;
	  	private boolean is_red;
	  	
	  	public void printTree() {
	        printTree(System.out);
	    }

	    public void printTree(PrintStream out) {
	        if (right != dummyNode) {
	            right.printTree(out, true, "");
	        }
	        printNodeValue(out);
	        if (left != dummyNode) {
	            left.printTree(out, false, "");
	        }
	    }
	    
	    private void printNodeValue(PrintStream out) {
	        out.print(toStringMinimal() + '\n');
	    }

	    private void printTree(PrintStream out, boolean isRight, String indent) {
	        if (right != dummyNode) {
	            right.printTree(out, true, indent + (isRight ? "        " : " |      "));
	        }
	        out.print(indent);
	        if (isRight) {
	            out.print(" /");
	        } else {
	            out.print(" \\");
	        }
	        out.print("----- ");
	        out.print(toStringMinimal() + '\n');
	        if (left != dummyNode) {
	            left.printTree(out, false, indent + (isRight ? " |      " : "        "));
	        }
	    }
	    
	    public String toStringMinimal(){
			if (this == dummyNode)
				return "Sentinal";
			
			if (!this.is_red)
				return Integer.toString(key);
			return "<" + Integer.toString(key) + ">";
			
		}
	  	
	  	RBNode(int key, String value, boolean is_red){
	  		this.key = key;
	  		this.value = value;
	  		this.is_red = is_red;
	  		this.left = null;
	  		this.right = null;
	  		this.parent = null;
	  	}
		boolean isRed(){ return is_red;}
		RBNode getLeft(){return left;}
		RBNode getRight(){return right;}
		RBNode getParent(){return parent;}
		String getValue(){return value;}
		int getKey(){return key;}
	}

//************************************************************************************
	
 /**
   * public RBNode getRoot()
   *
   * returns the root of the red black tree
   *
   */
  public RBNode getRoot() {
    return root;
  }
  
 //************************************************************************************
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
    return (size == 0);
  }

//************************************************************************************
 /**
   * public String search(int k)
   *
   * returns the value of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  if(this.empty())
    {
		  return null;
	  }
	  RBNode currentNode = root;

	  while(-1 != currentNode.key)
    {
      // if found the wanted node
		  if(currentNode.key == k)
      {
			  return currentNode.value;
		  }
      // if the wanted node has a larger key
		  else if(currentNode.key < k)
      {
			  currentNode = currentNode.right;
		  }
      // if the wanted node has a smaller key
		  else
      {
			  currentNode = currentNode.left;
		  }
	  }
	  return null;
  }

//************************************************************************************

  /**
   * public int insert(int k, String v)
   *
   * inserts an item with key k and value v to the red black tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of color switches, or 0 if no color switches were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String v) {
	   if(k <= -1)
	   {
		   return -1;
	   }
	   RBNode insertNode = new RBNode(k,v,true);
	   insertNode.left = dummyNode;
	   insertNode.right = dummyNode;
	   
	   // if the tree is empty insert as root
	   if(this.empty())
	   {
		   insertNode.parent = dummyNode;
		   this.root = insertNode;
		   this.root.is_red = false;
		   this.size++;
		   return 1;
	   }
	   
	   RBNode positionNode = getLocationToInsertNodeAt(this.root, k);
	   
	   // if the node the key exists in the tree return -1
	   if(positionNode.key == k)
	   {
		   return -1;
	   }
	   
	   // insert the node as a leaf
	   this.size++;
	   insertNode.parent = positionNode;
	   if(k < positionNode.key)
	   {
		   positionNode.left = insertNode;
	   }
	   else
	   {
		   positionNode.right = insertNode;
	   }
	   
	   // fix the RBTree to a valid state and return the number of color switches 
	   return insertFixup(insertNode);
   }

//************************************************************************************
  /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of color switches, or 0 if no color switches were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if(k <= -1)
	   {
		   return -1;
	   }
		RBNode nodeToDelete = getNodeWithKey(this.root, k);
		
		// the node we are requested to delete is not in the tree
		if(null == nodeToDelete)
		{
			return -1;
		}

		this.size -= 1;

		// if the node has both children
		if (-1 != nodeToDelete.left.key && -1 != nodeToDelete.right.key)
		{
			// delete the minimal node in the right subtree.
			RBNode nodeToReplaceWith = findMinimalNode(nodeToDelete.right);

			// node to replace with doesn't have a left child.
			nodeToDelete.key = nodeToReplaceWith.key;
			nodeToDelete.value = nodeToReplaceWith.value;
			nodeToDelete = nodeToReplaceWith;
		}

		// by the time we get here nodeToDelete has up to one child that is not a dummy.
		if (nodeToDelete.is_red) // if the node is red than the tree will be valid after removing it.
		{
			removeNodeWithUpToOneChild(nodeToDelete);
			return 0;	
		}

		// the node we want to delete is black.
		RBNode nodeToDeleteChild = nodeToDelete.right;
		RBNode nodeToDeleteParent = nodeToDelete.parent;
		if (-1 == nodeToDeleteChild.key) 
		{
			nodeToDeleteChild = nodeToDelete.left;
		}
		if (nodeToDeleteChild.is_red) 
		{
			nodeToDeleteChild.is_red = false;
			removeNodeWithUpToOneChild(nodeToDelete);
			return 1;		
		}

		// the node we want to delete is black and it has a black child.
		removeNodeWithUpToOneChild(nodeToDelete);
		if (-1 == nodeToDeleteChild.key) 
		{
			nodeToDeleteChild.parent = nodeToDeleteParent;
			nodeToDeleteChild.left = null;
			nodeToDeleteChild.right = null;
		}
		return(fixDeleteDoubleBlack(nodeToDeleteChild));

   }
	//************************************************************************************
	/**
   * public int fixDeleteDoubleBlack(RBNode doubleBlackNode)
   *
   * fixes the issue that the subtree under doubleBlackNode has a black depth lower by one
   * than any of it's relatives.
   * @ params
   *	doubleBlackNode: the node that requires the fix
   * @ return
   *	the number of color changes made.
   */
	private int fixDeleteDoubleBlack(RBNode doubleBlackNode)
	{
		
		// if this is the root with the double black issue.
		if (-1 == doubleBlackNode.parent.key) 
		{
			// in this case there is no actual problem.
			return 0;
		}
		
		if (doubleBlackNode.is_red) 
		{
			doubleBlackNode.is_red = false;
			return 1;
		}
		
		RBNode doubleBlackNodeSibling = doubleBlackNode.parent.left;
		if (doubleBlackNodeSibling == doubleBlackNode) 
		{
			doubleBlackNodeSibling = doubleBlackNode.parent.right;	
		}
		RBNode doubleBlackNodeParent = doubleBlackNode.parent;
		boolean isDoubleBlackNodeLeftChild = (doubleBlackNode.parent.left == doubleBlackNode);

		// if the sibling and the node with the problem are black
		if (!doubleBlackNode.is_red && !doubleBlackNodeSibling.is_red)
		{
			// if both of the sibling's children are black
			if (!doubleBlackNodeSibling.right.is_red && !doubleBlackNodeSibling.left.is_red) 
			{
				// if the parent is black
				if(!doubleBlackNodeParent.is_red)
				{
					doubleBlackNodeSibling.is_red = true;
					return (1 + fixDeleteDoubleBlack(doubleBlackNodeParent));
				}
				// if the parent is red
				else
				{
					doubleBlackNodeParent.is_red = false;
					doubleBlackNodeSibling.is_red = true;
					return 2;
				}
			}
			// at least one of the sibling's children is red
			else
			{	
				// if the sibling's right child is red (case 3)
				if (doubleBlackNodeSibling.right.is_red) 
				{
					if (isDoubleBlackNodeLeftChild) 
					{
						int colorFlipsNum = 0;
						if (doubleBlackNodeSibling.is_red != doubleBlackNodeParent.is_red) 
						{
							colorFlipsNum++;
						}
						if (doubleBlackNodeParent.is_red) 
						{
							colorFlipsNum++;	
						}

						doubleBlackNodeSibling.is_red = doubleBlackNodeParent.is_red;
						doubleBlackNodeParent.is_red = false;
						doubleBlackNodeSibling.right.is_red = false;		
						leftRotate(doubleBlackNode.parent);	
						return (colorFlipsNum + 1);
					}
					// if the double black node is a right child.
					else
					{
						int colorFlipsNum = 0;
						if (doubleBlackNodeSibling.is_red != doubleBlackNodeParent.is_red) 
						{
							colorFlipsNum++;
						}
						if (doubleBlackNodeParent.is_red) 
						{
							colorFlipsNum++;	
						}
						doubleBlackNodeSibling.is_red = doubleBlackNodeParent.is_red;
						doubleBlackNodeParent.is_red = false;
						RBNode newPossibleDoubleBlack = doubleBlackNodeSibling.left;
						rightRotate(doubleBlackNodeParent);
						if (newPossibleDoubleBlack.is_red) 
						{
							newPossibleDoubleBlack.is_red = false;
							++colorFlipsNum;
							return(colorFlipsNum);
						}
						// fix the parent pointer in case this is a dummy
						newPossibleDoubleBlack.parent = doubleBlackNodeSibling;
						return(colorFlipsNum + fixDeleteDoubleBlack(newPossibleDoubleBlack));
					}
				}
				// the siblig's left child is red and we can assume that the right one is black
				// other wise we would have been in case 3 (case 4)
				else 
				{
					if (isDoubleBlackNodeLeftChild) 
					{
						doubleBlackNodeSibling.left.is_red = false;
						doubleBlackNodeSibling.is_red = true;
						rightRotate(doubleBlackNodeSibling);
						// if double black node was a dummy rotate may have changed it's parent
						doubleBlackNode.parent = doubleBlackNodeParent;
						return(2 + fixDeleteDoubleBlack(doubleBlackNode));
					}
					// if the double black node is a right child.
					else
					{
						int colorFlipsNum = 0;
						if (doubleBlackNodeSibling.is_red != doubleBlackNodeParent.is_red) 
						{
							colorFlipsNum++;
						}
						if (doubleBlackNodeParent.is_red) 
						{
							colorFlipsNum++;	
						}
						doubleBlackNodeSibling.is_red = doubleBlackNodeParent.is_red;
						doubleBlackNodeParent.is_red = false;
						doubleBlackNodeSibling.left.is_red = false;
						rightRotate(doubleBlackNodeParent);
						return(colorFlipsNum + 1);
					}
				}
			}
		}
		// if only the problematic node is black and it's sibling is red.
		else
		{
			if (isDoubleBlackNodeLeftChild) 
			{
				doubleBlackNodeSibling.is_red = false;
				doubleBlackNodeParent.is_red = true;
				leftRotate(doubleBlackNodeParent);
				// if double black node was a dummy rotate may have changed it's parent
				doubleBlackNode.parent = doubleBlackNodeParent;
				return(2 + fixDeleteDoubleBlack(doubleBlackNode));
			}
			// if the double black node is a right child.
			else
			{
				doubleBlackNodeSibling.is_red = false;
				doubleBlackNodeParent.is_red = true;
				rightRotate(doubleBlackNodeParent);
				// if double black node was a dummy rotate may have changed it's parent
				doubleBlackNode.parent = doubleBlackNodeParent;
				return(2 + fixDeleteDoubleBlack(doubleBlackNode));
			}
		}

	}

//************************************************************************************
    /**
    * private void removeNodeWithUpToOneChild
    *
    * removes the specified node from the tree
    * assumes the node has one child at most.
    * @ param 
    * nodeToDelete: a pointer to the node to remove
    */
   private void removeNodeWithUpToOneChild(RBNode nodeToDelete)
   {
   		boolean isNodeLeftChild = (nodeToDelete.parent.left == nodeToDelete);
   		// if the node has no children
		if (-1 == nodeToDelete.left.key && -1 == nodeToDelete.right.key) 
		{
			if (isNodeLeftChild) 
			{
				nodeToDelete.parent.left = dummyNode;	
			}
			else
			{
				nodeToDelete.parent.right = dummyNode;		
			}
			// if the node we have just deleted was the root update the root to be null
			// as there are no longer any nodes in the tree.
			if (-1 == nodeToDelete.parent.key)
			{
				root = null;
			}
		}
		// if the node has one child
		else if ((-1 == nodeToDelete.right.key) || (-1 == nodeToDelete.left.key)) 
		{
			RBNode nodeToDeleteChild = nodeToDelete.right;
			if (-1 == nodeToDeleteChild.key) 
			{
				nodeToDeleteChild = nodeToDelete.left;
			}
			nodeToDeleteChild.parent = nodeToDelete.parent;

			// make nodeToDelete's child the child of nodeToDelete's parent. 
			if (isNodeLeftChild) 
			{
				nodeToDelete.parent.left = nodeToDeleteChild;
			}
			else
			{
				nodeToDelete.parent.right = nodeToDeleteChild;
			}
			
			// update the root as it has just changed!
			if (-1 == nodeToDelete.parent.key)
			{
				root = nodeToDeleteChild;
			}
		}
   }

//************************************************************************************
   /**
    * public String min()
    *
    * Returns the value of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
		if(this.empty())
		{
			return null;
		}

		RBNode currentNode = root;

		while(-1 != currentNode.left.key)
		{
			currentNode = currentNode.left;
		}
	   return currentNode.value;
	}

//************************************************************************************
	/**
    * public String max()
    *
    * Returns the value of the item with the largest key in the tree,
    * or null if the tree is empty
    */
	public String max()
	{
		if(this.empty())
		{
			return null;
		}
		RBNode currentNode = root;
		while(-1 != currentNode.right.key)
		{
			currentNode = currentNode.right;
		}
		return currentNode.value;
	}

//************************************************************************************
  /**
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  int[] keysArr = new int[this.size];
	  if (0 == this.size) 
	  {
		return keysArr;
	  }
	  
	  generateKeysArray(root, keysArr, 0);
	  return keysArr;
  }

//************************************************************************************
  /**
   * public String[] valuesToArray()
   *
   * Returns an array which contains all values in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] valuesToArray()
  {
	  String[] valuesArr = new String[size];
	  if (0 == this.size) 
	  {
		return valuesArr;
	  }
	  generateValuesArray(root, valuesArr, 0);
	  return valuesArr;
  }

//************************************************************************************   
   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size()
   {
	   return this.size;
   }

//************************************************************************************   
   /**
    * public int generateKeysArray()
    *
    * Returns a sorted array with the keys in the tree
    *
    * @ param 
    * root: a pointer to the tree's root node.
    * arr:  an array which will eventually contain the result, should be large enough
    *         to contain all keys.
    * index:  should be 0 when intialy invoked
    * 
    */
   public int generateKeysArray(RBNode root, int[] arr, int index)
   {
      if(-1 == root.key)
      {
        return index;
      }

      index = generateKeysArray(root.left, arr, index);
      arr[index] = root.key;
      index++;
      index = generateKeysArray(root.right, arr, index);
      return index;
   }

//************************************************************************************   
   /**
    * public int generateValuesArray()
    *
    * Returns a sorted array with the values in the tree (sorted by key)
    *
    * @ param 
    * root: a pointer to the tree's root node.
    * arr:  an array which will eventually contain the result, should be large enought
    *         to contain all keys.
    * index:  should be 0 when intialy invoked
    * @ return:
    * 	A sorted array with the values in the tree (sorted by key)
    */

  public int generateValuesArray(RBNode root, String[] arr, int index)
  {
    if(-1 == root.key)
    {
      return index;
    }
    
    index = generateValuesArray(root.left, arr, index);
    arr[index] = root.value;
    index++;
    index = generateValuesArray(root.right, arr, index);
    return index;
   }

//************************************************************************************
  
 // TODO - document this and getLocationToInsertNodeAt!
  // getLocationToInsertNodeAt didn't have a logical name so it was split to two functions 
  /**
   * public RBNode getNodeWithKey()
   * @ param 
   * 	root: the node to start the search from
   * 	requiredKey: the key of the node the user request to be found.
	* @ return
   *	A Red Black tree node that has the specified key, null if no such node
   *	exists
   */
  public RBNode getNodeWithKey(RBNode root, int requiredKey)
  {
	  RBNode locationNode = getLocationToInsertNodeAt(root, requiredKey);
	  if (null == locationNode) 
	  {
		return null;  
	  }
	  
	  if(requiredKey != locationNode.key)
	  {
		  return null;
	  }
	  return locationNode;
  }
  
//************************************************************************************
   /**
    * public RBNode getLocationToInsertNodeAt()
    * @ param 
    * 	root: the node to start the search from
    * 	requiredKey: the key of the node the user request to be found.
	* @ return
    *	A Red Black tree node that has the specified key, the parent of the node
    *	had it been in the tree.
    *	exists
    */
  public RBNode getLocationToInsertNodeAt(RBNode root, int requiredKey)
  {
	  RBNode currentNode = root;
	  if(null == root)
	  {
		  return null;
	  }
	  
	  while(-1 != currentNode.key)
	   {
		   if(requiredKey == currentNode.key)
		   {
			   return currentNode;
		   }
		   else if(requiredKey < currentNode.key)
		   {
			   if(currentNode.left == dummyNode)
			   {
				   return currentNode;
			   }
			   currentNode = currentNode.left;
		   }
		   else
		   {
			   if(currentNode.right == dummyNode)
			   {
				   return currentNode;
			   }
			   currentNode = currentNode.right;
		   }
	   }
	  return null;
  }

//************************************************************************************
  /**
   * public int insertFixup()
   *
   * fixes the RBTree to make it valid after insert.
   * @ params
   *	node: the node that requires the fix
   * @ return
   *	the number of color switches made.
   */
  public int insertFixup(RBNode node)
  {
	  int colorSwitchesNum = 0;
	  
	  // while the parent of the red node is red we need to continue fixing
	  while(node != dummyNode && node.parent != dummyNode && node.parent.is_red)
	  {
		  // the case where the node's parent is a left child
		  if(node.parent == node.parent.parent.left)
		  {
			  RBNode uncleNode = node.parent.parent.right;
			  // case 1: the node's uncle is red
			  if(uncleNode.is_red)
			  {
				  // the parent and the uncle become black
				  // the grandparent becomes red
				  colorSwitchesNum += 3;
				  node.parent.is_red = false;
				  uncleNode.is_red = false;
				  node.parent.parent.is_red = true;
				  node = node.parent.parent;
			  }
			  
			  else
			  {
				  // case 2: the node's uncle is black and the node is a right child
				  if(node == node.parent.right)
				  {
					  // use a left rotation to get to case 3
					  node = node.parent;
					  leftRotate(node);
				  }
				  
				  // case 3: the node's uncle is black and the node is a left child
				  // the parent becomes black and the grandparent too
				  // use a right rotation on the node's parent to resolve height issues
				  colorSwitchesNum += 2;
				  node.parent.is_red = false;
				  node.parent.parent.is_red = true;
				  rightRotate(node.parent.parent);
			  }
		  }
		  
		  // the symmetric case where the node's parent is a right child
		  else
		  {
			  RBNode uncleNode = node.parent.parent.left;
			  // case 1: the node's uncle is red
			  if(uncleNode.is_red)
			  {
				  // the parent and the uncle become black
				  // the grandparent becomes red
				  colorSwitchesNum += 3;
				  node.parent.is_red = false;
				  uncleNode.is_red = false;
				  node.parent.parent.is_red = true;
				  node = node.parent.parent;
			  }
			  
			  else
			  {
				  // case 2: the node's uncle is black and the node is a left child
				  if(node == node.parent.left)
				  {
					  // use a right rotation to get to case 3
					  node = node.parent;
					  rightRotate(node);
				  }
				  // case 3: the node's uncle is black and the node is a right child
				  // the parent becomes black and the grandparent too
				  // use a left rotation on the node's parent to resolve height issues
				  colorSwitchesNum += 2;
				  node.parent.is_red = false;
				  node.parent.parent.is_red = true;
				  leftRotate(node.parent.parent);
			  }
		  }
	  }
	  
	  // if the root became red we need to change it back
	  if(root.is_red)
	  {
		  root.is_red = false;
		  colorSwitchesNum++;
	  }
	  return colorSwitchesNum;
  }

//************************************************************************************
   /**
    * private void rightRotate()
    * rotates right around the speicifed node
    * @ param 
    * 	node: the parnet in the intersection we want to rotate
*/
  private void rightRotate(RBNode node)
  {
	  RBNode leftNode = node.left;
	  // if the node we are rotating is the root
	  // change the root to be its left child
	  if(node == root)
	  {
		  root = leftNode;
	  }
	  replaceNodes(node, leftNode);
	  makeLeftChild(node, leftNode.right);
	  makeRightChild(leftNode, node);
  }

//************************************************************************************
  /**
    * private void leftRotate()
    * rotates left around the speicifed node
    * @ param 
    * 	node: the node to rotate around
*/
  private void leftRotate(RBNode node)
  {
	  RBNode rightNode = node.right;
	  // if the node we are rotating is the root
	  // change the root to be its right child
	  if(node == root)
	  {
		  root = rightNode;
	  }
	  replaceNodes(node, rightNode);
	  makeRightChild(node, rightNode.left);
	  makeLeftChild(rightNode, node);
  }

//************************************************************************************
  /**
    * private void makeLeftChild()
    * makes child the left child of the parent, overriding
    * any exising left child
    * @ param 
    * 	parent: the node to act as parent
    * 	child:  the node that will become the left child
*/
  private void makeLeftChild(RBNode parent, RBNode child)
  {
	  parent.left = child;
	  child.parent = parent;
  }

//************************************************************************************
    /**
    * private void makeRightChild()
    * makes child the right child of the parent, overriding
    * any exising right child
    * @ param 
    * 	parent: the node to act as parent
    * 	child:  the node that will become the right child
*/
  private void makeRightChild(RBNode parent, RBNode child)
  {
	  parent.right = child;
	  child.parent = parent;
  }

//************************************************************************************
  /**
    * private void replaceNodes()
    * overrides nodeToReplace with replacingNode
    * maintains any pointers external entities had to the nodes.
    * @ param 
    * 	nodeToReplace: the node to be overriden
    * 	replacingNode: the node to override with
*/
	private void replaceNodes(RBNode nodeToReplace, RBNode replacingNode)
	{
		// if the node to replace is its parent's left child
		if(nodeToReplace == nodeToReplace.parent.left)
		{
			makeLeftChild(nodeToReplace.parent, replacingNode);
		}
		// if th node to replace is its prent's right child
		else
		{
			makeRightChild(nodeToReplace.parent, replacingNode);
		}
	}

//************************************************************************************
  /**
    * private RBNode findMinimalNode()
    * returnes an RBNode with the minimal key in the tree that origins at root
    * @ param 
    * 	root: the node to be refered as the root of the tree
*/
	private RBNode findMinimalNode(RBNode root)
	{
		RBNode currentNode = root;
		if (-1 == root.key) 
		{
			return root;	
		}
		while(-1 != currentNode.left.key)
		{
			currentNode = currentNode.left;
		}

		return currentNode;
	}
	

}
