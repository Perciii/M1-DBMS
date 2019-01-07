package struct;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class Node {
	private Node father;
	private int order;
	private SortedSet<Integer> keys = new TreeSet<>();
	private ArrayList<Node> pointers;
	
	public Node(int order) {
		this.order = order;
		//pointers = new ArrayList<>(Collections.nCopies(order, null));
		pointers = new ArrayList<>();
	}
	
	public Node(int order, Node child1, Node child2) {
		this(order);
		pointers = new ArrayList<>(Arrays.asList(child1, child2));
	}
	
	public Node(int order, SortedSet<Integer> keys) {
		this(order);
		this.keys = keys;
	}
	
	public Node(int order, SortedSet<Integer> keys, Node father, Node child1, Node child2) {
		this(order, child1, child2);
		this.keys = keys;
		this.father = father;
	}
	
	public Node getFather() {
		return father;
	}
	
	public boolean isRoot() {
		return father==null;
	}
	
	public boolean isLeaf() {
		return pointers.isEmpty();
	}
	
	public boolean containsKey(Integer i) {
		return keys.contains(i);
	}
	
	public int nbKeys() {
		return keys.size();
	}
	
	public boolean isFull() {
		return nbKeys() == order;
	}
	
	private Node getChild(Integer key) {
		if(key < keys.first()) {
			return pointers.get(0);
		}
		Integer[] keysArray = (Integer[]) keys.toArray();
		for(int i = 0; i<order-1; i++) {
			if(key > keysArray[i] && (key < keysArray[i+1] || keysArray[i+1] == null)) {
				return pointers.get(i+1);
			}
		}
		return pointers.get(order-1);
	}
	
	public Node searchKey(Integer key) {
		if(pointers.isEmpty() || containsKey(key)) return this;
		return getChild(key).searchKey(key);
	}
	
	public void insertKey(Integer key) {
		if(!isFull())
		keys.add(key);
	}
	
	public Integer getMiddleKey(Integer insertedKey) {
		SortedSet<Integer> set = new TreeSet<>(keys);
		set.add(insertedKey);
		Integer[] keysArray = (Integer[]) set.toArray();
		
		return keysArray[order/2];
	}
	
	public SortedSet<Integer> getHeadSet(Integer cuttingKey){
		return keys.headSet(getMiddleKey(cuttingKey));
	}
	
	public SortedSet<Integer> getTailSet(Integer cuttingKey){
		return keys.tailSet(getMiddleKey(cuttingKey+1));//middle goes into father
	}
	
	public void removeKey() {
		
	}

	public void addPointer(int index, Node node) {
		this.pointers.add(index, node);
	}

	public SortedSet<Integer> getKeys() {
		return keys;
	}

	public void setKeys(SortedSet<Integer> keys) {
		this.keys = keys;
	}

	public void setFather(Node father) {
		this.father = father;
	}
}
