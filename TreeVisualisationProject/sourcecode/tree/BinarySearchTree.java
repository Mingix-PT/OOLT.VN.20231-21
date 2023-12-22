package tree;

import java.util.*;

public class BinarySearchTree extends Tree {
    protected BinarySearchTree leftChild = null;
    protected BinarySearchTree rightChild = null;
    protected List<BinarySearchTree> dfsResult = new ArrayList<>();
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
    public Tree createTree() {
        return null;
    }

    @Override
    public Tree search(int key) {
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
    public boolean insert(int key) {
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
    public boolean delete(int key) {
        BinarySearchTree treeFound = (BinarySearchTree) search(key);
        // TO DO
        return treeFound != null;
    }

    @Override
    public boolean update(int currentKey, int newKey) {
        BinarySearchTree treeFound = (BinarySearchTree) search(currentKey);
        if (treeFound == null) {
            return false;
        }
        else {
            treeFound.key = newKey;
            return true;
        }
    }

    public List<BinarySearchTree> getDfsTraverse () {
        return dfsResult;
    }

    public static void dfsTraverse(BinarySearchTree tree) {
        if (tree==null) {
            return;
        }
        System.out.print(tree.getKey() + " ");
        dfsTraverse(tree.leftChild);
        dfsTraverse(tree.rightChild);
    }

    public List<BinarySearchTree> bfsTraverse() {
        List<BinarySearchTree> bfsResult = new ArrayList<>();
        Queue<BinarySearchTree> queueTree = new ArrayDeque<>();
        queueTree.offer(this);
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
        return bfsResult;
    }

    @Override
    public boolean insert(int parentKey, int key) {
        // Not available for binary search tree
        return false;
    }
}
