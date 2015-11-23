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

	  while(null != currentNode)
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
	   RBNode insertNode = new RBNode(k,v,true);
	   
	   if(this.empty())
	   {
		   this.root = insertNode;
		   this.root.is_red = false;
		   this.size++;
		   return 1;
	   }
	   
	   RBNode positionNode = getNodeWithKey(this.root, k);
	   if(positionNode.key == k)
	   {
		   return -1;
	   }
	   
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
	   return insertFixup(insertNode);
   }

//************************************************************************************
// TODO JONATHAN - make sure that our implemntation indeed has a black leaf to every node that has a key/value.
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

		// by the time we get here nodeToDelete has only one child that is not a dummy.
		if (nodeToDelete.is_red) // if the node is red than the tree will be valid after removing it.
		{
			removeNodeWithUpToOneChild(nodeToDelete);
			return 0;	
		}

		// the node we want to delete is black.

		RBNode nodeToDeleteChild = nodeToDelete.right;
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
		return(fixDeleteDoubleBlack(nodeToDeleteChild));

   }
	//************************************************************************************
	/**
   * public int fixDeleteDoubleBlack(int k)
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
					return (1 + fixDeleteDoubleBlack(doubleBlackNodeParent))	
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
						int colorFlipsNum = 0
						if (doubleBlackNodeSibling.is_red != doubleBlackNodeParent.is_red;) 
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
					// TODO - is there a simpler way to do this one? this is an ajacent cace I deduced.
					else
					{
						int colorFlipsNum = 0
						if (doubleBlackNodeSibling.is_red != doubleBlackNodeParent.is_red;) 
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
							++colorFlipsNum	
							return(colorFlipsNum)
						}
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
						return(2 + fixDeleteDoubleBlack(doubleBlackNode));
					}
					// if the double black node is a right child.
					else
					{
						int colorFlipsNum = 0
						if (doubleBlackNodeSibling.is_red != doubleBlackNodeParent.is_red;) 
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
				return(2 + fixDeleteDoubleBlack(doubleBlackNode));
			}
			// if the double black node is a right child.
			else
			{
				doubleBlackNodeSibling.is_red = false;
				doubleBlackNodeParent.is_red = true;
				rightRotate(doubleBlackNodeParent);
				return(2 + fixDeleteDoubleBlack(doubleBlackNode));
			}
		}

		// make sure we don't get here
		// TODO - remove this when we finish debuging.
		assert(false);
	}

//************************************************************************************
    /**
    * private void removeNodeWithUpToOneChild
    *
    * removes the specificed node from the tree
    *
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
				nodeToDeleteParent.left = dummy;	
			}
			else
			{
				nodeToDeleteParent.right = dummy;		
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
				nodeToDelete.parent.left = nodeToDeleteChild	
			}
			else
			{
				nodeToDelete.parent.right = nodeToDeleteChild		
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
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
  public int[] keysToArray()
  {
	  int[] keysArr = new int[this.size];
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
    * arr:  an array which will eventually contain the result, should be large enought
    *         to contain all keys.
    * index:  should be 0 when intialy invoked
    * 
    */
   public int generateKeysArray(RBNode root, int[] arr, int index)
   {
      if(null == root)
      {
        return index;
      }

      index = generateKeysArray(root.left, arr, index);
      arr[index++] = root.key;
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
    if(root == null)
    {
      return index;
    }
    index = generateValuesArray(root.left, arr, index);
    arr[index++] = root.value;
    index = generateValuesArray(root.right, arr, index);
    return index;
   }

//************************************************************************************  
   /**
    * public int getNodeWithKey()
    * @ param 
    * 	root: the node to start the search from
    * 	requiredKey: the key of the node the user request to be found.
	* @ return
    *	A Red Black tree node that has the specified key, null if no such node
    *	exists
    */
  public RBNode getNodeWithKey(RBNode root, int requiredKey)
  {
	  RBNode currentNode = root;
	  while(-1 != currentNode.key)
	   {
		   if(requiredKey == currentNode.key)
		   {
			   return currentNode;
		   }
		   else if(requiredKey < currentNode.key)
		   {
			   currentNode = currentNode.left;
		   }
		   else
		   {
			   currentNode = currentNode.right;
		   }
	   }
	  return null;
  }

//************************************************************************************
/*
	* TODO AVIV - document this and add support for having a dummy child.
*/ 
  public int insertFixup(RBNode node)
  {
	  int colorFlipsNum = 0;
	  while(node.parent != null && node.parent.is_red)
	  {
		  if(node.parent == node.parent.parent.left)
		  {
			  RBNode uncleNode = node.parent.parent.right;
			  if(uncleNode.is_red)
			  {
				  colorFlipsNum += 3;
				  node.parent.is_red = false;
				  uncleNode.is_red = false;
				  node.parent.parent.is_red = true;
				  node = node.parent.parent;
			  }
			  else
			  {
				  if(node == node.parent.right)
				  {
					  node = node.parent;
					  leftRotate(node);
				  }
				  colorFlipsNum += 2;
				  node.parent.is_red = false;
				  node.parent.parent.is_red = true;
				  rightRotate(node.parent.parent);
			  }
		  }
		  else
		  {
			  RBNode uncleNode = node.parent.parent.left;
			  if(uncleNode.is_red)
			  {
				  colorFlipsNum += 3;
				  node.parent.is_red = false;
				  uncleNode.is_red = false;
				  node.parent.parent.is_red = true;
				  node = node.parent.parent;
			  }
			  else
			  {
				  if(node == node.parent.left)
				  {
					  node = node.parent;
					  rightRotate(node);
				  }
				  colorFlipsNum += 2;
				  node.parent.is_red = false;
				  node.parent.parent.is_red = true;
				  leftRotate(node.parent.parent);
			  }
		  }
	  }
	  //T.left.color = BLACK??
	  return colorFlipsNum;
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
