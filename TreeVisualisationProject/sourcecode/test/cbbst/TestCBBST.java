package test.cbbst;

import tree.CompleteBalanceBinarySearchTree;

public class TestCBBST {
    public static void main (String[] args) {
        CompleteBalanceBinarySearchTree tree = new CompleteBalanceBinarySearchTree(5);
        tree.insert(3);
        tree.insert(1);
        tree.insert(2);
        tree.insert(8);
        tree.insert(7);
        tree.print();
        tree = tree.completeBalanceTheTree();
        tree.print();
    }
}
