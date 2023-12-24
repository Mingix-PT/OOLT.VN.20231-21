package tree;

import java.util.*;

public class GenericTree extends Tree {
    protected class GenericTreeNode {
        private int key;
        private List<GenericTreeNode> children = new ArrayList<>();

        public GenericTreeNode(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }

        public List<GenericTreeNode> getChildren() {
            return children;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }
    }
    
    private GenericTreeNode treeRoot;
    public GenericTree(int key) {
        treeRoot = new GenericTreeNode(key);
    }
    private List<GenericTree> dfsResult = new ArrayList<>();

    @Override
    public Tree createTree() {
        return null;
    }

    private GenericTreeNode search(int key) {
        // Search using BFS traverse
        Queue<GenericTreeNode> queueTree = new ArrayDeque<>();
        queueTree.add(treeRoot);
        while (!queueTree.isEmpty()) {
            GenericTreeNode topNode = queueTree.poll();
            if (topNode.key == key) {
                return topNode;
            }
            queueTree.addAll(topNode.getChildren());
        }
        return null;
    }

    private GenericTreeNode searchParent(int key) {
        // Search using BFS traverse
        if (treeRoot.key == key) {
            return null;
        }
        Queue<GenericTreeNode> queueTree = new ArrayDeque<>();
        queueTree.add(treeRoot);
        while (!queueTree.isEmpty()) {
            GenericTreeNode topNode = queueTree.poll();
            for (GenericTreeNode eachChild : topNode.children) {
                if (eachChild.key == key) {
                    return topNode;
                }
            }
            queueTree.addAll(topNode.getChildren());
        }
        return null;
    }

    @Override
    public boolean insert(int parentKey, int key) {
        GenericTreeNode parentNode = search(parentKey);
        if (parentNode == null) {
            return false;
        }
        parentNode.children.add(new GenericTreeNode(key));
        return true;
    }

    @Override
    public boolean delete(int key) {
        GenericTreeNode nodeFound = search(key);
        if (nodeFound == null) {
            return false;
        }
        GenericTreeNode parent = searchParent(key);
        if (parent == null) {
            // The root
            treeRoot = null;
            return true;
        }
        parent.children.remove(nodeFound);
        return true;
    }

    @Override
    public boolean update(int currentKey, int newKey) {
        return false;
    }

    @Override
    public void dfsTraverse() {
        dfsTraverse(treeRoot);
    }

    private void dfsTraverse(GenericTreeNode node) {
        if (node==null) {
            return;
        }
        System.out.println(node.key + " ");
        for (GenericTreeNode eachChild : node.children) {
            dfsTraverse(eachChild);
        }
    }

    @Override
    public void bfsTraverse() {
        List<GenericTreeNode> bfsResult = bfsTraverse(treeRoot);
        for (GenericTreeNode node : bfsResult) {
            System.out.print(node.key + " ");
        }
    }

    private List<GenericTreeNode> bfsTraverse(GenericTreeNode root) {
        List<GenericTreeNode> bfsResult = new ArrayList<>();
        Queue<GenericTreeNode> queueTree = new ArrayDeque<>();
        queueTree.add(root);
        while (!queueTree.isEmpty()) {
            GenericTreeNode topNode = queueTree.poll();
            bfsResult.add(topNode);
            queueTree.addAll(topNode.getChildren());
        }
        return bfsResult;
    }
    @Override
    public boolean insert(int key) {
        // Not available for generic tree
        return false;
    }
}
