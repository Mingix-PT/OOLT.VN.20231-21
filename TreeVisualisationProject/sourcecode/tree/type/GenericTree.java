package tree.type;

import tree.node.GenericTreeNode;
import tree.node.TreeNode;

import java.util.*;

public class GenericTree extends Tree {
    private GenericTreeNode treeRoot;

    public GenericTree(int key) {
        treeRoot = new GenericTreeNode(key);
    }

    public GenericTree() {
        treeRoot = null;
    }

    @Override
    public void createTree(int height) {
        createRandomTree(height);
    }

    @Override
    public GenericTreeNode getTreeRoot() {
        return treeRoot;
    }

    @Override
    public int height() {
        return height(treeRoot);
    }

    @Override
    public int depth(int key) {
        GenericTreeNode nodeFound = search(treeRoot, key);
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

    public boolean search(int key) {
        return search(treeRoot, key) != null;
    }
    public GenericTreeNode search(GenericTreeNode root, int key) {
        // Search using BFS traverse
        Queue<GenericTreeNode> queueTree = new ArrayDeque<>();
        queueTree.add(root);
        while (!queueTree.isEmpty()) {
            GenericTreeNode topNode = queueTree.poll();
            if (topNode.key == key) {
                return topNode;
            }
            queueTree.addAll(topNode.getChildren());
        }
        return null;
    }

    public GenericTreeNode searchParent(int key) {
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
        if (search(treeRoot, key) != null) {
            System.out.println("Insert failed: Key " + key + " already exists in the tree.");
            return false;
        }
        GenericTreeNode parentNode = search(treeRoot, parentKey);
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
        GenericTreeNode nodeFound = search(treeRoot, key);
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
        GenericTreeNode nodeFound = search(treeRoot, currentKey);
        if (nodeFound == null) {
            return false;
        }
        nodeFound.key = newKey;
        return true;
    }

    public int countNodes() {
        return countNodes(treeRoot);
    }

    public int countNodes(GenericTreeNode root) {
        if (root == null) {
            return 0;
        }
        int count = 1;
        for (GenericTreeNode eachChild : root.children) {
            count += countNodes(eachChild);
        }
        return count;
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

    public void createRandomTree(int height) {
        treeRoot = new GenericTreeNode(new Random().nextInt(100));
        print();
        createRandomTree(treeRoot, height);
    }

    private void createRandomTree(GenericTreeNode root, int height) {
        if (height == 0) {
            return;
        }
        Random random = new Random();
        int numberOfChildren = random.nextInt(2) + 1;
        for (int i = 0; i < numberOfChildren; i++) {
            int randomKey = 1;
            Random randomKeyGenerator = new Random();
            while (search(randomKey)) {
                randomKey = randomKeyGenerator.nextInt(100);
            }
            System.out.println("Inserting key " + randomKey + " under parent key " + root.key);
            root.children.add(new GenericTreeNode(randomKey));
            createRandomTree(root.children.get(i), height - 1);
        }
    }

    @Override
    public boolean insert(int key) {
        // Not available for generic tree
        return false;
    }

    // Print tree - idea by Copilot Chat
    public void print() {
        if (treeRoot != null) {
            printNode(treeRoot, "");
        }
    }

    private void printNode(GenericTreeNode node, String prefix) {
        if (node != null) {
            System.out.println(prefix + (prefix.isEmpty() ? "" : "└── ") + node.key);
            for (int i = 0; i < node.children.size() - 1; i++) {
                printNode(node.children.get(i), prefix + "    ");
            }
            if (!node.children.isEmpty()) {
                printNode(node.children.get(node.children.size() - 1), prefix + "    ");
            }
        }
    }

    public int width() {
        return width(treeRoot);
    }

    public int width(GenericTreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.isLeaf()) {
            return 1;
        }
        int maxWidth = 0;
        for (GenericTreeNode child : root.children) {
            maxWidth += width(child);
        }
        return maxWidth;
    }

    public boolean areIdentical(Tree otherTree) {
        if (!(otherTree instanceof GenericTree)) {
            return false;
        }
        return areIdentical(treeRoot,(GenericTreeNode) otherTree.getTreeRoot());
    }

    private boolean areIdentical(GenericTreeNode root1,  GenericTreeNode root2) {
        if (root1 == null && root2 == null) {
            return true;
        }
        if (root1 != null && root2 != null) {
            if (root1.key == root2.key) {
                if (root1.children.size() == root2.children.size()) {
                    for (int i = 0; i < root1.children.size(); i++) {
                        if (!areIdentical(root1.children.get(i), root2.children.get(i))) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void copy(Tree otherTree) {
        if (!(otherTree instanceof GenericTree)) {
            return;
        }
        treeRoot = copy((GenericTreeNode) otherTree.getTreeRoot());
    }

    private GenericTreeNode copy(GenericTreeNode root) {
        if (root == null) {
            return null;
        }
        GenericTreeNode newRoot = new GenericTreeNode(root.key);
        for (GenericTreeNode eachChild : root.children) {
            newRoot.children.add(copy(eachChild));
        }
        return newRoot;
    }
}