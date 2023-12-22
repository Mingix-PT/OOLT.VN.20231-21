package tree;

import java.util.*;

public class GenericTree extends Tree {
    private List<GenericTree> children = new ArrayList<>();
    private List<GenericTree> dfsResult = new ArrayList<>();

    public List<GenericTree> getChildren () {
        return children;
    }

    @Override
    public Tree createTree() {
        return null;
    }

    public GenericTree(int key) {
        super(key);
        dfsResult.add(this);
    }

    @Override
    public Tree search(int key) {
        return null;
    }

    @Override
    public boolean insert(int parentKey, int key) {
        return false;
    }



    @Override
    public boolean delete(int key) {
        return false;
    }

    @Override
    public boolean update(int currentKey, int newKey) {
        return false;
    }

    public List<GenericTree> dfsTraverse() {
        return dfsResult;
    }

    public List<GenericTree> bfsTraverse() {
        List<GenericTree> bfsResult = new ArrayList<>();
        Queue<Tree> queueTree = new ArrayDeque<>();
        queueTree.add(this);
        while (!queueTree.isEmpty()) {
            GenericTree topTree = (GenericTree) queueTree.poll();
            bfsResult.add(topTree);
            queueTree.addAll(topTree.getChildren());
        }
        return bfsResult;
    }
    @Override
    public boolean insert(int key) {
        // Not available for generic tree
        return false;
    }
}
