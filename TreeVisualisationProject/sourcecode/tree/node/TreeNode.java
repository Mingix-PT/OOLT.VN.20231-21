package tree.node;

public abstract class TreeNode {
    public int key;

    protected TreeNode(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public abstract boolean isLeaf();

}
