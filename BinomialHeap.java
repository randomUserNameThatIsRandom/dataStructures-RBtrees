package data_structures;

import java.util.Iterator;
import data_structures.BinomialHeap.HeapNodeLinkedList.HeapNodeIterator;

/**
 * BinomialHeap
 *
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	
	private int size = 0;
	private HeapNode minNode = null;
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
    	}
    	// if the heap is not empty
    	else
    	{
    		BinomialHeap heap = new BinomialHeap();
        	heap.insert(value);
        	if(this.minNode.value > heap.minNode.value)
        	{
        		this.minNode = heap.minNode;
        	}
        	this.meld(heap);
    	}
    	this.size++;
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
    	this.size--;
    	HeapNodeLinkedList subtrees = this.minNode.getChildren();
     	this.roots.remove(minNode);
     	Iterator<HeapNode> iterator = subtrees.iterator();
		while(iterator.hasNext())
		{
			BinomialHeap heap = new BinomialHeap();
			HeapNode node = iterator.next();
			heap.roots.add(node);
			heap.minNode = node;
			this.meld(heap);
		}
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
    	HeapNodeIterator heap1RootsIterator = (HeapNodeIterator)this.roots.iterator();
    	HeapNodeIterator heap2RootsIterator = (HeapNodeIterator)heap2.roots.iterator();
    	HeapNode currentNodeHeap2 = null;
    	HeapNode currentNodeHeap1 = null;
    	// if both have at least one root
		if(heap2RootsIterator.hasNext() && heap1RootsIterator.hasNext())
		{
			currentNodeHeap1 = heap1RootsIterator.next();
			currentNodeHeap2 = heap2RootsIterator.next();
		}
		
    	while(heap2RootsIterator.hasNext() && heap1RootsIterator.hasNext())
    	{
    		// if we have performed a merge in the previous iteration
    		if (remember1InPosition) 
    		{
				if (rankToRemember1For == currentNodeHeap1.rank) 
				{
					HeapNode prevNode = heap1RootsIterator.getPrev();
					joinSameRankHeapNodes(prevNode, currentNodeHeap1, heap1RootsIterator.getCurrentNodePosition());
					this.roots.removeNodeBeforePosition(heap1RootsIterator.getCurrentNodePosition());
					remember1InPosition = true;
	    			rankToRemember1For = currentNodeHeap1.rank;
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
    			// insert the node to the roots list, no conflicts arise.
    			this.roots.insertBeforePosition(heap1RootsIterator.getCurrentNodePosition(), currentNodeHeap2);
    			this.size += (int)Math.pow(2, currentNodeHeap2.rank);
				currentNodeHeap2 = heap2RootsIterator.next();
			}
    		// if the rank we are at in heap1 is smaller we just advance the pointer, we don't know 
    		//if a merge is required yet
    		else if(currentNodeHeap1.rank < currentNodeHeap2.rank)
    		{
    			currentNodeHeap1 = heap1RootsIterator.next();
    		}
    		// if they both have the same rank.
    		else
    		{
    			joinSameRankHeapNodes(currentNodeHeap1, currentNodeHeap2, heap1RootsIterator.getCurrentNodePosition());
    			remember1InPosition = true;
    			rankToRemember1For = currentNodeHeap1.rank;
    			currentNodeHeap1 = heap1RootsIterator.next();
    			currentNodeHeap2 = heap2RootsIterator.next();
    		}
    	}
    	// insert the remaining elements at the end of the list.
    	while(heap2RootsIterator.hasNext())
    	{
    		currentNodeHeap2 = heap2RootsIterator.next();
    		this.roots.insertAfterPosition(heap1RootsIterator.getCurrentNodePosition(), currentNodeHeap2);
    		this.size += (int) Math.pow(2, currentNodeHeap2.rank);
    		// we have inserted a new node so we will now have a next.
    		heap1RootsIterator.next();
    	}
    }
    
    //****************************************************************************************************************
    public void joinSameRankHeapNodes(HeapNode node1, HeapNode node2, Object position)
    {
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
		this.size += (int)Math.pow(2, newRoot.rank);
		newRoot.rank++;
		this.roots.replaceNodeAtPosition(position, newRoot);
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
    	String binaryRep = Integer.toBinaryString(this.size);
    	for(int i = 0; i < binaryRep.length(); i++)
		{
			if('1' == binaryRep.charAt(i))
			{
				return i + 1;
			}
		}
    	return 0;
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
    	String binaryRep = Integer.toBinaryString(this.size);
		boolean[] arr = new boolean[binaryRep.length()];
		for(int i = 0; i < arr.length; i++)
		{
			arr[i] = '1' == binaryRep.charAt(i);
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
        return; //	 to be replaced by student code
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
    	return false; // should be replaced by student code
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
    	
    	public int rank;
    	private int value;
    	private HeapNodeLinkedList children;
    	private HeapNode parent;
    	
    	//****************************************************************************************************************
    	public HeapNode(int value)
    	{
    		this.value = value;
    		this.children = new HeapNodeLinkedList();
    		this.parent = null;
    		this.rank = 0;
    	}
    	//****************************************************************************************************************
    	public HeapNodeLinkedList getChildren()
    	{
    		return this.children;
    	}
    }
  //****************************************************************************************************************
    // an implementation for a sorted linked list, sorted by heapNode rank, small to large.
    public class HeapNodeLinkedList implements Iterable<HeapNode> 
    {
    	
    	HeapNodeLinkedListNode mRoot = null;
    	HeapNodeLinkedListNode mLast = null;
    	
//****************************************************************************************************************
    	private class HeapNodeLinkedListNode
    	{
    		public HeapNode mValue;
    		public HeapNodeLinkedListNode mNext;
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
        	HeapNodeLinkedList mLinkedList;
        	HeapNodeLinkedListNode mCurrentNode;
        	//****************************************************************************************************************
        	public HeapNodeIterator(HeapNodeLinkedList listToIterate)
        	{
        		this.mLinkedList = listToIterate;
        		this.mCurrentNode = null;
        	}
        	//****************************************************************************************************************
        	public boolean hasNext() 
        	{
        		if(null == this.mCurrentNode)
        		{
        			return (this.mLinkedList.mRoot != null); 
        		}
        		
        		return (null != this.mCurrentNode.mNext);
            }
        	//****************************************************************************************************************
            public HeapNode next() 
            {
            	if (null == this.mCurrentNode) 
            	{
            		this.mCurrentNode = this.mLinkedList.mRoot;
            		return this.mCurrentNode.mValue;
				}
            	this.mCurrentNode = this.mCurrentNode.mNext;
                return this.mCurrentNode.mValue;
            }
            
            public Object getCurrentNodePosition()
            {
            	return this.mCurrentNode;
            }
            
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
				
			}
    		
    		// getting here means that the node we are adding is the largest one
    		HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(HeapNodeToAdd, null, currentNode);
    		currentNode.mNext = newNode;
    		this.mLast = currentNode;
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
				}
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
    		
    		HeapNodeLinkedListNode newNode = new HeapNodeLinkedListNode(nodeToAdd, this.mLast, null);
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
    	 }
    	 
    	 
    	//****************************************************************************************************************
    	 public void replaceNodeAtPosition(Object position, HeapNode nodeToRepaceWith)
    	 {
    		 if(HeapNodeLinkedListNode.class.isInstance(position))
    		 {
    			 HeapNodeLinkedListNode positionNode = (HeapNodeLinkedListNode) position;
    			 positionNode.mValue = nodeToRepaceWith;
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
    				 return;
    			 }
    			// jump over it from the previous.
    			nodeToRemove.mPrev.mNext = positionNode;
    			// set the prev for position Node as we are deleting its prev.
    			positionNode.mPrev = nodeToRemove.mPrev;
    		 }
    	 }
    }
}
