package tree;

import java.util.List;

public abstract class Tree {

    public abstract Tree createTree();
    public abstract int height();
    public abstract int depth(int key);

    public abstract boolean insert(int parentKey, int key);
    public abstract boolean insert(int key);

    public abstract boolean delete(int key);
    public abstract void dfsTraverse();
    public abstract void bfsTraverse();
    public abstract boolean update(int currentKey, int newKey);
}
