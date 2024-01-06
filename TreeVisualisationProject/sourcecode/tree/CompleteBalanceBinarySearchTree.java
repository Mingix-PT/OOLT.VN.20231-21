package tree;

import java.util.*;

public class CompleteBalanceBinarySearchTree extends BinarySearchTree {

    public CompleteBalanceBinarySearchTree() {
        super();
    }

    public CompleteBalanceBinarySearchTree(int key) {
        super(key);
    }

    public CompleteBalanceBinarySearchTree completeBalanceTheTree() {
        inorderTraverse(treeRoot);
        System.out.println();
        CompleteBalanceBinarySearchTree newTree = new CompleteBalanceBinarySearchTree();
        List<List<Integer>> splitLists = new ArrayList<>();
        splitLists.add(inorderTraverseList);
        while (!splitLists.isEmpty()) {
            List<List<Integer>> treeSplitLists = new ArrayList<>();
            for (List<Integer> tIntList : splitLists) {
                int length = tIntList.size();
                // compute starting point
                int mid = calcMid(length); // length/2 ; //+ calcOffset(length);
                newTree.insert(tIntList.get(mid));
                if (mid > 0) {
                    treeSplitLists.add(tIntList.subList(0, mid));
                }
                if (length - (mid + 1) > 0) {
                    treeSplitLists.add(tIntList.subList(mid + 1, length));
                }
            }
            splitLists = treeSplitLists;
        }
        return newTree;
    }

    private int calcMid(int length) {
        if (length <= 4) {
            return length / 2;
        }
        int levelSize = 1;
        int total = 1;
        while (total <= length) {
            levelSize *= 2;
            total += levelSize;
        }
        int excess = length - total + levelSize;
        int minMid = (total - levelSize + 1) / 2;
        if (excess <= levelSize / 2) {
            return minMid + (excess - 1);
        }
        return minMid + levelSize / 2 - 1;
    }

    public boolean isCompleteBalance() {
        if (treeRoot == null) {
            return true;
        }

        Queue<BinaryTreeNode> queue = new LinkedList<>();
        queue.add(treeRoot);

        boolean endOfCompleteTree = false;

        while (!queue.isEmpty()) {
            BinaryTreeNode currentNode = queue.poll();

            if (currentNode.leftChild != null) {
                if (endOfCompleteTree) {
                    return false;
                }
                queue.add(currentNode.leftChild);
            } else {
                endOfCompleteTree = true;
            }

            if (currentNode.rightChild != null) {
                if (endOfCompleteTree) {
                    return false;
                }
                queue.add(currentNode.rightChild);
            } else {
                endOfCompleteTree = true;
            }
        }

        return true;
    }
}
