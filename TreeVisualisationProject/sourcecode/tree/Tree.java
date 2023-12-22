package tree;

import java.util.List;

public abstract class Tree {
    protected int key;

    protected abstract Tree createTree();

    public Tree(int key) {
        this.key = key;
    }
    protected abstract Tree search(int key);
    protected abstract boolean insert(int parentKey, int key);
    protected abstract boolean insert(int key);

    protected abstract boolean delete(int key);
    protected abstract boolean update(int currentKey, int newKey);
}
