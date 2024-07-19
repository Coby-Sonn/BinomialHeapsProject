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
	
	
	//constructor of BinomialHeap:
	
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
	}
	*/

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
		BinomialHeap heap2 = new BinomialHeap(); //Create new heap from new HeapNode
		HeapNode newNode = new HeapNode(key, info);
		heap2.size = 1;
		heap2.last = newNode;
		heap2.min = newNode;
		this.meld(heap2);
		return newNode.item;
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
		//in case heap is empty
		if (this.size==0){
			this.size = heap2.size;
			this.last= heap2.last;
			this.min= heap2.min;
			return;
		}
		

		//in case heap is not empty 
		else {
			int[] originalArray = reverseArray(ToBinaryArray(this.size));
			int[] newArray =reverseArray(ToBinaryArray(heap2.size));
			int minLen = Math.min(originalArray.length, newArray.length);
			HeapNode prevOriTree = this.last;
			HeapNode curOriTree = prevOriTree.next;
			HeapNode curNewTree = heap2.last.next;
			HeapNode carry = null; 	//carry is a root
			if (curOriTree.getKey()>curNewTree.getKey() && this.min==curOriTree) { //update min pointer @noga
				this.min =curNewTree;
			}
			for (int i=0; i<minLen;i++) {
			if(carry==null) {          		//no carry
				if (newArray[i]==0 && originalArray[i]==1) {
					prevOriTree = prevOriTree.next;
					curOriTree = curOriTree.next;
				}
				else if (newArray[i]==1 && originalArray[i]==0) {
					this.simpleJoin(curNewTree, curOriTree,prevOriTree);
					curNewTree = curNewTree.next;
					//originalArray[i]=1;
				}
				else if (newArray[i]==1 && originalArray[i]==1) {
					carry=this.ComplexJoin(curOriTree, curNewTree, curOriTree.next, prevOriTree);
					prevOriTree = prevOriTree.next;
					curOriTree = curOriTree.next;
					curNewTree = curNewTree.next;
					originalArray[i+1]=1;
				}
			}
				else { 						//carry=1
					if (newArray[i]==0 && originalArray[i]==0) {
						this.simpleJoin(carry, curOriTree, prevOriTree);
						carry=null;
					}
					else if (newArray[i]==0 && originalArray[i]==1) {
						carry=this.ComplexJoin(curOriTree, carry, curOriTree.next, prevOriTree);
						prevOriTree = prevOriTree.next;
						curOriTree = curOriTree.next;
						originalArray[i+1]=1;
					}
					else if (newArray[i]==1 && originalArray[i]==0) {
						carry = this.hangHeapCarry(curNewTree, carry);
						curNewTree = curNewTree.next;
						originalArray[i+1]=1;
						
					}
					else if (newArray[i]==1 && originalArray[i]==1) {
						if (curOriTree.getKey() <= carry.getKey() && curOriTree.getKey() <= curNewTree.getKey()) { //original key is the smallest
							carry = this.hangHeapCarry(curNewTree, carry);
							prevOriTree = prevOriTree.next;
							curOriTree = curOriTree.next;
							curNewTree = curNewTree.next;
							originalArray[i+1]=1;
						}
						else if(curOriTree.getKey() > carry.getKey() && carry.getKey() < curNewTree.getKey()) { //carry key is the smallest
							
							carry.next = curOriTree.next;
							prevOriTree.next= carry;
							curOriTree.next = null;
							carry = this.hangHeapCarry(curNewTree, curOriTree);
							prevOriTree = prevOriTree.next;
							curOriTree = curOriTree.next;
							curNewTree = curNewTree.next;
							originalArray[i+1]=1;
							
						}
						
						else if(curNewTree.getKey() < carry.getKey() && curOriTree.getKey() > curNewTree.getKey()) { //newTree key is the smallest
						//TODO
						}
					}
				}
				
				//if the arrays are not the same size -> continue on the change
			}
		return;
		}
	}
	
	
	//example
	//original [0,0,1,0] 
	//insert[1,1]
	//carry [1,0,0,0]	
	
	/**
	 * 
	 * Get an array of 0,1 which represents binary number @noga
	 * Return the reversed array
	 * Complexity: O(n)
	 *   
	 */
	
	private static int[] reverseArray (int[] array) { //@noga
		int lenArray= array.length;
		int[] newArray = new int[lenArray];
		for (int i=0; i<lenArray; i++) {
			newArray[i] = array[lenArray-1-i];
		}
		return newArray;
	}
	
	
	/**
	 * 
	 *
	 *   
	 */
	
	//0+1 , 1+0
	private void simpleJoin (HeapNode root, HeapNode nodeNext, HeapNode nodePrev) {
		
		if (root.getKey() < this.min.getKey()) {
			this.min = root;
		}
		nodePrev.next = root;
		root.next= nodeNext;
	}
	
	
	/**
	 * 
	 * 
	 *   
	 */
	
	//1+1
	private HeapNode ComplexJoin (HeapNode originalRoot,HeapNode newRoot, HeapNode nodeNext, HeapNode nodePrev) {
		
		if (originalRoot.getKey() < newRoot.getKey()) { //simpler case - the original root is the 
			hangHeap (originalRoot, newRoot);
			return originalRoot;
		}
		
		else {
			
			if(this.min == originalRoot) {
				this.min = newRoot;
			}
			
			nodePrev.next = newRoot;
			newRoot.next = originalRoot.next;
			hangHeap (newRoot,originalRoot);
			return newRoot;
		}
	}
	
	
	/**
	 * 
	 * 
	 *   
	 */
	
	private HeapNode hangHeap(HeapNode topRoot, HeapNode lowRoot) {
		lowRoot.next = topRoot.child.next;
		topRoot.child.next = lowRoot;
		topRoot.child = lowRoot;
		lowRoot.parent = topRoot;
		topRoot.rank += lowRoot.rank;
		return topRoot;
	}
	
	/**
	 * 
	 * 
	 *   
	 */
	
	private HeapNode hangHeapCarry(HeapNode root1, HeapNode root2) {
		HeapNode topRoot = root1;
		HeapNode lowRoot = root2;
		if (root1.getKey() > root2.getKey()) {
			topRoot = root2;
			lowRoot = root1;
		}
		return hangHeap(topRoot, lowRoot);
	}
	
	/**
	 * 
	 * 
	 *   
	 */
	
	private static int[] ToBinaryArray(int size) {
		String binary = Integer.toBinaryString(size);
		int n = binary.length();
		int[] BinaryArray = new int[n+1];
		for (int i=0; i<n; i++) {
			BinaryArray[i]= binary.charAt(i) - '0';
		}
		BinaryArray[n]=0;
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
		
		//constructor HeapNode:
		public HeapNode(int key, String info) {
			item = new HeapItem(this, key, info);
			child = null;
			next = this;
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
		
		public int getKey() {
			return this.item.key;
		}
		
		
		/**
		 * setters
		 * @return 
		 */
		
		private void setChild(HeapNode child) {
			this.child= child;
		}
		//TODO	
	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public static class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		//constructor HeapItem:
		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info= info;
	}
		//TODO setters and getters - to think what and how
	}
}
