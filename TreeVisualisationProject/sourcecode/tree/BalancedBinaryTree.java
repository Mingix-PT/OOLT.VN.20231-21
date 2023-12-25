package tree;

public class BalancedBinaryTree extends BinarySearchTree {
    protected class BalancedBinaryTreeNode extends BinaryTreeNode {
        int balanceFactor;
        public BalancedBinaryTreeNode(int key) {
            super(key);
        }
    }

    public BalancedBinaryTree(int key) {
        super(key);
    }
}
