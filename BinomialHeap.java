

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
	public int numTree;
	
	
	/*
	 * constructor of BinomialHeap
	 */
	public BinomialHeap() {
		size = 0;
		last = null;
		min = null;
		numTree = 0;
	}
	
	/* Method that hangs roots correctly by value of key
	 * @pre: two roots of trees with the same degree
	 * complexity: O(1) 
	 * 
	 */
	private static HeapNode link(HeapNode tree1, HeapNode tree2)
	{
		//take the trees out of the heap:
		tree1.next = null;
		tree2.next = null;
		if (tree1.item.key > tree2.item.key) {
			// make sure that tree1 is the smaller 
			HeapNode temp = tree1;
			tree1 = tree2;
			tree2 = temp;
		}
		// make tree2 the child of tree1
		if (tree1.child == null){  //two of the trees are of rank 0
			tree2.next = tree2;
		}
		// if the ranks are higher than 0
		else{
			tree2.next = tree1.child.next;
			tree1.child.next = tree2;
		}
		tree1.child = tree2;
		tree1.rank++; 
		tree2.parent = tree1;

		return tree1;
	}


	/** Method which insert a new Node into the heap
	 * @pre: key > 0
	 * @param: int key, String info to insert to the new HeapNode and HeapItem
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 *complexity: O(logn) 
	 */
	public HeapItem insert(int key, String info) 
	{   
		
		BinomialHeap heap2 = new BinomialHeap(); //Create new heap from new HeapNode
		HeapNode newNode = new HeapNode(key, info);
		heap2.addToEndOfHeap(newNode);
		this.meld(heap2);
		return newNode.item;
	}

	/** Delete the minimal item
	 * complexity: O(logn)
	 *
	 */
	public void deleteMin() {
		
		//empty heap
		if (this.empty())
			return;
		
		//one tree heap
		if (this.numTree == 1) {
			//no children
			if (this.size == 1) {
				this.size = 0;
				this.last = null;
				this.min = null;
				this.numTree = 0;
				return;
			}
			//more children
			else {
				HeapNode curr = this.min.child;
				this.last = curr;
				this.min = curr;
				this.size -=1;
				this.numTree = 1;
				curr.parent = null;
				curr = curr.next;
				while (curr != this.last) {
					if (curr.item.key <= this.min.item.key) {
						this.min = curr;
					}
					curr.parent = null;
					curr = curr.next;
					this.numTree += 1;
				}
				return;
			}
		}
		// numTree > 1 but deleting a node with no children
		if (this.min.child == null) {
			// disconnect next pointers
			HeapNode nodeToRemove = this.min;
			HeapNode nextNode = nodeToRemove.next;
			this.last.next = nextNode;
			
			// find new minimum
			HeapNode firstNode = this.last.next;
			HeapNode minNode = firstNode;
			HeapNode node = firstNode.next;
			while (node != firstNode) {
				if (node.getKey() < minNode.getKey()) {
					minNode = node;
				}
				node = node.next;
			}
			
			this.min = minNode;
			this.size -= 1;
			this.numTree -= 1;

			return;
		}
		
		
		this.size -= (int) Math.pow(2, this.min.rank);
		this.numTree -= 1;
		HeapNode prevMin = this.min;
		HeapNode curr = this.min.next;
		this.min = curr;
		int numOfTrees = 0;
		while(curr.next != prevMin) {
			if(curr.getKey() <= this.min.getKey())
				this.min = curr;
			curr = curr.next;
			numOfTrees++;
		}

		if (curr.getKey() <= this.min.getKey()) { 
			this.min = curr;
		}
		
		if (this.last.equals(prevMin)) {
			this.last = curr;                    
		}

		curr.next = curr.next.next;
		prevMin.next = prevMin; // now prevMin is a separate tree;

		// this is now a heap without prevMin's tree
		
		if (prevMin.rank == 0) {
			return;
		}

		int newHeapSize = (int)Math.pow(2, prevMin.rank)-1;
		BinomialHeap newHeap = buildNewHeap(prevMin.child, newHeapSize, numOfTrees); 
		this.meld(newHeap);
		return; 
	}
	
	/* Method that builds a new Heap from given tree roots list
	 * @pre: the child of the previous minimum, the size of the prevMin tree -1, and the num of trees that were the children of the prevMin
	 * the method make a heap from the children of a deleted minNode
	 * complexity: O(logn) 
	 */
	private BinomialHeap buildNewHeap (HeapNode lastNode, int newSize, int numOfTrees) {
		
		BinomialHeap heap = new BinomialHeap();
		heap.min = findNewMin(lastNode, heap);
		heap.last=lastNode;
		heap.size = newSize;
		heap.numTree = numOfTrees;
		
		return heap;
	}
	
	/**
	 * 
	 * Return the minimal HeapItem, null if empty.
	 * O(1)
	 *
	 */
	public HeapItem findMin()
	{
		if (this.empty()) {
			return null;
		}
		return this.min.item;
	} 
	
	/** Method that finds a new minimum HeapItem and sets parent of nodes in the list to null
	 * @param node: child of a deleted node = largest ranked subtree of a deleted node or last of roots list if we deleted min
	 * complexity: O(logn)
	 */
	private HeapNode findNewMin (HeapNode node, BinomialHeap heap) {
		HeapNode tmp = node.next;
		HeapNode min = node;
		min.parent = null;
		heap.numTree = 1;
		while (tmp!=node) {
			tmp.parent = null;
			heap.numTree += 1;
			if(min.getKey()>tmp.getKey()) {
				min = tmp;
			}
			tmp = tmp.next;
		}
		return min;
		
	}

	/**
	 * Decrease the key of item by diff and fix the heap. 
	 * @pre: 0<diff<item.key, item of node that we want to decrease its key
	 * 
	 * complexity: O(logn)
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		//System.out.println(item.key+"->"+(item.key - diff));
		item.key -= diff;
		sift(item.node);
		if (item.key<this.min.getKey()){
			this.min = item.node;
		}
		return; 
	}
	
	/**
	 * Decrease the key of item to -1
	 * @pre: item of node that we want to delete
	 * @post: the calling method calls deleteMin to delete the required key
	 * complexity: O(logn)
	 */
	private void decreaseToMinus (HeapItem item) {
		item.key = -1;
		sift(item.node);
	}

	/**
	 * 
	 * Delete the item from the heap.
	 * complexity: O(logn)
	 *
	 */
	public void delete(HeapItem item) 
	{   
		this.decreaseToMinus(item);	
		this.deleteMin();
		return; 
	}
	
	/**
	 * Method that rises to the root of the tree and validates that the tree is legal (conducts changes if needed)
	 * @pre: node that we decreased its key
	 * @post: legal binomial tree
	 * complexity: O(logn)
	 *
	 */
	private void sift(HeapNode node) {	
		while(node.parent != null && node.getKey() <= node.parent.getKey()) { 
				HeapItem smallerItem = node.item;
				HeapItem largerItem = node.parent.item;
				node.item = largerItem;
				largerItem.node = node;
				node.parent.item = smallerItem;
				smallerItem.node = node.parent;
				node = node.parent;
			}
		if (node.getKey()<this.min.getKey())
			this.min = node;


		}
		
	/**
	 * 
	 * Meld the heap with heap2
	 * 
	 * complexity: o(logn)
	 */
	public void meld(BinomialHeap heap2)
	{

		if (heap2.empty()) 
			return;
		if (this.empty()) {
			this.last = heap2.last;
			this.min = heap2.min;
			this.size = heap2.size;
			this.numTree = heap2.numTree;
			return;
		}
		
		int newSize = this.size + heap2.size; // save sizes for later
		
		
		BinomialHeap smallerMaxRankHeap = (this.last.rank <= heap2.last.rank) ? this:heap2;
		BinomialHeap greaterMaxRankHeap = (this.last.rank > heap2.last.rank) ? this:heap2;
		BinomialHeap resHeap = new BinomialHeap();
				
		HeapNode currentSmaller = smallerMaxRankHeap.last.next;
		HeapNode currentGreater = greaterMaxRankHeap.last.next;
		HeapNode nextSmaller = null;
		HeapNode nextGreater = null;
		HeapNode carry = null;
		while(!smallerMaxRankHeap.empty()) {
			
			if (carry == null) {
				
				if (currentSmaller.rank == currentGreater.rank) {

					nextSmaller = currentSmaller.next;
					nextGreater = currentGreater.next;
					int smallerRank = currentSmaller.rank;
					int greaterRank = currentGreater.rank;
					carry = link(currentGreater, currentSmaller); // creates carry and removes trees from heaps
					
					
					smallerMaxRankHeap.size -= (int) Math.pow(2, smallerRank);
					greaterMaxRankHeap.size -= (int) Math.pow(2, greaterRank);
					smallerMaxRankHeap.numTree -= 1;
					greaterMaxRankHeap.numTree -= 1;
					
					currentSmaller = nextSmaller;
					currentGreater = nextGreater;
				}
				
				else if (currentSmaller.rank > currentGreater.rank) {
					
					nextGreater = currentGreater.next;
					
					greaterMaxRankHeap.size -= (int) Math.pow(2, currentGreater.rank);
					greaterMaxRankHeap.numTree -= 1;
					
					resHeap.addToEndOfHeap(currentGreater);
					
					currentGreater = nextGreater;

				}
				else { // currentGreater.rank >= currentSmaller.rank

					nextSmaller = currentSmaller.next;
					
					smallerMaxRankHeap.size -= (int) Math.pow(2, currentSmaller.rank);
					smallerMaxRankHeap.numTree -= 1;
					
					resHeap.addToEndOfHeap(currentSmaller);
					
					currentSmaller = nextSmaller;
				}
			}
			
			else { // carry is not null
				if (carry.rank < currentSmaller.rank && carry.rank < currentGreater.rank) {
					resHeap.addToEndOfHeap(carry);
					carry = null;
					continue;
				}
				if (currentSmaller.rank == currentGreater.rank && currentSmaller.rank == carry.rank) { // == carry.rank, 1+1+1
					
					nextSmaller = currentSmaller.next;
					nextGreater = currentGreater.next;
					int smallerRank = currentSmaller.rank;
					int greaterRank = currentGreater.rank;
					
					// calculate the node to add to tree by lower root value
					HeapNode treeToAdd = null;
					HeapNode link1 = null;
					HeapNode link2 = null;
					
					if (currentSmaller.getKey() < currentGreater.getKey() && currentSmaller.getKey() < carry.getKey()) {
						treeToAdd = currentSmaller;
						link1 = currentGreater;
						link2 = carry;
					}
					else if (currentGreater.getKey() < currentSmaller.getKey() && currentGreater.getKey() < carry.getKey()) {
						treeToAdd = currentGreater;
						link1 = currentSmaller;
						link2 = carry;
					}
					else {
						treeToAdd = carry;
						link1 = currentSmaller;
						link2 = currentGreater;
					}
					
					smallerMaxRankHeap.size -= (int) Math.pow(2, smallerRank);
					greaterMaxRankHeap.size -= (int) Math.pow(2, greaterRank);
					smallerMaxRankHeap.numTree -= 1;
					greaterMaxRankHeap.numTree -= 1;
					
					resHeap.addToEndOfHeap(treeToAdd);
					carry = link(link1,link2);
					
					currentSmaller = nextSmaller;
					currentGreater = nextGreater;

				}
				
				else if (currentSmaller.rank == carry.rank) { // 1+1+0
					
					nextSmaller = currentSmaller.next;
					
					smallerMaxRankHeap.size -= (int) Math.pow(2, currentSmaller.rank);
					smallerMaxRankHeap.numTree -= 1;
					
					carry = link(currentSmaller,carry);
					
					currentSmaller = nextSmaller;

					
				}
				else if (currentGreater.rank == carry.rank) {

					nextGreater = currentGreater.next;
					
					greaterMaxRankHeap.size -= (int) Math.pow(2, currentGreater.rank);
					greaterMaxRankHeap.numTree -= 1;

					
					carry = link(currentGreater,carry);
					
					currentGreater = nextGreater;
					
				}
				else { // carry != null but currentGreater==currentSmaller==null (-> the rank of the carry is smaller than the ranks of the current)
					resHeap.addToEndOfHeap(carry);
					carry = null;	
				}
			}
		}
		// OUT OF WHILE
		while (!greaterMaxRankHeap.empty()) {
			if (carry == null) {
				resHeap.addChange(currentGreater,greaterMaxRankHeap);
				break;	
			}
			
			else { // carry is not null
				if (carry.rank == currentGreater.rank) {
					
					nextGreater = currentGreater.next;
					
					greaterMaxRankHeap.size -= (int) Math.pow(2, currentGreater.rank);
					greaterMaxRankHeap.numTree -= 1;

					carry = link(currentGreater,carry);
					currentGreater = nextGreater;
				}
				
				else { // rank of carry is smaller than the rank of the currentGreater
					resHeap.addToEndOfHeap(carry);
					carry = null;
				}
			}
		}
		
		if (carry != null) 
			resHeap.addToEndOfHeap(carry);
		
		// Update this heap
		this.last = resHeap.last;
		this.numTree = resHeap.numTree;
		this.min = resHeap.min;
		this.size = newSize;
		
	}
	
	/**
	 *  Method that adds a tree to a heap at the end
	 * @pre: rank of newTree is greater than heap.last.rank
	 * complexity: O(1)
	 */
	private void addToEndOfHeap(HeapNode newTree) {
		
		if (this.empty()) {
			this.last = newTree;
			this.min = newTree;
			this.last.next = newTree;	
		}
		
		else {
			HeapNode temp = this.last.next;
			this.last.next = newTree;
			newTree.next = temp;
			this.last = newTree;
			//update min if neccessary:
			if(newTree.item.key < min.item.key) {
				this.min = newTree;
			}
		}
			
		this.numTree += 1;
		this.size += Math.pow(2, newTree.rank);
	}
	
	/**
	 *  Method that adds the end of a heap to the res heap
	 * @pre:  last node of the res heap, and heap of the remains of the longer heap
	 * complexity: O(1)
	 */
	private void addChange(HeapNode newTrees, BinomialHeap changeHeap) {
		
		HeapNode firstnode = this.last.next;
		this.last.next = newTrees;
		changeHeap.last.next = firstnode;
		this.last = changeHeap.last;		
		if (this.min.getKey() > changeHeap.min.getKey())
			this.min = changeHeap.min;
		this.numTree += changeHeap.numTree;
		
	}
	
	/*
	 * Method that returns the last HeapNode of a heap
	 * O(1)
	 */
	private HeapNode getLast() {
		return this.last;
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 * O(1)
	 */
	public int size()
	{
		return size;
	}

	/**
	 * The method returns true if and only if the heap is empty
	 * O(1)
	 */
	public boolean empty()
	{
		return size==0;
	}

	/**
	 * 
	 * Return the number of trees in the heap using the field that we defined
	 * o(1)
	 * 
	 */
	public int numTrees() {
		    return this.numTree;
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
			item = new HeapItem(this, key, info); // Create and connect HeapItem
			child = null;
			next = this;
			parent = null;
			rank = 0;
		}
		
		/**
		 * 
		 * Return the child of the HeapNode
		 * O(1)
		 */
		public HeapNode getChild() {
			return this.child;	
		}
		
		/**
		 * 
		 * Return the parent of the HeapNode
		 * O(1)
		 */
		public HeapNode getParent() {
			return this.parent;	
		}
		
		/**
		 * 
		 * Return the rank of the HeapNode
		 * O(1)
		 */
		public int getRank() {
			return this.rank;	
		}
		
		/**
		 * 
		 * Return the key of the HeapNode
		 * O(1)
		 */
		public int getKey() {
			return this.item.key;
		}

		/**
		 * 
		 * Return the HeapItem connected to the HeapNode
		 * O(1)
		 */
		public HeapItem getItem() {
			return this.item;
		}

		/**
		 * 
		 * Return the next HeapNode in the HeapNode linked list
		 * O(1)
		 */
		public HeapNode getNext() {
			return this.next;
		}
		

	}

	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public static class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		/* constructor HeapItem
		 * @pre: Matching HeapNode already created
		 * O(1)
		 */
		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info= info;
	}
		/**
		 * Return the key of the HeapItem
		 * O(1)
		 */
		public int getKey() {
			return this.key;
		}
	}
	

}


