package ui.controller.v2;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static javafx.util.Duration.millis;

public class ControllerV2 {
    private Tree tree;
    private Tree oldTree = null;
    private double nodeHeight = 30;
    private int speed = 1;
    private int timeDelaySet = 1020;
    private String lastAction = "nothing";
    private String lastActionRedo = "nothing";
    private int lastKey = -999;
    private int lastParentKey = -999;
    private List<ObservableList<Node>> sceneNodes = new ArrayList<>();
    private boolean pause = false;

    Timeline timeline = new Timeline();

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
    private Slider sliderProgress;

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
    void createTree(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Tree");
        dialog.setHeaderText("Enter the height of the tree");
        dialog.setContentText("Height: ");
        dialog.showAndWait();
        int height = Integer.parseInt(dialog.getEditor().getText());
        tree.createTree(height);
        tree.print();
        resetScreen();
        setLastAction("create", height);
    }

    @FXML
    void bfsTraverse(ActionEvent event) {

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
    void searchNode(ActionEvent event) throws InterruptedException {
        timeline.getKeyFrames().clear();
        resetHighlight();
        resetHighlight();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search");
        dialog.setHeaderText("Enter the key to search");
        dialog.setContentText("Key: ");
        dialog.showAndWait();
        int key = Integer.parseInt(dialog.getEditor().getText());
//        System.out.println("Counter: " + (searchUIGeneric(key, 0)));
//        searchUIGeneric(key, timeDelaySet);
        searchUI(key);
    }

    private int searchUI(int key) {
        if (tree instanceof GenericTree) {
            return searchUIGeneric(key);
        }
        else {
            return searchUIBST(key);
        }
    }
    private int searchUIGeneric(int key){
        List<GenericTreeNode> bfsSearchResult = searchTimesGeneric(key);
        if (bfsSearchResult == null) {
            return 0;
        }
        int max = bfsSearchResult.size();
        AtomicInteger counter = new AtomicInteger(0);
        KeyFrame keyFrame = new KeyFrame(millis(timeDelaySet), event -> {
            GenericTreeNode node = bfsSearchResult.getFirst();
            setProgressBar(max, counter.get());
            counter.getAndIncrement();
            if (node.key == key) {
                highLightNodeGreen(node.key);
                return;
            }
            else {
                highLightNodeRed(node.key);
                bfsSearchResult.removeFirst();
            }
            if (pause) {
                timeline.pause();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return bfsSearchResult.size();
    }

//    private int searchUIGeneric(int key, long timeDelay) {
//        timeline.getKeyFrames().clear();
//        if (tree == null) {
//            return 0;
//        }
//        Queue<GenericTreeNode> queueTree = new ArrayDeque<>();
//        queueTree.add((GenericTreeNode) tree.getTreeRoot());
//        AtomicInteger counter = new AtomicInteger();
//        KeyFrame keyFrame = new KeyFrame(millis(timeDelay), event -> {
//            GenericTreeNode node = queueTree.poll();
//            if (node == null) {
//                timeline.stop();
//                return;
//            }
//            if (node.key == key) {
//                highLightNodeGreen(node.key);
//                timeline.stop();
//            }
//            else {
//                highLightNodeRed(node.key);
//                queueTree.addAll(node.children);
//            }
//            if (pause) {
//                timeline.pause();
//            }
//        });
//        timeline.getKeyFrames().add(keyFrame);
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
//        return counter.get();
//    }

    private List<GenericTreeNode> searchTimesGeneric(int key) {
        if (tree == null) {
            return null;
        }
        List<GenericTreeNode> bfsSearchResult = new ArrayList<>();
        Queue<GenericTreeNode> queueTree = new ArrayDeque<>();
        queueTree.add((GenericTreeNode) tree.getTreeRoot());
        while (!queueTree.isEmpty()) {
            GenericTreeNode node = queueTree.poll();
            bfsSearchResult.add(node);
            if (node.key == key) {
                break;
            }
            else {
                queueTree.addAll(node.children);
            }
        }
        return bfsSearchResult;
    }

    private int searchUIBST(int key) {
        timeline.getKeyFrames().clear();
        List<BinaryTreeNode> dfsSearchResult = searchTimesBST((BinaryTreeNode) tree.getTreeRoot(), key, new ArrayList<>());
        if (dfsSearchResult == null) {
            return 0;
        }
        KeyFrame keyFrame = new KeyFrame(millis(timeDelaySet), event -> {
            BinaryTreeNode node = dfsSearchResult.getFirst();
            if (node.key == key) {
                highLightNodeGreen(node.key);
                return;
            }
            else {
                highLightNodeRed(node.key);
                dfsSearchResult.removeFirst();
            }
            if (pause) {
                timeline.pause();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return dfsSearchResult.size();
    }
    private List<BinaryTreeNode> searchTimesBST(BinaryTreeNode node, int key, List<BinaryTreeNode> result) {
        if (node == null) {
            return null;
        }
        if (node.key == key) {
            result.add(node);
            return result;
        }
        else if (node.key > key) {
            result.add(node);
            return searchTimesBST(node.leftChild, key, result);
        }
        else {
            result.add(node);
            return searchTimesBST(node.rightChild, key, result);
        }
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
            drawGenericTree(((GenericTree) tree).getTreeRoot(), treePane.getWidth()/2, treePane.getHeight()*0.1, treePane.getWidth()/4);
        }
        else {
            drawBST(((BinarySearchTree) tree).getTreeRoot(), treePane.getWidth()/2, treePane.getHeight()*0.1, treePane.getWidth()/4);
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
            resetScreen();
            lastActionRedo = lastAction;
            lastAction = "nothing";
        }
        else if (lastAction.equals("search") || lastAction.equals("dfs") || lastAction.equals("bfs")) {
            resetScreen();
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
            resetScreen();
            switch (lastActionRedo) {
//                case "insert" -> insertUI(lastParentKey, lastKey);
//                case "delete" -> deleteUI(lastKey);
//                case "search" -> searchUI(tree.getTreeRoot(), lastKey, timeDelaySet);
                case "dfs" -> {
                    resetTraverse(true);
                    dfsTraverse(event);
                }
                case "bfs" -> {
                    resetTraverse(true);
//                    bfsTraverseUI();
                }
//                case "update" -> updateUI(lastKey, lastParentKey);
                case "create" -> {
                    tree = new GenericTree();
                    tree.createTree(lastKey);
                    resetScreen();
                }
            }
        }
    }

    private void setLastAction(String action) {
        if (oldTree == null) {
            if (tree instanceof GenericTree) {
                oldTree = new GenericTree();
            }
            else {
                oldTree = new BinarySearchTree();
            }
        }
        oldTree.copy(tree);
        lastAction = action;
        lastActionRedo = action;
    }
    private void setLastAction(String action, int key) {
        if (oldTree == null) {
            if (tree instanceof GenericTree) {
                oldTree = new GenericTree();
            }
            else {
                oldTree = new BinarySearchTree();
            }
        }
        oldTree.copy(tree);
        lastAction = action;
        lastActionRedo = action;
        lastKey = key;
    }
    private void setLastAction(String action, int key, int parent) {
        if (oldTree == null) {
            if (tree instanceof GenericTree) {
                oldTree = new GenericTree();
            }
            else {
                oldTree = new BinarySearchTree();
            }
        }
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
    private void initialize() {
        final String NODE_FXML_FILE_PATH = "/ui/view/TreeNode.fxml";
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

    private void clearPane() {
        treePane.getChildren().clear();
        treePane.getChildren().add(traverseVBox);
        treePane.getChildren().add(hBoxTraverse);
    }

    private void clearAllPane() {
        treePane.getChildren().clear();
    }
    
    private void resetScreen() {
        clearPane();
        drawWholeTree();
    }

    private void setProgressBar(int counter, int current) {
        sliderProgress.setMax(counter);
        sliderProgress.setValue(current);
    }

//    private void playAnimation(int counter) {
//        timeline.getKeyFrames();
//        AtomicInteger currentValue = new AtomicInteger(0);
//        sliderProgress.setMax(counter);
//        sliderProgress.setMin(0);
//        sliderProgress.setBlockIncrement(1);
//        sliderProgress.setValue(currentValue.get());
//        KeyFrame keyFrame = new KeyFrame(millis(timeDelaySet), event -> {
//            clearAllPane();
//            treePane.getChildren().addAll(sceneNodes.get(currentValue.get()));
//            currentValue.getAndIncrement();
//            sliderProgress.setValue(currentValue.get());
//            if (currentValue.get() == counter) {
//                timeline.stop();
//            }
//        });
//        timeline.getKeyFrames().add(keyFrame);
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
//    }

    @FXML
    private void pause() {
        pause = !pause;
    }
}
