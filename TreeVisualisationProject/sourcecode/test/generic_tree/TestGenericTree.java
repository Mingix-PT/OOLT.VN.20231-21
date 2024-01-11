package test.generic_tree;

import tree.type.GenericTree;

public class TestGenericTree {
    public static void main (String[] args) {
        GenericTree tree = new GenericTree(5);
        tree.createRandomTree(4);
        tree.print();
        System.out.println(tree.getTreeRoot().key);
        System.out.println(tree.width());
        System.out.println(tree.countNodes());
    }
}
