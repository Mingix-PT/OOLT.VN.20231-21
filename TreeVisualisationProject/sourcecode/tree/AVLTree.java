package tree;

import java.util.ArrayDeque;
import java.util.Queue;

public class AVLTree extends BinarySearchTree {
    public AVLTree(int key) {
        super(key);
    }

    private int rank(int key) {
        BinaryTreeNode nodeFound = search(treeRoot, key);
        return height(nodeFound) + 1;
    }

    private int rank(BinaryTreeNode node) {
        if (node == null) {
            return 0;
        }
        return height(node) + 1;
    }

    public int balanceFactor(int key) {
        BinaryTreeNode nodeFound = search(treeRoot, key);
        if (nodeFound == null) {
            return -999;
        }
        return rank(nodeFound.leftChild.key) - rank(nodeFound.rightChild.key);
    }

    public boolean isBalance(int key) {
        int balanceFactor = balanceFactor(key);
        return balanceFactor == 1 || balanceFactor == 0 || balanceFactor == -1;
    }

    private int balanceFactor(BinaryTreeNode node) {
        if (node == null) {
            return -999;
        }
        int rankLeft = rank(node.leftChild);
        int rankRight = rank(node.rightChild);
        return rankLeft - rankRight;
    }

    private boolean isBalance(BinaryTreeNode node) {
        int balanceFactor = balanceFactor(node);
        return balanceFactor == 1 || balanceFactor == 0 || balanceFactor == -1;
    }

    private void rotateRight (BinaryTreeNode parentNode) {
        BinaryTreeNode childNode = parentNode.leftChild;
        parentNode.leftChild = childNode.rightChild;
        childNode.rightChild = parentNode;
        BinaryTreeNode grandParentNode = searchParent(treeRoot, parentNode.key);
        if (grandParentNode == null) {
            treeRoot = childNode;
        }
        else if (grandParentNode.leftChild == parentNode) {
            grandParentNode.leftChild = childNode;
        }
        else {
            grandParentNode.rightChild = childNode;
        }
    }

    private void rotateLeft (BinaryTreeNode parentNode) {
        BinaryTreeNode childNode = parentNode.rightChild;
        parentNode.rightChild = childNode.leftChild;
        childNode.leftChild = parentNode;
        BinaryTreeNode grandParentNode = searchParent(treeRoot, parentNode.key);
        if (grandParentNode.leftChild == parentNode) {
            grandParentNode.leftChild = childNode;
        }
        else {
            grandParentNode.rightChild = childNode;
        }
    }

    @Override
    public boolean insert (int key) {
        if (!super.insert(key)) {
            return false;
        }
        BinaryTreeNode node = search(treeRoot, key);
        while (node != null) {
            if (!isBalance(node)) {
                rebalanceNode(node);
            }
            node = searchParent(treeRoot, node.key);
        }
        return true;
    }

    private void rebalanceNode (BinaryTreeNode node) {
        if (rank(node.leftChild) > 1 && rank(node.rightChild) == 1) {
            leftLeftBalance(node);
        }
        else if (rank(node.leftChild) > 1 && rank(node.rightChild) == -1) {
            leftRightBalance(node);
        }
        else if (rank(node.leftChild) < -1 && rank(node.rightChild) == -1) {
            rightRightBalance(node);
        }
        else if (rank(node.leftChild) < -1 && rank(node.rightChild) == 1) {
            rightLeftBalance(node);
        }
    }

    private void leftLeftBalance (BinaryTreeNode node) {
        rotateRight(node);
    }


    private void leftRightBalance (BinaryTreeNode node) {
        rotateLeft(node.leftChild);
        rotateRight(node);
    }

    private void rightRightBalance (BinaryTreeNode node) {
        rotateLeft(node);
    }

    private void rightLeftBalance (BinaryTreeNode node) {
        rotateRight(node.rightChild);
        rotateLeft(node);
    }

    public void balanceFactorTree () {
        System.out.println();
        Queue<BinaryTreeNode> queue = new ArrayDeque<>();
        queue.add(treeRoot);
        while (!queue.isEmpty()) {
            BinaryTreeNode topNode = queue.poll();
            System.out.println("Node: " + topNode.key + " Rank: " + rank(topNode) + " Balance Factor: " + balanceFactor(topNode));
            if (topNode.leftChild != null) {
                queue.add(topNode.leftChild);
            }
            if (topNode.rightChild != null) {
                queue.add(topNode.rightChild);
            }
        }
    }
}
