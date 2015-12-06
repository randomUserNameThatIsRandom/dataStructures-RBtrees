package data_structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import data_structures.RBTree.RBNode;

public class mainTesting {
	static Map<Integer, Integer> memoMap;
	static Set<Integer> leafs;
	static Map<Integer, String> expectedValuesInTree = new HashMap<Integer, String>();
	static int blackHight = -1;
	
	public mainTesting() {
		// TODO Auto-generated constructor stub
	}
//****************************************************************************************
	public static void main(String[] args) 
	{

		RBTree tree = new RBTree();
		insertAndValidate(tree,1 ,"1abcd" );
		deleteAndValidate(tree , 1);
		insertAndValidate(tree, 4,"4adsflk");
		insertAndValidate(tree, 5,"5adsfdsglk");
		insertAndValidate(tree, 86,"86adasdgsflk");
		deleteAndValidate(tree , 5);
		deleteAndValidate(tree , 4);
		deleteAndValidate(tree , 86);
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
		insertAndValidate(tree, 324,"35adjh63ysflk");
		insertAndValidate(tree, 643,"930ads53hjmdflk");
		deleteAndValidate(tree, 213);
		insertAndValidate(tree, 1354,"86ada2sdgsflk");
		insertAndValidate(tree, 13457,"35ad43j2hysflk");
		insertAndValidate(tree, 2531,"930ad5shjmdflk");
		insertAndValidate(tree, 8612243,"8623adasdgsflk");
		deleteAndValidate(tree, 392);
		deleteAndValidate(tree, 643);
		insertAndValidate(tree, 3521,"35ad34jhysflk");
		insertAndValidate(tree, 93540,"934360adshjmdflk");
		deleteAndValidate(tree, 8612243);
		insertAndValidate(tree, 84636,"86426adasdgsflk");
		deleteAndValidate(tree, 1354);
		deleteAndValidate(tree, tree.getRoot().getKey());
		insertAndValidate(tree, 1252,"211253adasdgsflk");
		insertAndValidate(tree, 5312,"12243adasdgsflk");
		insertAndValidate(tree, 3921422,"392");
		deleteAndValidate(tree, 84636);
		insertAndValidate(tree, 1124252,"21121253adasdgsflk");
		insertAndValidate(tree, 5423,"1253243adasdgsflk");
		insertAndValidate(tree, 392121422,"352192");
		insertAndValidate(tree, 125452,"21131253adasdgsflk");
		insertAndValidate(tree, 535112,"1221243adasdgsflk");
		insertAndValidate(tree, 392132422,"392");
		insertAndValidate(tree, 125212,"211212553adasdgsflk");
		insertAndValidate(tree, 1215,"5412243adasdgsflk");
		insertAndValidate(tree, 392121422,"39332");
		deleteAndValidate(tree, 5423);
		deleteAndValidate(tree, 125212);
		deleteAndValidate(tree, 392121422);
		insertAndValidate(tree, 214125,"3521adjh63ysflk");
		insertAndValidate(tree, 641253,"521542");
		deleteAndValidate(tree, 3521);
		deleteAndValidate(tree, 324);
		deleteAndValidate(tree, 5);
		deleteAndValidate(tree, 13457);
		insertAndValidate(tree, 84636,"86426adasdgsflk");
		tree.printTree();
		
		System.err.println("done");
		
		
	}
//****************************************************************************************	
	public static int deleteAndValidate(RBTree tree, Integer key)
	{
		System.out.println("deleting " + key);
		expectedValuesInTree.remove(key);
		int deleteRet = tree.delete(key);
		if(null != tree.getRoot())
		{
			assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));	
		}
		validateBlackRule(tree.getRoot());
		assert(validateTreeValues(tree));
		return deleteRet;
	}
	
//****************************************************************************************	
	public static int insertAndValidate(RBTree tree, Integer key, String value)
	{
		System.out.println("inserting " + key);
		int insertRet = tree.insert(key, value);
		if(-1 != insertRet)
		{
			expectedValuesInTree.put(key, value);
		}
		if(null != tree.getRoot())
		{
			assert(makeSureTreeIsValidBinarySearchTree(tree.getRoot()));	
		}
		validateBlackRule(tree.getRoot());
		assert(validateTreeValues(tree));
		return insertRet;
	}
	
//****************************************************************************************
	public static boolean validateTreeValues(RBTree tree)
	{
		int[] keys = tree.keysToArray();
		if (null == keys) 
		{
			return expectedValuesInTree.size() == 0;
		}
		if (keys.length != expectedValuesInTree.size()) 
		{
			System.err.println("keys array length is " + keys.length + " while the expected length is: " + expectedValuesInTree.size());
			return false;
		}
		String[] values = tree.valuesToArray();
		for (int i = 0; i < keys.length; i++) 
		{
			String currentValue = expectedValuesInTree.get(keys[i]);
			if (!values[i].equals(currentValue))
			{
				System.err.println("found wrong value "+ currentValue.toString() + " expected " + values[i].toString() + " for key "+ keys[i]);
				return false;
			}
			
		}
		
		return true;
		
		
	}
	
//****************************************************************************************
	public static boolean makeSureTreeIsValidBinarySearchTree(RBNode root)
	{
		if (-1 == root.getKey())
		{
			if (root.isRed()) 
			{
				System.err.println("We have a red dummy on our hands, that souldn't happen!");
				return false;
			}
			
			return true;
		}
		
		if (root != root.getLeft().getParent() && -1 != root.getLeft().getKey())
		{
			System.err.println("incorrect parrent for left child");
			return false;
		}
		
		if (root != root.getRight().getParent() && -1 != root.getRight().getKey())
		{
			System.err.println("incorrect parrent for right child");
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
			System.err.println("tree is invalid");
			return false;
		}
		if ((root.getKey() > root.getRight().getKey()) && (-1 != root.getRight().getKey()))
		{
			System.err.println("tree is invalid");
			return false;
		}
		
		return true;
	}
//****************************************************************************************
	public static void validateBlackRule(RBNode root)
	{
		memoMap = new HashMap<Integer, Integer>();
		leafs = new HashSet<Integer>();
		blackHight = -1;
		if (null == root)
		{
			return;
		}
		
		validateBlackRuleImp(root, -1);
	}
//****************************************************************************************
	public static void validateBlackRuleImp(RBNode root, int parentKey)
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
					System.err.println("black hight mismatch");
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
