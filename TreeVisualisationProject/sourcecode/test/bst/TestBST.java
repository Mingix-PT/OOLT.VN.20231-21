package test.bst;

import tree.type.BinarySearchTree;

public class TestBST {
    public static void main (String[] args) {
        BinarySearchTree tree = new BinarySearchTree(5);
        System.out.println(tree.height());
        tree.insert(2);
        System.out.println(tree.height());
        tree.insert(4);
        tree.insert(8);
        tree.insert(6);
        tree.insert(10);
        tree.insert(1);
        tree.insert(0);
        tree.insert(-1);
        tree.insert(7);
        tree.bfsTraverse();
        tree.print();
        System.out.println();
//        List<BinarySearchTree> bfsList = tree.bfsTraverse();
//        for (BinarySearchTree treeTemp : bfsList) {
//            System.out.print(treeTemp.getKey()+" ");
//        }
//        System.out.println();
//        for (int i = 0;i<=10;i++) {
//            if (tree.search(i)==null) {
//                System.out.println("NULL");
//            }
//            else {
//                System.out.println(i);
//            }
//        }
//        System.out.println("Parent of 0 is: " + tree.searchParent(0).getKey());
//        tree.update(5,5);
//        tree.delete(5);
//        tree.print();
        System.out.println(tree.depth(7));
        BinarySearchTree newTree = new BinarySearchTree();
        System.out.println(newTree.areIdentical(tree));
        newTree.copy(tree);
        newTree.print();
        tree.insert(100);
        System.out.println(newTree.areIdentical(tree));
//        tree.bfsTraverse();
    }
}
