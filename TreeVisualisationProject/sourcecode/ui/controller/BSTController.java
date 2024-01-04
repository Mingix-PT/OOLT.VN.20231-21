package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import tree.BinarySearchTree;
import tree.BinaryTreeNode;

import javax.swing.*;
import java.io.IOException;

public class BSTController {
    private BinarySearchTree bst = new BinarySearchTree();

    @FXML
    private Pane treePane;
    @FXML
    private Button backButton;

    @FXML
    private Button createTreeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button insertButton;

    @FXML
    private Button searchButton;

    @FXML
    void backToMainMenu(ActionEvent event) {

    }

    @FXML
    void createRandomTree(ActionEvent event) {
        try
        {
            clearPane();
            String input = JOptionPane.showInputDialog("Enter the height of the tree: ");
            int height = Integer.parseInt(input);
            bst = new BinarySearchTree();
            while (bst.height() < height) {
                bst.insert((int) (Math.random() * 100));
            }
            bst.print();
            drawTree(treePane, bst.getTreeRoot());
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    void deleteNode(ActionEvent event) {

    }

    @FXML
    void insertNode(ActionEvent event) {

    }

    @FXML
    void searchNode(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        final String NODE_FXML_FILE_PATH = "/ui/view/TreeNode.fxml";
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(NODE_FXML_FILE_PATH));
            TreeNodeController treeNodeController = new TreeNodeController();
            fxmlLoader.setController(treeNodeController);
            AnchorPane anchorPane = fxmlLoader.load();
            treePane.getChildren().add(anchorPane);
            clearPane();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawTree(Pane pane, BinaryTreeNode root) {
        double paneWidth = pane.getWidth();
        double paneHeight = pane.getHeight();
        int treeDepth = getTreeDepth(root);

        double horizontalSpacing = paneWidth / (Math.pow(2, treeDepth + 1)); // Adjust as needed
        double verticalSpacing = paneHeight / (treeDepth + 1); // Adjust as needed
        double yOffset = verticalSpacing / 2; // Adjust as needed

        drawLines(pane, root, paneWidth / 2, yOffset, horizontalSpacing, verticalSpacing);
        drawNodes(pane, root, paneWidth / 2, yOffset, horizontalSpacing, verticalSpacing);
    }

    private void drawLines(Pane pane, BinaryTreeNode root, double x, double y, double horizontalSpacing, double verticalSpacing) {
        if (root == null) {
            return;
        }

        double nodeHeight = 30; // Replace with the actual height of the nodes

        // Draw the line to the left child
        if (root.leftChild != null) {
            Line line = new Line(x, y + nodeHeight / 2, x - horizontalSpacing, y + verticalSpacing - nodeHeight / 2);
            pane.getChildren().add(line);
            drawLines(pane, root.leftChild, x - horizontalSpacing, y + verticalSpacing, horizontalSpacing / 2, verticalSpacing);
        }

        // Draw the line to the right child
        if (root.rightChild != null) {
            Line line = new Line(x, y + nodeHeight / 2, x + horizontalSpacing, y + verticalSpacing - nodeHeight / 2);
            pane.getChildren().add(line);
            drawLines(pane, root.rightChild, x + horizontalSpacing, y + verticalSpacing, horizontalSpacing / 2, verticalSpacing);
        }
    }

    private int getTreeDepth(BinaryTreeNode node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + Math.max(getTreeDepth(node.leftChild), getTreeDepth(node.rightChild));
        }
    }

    private void drawNodes(Pane pane, BinaryTreeNode root, double x, double y, double horizontalSpacing, double verticalSpacing) {
        if (root == null) {
            return;
        }

        try {
            // Load the TreeNode.fxml
            final String NODE_FXML_FILE_PATH = "/ui/view/TreeNode.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(NODE_FXML_FILE_PATH));
            TreeNodeController treeNodeController = new TreeNodeController();
            fxmlLoader.setController(treeNodeController);
            AnchorPane nodePane = fxmlLoader.load();

            // Set the node key
            treeNodeController.setKey(root.key);

            // Position the node
            nodePane.setLayoutX(x);
            nodePane.setLayoutY(y);

            // Add the node to the pane
            pane.getChildren().add(nodePane);

            // Draw the left child
            drawNodes(pane, root.leftChild, x - horizontalSpacing, y + verticalSpacing, horizontalSpacing / 2, verticalSpacing);

            // Draw the right child
            drawNodes(pane, root.rightChild, x + horizontalSpacing, y + verticalSpacing, horizontalSpacing / 2, verticalSpacing);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    void clearPane() {
        treePane.getChildren().clear();
    }
}
