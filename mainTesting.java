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
		tree.insert(1, "1abcd");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.insert(4,"4adsflk");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.insert(5,"5adsfdsglk");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.insert(86,"86adasdgsflk");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.insert(35,"35adjhysflk");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.insert(930,"930adshjmdflk");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		System.out.println("finished inserting");
		tree.printTree();
		System.out.println("1111111111");
		tree.delete(tree.getRoot().getKey());
		tree.printTree();
		System.out.println("2222222222");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.delete(86);
		tree.printTree();
		System.out.println("33333333333");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.delete(930);
		tree.printTree();
		System.out.println("44444444444");
		assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));
		validateBlackRule(tree.getRoot());
		tree.delete(35);
		tree.printTree();
		System.out.println("55555555555");
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
