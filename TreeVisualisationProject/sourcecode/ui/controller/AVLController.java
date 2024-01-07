package ui.controller;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.*;
import tree.AVLTree;
import tree.BinaryTreeNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class AVLController {
    private AVLTree tree = new AVLTree();
    private AVLTree oldTree = new AVLTree(-999);
    private String lastAction = "nothing";
    private String lastActionRedo = "";
    private int lastKey = -999;
    private Map<Integer, List<Line>> mapHBoxLine = new HashMap<>();

    @FXML
    private Pane treePane;
    @FXML
    private Button backButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button createTreeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button insertButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button traverseButton;

    @FXML
    private VBox traverseVBox;

    @FXML
    private Button dfsButton;

    @FXML
    private Button bfsButton;

    @FXML
    private Button undoButton;

    @FXML
    private Slider sliderSpeed;

    @FXML
    private int speed = 1;

    @FXML
    private Label speedLabel;

    @FXML
    private Label treeTypeLabel;

    private int timeDelaySet = 1020;

    @FXML
    private HBox hBoxTraverse;

    @FXML
    private void backToMainMenu(ActionEvent event) {
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
    private void createRandomTree(ActionEvent event) {
        resetTraverse(false);
        try
        {
            oldTree.copy(tree);
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create random tree");
            dialog.setHeaderText("Enter the height of the tree");
            String input = dialog.showAndWait().get();
            int height = Integer.parseInt(input);
            tree = new AVLTree();
            while (tree.height() < height) {
                tree.insert((int) (Math.random() * 100));
            }
            tree.print();
            clearPane();
            drawWholeTree();
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteNode(ActionEvent event) {
        resetHighlight();
        resetTraverse(false);
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete");
            dialog.setHeaderText("Delete");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            oldTree.copy(tree);
            setLastAction("delete", key);
            deleteUI(key);
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
    private void insertNode(ActionEvent event) {
        resetTraverse(false);
        resetHighlight();
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Insert");
            dialog.setHeaderText("Insert");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            oldTree.copy(tree);
            setLastAction("insert", key);
            if (insertUI(tree.getTreeRoot(), key, timeDelaySet)) {
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
    private void dfsTraverse(ActionEvent event) {
        resetHighlight();
        traverseVBox.setVisible(false);
        resetTraverse(true);
        try {
            dfsTraverseUI(tree.getTreeRoot(), timeDelaySet);
            setLastAction("dfs");
            oldTree.copy(tree);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void bfsTraverse(ActionEvent event) {
        resetHighlight();
        traverseVBox.setVisible(false);
        resetTraverse(true);
        try {
            bfsTraverseUI();
            oldTree.copy(tree);
            setLastAction("bfs");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    private void searchNode(ActionEvent event) {
        resetTraverse(false);
        resetHighlight();
        try{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search");
            dialog.setHeaderText("Search");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            resetHighlight();
            setLastAction("search", key);
            oldTree.copy(tree);
            if (searchUI(tree.getTreeRoot(), key, 0) != 0) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Found", ButtonType.OK);
//                alert.showAndWait();
//                dialog.setContentText("Found");
            }
            else {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not found", ButtonType.OK);
//                alert.showAndWait();
//                dialog.setContentText("");
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
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            speed = newValue.intValue();
            timeDelaySet = 1020 / speed;
            speedLabel.setText(speed + "x");
        });
        try {
            treeTypeLabel.setText("AVL Tree");
            updateButton.setVisible(false);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(NODE_FXML_FILE_PATH));
            TreeNodeController treeNodeController = new TreeNodeController();
            fxmlLoader.setController(treeNodeController);
            AnchorPane anchorPane = fxmlLoader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawTree(BinaryTreeNode node, double x, double y, double horizontalSpacing) {
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
                mapHBoxLine.put(node.key, Collections.singletonList(line));
                mapHBoxLine.put(node.leftChild.key, Collections.singletonList(line));
                drawTree(node.leftChild, childX, childY, horizontalSpacing/2);
            }

            // Draw the line to the right child and the right child itself
            if (node.rightChild != null) {
                double childX = x + horizontalSpacing;
                Line line = new Line(x + nodeHeight/2, y + nodeHeight/2, childX + nodeHeight/2, childY  + nodeHeight/2);
                treePane.getChildren().add(line);
                mapHBoxLine.put(node.key, Collections.singletonList(line));
                mapHBoxLine.put(node.rightChild.key, Collections.singletonList(line));
                drawTree(node.rightChild, childX, childY, horizontalSpacing/2);
            }

            // Add the node to the pane
            treePane.getChildren().add(nodePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearPane() {
        treePane.getChildren().clear();
        treePane.getChildren().add(traverseVBox);
        treePane.getChildren().add(hBoxTraverse);
    }

    public long searchUI(BinaryTreeNode root,int key, long timeDelay) throws InterruptedException {
        try {
            if (root == null) {
                return 0;
            } else {
                timeDelay += timeDelaySet;
                if (root.key == key) {
                    delay(timeDelay, () -> highLightNodeGreen(key));
                    return timeDelay;
                }
                delay(timeDelay, () -> highLightNodeRed(root.key));
                timeDelay += timeDelaySet;
                if (root.key > key) {
                    if (root.leftChild == null) {
                        return 0;
                    }
                    delay(timeDelay, () -> highLightNodeRed(root.leftChild.key));
                    return searchUI(root.leftChild, key, timeDelay);
                }
                if (root.rightChild == null) {
                    return 0;
                }
                delay(timeDelay, () -> highLightNodeRed(root.rightChild.key));
                return searchUI(root.rightChild, key, timeDelay);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean insertUI(BinaryTreeNode root, int key, long timeDelay) throws InterruptedException {
        timeDelay += timeDelaySet;
        if (tree.getTreeRoot() == null) {
            tree.setTreeRoot(key);
            drawWholeTree();
            highLightNodeGreen(key);
            return true;
        }
        if (root.key == key) {
            return false;
        }
        timeDelay += timeDelaySet;
        delay(timeDelay, () -> highLightNodeRed(root.key));
        if (root.key > key) {
            if (root.leftChild == null) {
                root.leftChild = new BinaryTreeNode(key);
                timeDelay += timeDelaySet;
                delay(timeDelay, () -> drawLeftChild(root.key, key));
                timeDelay += timeDelaySet;
                delay(timeDelay, () -> highLightNodeGreen(key));
                return true;
            }
            return insertUI(root.leftChild, key, timeDelay);
        }
        if (root.rightChild == null) {
            root.rightChild = new BinaryTreeNode(key);
            timeDelay += timeDelaySet;
            delay(timeDelay, () -> drawRightChild(root.key, key));
            timeDelay += timeDelaySet;
            delay(timeDelay, () -> highLightNodeGreen(key));
            return true;
        }
        return insertUI(root.rightChild, key, timeDelay);
    }

    public boolean deleteUI(int key) throws InterruptedException {
        highLightNodeRed(tree.getTreeRoot().key);
        long timeDelay = searchUI(tree.getTreeRoot(), key, 0);
        if (timeDelay == 0) {
            return false;
        }
        timeDelay += timeDelaySet;
        HBox hBox = findNode(key);
        if (hBox == null)
            return false;
        HBox hBoxDelete = findNode(key);
        BinaryTreeNode nodeFound = tree.searchNode(key);
        if (nodeFound.isLeaf()) {
            delay(timeDelay + timeDelaySet, () -> {
                tree.delete(key);
//                deleteLine(key);
                clearPane();
                drawWholeTree();
            });
            return true;
        }
        if (nodeFound.leftChild == null || nodeFound.rightChild == null) {
            delay(timeDelay + timeDelaySet, () -> {
                hBoxDelete.setVisible(false);
//                deleteLine(key);
            });
            delay(timeDelay + 2L * timeDelaySet, () -> {
                tree.delete(key);
                clearPane();
                drawWholeTree();
            });
            return true;
        }
        BinaryTreeNode leftMostOfRight = tree.leftMostNode(nodeFound.rightChild);
        HBox hBoxLeftMostOfRight = findNode(leftMostOfRight.key);
        AtomicLong timeDelay2 = new AtomicLong();
        timeDelay += timeDelaySet;
        long finalTimeDelay = timeDelay;
        delay(timeDelay, () -> {
            try {
                timeDelay2.addAndGet(searchUI(nodeFound.rightChild, leftMostOfRight.key, 0));
                delay(timeDelay2.get() + timeDelaySet, () -> {
                    ObservableList<Node> children = hBoxDelete.getChildren();
                    for (Node child : children) {
                        if (!(child instanceof Label label))
                            continue;
                        label.setText(String.valueOf(leftMostOfRight.key));
                    }
//                    deleteLine(leftMostOfRight.key);
                    hBoxLeftMostOfRight.setVisible(false);
                });
                delay(timeDelay2.get() + 2 * timeDelaySet, () -> {
                    clearPane();
                    tree.delete(key);
                    drawWholeTree();
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return true;
    }

    public void dfsTraverseUI(BinaryTreeNode root, long timeDelay) throws InterruptedException {
        long timeDelayLeft = 0;
        if (root == null) {
            return;
        }
        else {
            delay(timeDelay, () -> {
                highLightNodeGreen(root.key);
                traversePrint(root.key);
            });
            timeDelay += timeDelaySet;
            if (root.leftChild != null) {
                dfsTraverseUI(root.leftChild, timeDelay);
            }
            if (root.rightChild != null) {
                long finalTimeDelay = timeDelay;
                delay(timeDelayLeft + timeDelaySet * tree.countNodes(root.leftChild), () -> {
                    try {
                        dfsTraverseUI(root.rightChild, finalTimeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void bfsTraverseUI () {
        Queue<BinaryTreeNode> queue = new ArrayDeque<>();
        if (tree.getTreeRoot() == null) {
            return;
        }
        queue.add(tree.getTreeRoot());
        long timeDelay = timeDelaySet;
        while (!queue.isEmpty()) {
            BinaryTreeNode node = queue.poll();
            delay(timeDelay, () -> {
                highLightNodeGreen(node.key);
                traversePrint(node.key);
            });
            timeDelay += timeDelaySet;
            if (node.leftChild != null) {
                queue.add(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.add(node.rightChild);
            }
        }
    }

    private void highLightNodeRed(int key) {
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
//        wait(timeDelaySet);
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

    private Point2D findCoordinate(int key) {
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

    private void drawLeftChild(int parent, int key) {
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

    private void drawRightChild(int parent, int key) {
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

    private void drawWholeTree() {
        drawTree(tree.getTreeRoot(), treePane.getWidth() / 2,5, treePane.getWidth() / 4);
    }

    private void deleteLine(int key) {
        List<Line> lines = mapHBoxLine.get(key);
        for (Line line : lines) {
            line.setVisible(false);
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
        if (oldTree.getTreeRoot().key == -999) {
            clearPane();
            tree = new AVLTree();
        }
        else if (!oldTree.areIdentical(tree)) {
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
                case "insert" -> insertUI(tree.getTreeRoot(), lastKey, timeDelaySet);
                case "delete" -> deleteUI(lastKey);
                case "search" -> searchUI(tree.getTreeRoot(), lastKey, timeDelaySet);
                case "dfs" -> dfsTraverse(event);
                case "bfs" -> bfsTraverseUI();
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
}
