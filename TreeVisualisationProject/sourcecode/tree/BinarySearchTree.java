package tree;

import java.util.*;

public class BinarySearchTree extends Tree {
    protected class BinaryTreeNode {
        private int key;
        private BinaryTreeNode leftChild;
        private BinaryTreeNode rightChild;

        public BinaryTreeNode(int key) {
            this.key = key;
            this.leftChild = null;
            this.rightChild = null;
        }
    }
    protected BinaryTreeNode root;
    public BinarySearchTree(int key) {
        this.root = new BinaryTreeNode(key);
    }

    protected BinaryTreeNode getLeftChild(BinaryTreeNode node) {
        return node.leftChild;
    }

    protected BinaryTreeNode getRightChild(BinaryTreeNode node) {
        return node.rightChild;
    }

    protected boolean isLeaf(BinaryTreeNode node) {
        return (node.leftChild == null && node.rightChild == null);
    }

    protected List<BinarySearchTree> dfsResult = new ArrayList<>();

    @Override
    public Tree createTree() {
        return null;
    }

    protected BinaryTreeNode leftMostNode(BinaryTreeNode node) {
        if (node.leftChild == null) {
            return node;
        }
        return leftMostNode(node.leftChild);
    }

    protected BinaryTreeNode rightMostNode(BinaryTreeNode node) {
        if (node.rightChild == null) {
            return node;
        }
        return rightMostNode(node.rightChild);
    }

    private void setNullChild(BinaryTreeNode node, int key) {
        if (node.leftChild.key == key) {
            node.leftChild = null;
        }
        else {
            node.rightChild = null;
        }
    }

    protected BinaryTreeNode searchParent(BinaryTreeNode node, int key) {
        if (node.key==key) {
            return null;
        }
        else if (node.key > key) {
            if (node.leftChild == null) {
                return null;
            }
            if (node.leftChild.key == key) {
                return node;
            }
            return searchParent(node.leftChild, key);
        }
        else {
            if (node.rightChild == null) {
                return null;
            }
            if (node.rightChild.key == key) {
                return node;
            }
            return searchParent(node.rightChild, key);
        }
    }

    protected BinaryTreeNode search(BinaryTreeNode node, int key) {
        if (node.key == key) {
            return node;
        }
        else if (node.key > key) {
            if (node.leftChild == null) {
                return null;
            }
            return search(node.leftChild, key);
        }
        else {
            if (node.rightChild == null) {
                return null;
            }
            return search(node.rightChild, key);
        }
    }


    @Override
    public boolean insert(int key) {
        return insert(root, key);
    }

    protected boolean insert(BinaryTreeNode node, int key) {
        if (node.key == key) {
            return false;
        }
        else if (key < node.key) {
            if (node.leftChild == null) {
                node.leftChild = new BinaryTreeNode(key);
                return true;
            }
            else {
                return insert(node.leftChild, key);
            }
        }
        else {
            if (node.rightChild == null) {
                node.rightChild = new BinaryTreeNode(key);
                return true;
            }
            else {
                return insert(node.rightChild, key);
            }
        }
    }

    @Override
    public boolean delete(int key) {
        return delete(root, key);
    }

    protected boolean delete(BinaryTreeNode node, int key) {
        BinarySearchTree treeFound = search(key);
        if (treeFound==null) {
            return false;
        }
        BinarySearchTree parent = searchParent(key);
        if (treeFound.isLeaf()) {
            parent.setNullChild(key);
        }
        else if (treeFound.leftChild!=null && treeFound.rightChild==null) {
            if (parent.leftChild == treeFound) {
                parent.leftChild = treeFound.leftChild;
            }
            else {
                parent.rightChild = treeFound.leftChild;
            }
        }
        else if (treeFound.leftChild==null && treeFound.rightChild!=null) {
            if (parent.leftChild == treeFound) {
                parent.leftChild = treeFound.rightChild;
            }
            else {
                parent.rightChild = treeFound.rightChild;
            }
        }
        else {
            BinarySearchTree right = treeFound.rightChild;
            BinarySearchTree leftMostOfRight = right.leftMostTree();
            if (parent != null) { // Not the root
                updateNode(treeFound, leftMostOfRight);
                leftMostOfRight.leftChild = treeFound.leftChild;
            } else { // The root
                BinarySearchTree temp = searchParent(leftMostOfRight.key);
                temp.leftChild = null;
                treeFound.key = leftMostOfRight.key;
            }
        }
        return true;
    }

    public void updateNode(BinarySearchTree currentTree, BinarySearchTree newTree) {
        BinarySearchTree parent = searchParent(currentTree.root.key);
        if (parent != null) {
            if (parent.root.leftChild == currentTree.root) {
                parent.leftChild = newTree;
            }
            else {
                parent.rightChild = newTree;
            }
        }
    }
    @Override
    public boolean update(int currentKey, int newKey) {
        return (delete(currentKey) && insert(newKey));
    }

    public void dfsTraverse() {
        dfsTraverse(root);
    }
    protected static void dfsTraverse(BinaryTreeNode node) {
        if (node==null) {
            return;
        }
        // TO DO
        System.out.print(node.key + " ");
        // Preorder traverse
        dfsTraverse(node.leftChild);
        dfsTraverse(node.rightChild);
    }

    @Override
    public void bfsTraverse() {

    }

    protected List<BinaryTreeNode> bfsTraverse(BinaryTreeNode node) {
        List<BinaryTreeNode> bfsResult = new ArrayList<>();
        Queue<BinaryTreeNode> queueNode = new ArrayDeque<>();
        queueNode.offer(node);
        while (!queueNode.isEmpty()) {
            BinaryTreeNode topNode = queueNode.poll();
            bfsResult.add(topNode);
            BinaryTreeNode left = topNode.leftChild;
            BinaryTreeNode right = topNode.rightChild;
            if (left!=null) {
                queueNode.add(left);
            }
            if (right!=null) {
                queueNode.add(right);
            }
        }
        return bfsResult;
    }

    @Override
    public boolean insert(int parentKey, int key) {
        // Not available for binary search tree
        return false;
    }
}
