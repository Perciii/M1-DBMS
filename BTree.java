package struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

public class BTree {
	private int order;
	private Node root;
	
	public BTree(int order) {
		this.root = new Node(order);
	}

	public Node searchKey(Integer key) {
		return root.searchKey(key);
	}
	
	public void insertKey(Integer key) {
		Node insertionLocation = searchKey(key);
		
		if(!insertionLocation.containsKey(key)) {
			if(insertionLocation.nbKeys() < order-1) {//if node where we insert new key is not full
				insertionLocation.insertKey(key);
			} else { //if node where we insert new key is full
				int middleKey = insertionLocation.getMiddleKey(key);
				SortedSet<Integer> child1Keys = insertionLocation.getHeadSet(middleKey);
				SortedSet<Integer> child2Keys = insertionLocation.getTailSet(middleKey);
				
				insertionLocation.setKeys(child1Keys);
				Node child2 = new Node(order, child2Keys);
				
				Node father = insertionLocation.getFather();
				if(father == null) {//if orphan
					//create father with children
					father = new Node(order, new TreeSet<>(Arrays.asList(middleKey)), null, insertionLocation, child2);
					
				} else {//if father exists
					if(!father.isFull()) {//if father is not full
						father.insertKey(middleKey);
						//pointers
						int insertionIndex = new ArrayList<>(father.getKeys()).indexOf(middleKey);
						father.addPointer(insertionIndex+1, child2);
					} else {//if father is full
						//TODO
					}
				}
				
				//update father of children
				insertionLocation.setFather(father);
				child2.setFather(father);
				
				// insertionLocation => child1 (update set of keys)
				// child2 => new Node with new set of keys 
				// father : exists ? 
				// - no : create with pointers and set (middleKey)
				// - yes : update pointers and set (if not full)
			}
		}
		insertionLocation.insertKey(key);
	}
	
	public void deleteKey(Integer key) {
		
	}
	
}
