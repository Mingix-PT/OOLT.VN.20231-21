package ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import tree.BinarySearchTree;
import tree.BinaryTreeNode;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
            String input = JOptionPane.showInputDialog("Enter the height of the tree: ");
            int height = Integer.parseInt(input);
            bst = new BinarySearchTree();
            while (bst.height() < height) {
                bst.insert((int) (Math.random() * 100));
            }
            bst.print();
            clearPane();
            drawTree(bst.getTreeRoot(), treePane.getWidth() / 2,treePane.getHeight()*0.1, bst.height()*40);
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void deleteNode(ActionEvent event) {

    }

    @FXML
    void insertNode(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Insert");
            dialog.setHeaderText("Insert");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            if (insertUI(bst.getTreeRoot(), key)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inserted", ButtonType.OK);
                alert.showAndWait();
                dialog.setContentText("Inserted");
                clearPane();
                drawTree(bst.getTreeRoot(), treePane.getWidth() / 2,treePane.getHeight()*0.1, bst.height()*40);
                searchUI(bst.getTreeRoot(), key);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed", ButtonType.OK);
                alert.showAndWait();
                dialog.setContentText("Failed");
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input", ButtonType.OK);
            alert.showAndWait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getSpeed() {

    }

    @FXML
    void searchNode(ActionEvent event) {
        try{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search");
            dialog.setHeaderText("Search");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            resetHighlight();
            if (searchUI(bst.getTreeRoot(), key)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Found", ButtonType.OK);
                alert.showAndWait();
                dialog.setContentText("Found");
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not found", ButtonType.OK);
                alert.showAndWait();
                dialog.setContentText("");
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input", ButtonType.OK);
            alert.showAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

    private void drawTree(BinaryTreeNode node, double x, double y, double horizontalSpacing) {
        if (node == null) return;

        try {
            double nodeHeight = 30; // Replace with the actual height of the nodes
            double yAdjustment = 30; // Adjust vertical spacing between nodes
            // Load the TreeNode.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/TreeNode.fxml"));
            TreeNodeController treeNodeController = new TreeNodeController();
            loader.setController(treeNodeController);
            Pane nodePane = loader.load();

            // Get the controller and set the node key
            TreeNodeController controller = loader.getController();
            controller.setKey(node.key);

            nodePane.setLayoutX(x);
            nodePane.setLayoutY(y);

            // Add the node to the pane
            treePane.getChildren().add(nodePane);

            // Calculate position and draw child nodes
            final double childY = y + 80; // Vertical distance between nodes

            // Draw the line to the left child and the left child itself
            if (node.leftChild != null) {
                double childX = x - horizontalSpacing;
                Line line = new Line(x + nodeHeight/2 , y - yAdjustment + 2 * nodeHeight, childX + nodeHeight/2, childY  + nodeHeight/2);
                treePane.getChildren().add(line);
                drawTree(node.leftChild, childX, childY, horizontalSpacing/2);
            }

            // Draw the line to the right child and the right child itself
            if (node.rightChild != null) {
                double childX = x + horizontalSpacing;
                Line line = new Line(x + nodeHeight/2, y - yAdjustment + 2*nodeHeight, childX + nodeHeight/2, childY  + nodeHeight/2);
                treePane.getChildren().add(line);
                drawTree(node.rightChild, childX, childY, horizontalSpacing/2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void clearPane() {
        treePane.getChildren().clear();
    }

    public boolean searchUI(BinaryTreeNode root,int key) throws InterruptedException {
        if (root == null) {
            return false;
        }
        else {
            if (root.key == key) {
                highlightNodeFound(key);
                return true;
            }
            highlightNode(root.key);
            if (root.key > key) {
                if (root.leftChild == null) {
                    return false;
                }
                highlightNode(root.leftChild.key);
                return searchUI(root.leftChild, key);
            }
            if (root.rightChild == null) {
                return false;
            }
            highlightNode(root.rightChild.key);
            return searchUI(root.rightChild, key);
        }
    }

    public boolean insertUI(BinaryTreeNode root, int key) {
        if (bst.getTreeRoot() == null) {
            bst.setTreeRoot(key);
            return true;
        }
        if (root.key == key) {
            return false;
        }
        if (root.key > key) {
            if (root.leftChild == null) {
                root.leftChild = new BinaryTreeNode(key);
                return true;
            }
            return insertUI(root.leftChild, key);
        }
        if (root.rightChild == null) {
            root.rightChild = new BinaryTreeNode(key);
            return true;
        }
        return insertUI(root.rightChild, key);
    }

    void highlightNode(int key) {
        ObservableList<Node> nodes = treePane.getChildren();
        for (Node node : nodes) {
            if (!(node instanceof AnchorPane anchorPane))
                continue;
            ObservableList<Node> children = anchorPane.getChildren();
            for (Node child : children) {
                if (!(child instanceof HBox hBox))
                    continue;
                ObservableList<Node> hBoxChildren = hBox.getChildren();
                for (Node hBoxChild : hBoxChildren) {
                    if (!(hBoxChild instanceof Label label))
                        continue;
                    if (!label.getText().equals(String.valueOf(key)))
                        continue;
                    hBox.setStyle("-fx-border-color: #ff0000; -fx-border-radius: 30; " +
                            "-fx-background-color: #ff0000; -fx-background-radius: 30");
                    label.setStyle("-fx-text-fill: #ffffff");
                }
            }
        }
    }

    void highlightNodeFound(int key) {
        ObservableList<Node> nodes = treePane.getChildren();
        for (Node node : nodes) {
            if (!(node instanceof AnchorPane anchorPane))
                continue;
            ObservableList<Node> children = anchorPane.getChildren();
            for (Node child : children) {
                if (!(child instanceof HBox hBox))
                    continue;
                ObservableList<Node> hBoxChildren = hBox.getChildren();
                for (Node hBoxChild : hBoxChildren) {
                    if (!(hBoxChild instanceof Label label))
                        continue;
                    if (!label.getText().equals(String.valueOf(key)))
                        continue;
                    hBox.setStyle("-fx-border-color: #000000; -fx-border-radius: 30; " +
                            "-fx-background-color: #00ff00; -fx-background-radius: 30");
                    label.setStyle("-fx-text-fill: #000000");
                }
            }
        }
    }

    void resetHighlight() {
        ObservableList<Node> nodes = treePane.getChildren();
        for (Node node : nodes) {
            if (!(node instanceof AnchorPane anchorPane))
                continue;
            ObservableList<Node> children = anchorPane.getChildren();
            for (Node child : children) {
                if (!(child instanceof HBox hBox))
                    continue;
                ObservableList<Node> hBoxChildren = hBox.getChildren();
                for (Node hBoxChild : hBoxChildren) {
                    if (!(hBoxChild instanceof Label label))
                        continue;
                    hBox.setStyle("-fx-border-color: #000000; -fx-border-radius: 30; " +
                            "-fx-background-color: #ffffff; -fx-background-radius: 30");
                    label.setStyle("-fx-text-fill: #000000");
                }
            }
        }
    }
}
