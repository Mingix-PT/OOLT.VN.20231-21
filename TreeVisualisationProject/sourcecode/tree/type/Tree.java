package tree.type;

import tree.node.TreeNode;

public abstract class Tree {

    public abstract void createTree(int height);

    public abstract int height();

    public abstract boolean search(int key);

    public abstract int depth(int key);

    public abstract boolean insert(int parentKey, int key);

    public abstract boolean insert(int key);

    public abstract boolean delete(int key);

    public abstract void dfsTraverse();

    public abstract void bfsTraverse();

    public abstract boolean update(int currentKey, int newKey);
    public abstract TreeNode getTreeRoot();

    public abstract boolean areIdentical(Tree tree);
    public abstract void copy(Tree tree);
    public abstract void print();
}
