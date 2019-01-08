package struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class BTree {
	private int order;
	private Node root;

	public BTree(int order) {
		this.order = order;
		this.root = new Node(order);
	}

	public Node searchKey(int key) {
		return root.searchKey(key);
	}

	public void insertKey(int key) {
		Node insertionLocation = searchKey(key);
		System.out.println(insertionLocation);

		if (!insertionLocation.containsKey(key)) {
			if (insertionLocation.nbKeys() < order - 1) {// if node where we insert new key is not full
				System.out.println("not full");
				insertionLocation.insertKey(key);
			} else { // if node where we insert new key is full
				System.out.println("full");
				this.splitNode(insertionLocation, key);

				// insertionLocation => child1 (update set of keys)
				// child2 => new Node with new set of keys
				// father : exists ?
				// - no : create with pointers and set (middleKey)
				// - yes : update pointers and set (if not full)
			}
		}
		System.out.println("BTREE : " + this.toString());
	}

	public void splitNode(Node insertionLocation, int newKey) {
		Objects.requireNonNull(insertionLocation);
		System.out.println("split");
		int middleKey = insertionLocation.getMiddleKey(newKey);
		System.out.println("middle key : " + middleKey);
		TreeSet<Integer> child1Keys = (TreeSet<Integer>) insertionLocation.getHeadSet(middleKey, newKey);
		TreeSet<Integer> child2Keys = (TreeSet<Integer>) insertionLocation.getTailSet(middleKey, newKey);
		
		System.out.println("child1keys : " + child1Keys);
		System.out.println("child2keys : " + child2Keys);

		insertionLocation.setKeys(child1Keys);
		Node child2 = new Node(order, child2Keys);

		Node father = insertionLocation.getFather();
		if (father == null) {// if orphan
			System.out.println("orphan, create Father :");
			// create father with children
			father = new Node(order, new TreeSet<>(Arrays.asList(middleKey)), null, insertionLocation, child2);

		} else {// if father exists
			System.out.println("has father");
			if (!father.isFull()) {// if father is not full
				System.out.println("father not full");
				father.insertKey(middleKey);
				// pointers
				int insertionIndex = new ArrayList<>(father.getKeys()).indexOf(middleKey);
				father.addPointer(insertionIndex + 1, child2);
			} else {// if father is full
				System.out.println("father full");
				this.splitNode(father, middleKey);
			}
		}

		// update father of children
		insertionLocation.setFather(father);
		child2.setFather(father);
		this.root = father;
	}

	public void deleteKey(int key) {
		System.out.println("deleting key " + key + "...");
		Node keyLocation = searchKey(key);
		if (keyLocation.isLeaf()) {
			if (keyLocation.nbKeys() >= order / 2) {
				keyLocation.removeKey(key);
			} else {// tree needs rebalancing because node will have less than (order/2)-1 keys
				Node rightSibling = keyLocation.getRightSibling();
				Node leftSibling = keyLocation.getLeftSibling();
				Node father = keyLocation.getFather();
				if (rightSibling != null && rightSibling.nbKeys() >= order / 2) {
					int keyToMoveDownFromFather = father.getKey(keyLocation.getNodeIndexInFather());
					keyLocation.insertKey(keyToMoveDownFromFather);

					int keyToMoveUpFromSibling = rightSibling.getKeys().first();
					father.insertKey(keyToMoveUpFromSibling);

				} else if (leftSibling != null && leftSibling.nbKeys() >= order / 2) {
					int keyToMoveDownFromFather = father.getKey(keyLocation.getNodeIndexInFather() - 1);
					keyLocation.insertKey(keyToMoveDownFromFather);

					int keyToMoveUpFromSibling = leftSibling.getKeys().last();
					father.insertKey(keyToMoveUpFromSibling);
				} else if (leftSibling != null){
					int keyToMoveDownFromFather = father.getKey(keyLocation.getNodeIndexInFather() - 1);
					keyLocation.insertKey(keyToMoveDownFromFather);

					SortedSet<Integer> keyLocationKeys = keyLocation.getKeys();
					leftSibling.getKeys().addAll(keyLocationKeys);
					
				}
			}
		}
	}

	@Override
	public String toString() {
		return "BTree [root=" + root + "]";
	}

}