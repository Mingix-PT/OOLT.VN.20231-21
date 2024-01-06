package ui.test;

import java.util.*;

public class GenericTree extends Tree {
    public class GenericTreeNode {
        public int key;
        public List<GenericTreeNode> children = new ArrayList<>();

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

    public GenericTreeNode getRoot() {
        return treeRoot;
    }

    private GenericTreeNode treeRoot;

    public GenericTree(int key) {
        treeRoot = new GenericTreeNode(key);
    }

    @Override
    public Tree createTree() {
        return null;
    }

    @Override
    public int height() {
        return height(treeRoot);
    }

    @Override
    public int depth(int key) {
        GenericTreeNode nodeFound = search(key);
        if (nodeFound == null) {
            return -1;
        }
        int distance = -1;
        while (nodeFound != null) {
            nodeFound = searchParent(nodeFound.key);
            distance++;
        }
        return distance;
    }

    private int height(GenericTreeNode root) {
        if (root.isLeaf()) {
            return 0;
        }
        List<Integer> childrenHeights = new ArrayList<>();
        for (GenericTreeNode child : root.children) {
            childrenHeights.add(height(child));
        }
        return 1 + Collections.max(childrenHeights);
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
        if (search(key) != null) {
            System.out.println("Insert failed: Key " + key + " already exists in the tree.");
            return false;
        }
        GenericTreeNode parentNode = search(parentKey);
        if (parentNode == null) {
            System.out.println("Insert failed: Parent key " + parentKey + " does not exist in the tree.");
            return false;
        }
        parentNode.children.add(new GenericTreeNode(key));
        System.out.println("Successfully inserted key " + key + " under parent key " + parentKey);
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
            if (treeRoot.children.isEmpty()) {
                treeRoot = null;
            } else {
                GenericTreeNode firstChild = treeRoot.children.get(0);
                treeRoot.children.remove(firstChild);
                if (!treeRoot.children.isEmpty()) {
                    firstChild.children.addAll(treeRoot.children);
                }
                treeRoot = firstChild;
            }
            return true;
        }
        parent.children.addAll(nodeFound.children);
        parent.children.remove(nodeFound);
        return true;
    }

    @Override
    public boolean update(int currentKey, int newKey) {
        GenericTreeNode nodeFound = search(currentKey);
        if (nodeFound == null) {
            return false;
        }
        nodeFound.key = newKey;
        return true;
    }

    @Override
    public void dfsTraverse() {
        dfsTraverse(treeRoot);
        System.out.println();
    }

    private void dfsTraverse(GenericTreeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.key + " ");
        for (GenericTreeNode eachChild : node.children) {
            dfsTraverse(eachChild);
        }
    }

    @Override
    public void bfsTraverse() {
        List<GenericTreeNode> bfsResult = bfsTraverse(treeRoot);
        if (bfsResult != null) {
            for (GenericTreeNode node : bfsResult) {
                System.out.print(node.key + " ");
            }
            System.out.println();
        } else {
            System.out.println("Tree is empty!");
        }
    }

    private List<GenericTreeNode> bfsTraverse(GenericTreeNode root) {
        if (treeRoot == null) {
            return null;
        }
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

    // Print tree - idea by Copilot Chat
    public void printTree() {
        if (treeRoot != null) {
            System.out.println(treeRoot.key);
            for (int i = 0; i < treeRoot.children.size() - 1; i++) {
                printNode(treeRoot.children.get(i), "", false);
            }
            if (!treeRoot.children.isEmpty()) {
                printNode(treeRoot.children.get(treeRoot.children.size() - 1), "", true);
            }
        }
    }

    private void printNode(GenericTreeNode node, String prefix, boolean isTail) {
        if (node != null) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + node.key);
            for (int i = 0; i < node.children.size() - 1; i++) {
                printNode(node.children.get(i), prefix + (isTail ? "    " : "│   "), false);
            }
            if (!node.children.isEmpty()) {
                printNode(node.children.get(node.children.size() - 1), prefix + (isTail ? "    " : "│   "), true);
            }
        }
    }
}