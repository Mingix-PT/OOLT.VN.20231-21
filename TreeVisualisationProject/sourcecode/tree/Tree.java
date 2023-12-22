package tree;

import java.util.List;

public abstract class Tree {
    protected int key;

    public abstract Tree createTree();

    public Tree(int key) {
        this.key = key;
    }
    public int getKey () {
        return key;
    }
    public abstract Tree search(int key);
    public abstract boolean insert(int parentKey, int key);
    public abstract boolean insert(int key);

    public abstract boolean delete(int key);
    public abstract boolean update(int currentKey, int newKey);
}
