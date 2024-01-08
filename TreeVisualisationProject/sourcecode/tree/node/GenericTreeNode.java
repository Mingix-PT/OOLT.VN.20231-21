package tree.node;

import java.util.ArrayList;
import java.util.List;

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
