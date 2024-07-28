package binomialheaptester.yourcode;

import java.util.Arrays;
import java.util.HashSet; // DELETE
import java.util.Set; // DELETE

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
	
	
	//constructor of BinomialHeap:
	public BinomialHeap() {
		size = 0;
		last = null;
		min = null;
		numTree = 0;
	}
	
	/*
	public BinomialHeap(HeapNode node) {
		size = node.getRank()+1;
		last = node;
		min = node;	
	}
	*/

	 @Override
	 public String toString() {
	        // Optionally, customize the toString method if needed
	        return super.toString();
	    }
	
	 private static String getLast7Characters(String str) {
	        if (str.length() <= 7) {
	            return str;
	        } else {
	            return str.substring(str.length() - 7);
	        }
	    }
	
	/** pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 * explaination: the func get key&string, creat a new heapNode and then creat a new BinomialHeap from the node,
	 * then meld the two heaps - the original and the one node heap @noga
	 *complexcity: TODO 
	 */
	public HeapItem insert(int key, String info) 
	{   
		//String heapString = this;
		BinomialHeap heap2 = new BinomialHeap(); //Create new heap from new HeapNode
		HeapNode newNode = new HeapNode(key, info);
		heap2.size = 1;
		heap2.last = newNode;
		heap2.min = newNode;
		heap2.numTree = 1;
		this.meld(heap2);
		
		System.out.println("Inserted: "+key);
		return newNode.item;
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		System.out.println(this.size +" " + this.numTree);

		if (this.empty())
			return;
		
		if (this.numTree == 1) {
			if (this.size == 1) {
				this.size = 0;
				this.last = null;
				this.min = null;
				this.numTree = 0;
				return;
			}
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

		this.size -= (int) Math.pow(2, this.min.rank);
		this.numTree -= 1;
		HeapNode prevMin = this.min;
		HeapNode curr = this.min.next;
		this.min = curr;
		while(curr.next != prevMin) {
			if(curr.getKey() <= this.min.getKey())
				this.min = curr;
			curr = curr.next;
		}

		if (curr.item.key <= this.min.item.key) { 
			this.min = curr;
		}
		
		if (this.last.equals(prevMin)) {
			this.last = curr;                    
		}
		System.out.println("HERE");

		curr.next = curr.next.next;
		prevMin.next = prevMin; // now prevMin is a seprate tree;
		System.out.println("HERE2");

		// this is now a heap without prevMin's tree
		
		if (prevMin.rank == 0) {
			System.out.println(prevMin.getKey());
			System.out.println(min.getKey());

			return;
		}
		System.out.println("HERE3");

		int newHeapSize = (int)Math.pow(2, prevMin.rank);
		BinomialHeap newHeap = buildNewHeap(prevMin.child, newHeapSize); 
		System.out.println("Got here");
		this.meld(newHeap);
		System.out.println(this.size);
		return; 
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
	
	/**
	 * Method also sets parent of nodes in the list to null
	 * @param node: child of a deleted node = largest ranked subtree of a deleted node or last of roots list if we deleted min
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
	
	private BinomialHeap buildNewHeap (HeapNode lastNode, int originalSize) {
		
		BinomialHeap heap = new BinomialHeap();
		heap.min = findNewMin(lastNode, heap);
		heap.last=lastNode;
		heap.size = originalSize - 1;
		
		return heap;
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
		//System.out.println(item.key+"->"+(item.key - diff));
		item.key -= diff;
		sift (item.node);
		if (item.key<this.min.getKey()){
			this.min = item.node;
		}
		return; 
	}
	
	private void decreaseToMinus (HeapItem item) {
		item.key = -1;
		sift(item.node);
		this.min = item.node;
	}

	/**
	 * 
	 * Delete the item from the heap.
	 *
	 */
	public void delete(HeapItem item) 
	{   
		System.out.println("Deleting "+ item.key + " from " + getLast7Characters(this.toString()));
		this.decreaseToMinus(item);
		this.deleteMin();
		return; 
	}
	
	/**
	 * 
	 * 
	 * O(logn)
	 *
	 */
	private void sift(HeapNode node) {	
		
		while(node.parent != null) { 
			if (node.parent.getKey() > node.getKey()) {
				HeapItem smallerItem = node.item;
				HeapItem largerItem = node.parent.item;
				node.item = largerItem;
				node.parent.item = smallerItem;
				node = node.parent;
			}
			else {
				return;
			}
		}
		
	}

	/**
	 * 
	 * Meld the heap with heap2
	 * 
	 * seperated to 3 different cases: meld into empty heap, and two more cases @noga
	 *complexity: o(logn), n is num of nodes
	 */
	public void meld(BinomialHeap heap2)
	{
		return;
	}
		
	/**
	 * 
	 * Get an array of 0,1 which represents binary number @noga
	 * Return the reversed array
	 * Complexity: O(t) , t=log(n), n = num of nodes in the heap
	 *   
	 */
	private static int[] reverseArray (int[] array) {
		int lenArray= array.length;
		int[] newArray = new int[lenArray];
		for (int i=0; i<lenArray; i++) {
			newArray[i] = array[lenArray-1-i];
		}
		int[] newNewArray = new int[lenArray+1];
		for (int i=0; i<lenArray;i++) {
			newNewArray[i] = newArray[i];
		}
		newNewArray[lenArray] = 0;
		return newNewArray;
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
	private HeapNode ComplexJoin (HeapNode originalRoot, HeapNode newRoot, HeapNode nodeNext, HeapNode nodePrev) {
		//System.out.println(originalRoot.getKey() + " <-ori,new-> " + newRoot.getKey());
		if (originalRoot.getKey() < newRoot.getKey()) { 
			hangHeap(originalRoot, newRoot);
			nodePrev.next = nodeNext;
			return originalRoot;
		}
		

		else {
			if(this.min == originalRoot) {
				this.min = newRoot;
			}
			
			if (originalRoot.child != null) {
				nodePrev.next = newRoot;
				newRoot.next = originalRoot.next;
			}
			//System.out.println(newRoot.getKey() + " <-top,low-> " + originalRoot.getKey());
			hangHeap(newRoot,originalRoot);
			
			



			return newRoot;
		}
	}
	
	/**
	 * 
	 * 
	 *   
	 */
	private HeapNode hangHeap(HeapNode topRoot, HeapNode lowRoot) {
		
		if(topRoot.child==null) {
			topRoot.child = lowRoot;
			//System.out.println("Key of child is now: " + topRoot.child.getKey());
			lowRoot.parent = topRoot;
			//System.out.println("Key of parent is now: " + lowRoot.parent.getKey());
			topRoot.rank += 1;
			return topRoot;
		}
		
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
		int[] BinaryArray = new int[n];
		for (int i=0; i<n; i++) {
			BinaryArray[i]= binary.charAt(i) - '0';
		}
		//BinaryArray[n]=0;
		return BinaryArray; 
	}
	
	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return size;
	}

	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 *   
	 */
	public boolean empty()
	{
		return size==0;
	}

	/**
	 * 
	 * Return the number of trees in the heap using Brian Kernighan's Algorithm.
	 * 
	 */
	public int numTrees() {
		    return bitcount(this.size);
	}
	
	private static int bitcount(int n) { // Method to count the number of 1s in the binary representation of n
        int count = 0;
        while (n > 0) {
            count = count + 1; // Increment the count for each 1 found
            n = n & (n - 1); // Remove the lowest set bit from n
        }
        return count; // Return the total count of 1s
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

		public HeapItem getItem() {
			return this.item;
		}

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
		
		//constructor HeapItem:
		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info= info;
	}
		//TODO setters and getters - to think what and how

		public int getKey() {
			return this.key;
		}
	}
	
	
	// PRINT TREE



	public void print_r() {
	    {
	        int s;
	        if(this.getLast().getRank() == 0){s = 1;}
	        else if(this.getLast().getRank() == 1){s = 2;}
	        else if(this.getLast().getRank() == 2){s = 3;}
	        else {
	            s = (int) Math.pow(2, this.getLast().getRank() - 1);
	        }
	        int[][][] answer = new int[this.numTrees()][s][s];
	        BinomialHeap.HeapNode curr_tree = this.getLast().getNext();
	        for (int k = 0; k < this.numTrees(); k++ ) {
	            Set<BinomialHeap.HeapNode> dic = new HashSet<>();
	            this.print_rec(curr_tree, 0, 0, dic, answer, k);
	            curr_tree = curr_tree.getNext();
	        }
	        int x = 0;
	        for (int i = 0; i < answer[x].length; i++) {
	            for (int j = 0; j < answer[x].length; j++) {
	                if(answer[x][i][j] != 0) {
	                    System.out.print(answer[x][i][j] + " ");
	                }
	                else{System.out.print("  ");}
	                if(x == answer.length-1 && j == answer[x].length-1) {break;}
	                if(j == answer[x].length-1) {
	                    if (x + 1 < answer.length) {x += 1;}
	                    j = -1;
	                }

	            }
	            System.out.println();
	            x = 0;
	        }
	    }
	}
	private HeapNode getLast() {
		return this.last;
	}

	public void print_rec(BinomialHeap.HeapNode first, int depth, int x, Set<BinomialHeap.HeapNode> dic, int[][][] answer, int num_in_row) {
	        if (dic.contains(first)){return;}
	        dic.add(first);
	        answer[num_in_row][depth][x] = first.getItem().getKey();
	        if(depth != 0) {
	            print_rec(first.getNext(), depth, x + 1, dic, answer, num_in_row);
	        }
	        if(first.getChild() != null){
	            print_rec(first.getChild().getNext(),depth+1,x,dic,answer, num_in_row);
	        }
	    }


	public static void main(String[] args) {
			BinomialHeap heap = new BinomialHeap();
			heap.insert(1,"hello");
			//heap.print_r();

			heap.insert(2,"2");
			//heap.print_r();

			heap.insert(3,"3");
			//heap.print_r();

			heap.insert(4,"3");
			heap.print_r();

			heap.insert(5,"3");
			heap.print_r();

			heap.insert(6,"3");
			heap.print_r();

			heap.insert(7,"3");
			heap.insert(8,"3");
			System.out.println("Printing tree now: ");
			//heap.print_r();

		}
	
	public static HeapNode link(HeapNode tree1, HeapNode tree2)
	{
		HeapNode top = tree1;
		HeapNode low = tree2;
		
		if (tree1.item.key > tree2.item.key) {
			top = tree2;
			low = tree1;
		}
		
		low.parent = top;
		if (top.child != null) {
			low.next = top.child.next;
			top.child.next = low;
		}
		else {
			low.next = low;
		}
		top.child = low;
		top.rank += 1;
		return top;
	}
	
	
	
	
}
