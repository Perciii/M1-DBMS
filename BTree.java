package struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class BTree {
	private int order;
	private Node root;
	
	public BTree(int order) {
		this.order = order;
		this.root = new Node(order);
	}

	public Node searchKey(Integer key) {
		return root.searchKey(key);
	}
	
	public void insertKey(Integer key) {
		Node insertionLocation = searchKey(key);
		System.out.println(insertionLocation);
		
		if(!insertionLocation.containsKey(key)) {
			if(insertionLocation.nbKeys() < order-1) {//if node where we insert new key is not full
				System.out.println("not full");
				insertionLocation.insertKey(key);
			} else { //if node where we insert new key is full
				System.out.println("full");
				this.splitNode(insertionLocation, key);
				
				// insertionLocation => child1 (update set of keys)
				// child2 => new Node with new set of keys 
				// father : exists ? 
				// - no : create with pointers and set (middleKey)
				// - yes : update pointers and set (if not full)
			}
		}
	}
	
	public void splitNode(Node insertionLocation, Integer key) {
		System.out.println("split");
		int middleKey = insertionLocation.getMiddleKey(key);
		System.out.println("middle key : " + middleKey);
		TreeSet<Integer> child1Keys = (TreeSet<Integer>) insertionLocation.getHeadSet(middleKey);
		TreeSet<Integer> child2Keys = (TreeSet<Integer>) insertionLocation.getTailSet(middleKey);
		 
		
		insertionLocation.setKeys(child1Keys);
		Node child2 = new Node(order, child2Keys);
		
		Node father = insertionLocation.getFather();
		if(father == null) {//if orphan
			System.out.println("orphan");
			//create father with children
			father = new Node(order, new TreeSet<>(Arrays.asList(middleKey)), null, insertionLocation, child2);
			
		} else {//if father exists
			System.out.println("has father");
			if(!father.isFull()) {//if father is not full
				System.out.println("father not full");
				father.insertKey(middleKey);
				//pointers
				int insertionIndex = new ArrayList<>(father.getKeys()).indexOf(middleKey);
				father.addPointer(insertionIndex+1, child2);
			} else {//if father is full
				System.out.println("father full");
				this.splitNode(father, middleKey);
			}
		}
		
		//update father of children
		insertionLocation.setFather(father);
		child2.setFather(father);
		this.root = father;
	}
	
	public void deleteKey(Integer key) {
		
	}

	@Override
	public String toString() {
		return "BTree [root=" + root + "]";
	}
	
	
}