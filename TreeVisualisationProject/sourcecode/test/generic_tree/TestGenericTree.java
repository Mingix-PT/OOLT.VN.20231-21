package test.generic_tree;

import tree.GenericTree;

public class TestGenericTree {
    public static void main (String[] args) {
        GenericTree tree = new GenericTree(10);
        tree.insert(10,1);
        tree.insert(10,2);
        tree.insert(10,3);
        tree.insert(1,4);
        tree.insert(2,5);
        tree.insert(2,6);
        tree.insert(5,8);
        tree.printTree();
        System.out.println("Tree height: " + tree.height());
        tree.delete(10);
        tree.printTree();
        tree.bfsTraverse();
        tree.dfsTraverse();
    }
}
