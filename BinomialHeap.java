package data_structures;

import java.util.Iterator;


/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	public int linking=0; 
	
	/*
	 * represents the number of nodes in the heap
	 */
	private int size = 0;
	/*
	 * represents the node with the minimal value
	 */
	private HeapNode minNode = null;
	/*
	 * represents the list of binomial trees in the heap
	 */
	private HeapNodeLinkedList roots = new HeapNodeLinkedList();	

	//****************************************************************************************************************
	
   /**
    * public boolean empty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *   
    */
    public boolean empty()
    {
    	return (0 == this.size);
    }
	
  //****************************************************************************************************************
    
   /**
    * public void insert(int value)
    *
    * Insert value into the heap 
    *
    */
    public void insert(int value) 
    {
    	
    	// if the heap is empty
    	if(this.empty())
    	{
    		this.minNode = new HeapNode(value);
    		this.roots.add(minNode);
    		this.size = 1;
    	}
    	// if the heap is not empty
    	else
    	{
    		BinomialHeap heap = new BinomialHeap();
        	heap.insert(value);
        	this.meld(heap);
    	}
    }
  //****************************************************************************************************************
   /**
    * public void deleteMin()
    *
    * Delete the minimum value
    *
    */
    public void deleteMin()
    {
    	// get the children of minNode
    	HeapNodeLinkedList subtrees = this.minNode.children;
    	// remove the tree of which minNode is the root
    	this.roots.remove(minNode);
    	this.size -= (int) Math.pow(2, this.minNode.rank);
    	this.minNode = this.findMinAfterMinDelete();
    	// iterate all children to add them back to the heap
    	HeapNodeLinkedList.HeapNodeIterator iterator = (HeapNodeLinkedList.HeapNodeIterator)subtrees.iterator();
		while(iterator.hasNext())
		{
			HeapNode node = iterator.next();
			node.parent = null;
			
			// create a new heap with this child and meld it with our heap
			BinomialHeap heap = new BinomialHeap();
			heap.roots.add(node);
			heap.minNode = node;
			heap.size = (int) Math.pow(2, node.rank);
			this.meld(heap);
		}
    }
    
    //****************************************************************************************************************
 // TODO: document this function.
    private HeapNode findMinAfterMinDelete()
    {
    	Iterator<HeapNode> it = this.roots.iterator();
    	HeapNode currMinNode = null;
    	HeapNode currNode = null; 
		while(it.hasNext())
		{
			currNode = it.next();
			if (null == currMinNode) 
			{
				currMinNode = currNode;
				continue;
			}
			if(currMinNode.value > currNode.value)
			{
				currMinNode = currNode;
			}
		}
		return currMinNode;
    }
  //****************************************************************************************************************
   /**
    * public int findMin()
    *
    * Return the minimum value
    *
    */
    public int findMin()
    {
    	return this.minNode.value;
    } 
    
  //****************************************************************************************************************
   /**
    * public void meld (BinomialHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (BinomialHeap heap2)
    {    	
    	boolean remember1InPosition = false;
    	int rankToRemember1For = -1;
    	HeapNodeLinkedList.HeapNodeIterator heap1RootsIterator = 
    			(HeapNodeLinkedList.HeapNodeIterator)this.roots.iterator();
    	HeapNodeLinkedList.HeapNodeIterator heap2RootsIterator 
    		= (HeapNodeLinkedList.HeapNodeIterator)heap2.roots.iterator();
    	HeapNode currentNodeHeap2 = heap2RootsIterator.next();
    	HeapNode currentNodeHeap1 = heap1RootsIterator.next();
    	
    	// if the heap we are merging into is empty
    	if (null == currentNodeHeap1) 
    	{
    		while(null != currentNodeHeap2)
        	{
    			this.roots.add(currentNodeHeap2);
    			currentNodeHeap2 = heap2RootsIterator.next();
        	}
    		this.minNode = heap2.minNode;
    		this.size = heap2.size;
    		return;
		}
		
    	while(null != currentNodeHeap1 && null != currentNodeHeap2)
    	{    	
    		// if we have performed a merge in the previous iteration
    		if (remember1InPosition) 
    		{
				if (rankToRemember1For == currentNodeHeap1.rank) 
				{
					HeapNode prevNode = heap1RootsIterator.getPrev();
					int rankAdded = currentNodeHeap1.rank;
					joinSameRankHeapNodes(prevNode, currentNodeHeap1, heap1RootsIterator.getCurrentNodePosition());
					this.roots.removeNodeBeforePosition(heap1RootsIterator.getCurrentNodePosition());
					remember1InPosition = true;
	    			rankToRemember1For = rankAdded + 1;
	    			currentNodeHeap1 = heap1RootsIterator.next();
					continue;
				}
				else
				{
					remember1InPosition = false;
					rankToRemember1For = -1;
				}
			}
    		
    		// if the nodes don't have the same rank and heap1 doesn't have a node with that value, so we can simply add the node.
    		if (currentNodeHeap1.rank > currentNodeHeap2.rank) 
    		{
    			this.roots.insertBeforePosition(heap1RootsIterator.getCurrentNodePosition(), currentNodeHeap2);
    			this.size += (int)Math.pow(2, currentNodeHeap2.rank);
    			updateMinIfRequired(currentNodeHeap2);
				currentNodeHeap2 = heap2RootsIterator.next();
			}
    		// if the rank we are at in heap1 is smaller we just advance the pointer, we don't know 
    		// if a merge is required yet
    		else if(currentNodeHeap1.rank < currentNodeHeap2.rank)
    		{
    			currentNodeHeap1 = heap1RootsIterator.next();
    		}
    		// if they both have the same rank.
    		else
    		{
    			int rankAdded = currentNodeHeap2.rank;
    			joinSameRankHeapNodes(currentNodeHeap1, currentNodeHeap2, heap1RootsIterator.getCurrentNodePosition());
    			this.size += (int) Math.pow(2, rankAdded);
    			remember1InPosition = true;
    			rankToRemember1For = rankAdded + 1;
    			currentNodeHeap1 = heap1RootsIterator.next();
    			currentNodeHeap2 = heap2RootsIterator.next();
    		}
    	}
    	
    	while (remember1InPosition && null != currentNodeHeap1) 
		{
			if (rankToRemember1For == currentNodeHeap1.rank) 
			{
				HeapNode prevNode = heap1RootsIterator.getPrev();
				int rankAdded = currentNodeHeap1.rank;
				joinSameRankHeapNodes(prevNode, currentNodeHeap1, heap1RootsIterator.getCurrentNodePosition());
				this.roots.removeNodeBeforePosition(heap1RootsIterator.getCurrentNodePosition());
    			currentNodeHeap1 = heap1RootsIterator.next();
    			rankToRemember1For = rankAdded + 1;
			}
			else
			{
				remember1InPosition = false;
			}
		}
    	
    	// insert the remaining elements we need to call the function again as 
    	// we don't have an indication where to place them (there may have been)
    	// multiple joinnings in the previous step.
    	if (null != currentNodeHeap2)
    	{
    		while(null != currentNodeHeap2)
    		{
     			int thisMaxRank = this.roots.getMaxRank();
     			if(currentNodeHeap2.rank > thisMaxRank)
     			{			
     				this.roots.addLast(currentNodeHeap2);
     				if (currentNodeHeap2.value < this.minNode.value) 
     				{
     					this.minNode = currentNodeHeap2;
 					}
     				this.size += (int)Math.pow(2, currentNodeHeap2.rank);
     				currentNodeHeap2 = heap2RootsIterator.next();
     				continue;
     			}
     			
    			BinomialHeap heap = new BinomialHeap();
    			heap.roots.add(currentNodeHeap2);
    			heap.minNode = currentNodeHeap2;
    			heap.size = (int) Math.pow(2, currentNodeHeap2.rank);
    			this.meld(heap);
    			currentNodeHeap2 = heap2RootsIterator.next();
        	}
    	}
    }
    
    //****************************************************************************************************************
    private void joinSameRankHeapNodes(HeapNode node1, HeapNode node2, Object position)
    {
    	this.linking++;
    	HeapNode newRoot = null;
		HeapNode newLeaf = null;
		if(node1.value < node2.value)
		{
			newRoot = node1;
			newLeaf = node2;
		}
		else
		{
			newRoot = node2;
			newLeaf = node1;
		}
		HeapNodeLinkedList newRootChildren = newRoot.children;
		newRootChildren.addLast(newLeaf);
		newRoot.rank++;
		newLeaf.parent = newRoot;
		updateMinIfRequired(newRoot);
		this.roots.replaceNodeAtPosition(position, newRoot);
    }
    
  //****************************************************************************************************************
    private void updateMinIfRequired(HeapNode newNode)
    {
    	if(null == this.minNode)
    	{
    		this.minNode = newNode;
    		return;
    	}
    	
    	if (newNode.value <= this.minNode.value) 
		{
			this.minNode = newNode;
		}
    }

    //****************************************************************************************************************
   /**
    * public int size()
    *
    * Return the number of elements in the heap
    *   
    */
    public int size()
    {
    	return this.size;
    }
    
   /**
    * public int minTreeRank()
    *
    * Return the minimum rank of a tree in the heap.
    * 
    */
    
  //****************************************************************************************************************
    public int minTreeRank()
    {
    	return this.roots.mRoot.mValue.rank;
    }
	
  //****************************************************************************************************************
	   /**
    * public boolean[] binaryRep()
    *
    * Return an array containing the binary representation of the heap.
    * 
    */
    public boolean[] binaryRep()
    {
    	// if the heap is empty return an empty array
    	if(this.empty())
    	{
    		return new boolean[0];
    	}
    	
    	// the length of the binary representation is log n
    	boolean[] arr = new boolean[(int) (Math.log(this.size)/Math.log(2)) + 1];

    	// iterate all roots and update the array in the place of the rank to true
    	Iterator<HeapNode> iterator = this.roots.iterator();
    	while(iterator.hasNext())
    	{
    		arr[iterator.next().rank] = true;
    	}
    	
    	return arr;
   }

  //****************************************************************************************************************
   /**
    * public void arrayToHeap()
    *
    * Insert the array to the heap. Delete previous elemnts in the heap.
    * 
    */
    public void arrayToHeap(int[] array)
    {
    	this.size = 0;
        this.minNode = null;
        this.roots = new HeapNodeLinkedList();
        for(int i=0;i<array.length;i++)
        {
        	this.insert(array[i]);
        }
    }
	
  //****************************************************************************************************************
   /**
    * public boolean isValid()
    *
    * Returns true if and only if the heap is valid.
    *   
    */
    public boolean isValid() 
    {
    	// an empty heap is valid
    	if(this.empty())
    	{
    		return true;
    	}
    	
    	// array to check that there is only one tree of each degree
    	HeapNode[] arr = new HeapNode[(int) (Math.log(this.size)/Math.log(2)) + 1];
    	
    	// iterate all roots to check each tree
    	Iterator<HeapNode> iterator = this.roots.iterator();
    	while(iterator.hasNext())
    	{
    		HeapNode node = iterator.next();
    		
    		// if there was already a tree of this degree the heap is not valid
    		if(arr[node.rank] != null)
    		{
    			return false;
    		}
    		// else update that this degree was seen
    		else
    		{
    			arr[node.rank] = node;
    		}
    		
    		// if this is not a valid binomial tree the heap is not valid
    		if(!isValidBinomialTree(node))
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    public static boolean isValidBinomialTree(HeapNode node)
    {
    	// if the node is null or does not have children it is valid
    	if(node == null || (node.children.mRoot == null && node.rank == 0))
    	{
    		return true;
    	}
    	
    	// divide the node to two binomial trees of rank n-1
    	HeapNode subtree = node.children.mLast.mValue;
    	if(subtree.rank != node.rank - 1)
    	{
    		return false;
    	}
    	subtree.parent = null;
    	node.children.remove(subtree);
    	node.rank--;
    	
    	// check that the value of child is larger and that the two subtrees are valid
    	boolean result = subtree.value >= node.value && isValidBinomialTree(node) && isValidBinomialTree(subtree);
    	
    	// assemble the tree back
    	subtree.parent = node;
    	node.children.add(subtree);
    	node.rank++;
    	return result;
    }
    
  //****************************************************************************************************************
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than BinomialHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	
    	/*
    	 * represents the rank of the binomial tree that this is its root
    	 */
    	public int rank;
    	/*
    	 * represents the value of the node
    	 */
    	private int value;
    	/*
    	 * represents the list of children of the node in the tree
    	 */
    	private HeapNodeLinkedList children;
    	/*
    	 * represents the parent of the node in the tree
    	 */
    	private HeapNode parent;
    	
    	//****************************************************************************************************************
    	public HeapNode(int value)
    	{
    		this.value = value;
    		this.children = new HeapNodeLinkedList();
    		this.parent = null;
    		this.rank = 0;
    	}
    }
  //****************************************************************************************************************
    // an implementation for a sorted linked list, sorted by heapNode rank, small to large.
    public class HeapNodeLinkedList implements Iterable<HeapNode> 
    {
    	
    	/*
    	 * represents the first node in the list
    	 */
    	HeapNodeLinkedListNode mRoot = null;
    	/*
    	 * represents the last node in the list
    	 */
    	HeapNodeLinkedListNode mLast = null;
    	
//****************************************************************************************************************

    	private class HeapNodeLinkedListNode
    	{
    		/*
    		 * represents the HeapNode of the node in the list
    		 */
    		public HeapNode mValue;
    		/*
    		 * represents the next node in the list
    		 */
    		public HeapNodeLinkedListNode mNext;
    		/*
    		 * represents the previous node in the list
    		 */
    		public HeapNodeLinkedListNode mPrev;
    		
    		public HeapNodeLinkedListNode(HeapNode nodeToRepresent, HeapNodeLinkedListNode next, HeapNodeLinkedListNode previous)
    		{
    			super();
    			this.mValue = nodeToRepresent;
    			this.mNext = next;
    			this.mPrev = previous;
    		}
    		
    	}
    	
//****************************************************************************************************************
    	public class HeapNodeIterator implements Iterator<HeapNode>
        {
    		/*
    		 * represents the list
    		 */
        	HeapNodeLinkedList mLinkedList;
        	/*
        	 * represents the node in the current iteration
        	 */
        	HeapNodeLinkedListNode mCurrentNode;
        	/*
        	 * represents the state that the iterator hasn't been iterated.
        	 */
        	boolean hasBeenIterated = false;
        	//****************************************************************************************************************
        	public HeapNodeIterator(HeapNodeLinkedList listToIterate)
        	{
        		this.mLinkedList = listToIterate;
        		this.mCurrentNode = null;
        	}
        	//****************************************************************************************************************
        	public boolean hasNext() 
        	{
        		if(!hasBeenIterated)
        		{
        			return (this.mLinkedList.mRoot != null); 
        		}
        		
        		return (null != this.mCurrentNode.mNext);
            }
        	//****************************************************************************************************************
            public HeapNode next() 
            {
            	hasBeenIterated = true;
            	
            	// if this is the beginning of the list
            	if (null == this.mCurrentNode) 
            	{
            		this.mCurrentNode = this.mLinkedList.mRoot;
				}
            	// if this is not the beginning
            	else
            	{
            		this.mCurrentNode = this.mCurrentNode.mNext;
            	}
            	
            	if (null == mCurrentNode)
        		{
        			return null;
        		}
            	
                return this.mCurrentNode.mValue;
            }
          //****************************************************************************************************************
            public Object getCurrentNodePosition()
            {
            	return this.mCurrentNode;
            }
          //**************************************************************************************************************** 
            public HeapNode getPrev()
            {
            	return this.mCurrentNode.mPrev.mValue;
            }

        }
    	
//****************************************************************************************************************
    	// addition is done in O(n), n being the number of elements in the list, the list is kept sorted.
    	public void add(HeapNode HeapNodeToAdd)
    	{
    		// if the list is empty
    		if(null == mRoot)
    		{
    			this.mRoot = new HeapNodeLinkedListNode(HeapNodeToAdd, null, null);
    			this.mLast = this.mRoot;
    			return;
    		}
    		
    		if (HeapNodeToAdd.rank < this.mRoot.mValue.rank)
    		{
    			HeapNodeLinkedListNode newRoot  = new HeapNodeLinkedListNode(HeapNodeToAdd, mRoot, null);
    			this.mRoot.mPrev = newRoot;
    			this.mRoot = newRoot;
    			return;
    		}
    		
    		HeapNodeLinkedListNode currentNode = mRoot;
    		// find the position for the new node and insert it in the correct position.
    		while (null != currentNode.mNext) 
    		{
				if (HeapNodeToAdd.rank < currentNode.mNext.mValue.rank) 
				{
					HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(HeapNodeToAdd, currentNode.mNext, currentNode);
					currentNode.mNext.mPrev = newNode;
					currentNode.mNext = newNode;
					return;
				}
				currentNode = currentNode.mNext;
				
			}
    		
    		// getting here means that the node we are adding is the largest one
    		HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(HeapNodeToAdd, null, currentNode);
    		currentNode.mNext = newNode;
    		this.mLast = newNode;
    	}
    	
    	//****************************************************************************************************************
    	// removes an element from the list, O(1) if the element is the min.
    	public void remove(HeapNode nodeToRemove)
    	{
    		// if the node to remove is the root
    		if (nodeToRemove == this.mRoot.mValue)
    		{
    			this.mRoot = this.mRoot.mNext;
    			if (null == mRoot) 
    			{
    				this.mLast = null;
				}
    			else
    			{
    				this.mRoot.mPrev = null;
    			}
    			return;
    		}
    		
    		HeapNodeLinkedListNode currentNode = mRoot;
    		while (null != currentNode)
    		{
    			// if this is the node to remove
    			if (currentNode.mValue == nodeToRemove) 
    			{
    				// jump over it from the previous.
    				currentNode.mPrev.mNext = currentNode.mNext;
    				// if it isn't the last one, set the correct new prev for it's next.
    				if (null != currentNode.mNext) 
    				{
    					currentNode.mNext.mPrev = currentNode.mPrev;
					}
    				// if it is the last node that we are removing.
    				else
    				{
    					this.mLast = currentNode.mPrev;
    				}
    				
    				return;
				}
    			currentNode = currentNode.mNext;
    		}
    		
    	}
    	
    	//****************************************************************************************************************
    	
    	public void addLast(HeapNode nodeToAdd)
    	{
    		if (null == this.mLast) 
    		{
    			HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(nodeToAdd, null, null);
    			this.mRoot = newNode;
    			this.mLast = newNode;
    			return;
			}
    		
    		HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(nodeToAdd, null, this.mLast);
    		this.mLast.mNext = newNode;
    		this.mLast = newNode;
    	}
    	
    	//****************************************************************************************************************
    	
    	 public Iterator<HeapNode> iterator() 
    	 {
    	        return new HeapNodeIterator(this);
    	 }
    	 
    	//****************************************************************************************************************
    	 public void insertBeforePosition(Object position, HeapNode nodeToInsert)
    	 {
    		 if(HeapNodeLinkedListNode.class.isInstance(position))
    		 {
    			 HeapNodeLinkedListNode positionNode = (HeapNodeLinkedListNode) position;
    			 
    			 HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(nodeToInsert, positionNode, positionNode.mPrev);
    			 // if we aren't inserting before the root
    			 if (null != newNode.mPrev) 
    			 {
    				 newNode.mPrev.mNext = newNode;
    			 }
    			 // if we are inserting before the root.
    			 else
    			 {
    				 this.mRoot = newNode;
    			 }
    			 
    			 newNode.mNext.mPrev = newNode;
    			 return;
    		 }

    	 }
     	//****************************************************************************************************************
    	 
    	 public void insertAfterPosition(Object position, HeapNode nodeToInsert)
    	 {
    		 if(HeapNodeLinkedListNode.class.isInstance(position))
    		 {
    			 HeapNodeLinkedListNode positionNode = (HeapNodeLinkedListNode) position;
    			 HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(nodeToInsert, positionNode.mNext, positionNode);
    			 positionNode.mNext = newNode;
    			 // if we are inserting after the last node 
    			 if(null == newNode.mNext)
    			 {
    				 this.mLast = newNode;
    			 }
    			// if we are NOT inserting after the last node
    			 else
    			 {
    				 newNode.mNext.mPrev = newNode;
    			 }
    		 }
    		 // if this is the first node in the list
    		 else if(null == position)
    		 {
    			 HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(nodeToInsert, null, null);
    			 this.mRoot = newNode;
    			 this.mLast = newNode;
    		 }
    	 }
    	 
    	 
    	//****************************************************************************************************************
    	 public void replaceNodeAtPosition(Object position, HeapNode nodeToRepaceWith)
    	 {
    		 if(HeapNodeLinkedListNode.class.isInstance(position))
    		 {
    			 HeapNodeLinkedListNode positionNode = (HeapNodeLinkedListNode) position;
    			 positionNode.mValue = nodeToRepaceWith;
    			 return;
    		 }
    		 if(null == position)
    		 {
    			 this.mLast.mValue = nodeToRepaceWith;
    			 return;
    		 }
    	 }
    	//****************************************************************************************************************
    	 public void removeNodeBeforePosition(Object position)
    	 {
    		 if(HeapNodeLinkedListNode.class.isInstance(position))
    		 {
    			 HeapNodeLinkedListNode positionNode = (HeapNodeLinkedListNode) position;
    			 HeapNodeLinkedListNode nodeToRemove = positionNode.mPrev;
    			 if (this.mRoot == nodeToRemove) 
    			 {
    				 this.mRoot = positionNode;
    				 positionNode.mPrev = null;
    				 return;
    			 }
    			// jump over it from the previous.
    			nodeToRemove.mPrev.mNext = positionNode;
    			// set the prev for position Node as we are deleting its prev.
    			positionNode.mPrev = nodeToRemove.mPrev;
    			return;
    		 }
    	 }
    	 int getMaxRank()
    	 {
    	 	if (null == mLast) 
    	 	{
    	 		return 0;
    	 	}
    	 	return this.mLast.mValue.rank;
    	 }
    	 
    }
}
