package struct;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BTreeTest {

	@Test
	void insertKeyTest() {
		BTree btree = new BTree(4);
		btree.insertKey(1);
		btree.insertKey(2);
		btree.insertKey(10);
		btree.insertKey(4);
		btree.insertKey(6);
		System.out.println(btree);
	}

}
