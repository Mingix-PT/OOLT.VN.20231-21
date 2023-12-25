package tree;

public class AVLTree extends BinarySearchTree {
    public AVLTree(int key) {
        super(key);
    }

    public int balanceFactor(int key) {
        BinaryTreeNode nodeFound = search(treeRoot, key);
        if (nodeFound == null) {
            return -9999;
        }
        return depth(nodeFound.leftChild.key) - depth(nodeFound.rightChild.key);
    }

    public boolean isBalance(int key) {
        int balanceFactor = balanceFactor(key);
        return balanceFactor == 1 || balanceFactor == 0 || balanceFactor == -1;
    }

    private int balanceFactor(BinaryTreeNode node) {
        if (node == null) {
            return -9999;
        }
        return depth(node.leftChild) - depth(node.rightChild);
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
        if (grandParentNode.leftChild == parentNode) {
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
        if (balanceFactor(node.leftChild) > 1 && balanceFactor(node) == 1) {
            leftLeftBalance(node);
        }
        else if (balanceFactor(node.leftChild) > 1 && balanceFactor(node) == -1) {
            leftRightBalance(node);
        }
        else if (balanceFactor(node.leftChild) < -1 && balanceFactor(node) == -1) {
            rightRightBalance(node);
        }
        else if (balanceFactor(node.leftChild) < -1 && balanceFactor(node) == 1) {
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

}
