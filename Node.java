package struct;

import java.util.ArrayList;
import java.util.Arrays;
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
		return nbKeys() == order;
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

	public SortedSet<Integer> getHeadSet(Integer cuttingKey) {
		return keys.headSet(getMiddleKey(cuttingKey));
	}

	public SortedSet<Integer> getTailSet(Integer cuttingKey) {
		return keys.tailSet(getMiddleKey(cuttingKey + 1));// middle goes into father
	}

	public void removeKey() {

	}

	public void addPointer(int index, Node node) {
		this.pointers.add(index, node);
	}

	public SortedSet<Integer> getKeys() {
		return keys;
	}

	public void setKeys(TreeSet<Integer> keys) {
		this.keys = keys;
	}

	public void setFather(Node father) {
		this.father = father;
	}

	@Override
	public String toString() {
		return "Node [keys=" + keys + ", pointers=" + Arrays.deepToString(pointers.toArray()) + ", father="
				+ (father != null) + "]";
	}

}