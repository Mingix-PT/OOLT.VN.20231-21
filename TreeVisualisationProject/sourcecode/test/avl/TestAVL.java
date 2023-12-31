package test.avl;

import tree.AVLTree;


public class TestAVL {
    public static void main (String[] args) {
        AVLTree tree = new AVLTree(5);
        tree.insert(2);
        tree.insert(6);
        tree.insert(1);
        tree.insert(3);
        tree.insert(0);
        tree.print();
        tree.delete(1);
        tree.delete(0);
        tree.insert(7);
        tree.print();
    }
}
