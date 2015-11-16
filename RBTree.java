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
	  	private boolean is_red;
	  	
		boolean isRed(){ return is_red;}
		RBNode getLeft(){return left;}
		RBNode getRight(){return right;}
		String getValue(){return value;}
		int getKey(){return key;}
	}
	
 /**
   * public RBNode getRoot()
   *
   * returns the root of the red black tree
   *
   */
  public RBNode getRoot() {
    return root;
  }
  
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
    return (size == 0);
  }

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

  /**
   * public int insert(int k, String v)
   *
   * inserts an item with key k and value v to the red black tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of color switches, or 0 if no color switches were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String v) {
	  return 42;	// to be replaced by student code
   }

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
	  return arr;
  }

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
	  return arr;
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
   
 /**
   * If you wish to implement classes, other than RBTree and RBNode, do it in this file, not in 
   * another file.
   */
   

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
    * 
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

}
  

