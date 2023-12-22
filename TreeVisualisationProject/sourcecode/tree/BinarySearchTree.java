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

    public boolean isLeaf() {
        return (leftChild == null && rightChild == null);
    }

    public void updateNode(BinarySearchTree currentTree, BinarySearchTree newTree) {
        BinarySearchTree parent = searchParent(currentTree.key);
        if (parent != null) {
            if (parent.leftChild == currentTree) {
                parent.leftChild = newTree;
            }
            else {
                parent.rightChild = newTree;
            }
        }
    }

    @Override
    public Tree createTree() {
        return null;
    }

    public BinarySearchTree leftMostTree() {
        if (this.leftChild == null) {
            return this;
        }
        return leftChild.leftMostTree();
    }

    public BinarySearchTree rightMostTree() {
        if (this.rightChild == null) {
            return this;
        }
        return rightChild.rightMostTree();
    }

    private void setNullChild(int key) {
        if (this.leftChild.key == key) {
            leftChild = null;
        }
        else {
            rightChild = null;
        }
    }

    public BinarySearchTree searchParent(int key) {
        if (this.key==key) {
            return null;
        }
        else if (this.key > key) {
            if (leftChild == null) {
                return null;
            }
            if (leftChild.key == key) {
                return this;
            }
            return leftChild.searchParent(key);
        }
        else {
            if (rightChild == null) {
                return null;
            }
            if (rightChild.key == key) {
                return this;
            }
            return rightChild.searchParent(key);
        }
    }

    public BinarySearchTree search(int key) {
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

    @Override
    public boolean update(int currentKey, int newKey) {
        return (delete(currentKey) && insert(newKey));
    }

    public List<BinarySearchTree> getDfsTraverseResult () {
        return dfsResult;
    }

    public static void dfsTraverse(BinarySearchTree tree) {
        if (tree==null) {
            return;
        }
        // TO DO
        System.out.print(tree.key + " ");
        // Preorder traverse
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
