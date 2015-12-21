import java.util.LinkedList;
import java.util.ListIterator;

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
	private LinkedList<HeapNode> roots = new LinkedList<HeapNode>();
	
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
		
   /**
    * public void insert(int value)
    *
    * Insert value into the heap 
    *
    */
    public void insert(int value) 
    {
    	this.size++;
    	if(this.empty())
    	{
    		this.minNode = new HeapNode(value);
    		this.roots.add(minNode);
    	}
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
    }

   /**
    * public void deleteMin()
    *
    * Delete the minimum value
    *
    */
    public void deleteMin()
    {
    	this.size--;
     	LinkedList<HeapNode> subtrees = this.minNode.getChildren();
     	this.roots.remove(minNode);
     	ListIterator<HeapNode> iterator = subtrees.listIterator();
		while(iterator.hasNext())
		{
			BinomialHeap heap = new BinomialHeap();
			HeapNode node = iterator.next();
			heap.roots.add(node);
			heap.minNode = node;
			this.meld(heap);
		}
    }

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
    
   /**
    * public void meld (BinomialHeap heap2)
    *
    * Meld the heap with heap2
    *
    */
    public void meld (BinomialHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

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
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than BinomialHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{
    	
    	private int value;
    	private LinkedList<HeapNode> children;
    	private HeapNode parent;
    	
    	public HeapNode(int value)
    	{
    		this.value = value;
    		this.children = new LinkedList<HeapNode>();
    		this.parent = null;
    	}
    	
    	public LinkedList<HeapNode> getChildren()
    	{
    		return this.children;
    	}
    }
}
