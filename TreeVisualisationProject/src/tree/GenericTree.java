package tree;

import java.util.List;

public class GenericTree extends Tree {
    List<GenericTree> children;

    @Override
    protected Tree createTree() {
        return null;
    }
}
