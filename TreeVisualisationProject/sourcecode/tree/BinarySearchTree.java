package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class BinarySearchTree extends Tree {
    protected BinarySearchTree leftChild;
    protected BinarySearchTree rightChild;
    public BinarySearchTree(int key) {
        super(key);
    }

    public BinarySearchTree getLeftChild() {
        return leftChild;
    }

    public BinarySearchTree getRightChild() {
        return rightChild;
    }

    @Override
    protected Tree createTree() {
        return null;
    }

    @Override
    protected Tree search(int key) {
        if (this.key == key) {
            return this;
        }
        else if (this.key > key) {
            if (leftChild == null) {
                return null;
            }
            else {
                return leftChild.search(key);
            }
        }
        else {
            if (rightChild == null) {
                return null;
            }
            else {
                return rightChild.search(key);
            }
        }
    }


    @Override
    protected boolean insert(int key) {
        if (this.key == key) {
            return false;
        }
        else if (key < this.key) {
            if (leftChild == null) {
                leftChild = new BinarySearchTree(key);
                return true;
            }
            else {
                return leftChild.insert(key);
            }
        }
        else {
            if (rightChild == null) {
                rightChild = new BinarySearchTree(key);
                return true;
            }
            else {
                return rightChild.insert(key);
            }
        }
    }

    @Override
    protected boolean delete(int key) {
        BinarySearchTree treeFound = (BinarySearchTree) search(key);
        if (treeFound == null) {
            return false;
        }
        else {
            // TO DO
        }
    }

    @Override
    protected boolean update(int currentKey, int newKey) {
        BinarySearchTree treeFound = (BinarySearchTree) search(currentKey);
        if (treeFound == null) {
            return false;
        }
        else {
            treeFound.key = newKey;
            return true;
        }
    }

    protected List<BinarySearchTree> dfsTraverse() {

        return null;
    }

    protected List<BinarySearchTree> bfsTraverse() {
        List<BinarySearchTree> bfsResult = new ArrayList<>();
        Queue<BinarySearchTree> queueTree = new PriorityQueue<>();
        queueTree.add(this);
        while (!queueTree.isEmpty()) {
            BinarySearchTree topTree = queueTree.poll();
            bfsResult.add(topTree);
            BinarySearchTree left = topTree.getLeftChild();
            BinarySearchTree right = topTree.getRightChild();
            if (left!=null) {
                queueTree.add(left);
            }
            if (right!=null) {
                queueTree.add(right);
            }
        }
        return null;
    }

    @Override
    protected boolean insert(int parentKey, int key) {
        // Not available for binary search tree
        return false;
    }
}
