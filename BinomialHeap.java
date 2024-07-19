/**
 * BinomialHeap
 *
 * An implementation of binomial heap over positive integers.
 *
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;
	
	//constructor of BinomialHeap: @noga
	
	public BinomialHeap() {
		size = 0;
		last=null;
		min=null;
	}
	/*
	public BinomialHeap(HeapNode node) {
		size = node.getRank()+1;
		last = node;
		min = node;	
	}*/

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 * explaination: the func get key&string, creat a new heapNode and then creat a new BinomialHeap from the node,
	 * then meld the two heaps - the original and the one node heap @noga
	 *complexcity: TODO 
	 */
	public HeapItem insert(int key, String info) 
	{   
		BinomialHeap heap2 = new BinomialHeap(); //creat new heap from new HeapNode
		HeapNode newNode = new HeapNode(key, info);
		heap2.size = 1;
		heap2.last = newNode;
		heap2.min = newNode;
		this.meld(heap2);
		return newNode.item; // should return this?
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		return; // should be replaced by student code

	}

	/**
	 * 
	 * Return the minimal HeapItem, null if empty.
	 *
	 */
	public HeapItem findMin()
	{
		return null; // should be replaced by student code
	} 

	/**
	 * 
	 * pre: 0<diff<item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{    
		return; // should be replaced by student code
	}

	/**
	 * 
	 * Meld the heap with heap2
	 * 
	 * seperated to 3 different cases: meld into empty heap, and two more cases @noga
	 *
	 */
	public void meld(BinomialHeap heap2)
	{
		//TODO
		//in case heap is empty @noga
		if (this.size==0){
			this.size = heap2.size;
			this.last= heap2.last;
			this.min= heap2.min;
			return;
		}
		
		//key<min? is so
		//in case heap is not empty @noga
		else {
			for 

			//if we have 2 trees of the same degree
			
			//otherwise we add the relevant Bi to the heap
			
		return;
		}
	}
	
	//original [1,1,0,1] 
	//insert[1]
	   //heap2 [1,1,0,0]
	//carry [0,1,0,0]
	//we should use recursion/loop to jump from parent to parent in the first level and decide where to meld 2 branches
	//how to use the external method?
	
	private BinomialHeap joinSameDegree(BinomialHeap heap2) {
		//TODO
		return null; //need to return the new root?
	}
	
	//0+1 , 1+0
	private binomialHeap simpleJoin () {
		
	}
	//1+1 when its next is 0
	   //call to meldSameDegree and call to simple join and put 1
	
	
	//1+1 when its next is 1
	
	//carry is a tree
	//חיבור בינארי
		
	
	
	private static int[] sizeToBinaryArray(int size) {
		String binary = Integer.toBinaryString(size);
		int n = binary.length();
		int[] BinaryArray = new int[n];
		for (int i=0; i<n; i++) {
			BinaryArray[i]= binary.charAt(i); //list of string of 1,0
		}
		return BinaryArray; 
	}
	
	
	
	

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return false; // should be replaced by student code
	}

	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return 0; // should be replaced by student code
	}

	/**
	 * Class implementing a node in a Binomial Heap.
	 *  
	 */
	public static class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
		
		//constructor HeapNode: @noga
		public HeapNode(int key, String info) {
			item = new HeapItem(this, key, info);
			child = null;
			next = this; //because the next of B0 is itself
			parent = null;
			rank = 0;
		}
		
		/**
		 * getters //for Noga: important that it would not be static
		 * @return 
		 */
		
		public HeapNode getChild() {
			return this.child;	
		}
		
		public HeapNode getParent() {
			return this.parent;	
		}
		
		public int getRank() {
			return this.rank;	
		}
		
		//TODO ?
		
		/**
		 * setters
		 * @return 
		 */
		
		private void setChild(HeapNode child) {
			this.child= child;
		}
		//TODO all	
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public static class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		//constructor HeapItem: @noga
		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info= info;
	}
		//TODO setters and getters - to think what and how
	}
}
