package test.traverse;

import tree.BinarySearchTree;

import java.util.List;

public class TestTraverseBST {
    public static void main (String[] args) {
        BinarySearchTree tree = new BinarySearchTree(5);
        for (int i = 0; i <= 10; i = i + 2) {
            if (!tree.insert(i)) {
                System.out.println(i);
            }
        }

        List<BinarySearchTree> bfsList = tree.bfsTraverse();
        for (BinarySearchTree treeTemp : bfsList) {
            System.out.print(treeTemp.getKey()+" ");
        }
        System.out.println("");
        BinarySearchTree.dfsTraverse(tree);
    }
}
