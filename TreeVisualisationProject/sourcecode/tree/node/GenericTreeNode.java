package tree.node;

import java.util.ArrayList;
import java.util.List;

public class GenericTreeNode extends TreeNode {
    public List<GenericTreeNode> children = new ArrayList<>();

    public GenericTreeNode(int key) {
        super(key);
    }

    public List<GenericTreeNode> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }
}
