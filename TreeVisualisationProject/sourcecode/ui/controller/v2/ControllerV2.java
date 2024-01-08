package ui.controller.v2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import tree.node.BinaryTreeNode;
import tree.node.GenericTreeNode;
import tree.type.BinarySearchTree;
import tree.type.GenericTree;
import tree.type.Tree;
import ui.controller.ultility.MenuController;
import ui.controller.ultility.TreeNodeController;

import java.io.IOException;

public class ControllerV2 {
    private Tree tree;
    private Tree oldTree;
    private double nodeHeight = 30;
    private int speed = 1;
    private int timeDelaySet = 1020;
    private String lastAction = "nothing";
    private String lastActionRedo = "nothing";
    private int lastKey = -999;
    private int lastParentKey = -999;

    public ControllerV2(Tree tree) {
        this.tree = tree;
    }

    @FXML
    private Button backButton;

    @FXML
    private Button bfsButton;

    @FXML
    private Button createTreeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button dfsButton;

    @FXML
    private HBox hBoxTraverse;

    @FXML
    private Button insertButton;

    @FXML
    private Button redoButton;

    @FXML
    private Button searchButton;

    @FXML
    private Slider sliderProgress1;

    @FXML
    private Slider sliderSpeed;

    @FXML
    private Label speedLabel;

    @FXML
    private Button traverseButton;

    @FXML
    private VBox traverseVBox;

    @FXML
    private Pane treePane;

    @FXML
    private Label treeTypeLabel;

    @FXML
    private Button undoButton;

    @FXML
    private Button updateButton;

    @FXML
    void backToMainMenu(ActionEvent event) {
        final String MENU_FXML_FILE_PATH = "/ui/view/Menu.fxml";
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(MENU_FXML_FILE_PATH));
            MenuController menuController = new MenuController();
            fxmlLoader.setController(menuController);
            Parent parent = fxmlLoader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Main Menu");
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void bfsTraverse(ActionEvent event) {

    }


    @FXML
    void createRandomTree(ActionEvent event) {

    }

    @FXML
    void deleteNode(ActionEvent event) {

    }

    @FXML
    void dfsTraverse(ActionEvent event) {

    }

    @FXML
    void insertNode(ActionEvent event) {

    }

    @FXML
    void searchNode(ActionEvent event) {

    }

    @FXML
    void undo(ActionEvent event) {

    }

    @FXML
    void updateNode(ActionEvent event) {

    }

    private void drawWholeTree() {
        treePane.getChildren().clear();
        if (tree instanceof GenericTree) {
            drawGenericTree(((GenericTree) tree).getTreeRoot(), 0, 0, 1000);
        }
        else {
            drawBST(((BinarySearchTree) tree).getTreeRoot(), treePane.getWidth()/2, treePane.getHeight()*0.1, 1000);
        }
    }


