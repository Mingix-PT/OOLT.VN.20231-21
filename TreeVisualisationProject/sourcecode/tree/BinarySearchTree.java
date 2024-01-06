package tree;

import java.util.*;

public class BinarySearchTree extends Tree {

    protected List<Integer> inorderTraverseList = new ArrayList<>();

    public BinarySearchTree(int key) {
        this.treeRoot = new BinaryTreeNode(key);
    }

    public BinarySearchTree() {
        this.treeRoot = null;
    }

    public BinaryTreeNode leftMostNode(BinaryTreeNode root) {
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

    @Override
    public int height() {
        return height(treeRoot);
    }

    @Override
    public boolean search(int key) {
        return search(treeRoot, key) != null;
    }

    protected int height(BinaryTreeNode root) {
        if (root == null || root.isLeaf()) {
            return 0;
        }
        return 1 + Math.max(height(root.leftChild), height(root.rightChild));
    }

    protected BinaryTreeNode treeRoot;

    public BinaryTreeNode getTreeRoot () {
        return treeRoot;
    }

    public int depth(int key) {
        BinaryTreeNode nodeFound = search(treeRoot, key);
        if (nodeFound == null) {
            return -1;
        }
        int distance = -1;
        while (nodeFound != null) {
            distance++;
            nodeFound = searchParent(treeRoot, nodeFound.key);
        }
        return distance;
    }

    protected int depth(BinaryTreeNode node) {
        if (node == null) {
            return -1;
        }
        int distance = -1;
        while (node != null) {
            distance++;
            node = searchParent(treeRoot, node.key);
        }
        return distance;
    }

    @Override
    public Tree createTree() {
        return null;
    }

    protected void setNodeToChild(BinaryTreeNode node) {
        if (node.leftChild == null && node.rightChild != null) {
            node.setNode(node.rightChild);
        } else if (node.leftChild != null && node.rightChild == null) {
            node.setNode(node.leftChild);
        } else if (node.isLeaf()) {
            deleteFromParent(node);
        }
    }

    protected void deleteFromParent(BinaryTreeNode childNode) {
        BinaryTreeNode parentNode = searchParent(treeRoot, childNode.key);
        if (parentNode.leftChild == childNode) {
            parentNode.leftChild = null;
        } else {
            parentNode.rightChild = null;
        }
    }

    protected BinaryTreeNode searchParent(BinaryTreeNode root, int key) {
        if (root.key == key) {
            return null;
        } else if (root.key > key) {
            if (root.leftChild == null) {
                return null;
            }
            if (root.leftChild.key == key) {
                return root;
            }
            return searchParent(root.leftChild, key);
        } else {
            if (root.rightChild == null) {
                return null;
            }
            if (root.rightChild.key == key) {
                return root;
            }
            return searchParent(root.rightChild, key);
        }
    }

    public BinaryTreeNode searchParent(int key) {
        return searchParent(treeRoot, key);
    }

    protected BinaryTreeNode search(BinaryTreeNode root, int key) {
        if (root.key == key) {
            return root;
        } else if (root.key > key) {
            if (root.leftChild == null) {
                return null;
            }
            return search(root.leftChild, key);
        } else {
            if (root.rightChild == null) {
                return null;
            }
            return search(root.rightChild, key);
        }
    }

    public BinaryTreeNode searchNode(int key) {
        return search(treeRoot, key);
    }

    @Override
    public boolean insert(int key) {
        return insert(treeRoot, key);
    }

    protected boolean insert(BinaryTreeNode root, int key) {
        if (treeRoot == null) {
            treeRoot = new BinaryTreeNode(key);
            return true;
        }
        if (root.key == key) {
            return false;
        } else if (key < root.key) {
            if (root.leftChild == null) {
                root.leftChild = new BinaryTreeNode(key);
                return true;
            } else {
                return insert(root.leftChild, key);
            }
        } else {
            if (root.rightChild == null) {
                root.rightChild = new BinaryTreeNode(key);
                return true;
            } else {
                return insert(root.rightChild, key);
            }
        }
    }

    @Override
    public boolean delete(int key) {
        BinaryTreeNode nodeFound = search(treeRoot, key);
        if (nodeFound == null) {
            return false;
        }
        BinaryTreeNode parent = searchParent(treeRoot, key);
        if (nodeFound.leftChild == null || nodeFound.rightChild == null) {
            setNodeToChild(nodeFound);
        }
        else {
            BinaryTreeNode right = nodeFound.rightChild;
            BinaryTreeNode leftMostOfRight = leftMostNode(right);
            if (leftMostOfRight == right) {
                nodeFound.rightChild = right.rightChild;
                nodeFound.key = right.key;
            } else {
                BinaryTreeNode parentLeftMostOfRight = searchParent(treeRoot, leftMostOfRight.key);
                parentLeftMostOfRight.leftChild = leftMostOfRight.rightChild;
                if (parent != null) { // Not the root
                    updateNode(nodeFound, leftMostOfRight);
                } else { // The root
                    leftMostOfRight.leftChild = nodeFound.leftChild;
                    leftMostOfRight.rightChild = nodeFound.rightChild;
                    treeRoot = leftMostOfRight;
                }
            }
        }
        return true;
    }

    protected void updateNode(BinaryTreeNode currentNode, BinaryTreeNode newNode) {
//        BinaryTreeNode parent = searchParent(treeRoot, currentNode.key);
//        if (parent != null) {
//            if (parent.leftChild == currentNode) {
//                parent.leftChild = newNode;
//            } else {
//                parent.rightChild = newNode;
//            }
//        }
        currentNode.key = newNode.key;
    }

    @Override
    public boolean update(int currentKey, int newKey) {
        return (delete(currentKey) && insert(newKey));
    }

    @Override
    public void dfsTraverse() {
        System.out.println();
        dfsTraverse(treeRoot);
        System.out.println();
    }

    protected static void dfsTraverse(BinaryTreeNode node) {
        if (node == null) {
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
        List<BinaryTreeNode> bfsResult = bfsTraverse(treeRoot);
        for (BinaryTreeNode node : bfsResult) {
            System.out.print(node.key + " ");
        }
        System.out.println();
    }

    public List<BinaryTreeNode> bfsTraverse(BinaryTreeNode node) {
        List<BinaryTreeNode> bfsResult = new ArrayList<>();
        Queue<BinaryTreeNode> queueNode = new ArrayDeque<>();
        queueNode.offer(node);
        while (!queueNode.isEmpty()) {
            BinaryTreeNode topNode = queueNode.poll();
            bfsResult.add(topNode);
            BinaryTreeNode left = topNode.leftChild;
            BinaryTreeNode right = topNode.rightChild;
            if (left != null) {
                queueNode.add(left);
            }
            if (right != null) {
                queueNode.add(right);
            }
        }
        return bfsResult;
    }

    protected void inorderTraverse(BinaryTreeNode root) {
        if (root == null) {
            return;
        }
        inorderTraverse(root.leftChild);
        System.out.print(root.key + " ");
        inorderTraverseList.add(root.key);
        inorderTraverse(root.rightChild);
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
        System.out.println();
    }

    @Override
    public boolean insert(int parentKey, int key) {
        // Not available for binary search tree
        return false;
    }

    public void setTreeRoot(int key) {
        treeRoot = new BinaryTreeNode(key);
    }

    public long countNodes(BinaryTreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.isLeaf()) {
            return 1;
        }
        return 1 + countNodes(root.leftChild) + countNodes(root.rightChild);
    }

    public void setNullTree() {
        treeRoot = null;
    }

    // Method to compare two BSTs
    public boolean areIdentical(BinarySearchTree bst) {
        return areIdentical(this.treeRoot, bst.treeRoot);
    }

    private boolean areIdentical(BinaryTreeNode node1, BinaryTreeNode node2) {
        // 1. both empty -> true
        if (node1 == null && node2 == null) {
            return true;
        }

        // 2. both non-empty -> compare them
        if (node1 != null && node2 != null) {
            return (node1.key == node2.key
                    && areIdentical(node1.leftChild, node2.leftChild)
                    && areIdentical(node1.rightChild, node2.rightChild));
        }

        // 3. one empty, one not -> false
        return false;
    }

    public void copy(BinarySearchTree bst) {
        this.treeRoot = copy(bst.treeRoot);
    }

    private BinaryTreeNode copy(BinaryTreeNode node) {
        if (node == null) {
            return null;
        }
        BinaryTreeNode newNode = new BinaryTreeNode(node.key);
        newNode.leftChild = copy(node.leftChild);
        newNode.rightChild = copy(node.rightChild);
        return newNode;
    }
}
