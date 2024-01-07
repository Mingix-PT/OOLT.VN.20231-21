package ui.controller;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import tree.CompleteBalanceBinarySearchTree;
import tree.*;
import tree.GenericTreeNode;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class GenericTreeController {
    private int speed;
    private long timeDelaySet = 1020;
    private GenericTree tree = new GenericTree();
    private GenericTree oldTree = new GenericTree(-999);
    private String lastAction = "nothing";
    private String lastActionRedo = "nothing";
    private int lastKey = -999;
    private int lastParentKey = -999;
    private double nodeHeight = 30;

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
    void createRandomTree(ActionEvent event) {
        if (tree!=null) {
            oldTree.copy(tree);
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Random Tree");
        dialog.setHeaderText("Enter the height of the tree:");
        dialog.showAndWait();
        int height = Integer.parseInt(dialog.getEditor().getText());
        setLastAction("create", height);
        tree = new GenericTree();
        tree.createRandomTree(height);
        tree.printTree();
        clearPane();
        drawWholeTree();
    }

    @FXML
    void updateNode(ActionEvent event) {
        resetHighlight();
        resetTraverse(false);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Node");
        dialog.setHeaderText("Enter the key of the node:");
        dialog.showAndWait();
        int oldKey = Integer.parseInt(dialog.getEditor().getText());
        TextInputDialog dialog1 = new TextInputDialog();
        dialog1.setTitle("Update Node");
        dialog1.setHeaderText("Enter the new key of the node:");
        dialog1.showAndWait();
        int newKey = Integer.parseInt(dialog1.getEditor().getText());
        setLastAction("update", newKey, oldKey);
        long timeDelay = updateUI(oldKey, newKey);
    }

    private long updateUI(int oldKey, int newKey) {
        if (tree.getTreeRoot() == null) {
            return 0;
        }
        if (tree.search(newKey)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Key " + newKey + " already exists in the tree", ButtonType.OK);
            alert.showAndWait();
            return 0;
        }
        long timeDelay = searchUI(tree.getTreeRoot(), oldKey, timeDelaySet);
        if (timeDelay != 0) {
            delay(timeDelay + timeDelaySet, () -> {
                tree.update(oldKey, newKey);
                clearPane();
                drawWholeTree();
                highLightNodeGreen(newKey);
                delay(timeDelaySet, () -> {
                    clearPane();
                    drawWholeTree();
                });
            });
        }
        return 0;
    }

    @FXML
    void dfsTraverse(ActionEvent event) {
        resetHighlight();
        resetTraverse(true);
        traverseVBox.setVisible(false);
        dfsTraverseUI(tree.getTreeRoot(), timeDelaySet);
        setLastAction("dfs");
    }

    @FXML
    private long dfsTraverseUI(GenericTreeNode node, long timeDelay) {
        resetTraverse(true);
        if (node == null) {
            return 0;
        }
        if (node.isLeaf()) {
            delay(timeDelay, () -> {
                highLightNodeGreen(node.key);
                traversePrint(node.key);
            });
            return timeDelay;
        }
        delay(timeDelay, () -> {
            highLightNodeGreen(node.key);
            traversePrint(node.key);
        });
        timeDelay += timeDelaySet;
        List<GenericTreeNode> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            timeDelay = dfsTraverseUI(children.get(i), timeDelay + timeDelaySet);
        }
        return timeDelay;
    }
    @FXML
    private void bfsTraverse(ActionEvent event) {
        resetHighlight();
        resetTraverse(true);
        traverseVBox.setVisible(false);
        bfsTraverseUI();
        setLastAction("bfs");
    }

    private void bfsTraverseUI() {
        resetTraverse(true);
        Queue<GenericTreeNode> queue = new ArrayDeque<>();
        if (tree.getTreeRoot() == null) {
            return;
        }
        queue.add(tree.getTreeRoot());
        long timeDelay = timeDelaySet;
        while (!queue.isEmpty()) {
            GenericTreeNode topNode = queue.poll();
            delay(timeDelay, () -> {
                highLightNodeGreen(topNode.key);
                traversePrint(topNode.key);
            });
            timeDelay += timeDelaySet;
            queue.addAll(topNode.getChildren());
        }
    }

    @FXML
    void insertNode(ActionEvent event) {
        resetHighlight();
        resetTraverse(false);
        TextInputDialog parentDialog = new TextInputDialog();
        parentDialog.setTitle("Insert Node");
        parentDialog.setHeaderText("Enter the key of the parent:");
        parentDialog.showAndWait();
        int parentKey = Integer.parseInt(parentDialog.getEditor().getText());
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Insert Node");
        dialog.setHeaderText("Enter the key of the node:");
        dialog.showAndWait();
        int key = Integer.parseInt(dialog.getEditor().getText());
        setLastAction("insert", key, parentKey);
        long timeDelay = insertUI(parentKey, key);
    }

    private long insertUI(int parent, int key) {
        if (tree.getTreeRoot() == null) {
            tree.insert(key);
            return 0;
        }
        Queue<GenericTreeNode> queue = new ArrayDeque<>();
        queue.add(tree.getTreeRoot());
        long timeDelay = timeDelaySet;
        while (!queue.isEmpty()) {
            GenericTreeNode topNode = queue.poll();
            if (topNode.key != parent) {
                delay(timeDelay, () -> {
                    highLightNodeRed(topNode.key);
                });
                timeDelay += timeDelaySet;
                queue.addAll(topNode.getChildren());
            }
            else {
                delay(timeDelay, () -> {
                    highLightNodeGreen(topNode.key);
                    tree.insert(parent, key);
                    delay(timeDelaySet, () -> {
                        clearPane();
                        drawWholeTree();
                    });
                });
                return timeDelay;
            }
        }
        return 0;
    }

    @FXML
    public long searchUI(GenericTreeNode root, int key, long timeDelay) {
        if (root == null) {
            return 0;
        }
        Queue<GenericTreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            GenericTreeNode topNode = queue.poll();
            if (topNode.key == key) {
                delay(timeDelay, () -> {
                    highLightNodeGreen(topNode.key);
                });
                return timeDelay;
            }
            delay(timeDelay, () -> {
                highLightNodeRed(topNode.key);
            });
            timeDelay += timeDelaySet;
            queue.addAll(topNode.getChildren());
        }
        return 0;
    }

    @FXML
    void searchNode(ActionEvent event) {
        resetHighlight();
        resetTraverse(false);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Node");
        dialog.setHeaderText("Search");
        dialog.showAndWait();
        int key = Integer.parseInt(dialog.getEditor().getText());
        setLastAction("search", key);
        long timeDelay = searchUI(tree.getTreeRoot(), key, timeDelaySet);
    }


    @FXML
    void deleteNode(ActionEvent event) {
        resetHighlight();
        resetTraverse(false);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Node");
        dialog.setHeaderText("Delete");
        dialog.showAndWait();
        int key = Integer.parseInt(dialog.getEditor().getText());
        setLastAction("delete", key);
        long timeDelay = deleteUI(key);
    }


    private long deleteUI(int key) {
        if (tree.getTreeRoot() == null) {
            return 0;
        }
        Queue<GenericTreeNode> queue = new ArrayDeque<>();
        queue.add(tree.getTreeRoot());
        long timeDelay = timeDelaySet;
        while (!queue.isEmpty()) {
            GenericTreeNode topNode = queue.poll();
            if (topNode.key == key) {
                delay(timeDelay, () -> {
                    highLightNodeGreen(topNode.key);
                    tree.delete(key);
                    delay(timeDelaySet, () -> {
                        clearPane();
                        drawWholeTree();
                    });
                });
                return timeDelay;
            }
            delay(timeDelay, () -> {
                highLightNodeRed(topNode.key);
            });
            timeDelay += timeDelaySet;
            queue.addAll(topNode.getChildren());
        }
        return 0;
    }

    public void clearPane() {
        treePane.getChildren().clear();
        treePane.getChildren().add(traverseVBox);
        treePane.getChildren().add(hBoxTraverse);
    }

    public void drawWholeTree() {
        int width = tree.width();
        drawTree(tree.getTreeRoot(), treePane.getWidth()/2, treePane.getHeight()*0.1, treePane.getWidth()/3);
    }

    public void drawTree(GenericTreeNode node, double x, double y, double horizontalSpacing) {
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
                drawTree(node.children.get(i), childX, childY, horizontalSpacing / 2);
            }
            if (!treePane.getChildren().contains(nodePane)) {
                treePane.getChildren().add(nodePane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        speed = (int) sliderSpeed.getValue();
        speedLabel.setText(speed + "x");
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            speed = newValue.intValue();
            speedLabel.setText(speed + "x");
        });
        sliderProgress1.valueProperty().addListener((observable, oldValue, newValue) -> {
            timeDelaySet = 1020 - newValue.intValue();
        });
        treeTypeLabel.setText("Generic Tree");
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

    @FXML
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
}
