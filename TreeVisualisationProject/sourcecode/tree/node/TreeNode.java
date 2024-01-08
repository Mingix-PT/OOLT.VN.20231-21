package tree.node;

public abstract class TreeNode {
    public int key;

    public TreeNode(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public abstract boolean isLeaf();
}
