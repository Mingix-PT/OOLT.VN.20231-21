package test.avl;

import tree.AVLTree;
import tree.BinarySearchTree;

public class TestAVL {
    public static void main (String[] args) {
        AVLTree tree = new AVLTree(5);
        tree.insert(2);
        tree.insert(4);
        tree.insert(8);
        tree.insert(6);
        tree.insert(10);
        tree.insert(1);
        tree.insert(0);
        tree.insert(-1);
        tree.insert(7);
        tree.print();
    }
}
