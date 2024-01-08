package tree.node;

public class BinaryTreeNode {
    public int key;
    public BinaryTreeNode leftChild;
    public BinaryTreeNode rightChild;

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

    public void setNode(BinaryTreeNode node) {
        this.key = node.key;
        this.leftChild = node.leftChild;
        this.rightChild = node.rightChild;
    }
}
