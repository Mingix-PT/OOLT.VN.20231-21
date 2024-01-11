package ui.controller.v2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import tree.node.BinaryTreeNode;
import tree.node.GenericTreeNode;
import tree.node.TreeNode;
import tree.type.BinarySearchTree;
import tree.type.GenericTree;
import tree.type.Tree;
import ui.controller.ultility.TreeNodeController;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

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
    private boolean isRunning = true;

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
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private ImageView pauseImage;

    private final Image pauseIcon = new Image("/ui/resources/image/pauseButton.png");
    private final Image playIcon = new Image("/ui/resources/image/playButton.png");

    @FXML
    void backToMainMenu(ActionEvent event) {
        final String MENU_FXML_FILE_PATH = "/ui/view/common/Menu.fxml";
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(MENU_FXML_FILE_PATH));
            MenuControllerV2 menuControllerV2 = new MenuControllerV2();
            fxmlLoader.setController(menuControllerV2);
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
        try
        {
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
        catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid height");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void bfsTraverse(ActionEvent event) {
        resetTraverse(true);
        resetHighlight();
        bfsUI();
        traverseVBox.setVisible(true);
        setLastAction("bfs");
    }

    private void bfsUI() {
        List<TreeNode> bfsList = bfsList();
        if (bfsList == null) {
            return;
        }
        int max = bfsList.size();
        resetProgressBar(max);
        timeline = listTraverseUI(bfsList);
        timeline.play();
    }

    private List<TreeNode> bfsList() {
        if (tree == null) {
            return null;
        }
        List<TreeNode> bfsList = new ArrayList<>();
        Queue<TreeNode> queueTree = new ArrayDeque<>();
        queueTree.add(tree.getTreeRoot());
        while (!queueTree.isEmpty()) {
            TreeNode node = queueTree.poll();
            bfsList.add(node);
            if (node instanceof BinaryTreeNode)
            {
                if (((BinaryTreeNode) node).leftChild != null) {
                    queueTree.add(((BinaryTreeNode) node).leftChild);
                }
                if (((BinaryTreeNode) node).rightChild != null) {
                    queueTree.add(((BinaryTreeNode) node).rightChild);
                }
            }
            else {
                queueTree.addAll(((GenericTreeNode) node).children);
            }
        }
        return bfsList;
    }


    @FXML
    void dfsTraverse(ActionEvent event) {
        resetTraverse(true);
        resetHighlight();
        dfsUI();
        traverseVBox.setVisible(true);
        setLastAction("dfs");
    }

    private void dfsUI() {
        List<TreeNode> dfsList = dfsList(tree.getTreeRoot(), new ArrayList<>());
        if (dfsList == null) {
            return;
        }
        for (TreeNode node : dfsList) {
            System.out.print(node.key + " ");
        }
        int max = dfsList.size();
        resetProgressBar(max);
        timeline = listTraverseUI(dfsList);
        timeline.play();
    }

    private List<TreeNode> dfsList(TreeNode node, List<TreeNode> dfsList) {
        if (node == null) {
            return dfsList;
        }
        dfsList.add(node);
        if (node instanceof GenericTreeNode)
            for (GenericTreeNode child : ((GenericTreeNode) node).children) {
                dfsList(child, dfsList);
            }
        else {
            dfsList(((BinaryTreeNode) node).leftChild, dfsList);
            dfsList(((BinaryTreeNode) node).rightChild, dfsList);
        }
        return dfsList;
    }

    @FXML
    void deleteNode(ActionEvent event) {
        resetHighlight();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete");
        dialog.setHeaderText("Enter the key to delete");
        dialog.setContentText("Key: ");
        dialog.showAndWait();
        int key = Integer.parseInt(dialog.getEditor().getText());
        deleteUI(key);
        setLastAction("delete", key);
    }


    private void deleteUI(int key) {
        Duration timeDelay = Duration.ZERO;
        int step;
        List<TreeNode> listSearchResult;
        if (tree instanceof GenericTree) {
            listSearchResult = searchTimes(key);
        }
        else {
            listSearchResult = searchTimes(tree.getTreeRoot(), key, new ArrayList<>());
        }
        step = listSearchResult.size();
        timeline = listTraverseUI(listSearchResult, key);
        System.out.println(step);
        timeDelay = timeDelay.add(millis(timeDelaySet * (step+1)));
        System.out.println(timeDelay);
        KeyFrame keyFrame1 = new KeyFrame(timeDelay, event -> {
            System.out.println("Delete " + key +
                    tree.delete(key));
            resetScreen();
        });
        timeline.getKeyFrames().add(keyFrame1);
        System.out.println(timeline.getKeyFrames());
        timeline.play();
    }

    @FXML
    void insertNode(ActionEvent event) {
        resetHighlight();
        if (tree instanceof GenericTree) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Insert");
            dialog.setHeaderText("Enter the parent key: ");
            dialog.setContentText("Key: ");
            dialog.showAndWait();
            int parentKey = Integer.parseInt(dialog.getEditor().getText());
            dialog = new TextInputDialog();
            dialog.setTitle("Insert");
            dialog.setHeaderText("Enter the key to insert");
            dialog.setContentText("Key: ");
            dialog.showAndWait();
            int key = Integer.parseInt(dialog.getEditor().getText());
            insertUI(parentKey, key);
            setLastAction("insert", key, parentKey);
        }
        else {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Insert");
            dialog.setHeaderText("Enter the key to insert");
            dialog.setContentText("Key: ");
            dialog.showAndWait();
            int key = Integer.parseInt(dialog.getEditor().getText());
            insertUI(key);
            setLastAction("insert", key);
        }
    }

    private void insertUI(int parent, int key) {
        if (tree.search(key)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Key already exists");
            alert.setContentText("Key " + key + " already exists in the tree");
            alert.showAndWait();
            return;
        }
        List<TreeNode> bfsSearchResult = searchTimes(parent);
        for (TreeNode node : bfsSearchResult) {
            System.out.print(node.key + " ");
        }
        System.out.println(bfsSearchResult.size());
        timeline = listTraverseUI(bfsSearchResult, parent);
        int step = (bfsSearchResult.size()+1)*timeDelaySet;
        Duration timeDelay = Duration.ZERO;
        timeDelay = timeDelay.add(Duration.millis((step)));
        System.out.println(timeDelay);
        KeyFrame keyFrame1 = new KeyFrame(timeDelay, event -> {
            resetScreen();
            tree.insert(parent, key);
            drawWholeTree();
            highLightNodeGreen(key);
        });
        timeDelay = timeDelay.add(millis(timeDelaySet));
        System.out.println(timeDelay);
        KeyFrame keyFrame2 = new KeyFrame(timeDelay, event -> {
            resetScreen();
            drawWholeTree();
        });
        timeline.getKeyFrames().add(keyFrame1);
        timeline.getKeyFrames().add(keyFrame2);
        timeline.play();
    }

    private void insertUI(int key) {
        List<TreeNode> dfsSearchResult = searchTimes(tree.getTreeRoot(), key, new ArrayList<>());
        if (dfsSearchResult == null) {
            return;
        }
        timeline = listTraverseUI(dfsSearchResult, key);
        Duration timeDelay = Duration.ZERO;
        timeDelay = timeDelay.add(millis(timeDelaySet*(dfsSearchResult.size()+1)));
        System.out.println(timeDelay);
        KeyFrame keyFrame1 = new KeyFrame(timeDelay, event -> {
            resetScreen();
            tree.insert(key);
            drawWholeTree();
            highLightNodeGreen(key);
        });
        timeDelay = timeDelay.add(millis(timeDelaySet));
        System.out.println(timeDelay);
        KeyFrame keyFrame2 = new KeyFrame(timeDelay, event -> {
            resetScreen();
            drawWholeTree();
        });
        timeline.getKeyFrames().add(keyFrame1);
        timeline.getKeyFrames().add(keyFrame2);
        timeline.play();
    }
    @FXML
    void updateNode(ActionEvent event) {
        resetHighlight();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update");
        dialog.setHeaderText("Enter the key to update");
        dialog.setContentText("Key: ");
        dialog.showAndWait();
        int oldKey = Integer.parseInt(dialog.getEditor().getText());
        dialog = new TextInputDialog();
        dialog.setTitle("Update");
        dialog.setHeaderText("Enter the new key");
        dialog.setContentText("Key: ");
        dialog.showAndWait();
        int newKey = Integer.parseInt(dialog.getEditor().getText());
        updateUI(oldKey, newKey);
    }

    private void updateUI(int oldKey, int newKey) {
        if (!(tree instanceof GenericTree)) {
            return;
        }
        if (tree.search(newKey)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Key already exists");
            alert.setContentText("Key " + newKey + " already exists in the tree");
            alert.showAndWait();
            return;
        }
        if (tree == null) {
            return;
        }
        timeline = listTraverseUI(searchTimes(oldKey), oldKey);
        Duration timeDelay = Duration.ZERO;
        for (KeyFrame keyFrame : timeline.getKeyFrames()) {
            timeDelay = timeDelay.add(keyFrame.getTime());
        }
        timeDelay = timeDelay.add(millis(timeDelaySet));
        KeyFrame keyFrame1 = new KeyFrame(timeDelay, event -> {
            resetScreen();
            tree.update(oldKey, newKey);
            drawWholeTree();
            highLightNodeGreen(newKey);
        });
        timeDelay.add(millis(timeDelaySet));
        timeDelay.add(keyFrame1.getTime());
        KeyFrame keyFrame2 = new KeyFrame(timeDelay, event -> {
            resetScreen();
            drawWholeTree();
        });
        timeline.getKeyFrames().add(keyFrame1);
        timeline.getKeyFrames().add(keyFrame2);
        timeline.play();
    }


    @FXML
    void searchNode(ActionEvent event) throws InterruptedException {
        resetHighlight();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search");
        dialog.setHeaderText("Enter the key to search");
        dialog.setContentText("Key: ");
        dialog.showAndWait();
        int key = Integer.parseInt(dialog.getEditor().getText());
        searchUI(key);
        setLastAction("search", key);
    }

    private void searchUI(int key) {
        List<TreeNode> listSearchResult;
        if (tree instanceof GenericTree) {
            listSearchResult = searchTimes(key);
        }
        else {
            listSearchResult = searchTimes(tree.getTreeRoot(), key, new ArrayList<>());
        }
        int max = listSearchResult.size();
        resetProgressBar(max);
        timeline = listTraverseUI(listSearchResult, key);
        timeline.play();
    }

    private Timeline listTraverseUI(List<TreeNode> list) {
        resetProgressBar(list.size());
        Timeline timelineIn = new Timeline();
        for (int i = 0; i < list.size(); i++) {
            int index = i; // Need a effectively final variable for the lambda
            KeyFrame keyFrame = new KeyFrame(Duration.millis(timeDelaySet * (i + 1)), event -> {
                int keyList = list.get(index).key;
                sliderProgress.setValue(index + 1);
                highLightNodeGreen(keyList);
            });
            timelineIn.getKeyFrames().add(keyFrame);
        }
        return timelineIn;
    }

    private Timeline listTraverseUI(List<TreeNode> list, int key) {
        resetProgressBar(list.size());
        Timeline timelineIn = new Timeline();
        for (int i = 0; i < list.size(); i++) {
            int index = i; // Need a effectively final variable for the lambda
            KeyFrame keyFrame = new KeyFrame(Duration.millis(timeDelaySet * (i + 1)), event -> {
                int keyList = list.get(index).key;
                sliderProgress.setValue(index + 1);
                if (keyList == key) {
                    highLightNodeGreen(key);
                } else {
                    highLightNodeRed(keyList);
                }
            });
            timelineIn.getKeyFrames().add(keyFrame);
        }
        return timelineIn;
    }


    private List<TreeNode> searchTimes(int key) {
        if (tree == null) {
            return new ArrayList<>();
        }
        List<TreeNode> bfsSearchResult = new ArrayList<>();
        Queue<TreeNode> queueTree = new ArrayDeque<>();
        queueTree.add(tree.getTreeRoot());
        while (!queueTree.isEmpty()) {
            TreeNode node = queueTree.poll();
            bfsSearchResult.add(node);
            if (node.key == key) {
                break;
            }
            else {
                queueTree.addAll(((GenericTreeNode) node).children);
            }
        }
        return bfsSearchResult;
    }

    private List<TreeNode> searchTimes(TreeNode node, int key, List<TreeNode> result) {
        if (node == null) {
            return result;
        }
        if (node.key == key) {
            result.add(node);
            return result;
        }
        else if (node.key > key) {
            result.add(node);
            return searchTimes(((BinaryTreeNode)node).leftChild, key, result);
        }
        else {
            result.add(node);
            return searchTimes(((BinaryTreeNode)node).rightChild, key, result);
        }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/common/TreeNode.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/view/common/TreeNode.fxml"));
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
        if (!treePane.getChildren().contains(traverseVBox)) {
            treePane.getChildren().add(traverseVBox);
            traverseVBox.setVisible(false);
            return;
        }
        traverseVBox.setVisible(!traverseVBox.isVisible());
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
    private void undo(ActionEvent event) {
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
    private void redo(ActionEvent event) {
        undo(event);
        resetHighlight();
        resetScreen();
        sliderProgress.setValue(0);
        Timeline timelineIn = timeline;
        timelineIn.stop();
        timelineIn.jumpTo(Duration.ZERO); // reset the timeline to the start
        timelineIn.play();
        switch (lastActionRedo) {
            case "create":
                tree.createTree(lastKey);
                resetScreen();
                break;
            case "insert":
                if (tree instanceof GenericTree)
                    tree.insert(lastParentKey, lastKey);
                else
                    tree.insert(lastKey);
                break;
            case "delete":
                tree.delete(lastKey);
                break;
            case "search":
                break;
            case "dfs":
                break;
            case "bfs":
                break;
            case "update":
                tree.update(lastKey, lastParentKey);
                break;
            default:
                break;
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

    private void resetScreen() {
        clearPane();
        drawWholeTree();
    }

    private void resetProgressBar(int counter) {
        sliderProgress.setMax(counter);
        sliderProgress.setValue(0);
    }

    @FXML
    private void pause(ActionEvent event) {
        System.out.println("\nPause button pressed");
        if (isRunning) {
            System.out.println("Pause");
            isRunning = false;
            timeline.pause();
            pauseImage.setImage(playIcon);
        }
        else {
            System.out.println("Play");
            isRunning = true;
            timeline.play();
            pauseImage.setImage(pauseIcon);
        }
    }
}
