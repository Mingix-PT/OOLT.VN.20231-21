package tree.node;

public class BinaryTreeNode extends TreeNode {
    public BinaryTreeNode leftChild;
    public BinaryTreeNode rightChild;

    public BinaryTreeNode(int key) {
        super(key);
        this.leftChild = null;
        this.rightChild = null;
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
