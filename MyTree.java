package data_structures;

import java.util.Iterator;
import java.util.TreeSet;

class MyTree {
	private TreeSet<Integer> set;

	MyTree() {
		this.set = new TreeSet<Integer>();
	}

	public int size() {
		return this.set.size();
	}

	public boolean empty() {
		return this.set.isEmpty();
	}

	public void insert(int v) {
		this.set.add((Integer)v);
	}

	public void delete(int v) {
		this.set.remove((Integer)v);
	}

	public int min() {
		if (this.empty())
			return -1;
		return (int)(this.set.first());
	}

	public int max() {
		if (this.empty())
			return -1;
		return (int)(this.set.last());
	}

	public boolean contains(int v) {
		return this.set.contains((Integer)v);
	}

	public int[] array() {
		int[] arr = new int[this.size()];
		Iterator<Integer> itr = this.set.iterator();
		for (int i = 0; i < this.size(); i++)
			arr[i] = (int)(itr.next());
		return arr;
	}
}


class TestRun implements Runnable {

    private int test_num;
    public boolean success = false;

    public TestRun(int test_num) {
        this.test_num = test_num;
    }

    public void run() {
        try {
        	switch (this.test_num) {
        		case 0: this.success = ExTester.emptyTreeTest();
        				break;
        		case 1: this.success = ExTester.insertAndSearchTest();
        				break;
        		case 2: this.success = ExTester.deleteAndSearchTest();
        				break;
        		case 3: this.success = ExTester.insertAndMinMaxTest();
        				break;
        		case 4: this.success = ExTester.deleteMinMaxTest();
        				break;
        		case 5: this.success = ExTester.insertAndSizeEmptyTest();
        				break;
        		case 6: this.success = ExTester.insertAndArraysTest();
        				break;
        		case 7: this.success = ExTester.deleteAndArraysTest();
        				break;
        		case 8: this.success = ExTester.doubleInsertTest();
        				break;
        		case 9: this.success = ExTester.doubleDeleteTest();
        				break;
        	}
        } catch (Exception e) {
        	System.out.println("Exception on Test " + test_num + " : " + e);
        }
    }
}
