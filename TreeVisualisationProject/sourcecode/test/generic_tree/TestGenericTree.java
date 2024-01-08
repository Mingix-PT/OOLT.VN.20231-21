package test.generic_tree;

import tree.type.GenericTree;

public class TestGenericTree {
    public static void main (String[] args) {
        GenericTree tree = new GenericTree();
        tree.createRandomTree(4);
        tree.printTree();
        System.out.println(tree.width());
        System.out.println(tree.countNodes());
    }
}
