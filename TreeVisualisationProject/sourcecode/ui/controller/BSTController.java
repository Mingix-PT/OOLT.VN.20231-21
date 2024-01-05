package ui.controller;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
import java.util.ArrayList;
import java.util.List;
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
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete");
            dialog.setHeaderText("Delete");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            if (deleteUI(bst.getTreeRoot(), key)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Deleted", ButtonType.OK);
                alert.showAndWait();
                dialog.setContentText("Deleted");
                clearPane();
                drawTree(bst.getTreeRoot(), treePane.getWidth() / 2,treePane.getHeight()*0.1, bst.height()*40);
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
    void insertNode(ActionEvent event) {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Insert");
            dialog.setHeaderText("Insert");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            if (insertUI(bst.getTreeRoot(), key, 1000)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inserted", ButtonType.OK);
                alert.showAndWait();
                dialog.setContentText("Inserted");
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
            if (searchUI(bst.getTreeRoot(), key, 1000)) {
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
                Line line = new Line(x + nodeHeight/2 , y + nodeHeight/2, childX + nodeHeight/2, childY  + nodeHeight/2);
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

    public boolean searchUI(BinaryTreeNode root,int key, long timeDelay) throws InterruptedException {
        if (root == null) {
            return false;
        }
        else {
            timeDelay += 1000;
            if (root.key == key) {
                delay(timeDelay, () -> highLightNodeGreen(key));
                return true;
            }
            delay(timeDelay, () -> highLightNodeRed(root.key));
            timeDelay += 1000;
            if (root.key > key) {
                if (root.leftChild == null) {
                    return false;
                }
                delay(timeDelay, () -> highLightNodeRed(root.leftChild.key));
                return searchUI(root.leftChild, key, timeDelay);
            }
            if (root.rightChild == null) {
                return false;
            }
            delay(timeDelay, () -> highLightNodeRed(root.rightChild.key));
            return searchUI(root.rightChild, key, timeDelay);
        }
    }

    public boolean insertUI(BinaryTreeNode root, int key, long timeDelay) throws InterruptedException {
        timeDelay += 1000;
        if (bst.getTreeRoot() == null) {
            bst.setTreeRoot(key);
            drawTree(bst.getTreeRoot(), treePane.getWidth() / 2,treePane.getHeight()*0.1, bst.height()*40);
            highLightNodeGreen(key);
            return true;
        }
        if (root.key == key) {
            return false;
        }
        timeDelay += 1000;
        delay(timeDelay, () -> highLightNodeRed(root.key));
        if (root.key > key) {
            if (root.leftChild == null) {
                root.leftChild = new BinaryTreeNode(key);
                timeDelay += 1000;
                delay(timeDelay, () -> drawLeftChild(root.key, key));
                timeDelay += 1000;
                delay(timeDelay, () -> highLightNodeGreen(key));
                return true;
            }
            return insertUI(root.leftChild, key, timeDelay);
        }
        if (root.rightChild == null) {
            root.rightChild = new BinaryTreeNode(key);
            timeDelay += 1000;
            delay(timeDelay, () -> drawRightChild(root.key, key));
            timeDelay += 1000;
            delay(timeDelay, () -> highLightNodeGreen(key));
            return true;
        }
        return insertUI(root.rightChild, key, timeDelay);
    }

    public boolean deleteUI(BinaryTreeNode root, int key) throws InterruptedException {
        if (!searchUI(root, key, 1000)) {
            return false;
        }
        HBox hBox = findNode(key);
        if (hBox == null)
            return false;
//        treePane.getChildren().remove(hBox);
        bst.delete(key);
//        bst.print();
        return true;
    }

    void highLightNodeRed(int key) {
        HBox hBox = findNode(key);
        if (hBox == null)
            return;
        hBox.setStyle("-fx-border-color: #000000; -fx-border-radius: 30; " +
                "-fx-background-color: #ff0000; -fx-background-radius: 30");
        ObservableList<Node> hBoxChildren = hBox.getChildren();
        for (Node hBoxChild : hBoxChildren) {
            if (!(hBoxChild instanceof Label label))
                continue;
            label.setStyle("-fx-text-fill: #ffffff");
        }
    }

    void highLightNodeGreen(int key) {
        HBox hBox = findNode(key);
        if (hBox == null)
            return;
        hBox.setStyle("-fx-border-color: #000000; -fx-border-radius: 30; " +
                "-fx-background-color: #00ff00; -fx-background-radius: 30");
        ObservableList<Node> hBoxChildren = hBox.getChildren();
        for (Node hBoxChild : hBoxChildren) {
            if (!(hBoxChild instanceof Label label))
                continue;
            label.setStyle("-fx-text-fill: #000000");
        }
//        wait(1000);
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

    HBox findNode(int key) {
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
                    if (hBoxChild instanceof Label label && label.getText().equals(String.valueOf(key)))
                        return hBox;
                }
            }
        }
        return null;
    }

    Point2D findCoordinate(int key) {
        List<Double> coordinate = new ArrayList<>();
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
                    if (hBoxChild instanceof Label label && label.getText().equals(String.valueOf(key))) {// Assuming hbox is your HBox, anchorPane is your AnchorPane, and pane is your Pane
                        Point2D hboxInAnchorPane = new Point2D(hBox.getLayoutX(), hBox.getLayoutY());
                        return anchorPane.localToParent(hboxInAnchorPane);
                    }
                }
            }
        }
        return null;
    }

    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException e) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }

    void drawLeftChild(int parent, int key) {
        try {
            double nodeHeight = 30; // Replace with the actual height of the nodes
            double yAdjustment = 30; // Adjust vertical spacing between nodes
            Point2D coordinate = findCoordinate(parent);
            double x = coordinate.getX() - nodeHeight;
            double y = coordinate.getY() + 80;
            // Load the TreeNode.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/TreeNode.fxml"));
            TreeNodeController treeNodeController = new TreeNodeController();
            loader.setController(treeNodeController);
            Pane nodePane = loader.load();
            // Get the controller and set the node key
            TreeNodeController controller = loader.getController();
            controller.setKey(key);

            nodePane.setLayoutX(x);
            nodePane.setLayoutY(y);

            // Add the node to the pane

            // Calculate position and draw child nodes
            final double childY = y - 80; // Vertical distance between nodes
            // Draw the line to the left child and the left child itself
            double childX = x + nodeHeight;
            Line line = new Line(x + nodeHeight/2 , y + nodeHeight/2, childX + nodeHeight/2 , childY  + nodeHeight/2 );
            treePane.getChildren().add(line);
            treePane.getChildren().add(nodePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void drawRightChild(int parent, int key) {
        try {
            Point2D coordinate = findCoordinate(parent);
            double nodeHeight = 30; // Replace with the actual height of the nodes
            double yAdjustment = 30; // Adjust vertical spacing between nodes
            double x = coordinate.getX() + nodeHeight;
            double y = coordinate.getY() + 80;
            // Load the TreeNode.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/TreeNode.fxml"));
            TreeNodeController treeNodeController = new TreeNodeController();
            loader.setController(treeNodeController);
            Pane nodePane = loader.load();
            // Get the controller and set the node key
            TreeNodeController controller = loader.getController();
            controller.setKey(key);

            nodePane.setLayoutX(x);
            nodePane.setLayoutY(y);

            // Add the node to the pane

            // Calculate position and draw child nodes
            final double childY = y - 80; // Vertical distance between nodes
            // Draw the line to the left child and the left child itself
            double childX = x - nodeHeight;
            Line line = new Line(x + nodeHeight/2 , y + nodeHeight/2, childX + nodeHeight/2, childY  + nodeHeight/2);
            treePane.getChildren().add(line);
            treePane.getChildren().add(nodePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void drawChild (int parent, int key) {

    }
}
