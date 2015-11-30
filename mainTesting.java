package data_structures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import data_structures.RBTree.RBNode;

public class mainTesting {
	static Map<Integer, Integer> memoMap;
	static Set<Integer> leafs;
	static int blackHight = -1;
	
	public mainTesting() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) 
	{

		RBTree tree = new RBTree();
		insertAndValidate(tree,1 ,"1abcd" );
		insertAndValidate(tree, 4,"4adsflk");
		insertAndValidate(tree, 5,"5adsfdsglk");
		insertAndValidate(tree, 86,"86adasdgsflk");
		insertAndValidate(tree, 35,"35adjhysflk");
		insertAndValidate(tree, 930,"930adshjmdflk");
		insertAndValidate(tree, 86,"86adasdgsflk");
		deleteAndValidate(tree, tree.getRoot().getKey());
		deleteAndValidate(tree, 86);
		deleteAndValidate(tree, 930);
		deleteAndValidate(tree, 35);
		insertAndValidate(tree, 83,"83adasdgsflk");
		insertAndValidate(tree, 23,"23adasdgsflk");
		tree.printTree();
		deleteAndValidate(tree, 1);
		insertAndValidate(tree, 213,"213adasdgsflk");
		insertAndValidate(tree, 123,"123adasdgsflk");
		insertAndValidate(tree, 392,"392");
		deleteAndValidate(tree, 83);
		deleteAndValidate(tree, 23);
		deleteAndValidate(tree, 213);
		deleteAndValidate(tree, 392);
		
		
		
	}
	
	private static void deleteAndValidate(RBTree tree, Integer key)
	{
		System.out.println("deleting " + key);
		tree.delete(key);
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
	}
	
	private static void insertAndValidate(RBTree tree, Integer key, String value)
	{
		System.out.println("inserting " + key);
		tree.insert(key, value);
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
	}
	
	private static boolean makeSureTreeIsValidBinarySearchTree(RBNode root)
	{
		if (-1 == root.getKey())
		{
			if (root.isRed()) 
			{
				System.out.println("We have a red dummy on our hands, that souldn't happen!");
				return false;
			}
			
			return true;
		}
		
		if (root != root.getLeft().getParent() && -1 != root.getLeft().getKey())
		{
			System.out.println("incorrect parrent for left child");
			return false;
		}
		
		if (root != root.getRight().getParent() && -1 != root.getRight().getKey())
		{
			System.out.println("incorrect parrent for right child");
			return false;
		}
		
		boolean isLeftValid = makeSureTreeIsValidBinarySearchTree(root.getLeft());
		boolean isRightValid = makeSureTreeIsValidBinarySearchTree(root.getRight());
		
		if(!(isLeftValid && isRightValid))
		{
			return false;
		}
		
		// both are valid...
		if ((root.getKey() < root.getLeft().getKey()) && (-1 != root.getLeft().getKey()))
		{
			System.out.println("tree is invalid");
			return false;
		}
		if ((root.getKey() > root.getRight().getKey()) && (-1 != root.getRight().getKey()))
		{
			System.out.println("tree is invalid");
			return false;
		}
		
		return true;
	}
	
	private static void validateBlackRule(RBNode root)
	{
		memoMap = new HashMap<Integer, Integer>();
		leafs = new HashSet<Integer>();
		blackHight = -1;
		validateBlackRuleImp(root, -1);
	}
	
	private static void validateBlackRuleImp(RBNode root, int parentKey)
	{
		
		if(-1 == parentKey)
		{
			memoMap.put(root.getKey(), root.isRed() ? 0 : 1);
		}
		else
		{
			memoMap.put(root.getKey(), root.isRed() ? memoMap.get(parentKey) : memoMap.get(parentKey) + 1);
		}
		
		if(-1 == root.getKey())
		{
			if (-1 == blackHight)
				blackHight = memoMap.get(parentKey);
			else
			{
				if (blackHight != memoMap.get(parentKey))
				{
					System.out.println("black hight mismatch");
					assert(false);
				}
			}
		}
		else
		{	
			validateBlackRuleImp(root.getLeft(), root.getKey());
			validateBlackRuleImp(root.getRight(), root.getKey());
		}
	}
	
}
