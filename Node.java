package struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Node {
	private Node father;
	private int order;
	private TreeSet<Integer> keys = new TreeSet<>();
	private ArrayList<Node> pointers;

	public Node(int order) {
		this.order = order;
		// pointers = new ArrayList<>(Collections.nCopies(order, null));
		pointers = new ArrayList<>();
	}

	public Node(int order, Node child1, Node child2) {
		this(order);
		pointers = new ArrayList<>(Arrays.asList(child1, child2));
		System.out.println(child1 + " - " + child2);
	}

	public Node(int order, TreeSet<Integer> keys) {
		this(order);
		this.keys = keys;
	}

	public Node(int order, TreeSet<Integer> keys, Node father, Node child1, Node child2) {
		this(order, child1, child2);
		this.keys = keys;
		this.father = father;
	}

	public Node getFather() {
		return father;
	}

	public boolean isRoot() {
		return father == null;
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
		return nbKeys() == order-1;
	}

	private Node getChild(Integer key) {
		System.out.println("get child key : "+key);
		System.out.println("keysfirst : " + keys.first());
		if (key < keys.first()) {
			System.out.println("child before keys "+0);
			return pointers.get(0);
		}

		List<Integer> keysArray = new ArrayList<>(keys);
		int size = keysArray.size();
		for (int i = 0; i < size - 1; i++) {
			if (key > keysArray.get(i) && key < keysArray.get(i + 1)) {
				System.out.println("child between keys at index : "+ (i+1));
				return pointers.get(i + 1);
			}
		}
		System.out.println("child after keys "+ (size));
		return pointers.get(size);
	}

	public Node searchKey(Integer key) {
		if (pointers.isEmpty() || containsKey(key))
			return this;
		return getChild(key).searchKey(key);
	}

	public void insertKey(int key) {
		if (!isFull())
			//keys.add(key);
		keys = new TreeSet<Integer>() {{
			addAll(keys);
			add(key);
		}};
	}

	public Integer getMiddleKey(Integer insertedKey) {
		SortedSet<Integer> set = new TreeSet<>(keys);
		set.add(insertedKey);
		// Object[] keysArray = set.toArray();

		// return (Integer) keysArray[(order / 2) - 1];

		return new ArrayList<>(set).get((order / 2));
	}

	public SortedSet<Integer> getHeadSet(Integer cuttingKey, Integer insertedKey) {
		System.out.println("head set cutting key : " + cuttingKey);
		SortedSet<Integer> headKeys = keys.headSet(getMiddleKey(cuttingKey));
		if(insertedKey < cuttingKey) {
			keys = new TreeSet<Integer>() {{
				addAll(headKeys);
				add(insertedKey);
			}};
			return keys;
		}
		return headKeys;
	}

	public SortedSet<Integer> getTailSet(Integer cuttingKey, Integer insertedKey) {
		System.out.println("tail set cutting key : " + cuttingKey);
		SortedSet<Integer> tailKeys = keys.tailSet(getMiddleKey(cuttingKey), false);
		
		if(cuttingKey < insertedKey) {
			keys = new TreeSet<Integer>() {{
				addAll(tailKeys);
				add(insertedKey);
			}};
			
			return keys;// middle goes into father
		}
		return tailKeys;
	}

	public void removeKey(int key) {
		this.keys.remove(key);
	}

	public void addPointer(int index, Node node) {
		this.pointers.add(index, node);
	}

	public ArrayList<Node> getPointers() {
		return pointers;
	}

	public SortedSet<Integer> getKeys() {
		return keys;
	}
	
	public Integer getKey(int index) {
		Iterator<Integer> i = keys.iterator();
	    int element = 0;
	    while (i.hasNext() && index > 0)
	    {
	        element = i.next();
	    }
	    return element;
	}

	public void setKeys(TreeSet<Integer> keys) {
		this.keys = keys;
	}

	public void setFather(Node father) {
		this.father = father;
	}
	
	public int getNodeIndexInFather() {
		return getFather().getPointers().indexOf(this);
	}
	
	public Node getRightSibling() {
		ArrayList<Node> fatherPointers = getFather().getPointers();
		int indexOfThisNode = fatherPointers.indexOf(this);
		if(indexOfThisNode+1 < fatherPointers.size()) {
			Node rightSibling = fatherPointers.get(indexOfThisNode+1);
			return rightSibling;
		}
		return null;
	}
	
	public Node getLeftSibling() {
		ArrayList<Node> fatherPointers = getFather().getPointers();
		int indexOfThisNode = fatherPointers.indexOf(this);
		Node leftSibling = fatherPointers.get(indexOfThisNode);
		return leftSibling;
	}

	@Override
	public String toString() {
		return "Node [keys=" + keys + ", pointers=" + Arrays.deepToString(pointers.toArray()) + ", father="
				+ (father != null) + "]";
	}

}