    public void drawGenericTree(GenericTreeNode node, double x, double y, double horizontalSpacing) {
        if (node == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/TreeNode.fxml"));
            TreeNodeController treeNodeController = new TreeNodeController();
            loader.setController(treeNodeController);
            Pane nodePane = loader.load();

            TreeNodeController controller = loader.getController();
            controller.setKey(node.key);

            nodePane.setLayoutX(x);
            nodePane.setLayoutY(y);

            // Calculate positions and draw child nodes
            double childY = y + 80; // Vertical distance between nodes
            int numChildren = node.children.size();

            // Increase horizontal spacing between child nodes
            double newHorizontalSpace = horizontalSpacing * (numChildren + 1);

            for (int i = 0; i < numChildren; i++) {
                double childX = x - (newHorizontalSpace / 2) + newHorizontalSpace / (numChildren + 1) * (i + 1);
                double lineEndX = childX;
                double lineEndY = childY - nodeHeight;
                Line line = new Line(x + nodeHeight/2, y + nodeHeight/2, lineEndX + nodeHeight/2, lineEndY + nodeHeight);
                treePane.getChildren().add(line);
                drawGenericTree(node.children.get(i), childX, childY, horizontalSpacing / 2);
            }
            if (!treePane.getChildren().contains(nodePane)) {
                treePane.getChildren().add(nodePane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawBST(BinaryTreeNode node, double x, double y, double horizontalSpacing) {
        if (node == null) return;

        try {
            double nodeHeight = 30; // Replace with the actual height of the nodes
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

            // Calculate position and draw child nodes
            final double childY = y + 80; // Vertical distance between nodes

            // Draw the line to the left child and the left child itself
            if (node.leftChild != null) {
                double childX = x - horizontalSpacing;
                Line line = new Line(x + nodeHeight/2 , y + nodeHeight/2, childX + nodeHeight/2, childY  + nodeHeight/2);
                treePane.getChildren().add(line);
                drawBST(node.leftChild, childX, childY, horizontalSpacing/2);
            }

            // Draw the line to the right child and the right child itself
            if (node.rightChild != null) {
                double childX = x + horizontalSpacing;
                Line line = new Line(x + nodeHeight/2, y + nodeHeight/2, childX + nodeHeight/2, childY  + nodeHeight/2);
                treePane.getChildren().add(line);
                drawBST(node.rightChild, childX, childY, horizontalSpacing/2);
            }

            // Add the node to the pane
            treePane.getChildren().add(nodePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }@FXML
    private void chooseTraverse(ActionEvent event) {
        System.out.println("Choose traverse");
        if (traverseVBox.isVisible()) {
            traverseVBox.setVisible(false);
        }
        else {
            traverseVBox.setVisible(true);
        }
    }

    private void traversePrint (int key) {
        Label label = new Label(key + "    ");
        label.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 15; -fx-font-weight: bold"
                + "; -fx-font-family: \"Times New Roman\"; -fx-margin: 20");
        hBoxTraverse.getChildren().add(label);
    }

    private void resetTraverse(boolean visibility) {
        hBoxTraverse.getChildren().clear();
        hBoxTraverse.setVisible(visibility);
    }

    @FXML
    private void undo() {
        System.out.println("Undo");
        if (oldTree.getTreeRoot() == null) {
            System.out.println("Tree is empty");
            clearPane();
            tree = new GenericTree();
        }
        else if (!oldTree.areIdentical(tree)) {
            System.out.println("Tree is not identical");
            tree.copy(oldTree);
            clearPane();
            drawWholeTree();
            lastActionRedo = lastAction;
            lastAction = "nothing";
        }
        else if (lastAction.equals("search") || lastAction.equals("dfs") || lastAction.equals("bfs")) {
            clearPane();
            drawWholeTree();
        }
    }

    @FXML
    private void redo(ActionEvent event) throws InterruptedException {
        if (lastAction.equals("nothing") ) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No action to redo", ButtonType.OK);
            alert.showAndWait();
        }
        else {
            undo();
            clearPane();
            drawWholeTree();
            switch (lastActionRedo) {
                case "insert" -> insertUI(lastParentKey, lastKey);
                case "delete" -> deleteUI(lastKey);
                case "search" -> searchUI(tree.getTreeRoot(), lastKey, timeDelaySet);
                case "dfs" -> {
                    resetTraverse(true);
                    dfsTraverse(event);
                }
                case "bfs" -> {
                    resetTraverse(true);
                    bfsTraverseUI();
                }
                case "update" -> updateUI(lastKey, lastParentKey);
                case "create" -> {
                    tree = new GenericTree();
                    tree.createRandomTree(lastKey);
                    clearPane();
                    drawWholeTree();
                }
            }
        }
    }

    private void setLastAction(String action) {
        oldTree.copy(tree);
        lastAction = action;
        lastActionRedo = action;
    }
    private void setLastAction(String action, int key) {
        oldTree.copy(tree);
        lastAction = action;
        lastActionRedo = action;
        lastKey = key;
    }
    private void setLastAction(String action, int key, int parent) {
        oldTree.copy(tree);
        lastAction = action;
        lastActionRedo = action;
        lastKey = key;
        lastParentKey = parent;
    }
    private void highLightNodeRed(int key) {
        HBox hBox = findNode(key);
        if (hBox == null) {
            System.out.println("HBox is null");
            return;
        }
        hBox.setStyle("-fx-border-color: #000000; -fx-border-radius: 30; " +
                "-fx-background-color: #ff0000; -fx-background-radius: 30");
        ObservableList<Node> hBoxChildren = hBox.getChildren();
        for (Node hBoxChild : hBoxChildren) {
            if (!(hBoxChild instanceof Label label))
                continue;
            label.setStyle("-fx-text-fill: #ffffff");
        }
    }

    private void highLightNodeGreen(int key) {
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
    }

    private void resetHighlight() {
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

    private HBox findNode(int key) {
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

    @FXML
    private void initialize() {final String NODE_FXML_FILE_PATH = "/ui/view/TreeNode.fxml";
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            speed = newValue.intValue();
            timeDelaySet = 1020 / speed;
            speedLabel.setText(speed + "x");
        });
        treeTypeLabel.setText(MenuControllerV2.getClass(tree));
        if (!(tree instanceof GenericTree)) {
            updateButton.setVisible(false);
        }
    }

}
