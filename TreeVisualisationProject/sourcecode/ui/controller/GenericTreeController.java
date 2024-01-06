package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tree.GenericTree;

import java.io.IOException;

public class GenericTreeController {
    private int speed;
    private long timeDelaySet = 1020;
    private GenericTree tree;

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
    void chooseTraverse(ActionEvent event) {

    }

    @FXML
    void createRandomTree(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Random Tree");
        dialog.setHeaderText("Enter height of the tree");
        dialog.showAndWait();
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
    public void initialize() {
        final String NODE_FXML_FILE_PATH = "/ui/view/TreeNode.fxml";
        sliderSpeed.valueProperty().addListener((observable, oldValue, newValue) -> {
            speed = newValue.intValue();
            timeDelaySet = 1020 / speed;
            speedLabel.setText(speed + "x");
        });
        try {
            treeTypeLabel.setText("Generic Tree");
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
}
