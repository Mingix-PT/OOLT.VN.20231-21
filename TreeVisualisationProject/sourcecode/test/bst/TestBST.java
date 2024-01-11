package test.bst;

import tree.type.BinarySearchTree;

public class TestBST {
    public static void main (String[] args) {
        BinarySearchTree tree = new BinarySearchTree(0);
        tree.createTree(3);
        tree.print();
//        tree.bfsTraverse();
    }
}
