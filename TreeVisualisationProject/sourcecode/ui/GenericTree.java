package ui;

public class GenericTree {
    Node root;

    public GenericTree() {
        root = null;
    }
    // Trong lớp GenericTree
public void reset() {
    root = null;
}
    public void insert(int parentValue, int childValue) {
        if (root == null) {
            root = new Node(parentValue);
        }

        Node parentNode = findNode(root, parentValue);
        if (parentNode != null) {
            parentNode.addChild(new Node(childValue));
        }
    }

    private Node findNode(Node current, int value) {
        if (current == null) return null;
        if (current.value == value) return current;
        for (Node child : current.children) {
            Node result = findNode(child, value);
            if (result != null) return result;
        }
        return null;
    }
}
