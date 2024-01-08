package test.cbbst;

import tree.type.CompleteBalanceBinarySearchTree;

public class TestCBBST {
    public static void main (String[] args) {
        CompleteBalanceBinarySearchTree tree = new CompleteBalanceBinarySearchTree(4);
        tree.insert(2);
        tree.insert(6);
        tree.insert(3);
        tree.print();
        System.out.println(tree.isCompleteBalance());
        tree = tree.completeBalanceTheTree();
        System.out.println(tree.isCompleteBalance());
        tree.print();
    }
}
