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
		   return 1;
	   }
	   
	   RBNode positionNode = findPosition(this.root, k);
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
	   return 42;	// to be replaced by student code
   }

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

	   while(null != currentNode.left)
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
	   while(null != currentNode.right)
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
    * public int findPosition()
    * @ param 
    * 	root: the node to start the search from
    * 	requiredKey: the key of the node the user request to be found.
	* @ return
    *	A Red Black tree node that has the specified key, null if no such node
    *	exists
    */
  public RBNode findPosition(RBNode root, int requiredKey)
  {
	  RBNode currentNode = root;
	  while(null != currentNode)
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
	* TODO AVIV - document this.
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
    * public void rightRotate()
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
    * public void leftRotate()
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
    * public void makeLeftChild()
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
    * public void makeRightChild()
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
    * public void replaceNodes()
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

}
