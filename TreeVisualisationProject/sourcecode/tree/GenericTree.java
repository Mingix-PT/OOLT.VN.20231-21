package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class GenericTree extends Tree {
    private List<GenericTree> children = new ArrayList<>();

    public List<GenericTree> getChildren () {
        return children;
    }

    @Override
    protected Tree createTree() {
        return null;
    }

    public GenericTree(int key) {
        super(key);
    }

    @Override
    protected Tree search(int key) {
        return null;
    }

    @Override
    protected boolean insert(int parentKey, int key) {
        return false;
    }



    @Override
    protected boolean delete(int key) {
        return false;
    }

    @Override
    protected boolean update(int currentKey, int newKey) {
        return false;
    }

    protected List<GenericTree> dfsTraverse() {

        return null;
    }

    protected List<GenericTree> bfsTraverse() {
        List<GenericTree> bfsResult = new ArrayList<>();
        Queue<Tree> queueTree = new PriorityQueue<>();
        queueTree.add(this);
        while (!queueTree.isEmpty()) {
            GenericTree topTree = (GenericTree) queueTree.poll();
            bfsResult.add(topTree);
            queueTree.addAll(topTree.getChildren());
        }
        return bfsResult;
    }
    @Override
    protected boolean insert(int key) {
        // Not available for generic tree
        return false;
    }
}
