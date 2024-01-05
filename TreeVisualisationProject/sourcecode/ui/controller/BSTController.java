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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import tree.BinarySearchTree;
import tree.BinaryTreeNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class BSTController {
    private BinarySearchTree bst = new BinarySearchTree();
    private Map<Pane, List<Line>> mapHBoxLine = new HashMap<>();

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
    void backToMainMenu(ActionEvent event) {

    }

    @FXML
    void createRandomTree(ActionEvent event) {
        try
        {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Create random tree");
            dialog.setHeaderText("Enter the height of the tree");
            String input = dialog.showAndWait().get();
            int height = Integer.parseInt(input);
            bst = new BinarySearchTree();
            while (bst.height() < height) {
                bst.insert((int) (Math.random() * 100));
            }
            bst.print();
            clearPane();
            drawWholeTree();
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid input", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void deleteNode(ActionEvent event) {
        resetHighlight();
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Delete");
            dialog.setHeaderText("Delete");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            if (deleteUI(key)) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Deleted", ButtonType.OK);
//                alert.showAndWait();
//                dialog.setContentText("Deleted");
//                clearPane();
//                drawWholeTree();
            }
            else {
//                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed", ButtonType.OK);
//                alert.showAndWait();
//                dialog.setContentText("Failed");
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
        resetHighlight();
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Insert");
            dialog.setHeaderText("Insert");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            if (insertUI(bst.getTreeRoot(), key, 1000)) {
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inserted", ButtonType.OK);
//                alert.showAndWait();
//                dialog.setContentText("Inserted");
            }
            else {
//                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed", ButtonType.OK);
//                alert.showAndWait();
//                dialog.setContentText("Failed");
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
    void dfsTraverse(ActionEvent event) {
        resetHighlight();
        traverseVBox.setVisible(false);
        try {
            dfsTraverseUI(bst.getTreeRoot(), 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void bfsTraverse(ActionEvent event) {
        resetHighlight();
        traverseVBox.setVisible(false);
        try {
            bfsTraverseUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void chooseTraverse(ActionEvent event) {
        System.out.println("Choose traverse");
        if (traverseVBox.isVisible()) {
            traverseVBox.setVisible(false);
        }
        else {
            traverseVBox.setVisible(true);
        }
    }

    @FXML
    void searchNode(ActionEvent event) {
        resetHighlight();
        try{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Search");
            dialog.setHeaderText("Search");
            String input = dialog.showAndWait().get();
            int key = Integer.parseInt(input);
            resetHighlight();
            if (searchUI(bst.getTreeRoot(), key, 0) != 0) {
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
        try {
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

    private Pane drawTree(BinaryTreeNode node, double x, double y, double horizontalSpacing) {
        if (node == null) return null;

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
                mapHBoxLine.put(nodePane, Collections.singletonList(line));
                mapHBoxLine.put(drawTree(node.leftChild, childX, childY, horizontalSpacing/2), Collections.singletonList(line)) ;
            }

            // Draw the line to the right child and the right child itself
            if (node.rightChild != null) {
                double childX = x + horizontalSpacing;
                Line line = new Line(x + nodeHeight/2, y + nodeHeight/2, childX + nodeHeight/2, childY  + nodeHeight/2);
                treePane.getChildren().add(line);
                mapHBoxLine.put(nodePane, Collections.singletonList(line));
                mapHBoxLine.put(drawTree(node.rightChild, childX, childY, horizontalSpacing/2), Collections.singletonList(line));
            }

            // Add the node to the pane
            treePane.getChildren().add(nodePane);
            return nodePane;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    void clearPane() {
        treePane.getChildren().clear();
        treePane.getChildren().add(traverseVBox);
    }

    public long searchUI(BinaryTreeNode root,int key, long timeDelay) throws InterruptedException {
        try {
            if (root == null) {
                return 0;
            } else {
                timeDelay += 1000;
                if (root.key == key) {
                    delay(timeDelay, () -> highLightNodeGreen(key));
                    return timeDelay;
                }
                delay(timeDelay, () -> highLightNodeRed(root.key));
                timeDelay += 1000;
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
        timeDelay += 1000;
        if (bst.getTreeRoot() == null) {
            bst.setTreeRoot(key);
            drawWholeTree();
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

    public boolean deleteUI(int key) throws InterruptedException {
        highLightNodeRed(bst.getTreeRoot().key);
        long timeDelay = searchUI(bst.getTreeRoot(), key, 0);
        if (timeDelay == 0) {
            return false;
        }
        timeDelay += 1000;
        HBox hBox = findNode(key);
        if (hBox == null)
            return false;
        HBox hBoxDelete = findNode(key);
        BinaryTreeNode nodeFound = bst.searchNode(key);
        if (nodeFound.isLeaf()) {
            delay(timeDelay + 1000, () -> {
                bst.delete(key);
                clearPane();
                drawWholeTree();
            });
            return true;
        }
        if (nodeFound.leftChild == null || nodeFound.rightChild == null) {
            delay(timeDelay + 1000, () -> {
                hBoxDelete.setVisible(false);
                deleteLine(hBoxDelete);
            });
            delay(timeDelay + 2000, () -> {
                bst.delete(key);
                clearPane();
                drawWholeTree();
            });
            return true;
        }
        BinaryTreeNode leftMostOfRight = bst.leftMostNode(nodeFound.rightChild);
        HBox hBoxLeftMostOfRight = findNode(leftMostOfRight.key);
        AtomicLong timeDelay2 = new AtomicLong();
        timeDelay += 1000;
        long finalTimeDelay = timeDelay;
        delay(timeDelay, () -> {
            try {
                timeDelay2.addAndGet(searchUI(nodeFound.rightChild, leftMostOfRight.key, 0));
                delay(timeDelay2.get() + 1000, () -> {
                    ObservableList<Node> children = hBoxDelete.getChildren();
                    for (Node child : children) {
                        if (!(child instanceof Label label))
                            continue;
                        label.setText(String.valueOf(leftMostOfRight.key));
                    }
                    deleteLine(hBoxLeftMostOfRight);
                    hBoxLeftMostOfRight.setVisible(false);
                });
                delay(timeDelay2.get() + 2000, () -> {
                    clearPane();
                    bst.delete(key);
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
            delay(timeDelay, () -> highLightNodeGreen(root.key));
            timeDelay += 1000;
            if (root.leftChild != null) {
                dfsTraverseUI(root.leftChild, timeDelay);
            }
            if (root.rightChild != null) {
                long finalTimeDelay = timeDelay;
                delay(timeDelayLeft + 1000 * bst.countNodes(root.leftChild), () -> {
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
        if (bst.getTreeRoot() == null) {
            return;
        }
        queue.add(bst.getTreeRoot());
        long timeDelay = 1000;
        while (!queue.isEmpty()) {
            BinaryTreeNode node = queue.poll();
            delay(timeDelay, () -> highLightNodeGreen(node.key));
            timeDelay += 1000;
            if (node.leftChild != null) {
                queue.add(node.leftChild);
            }
            if (node.rightChild != null) {
                queue.add(node.rightChild);
            }
        }
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

    Point2D findCoordinate(HBox node) {
        Point2D point2D = new Point2D(node.getLayoutX(), node.getLayoutY());
        return treePane.localToParent(point2D);
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

    void drawWholeTree() {
        drawTree(bst.getTreeRoot(), treePane.getWidth() / 2,treePane.getHeight()*0.1, bst.height()*40);
    }

    void deleteLine(HBox hBox) {
//        ObservableList<Node> children = treePane.getChildren();
//        double nodeHeight = 30;
//        Point2D point2D = findCoordinate(hBox);
//        double x = point2D.getX();
//        double y = point2D.getY();
//        System.out.println("HBox with Point2D: " + x + " " + y);
//        Bounds bounds = hBox.localToScreen(hBox.getBoundsInLocal());
//        System.out.println("HBox with Bounds: " + bounds.getMinX() + " " + bounds.getMinY() + " " + bounds.getMaxX() + " " + bounds.getMaxY());
//        Bounds bounds1 = hBox.localToScene(hBox.getBoundsInLocal());
//        System.out.println("HBox with Bounds1: " + bounds1.getMinX() + " " + bounds1.getMinY() + " " + bounds1.getMaxX() + " " + bounds1.getMaxY());
//        for (Node child : children) {
//            if ((child instanceof Line line)) {
//                Point2D point2D1 = line.localToScene(line.getStartX(), line.getStartY());
//                Point2D point2D2 = line.localToScene(line.getEndX(), line.getEndY());
//                System.out.println("Line with Point2D: " + point2D1.getX() + " " + point2D1.getY() + " " + point2D2.getX() + " " + point2D2.getY());
//                System.out.println(line.getStartX() + " " + line.getStartY() + " " + line.getEndX() + " " + line.getEndY());
//                if ((line.getStartX() == x + nodeHeight /2 && line.getStartY() == y + nodeHeight /2)
//                || (line.getEndX() == x + nodeHeight/2 && line.getEndY() == y + nodeHeight/2))
//                {
//                    line.setVisible(false);
//                }
//            }
//        }
        Pane pane = (Pane) hBox.getParent();
        List<Line> lines = mapHBoxLine.get(pane);
        for (Line line : lines) {
            line.setVisible(false);
        }
    }

    void HBoxContainsLine(HBox hBox, Line line) {
        Point2D point2D = findCoordinate(hBox);
        double x = point2D.getX();
        double y = point2D.getY();
    }
}
