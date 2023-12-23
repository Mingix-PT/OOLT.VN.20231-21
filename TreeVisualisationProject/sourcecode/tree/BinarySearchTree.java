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

        public int getKey() {
            return key;
        }

        public boolean isLeaf() {
            return (leftChild == null && rightChild == null);
        }

        public BinaryTreeNode getLeftChild() {
            return leftChild;
        }

        public BinaryTreeNode getRightChild() {
            return rightChild;
        }
    }
    protected BinaryTreeNode leftMostNode(BinaryTreeNode root) {
        if (root.leftChild == null) {
            return root;
        }
        return leftMostNode(root.leftChild);
    }

    protected BinaryTreeNode rightMostNode(BinaryTreeNode root) {
        if (root.rightChild == null) {
            return root;
        }
        return rightMostNode(root.rightChild);
    }
    protected BinaryTreeNode treeRoot;
    public BinarySearchTree(int key) {
        this.treeRoot = new BinaryTreeNode(key);
    }
    protected List<BinarySearchTree> dfsResult = new ArrayList<>();

    @Override
    public Tree createTree() {
        return null;
    }


    protected void setNullChild(BinaryTreeNode parentNode, BinaryTreeNode childNode) {
        if (parentNode.leftChild == childNode) {
            parentNode.leftChild = null;
        }
        else {
            parentNode.rightChild = null;
        }
    }

    protected BinaryTreeNode searchParent(BinaryTreeNode root, int key) {
        if (root.key==key) {
            return null;
        }
        else if (root.key > key) {
            if (root.leftChild == null) {
                return null;
            }
            if (root.leftChild.key == key) {
                return root;
            }
            return searchParent(root.leftChild, key);
        }
        else {
            if (root.rightChild == null) {
                return null;
            }
            if (root.rightChild.key == key) {
                return root;
            }
            return searchParent(root.rightChild, key);
        }
    }

    protected BinaryTreeNode search(BinaryTreeNode root, int key) {
        if (root.key == key) {
            return root;
        }
        else if (root.key > key) {
            if (root.leftChild == null) {
                return null;
            }
            return search(root.leftChild, key);
        }
        else {
            if (root.rightChild == null) {
                return null;
            }
            return search(root.rightChild, key);
        }
    }


    @Override
    public boolean insert(int key) {
        return insert(treeRoot, key);
    }

    protected boolean insert(BinaryTreeNode root, int key) {
        if (root.key == key) {
            return false;
        }
        else if (key < root.key) {
            if (root.leftChild == null) {
                root.leftChild = new BinaryTreeNode(key);
                return true;
            }
            else {
                return insert(root.leftChild, key);
            }
        }
        else {
            if (root.rightChild == null) {
                root.rightChild = new BinaryTreeNode(key);
                return true;
            }
            else {
                return insert(root.rightChild, key);
            }
        }
    }

    @Override
    public boolean delete(int key) {
        return delete(treeRoot, key);
    }

    protected boolean delete(BinaryTreeNode root, int key) {
        BinaryTreeNode nodeFound = search(root, key);
        if (nodeFound==null) {
            return false;
        }
        BinaryTreeNode parent = searchParent(treeRoot, key);
        if (nodeFound.isLeaf()) {
            setNullChild(parent, nodeFound);
        }
        else if (nodeFound.leftChild==null || nodeFound.rightChild==null) {
            setNullChild(parent, nodeFound);
        }
        else {
            BinaryTreeNode right = nodeFound.rightChild;
            BinaryTreeNode leftMostOfRight = leftMostNode(right);
            if (parent != null) { // Not the root
                updateNode(nodeFound, leftMostOfRight);
                leftMostOfRight.leftChild = nodeFound.leftChild;
            } else { // The root
                BinaryTreeNode temp = searchParent(treeRoot, leftMostOfRight.key);
                temp.leftChild = null;
                leftMostOfRight.leftChild = nodeFound.leftChild;
                leftMostOfRight.rightChild = nodeFound.rightChild;
                treeRoot = leftMostOfRight;
            }
        }
        return true;
    }

    protected void updateNode(BinaryTreeNode currentNode, BinaryTreeNode newNode) {
        BinaryTreeNode parent = searchParent(treeRoot, currentNode.key);
        if (parent != null) {
            if (parent.leftChild == currentNode) {
                parent.leftChild = newNode;
            }
            else {
                parent.rightChild = newNode;
            }
        }
    }
    @Override
    public boolean update(int currentKey, int newKey) {
        return (delete(currentKey) && insert(newKey));
    }

    public void dfsTraverse() {
        dfsTraverse(treeRoot);
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

    protected static void traverseTree(StringBuilder sb, String padding, String pointer, BinaryTreeNode tree,
                                     boolean hasRightSibling) {
        if (tree != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(tree.getKey());

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└──";
            String pointerLeft = (tree.getRightChild() != null) ? "├──" : "└──";

            traverseTree(sb, paddingForBoth, pointerLeft, tree.getLeftChild(), tree.getRightChild() != null);
            traverseTree(sb, paddingForBoth, pointerRight, tree.getRightChild(), false);
        }
    }


    // Print tree
    public static String traversePreOrder(BinarySearchTree tree) {

        if (tree == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(tree.treeRoot.getKey());

        String pointerRight = "└──";
        String pointerLeft = (tree.treeRoot.getRightChild() != null) ? "├──" : "└──";

        traverseTree(sb, "", pointerLeft, tree.treeRoot.getLeftChild(), tree.treeRoot.getRightChild() != null);
        traverseTree(sb, "", pointerRight, tree.treeRoot.getRightChild(), false);

        return sb.toString();
    }

    public void print() {
        System.out.print(traversePreOrder(this));
    }

    @Override
    public boolean insert(int parentKey, int key) {
        // Not available for binary search tree
        return false;
    }
}
