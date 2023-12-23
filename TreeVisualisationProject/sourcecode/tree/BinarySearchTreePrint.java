//package tree;
//
//import java.io.PrintStream;
//
//public class BinarySearchTreePrint {
//    public static String traversePreOrder(BinarySearchTree tree) {
//
//        if (tree == null) {
//            return "";
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(tree.treeRoot.getKey());
//
//        String pointerRight = "└──";
//        String pointerLeft = (tree.treeRoot.getRightChild() != null) ? "├──" : "└──";
//
//        traverseTree(sb, "", pointerLeft, tree.treeRoot.getLeftChild(), tree.treeRoot.getRightChild() != null);
//        traverseTree(sb, "", pointerRight, tree.getRightChild(), false);
//
//        return sb.toString();
//    }
//
//    public static void traverseTree(StringBuilder sb, String padding, String pointer, BinarySearchTree.BinaryTreeNode tree,
//                                     boolean hasRightSibling) {
//        if (tree != null) {
//            sb.append("\n");
//            sb.append(padding);
//            sb.append(pointer);
//            sb.append(tree.treeRoot.getKey());
//
//            StringBuilder paddingBuilder = new StringBuilder(padding);
//            if (hasRightSibling) {
//                paddingBuilder.append("│  ");
//            } else {
//                paddingBuilder.append("   ");
//            }
//
//            String paddingForBoth = paddingBuilder.toString();
//            String pointerRight = "└──";
//            String pointerLeft = (tree.treeRoot.getRightChild() != null) ? "├──" : "└──";
//
//            traverseTree(sb, paddingForBoth, pointerLeft, tree.treeRoot.getLeftChild(), tree.treeRoot.getRightChild() != null);
//            traverseTree(sb, paddingForBoth, pointerRight, tree.t.getRightChild(), false);
//        }
//    }
//
//    public static void print(BinarySearchTree tree) {
//        System.out.print(traversePreOrder(tree));
//    }
//}
