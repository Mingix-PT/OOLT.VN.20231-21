package ui.controller.v2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.*;
import tree.type.*;
import ui.controller.ultility.HelpController;

import java.io.IOException;

public class MenuControllerV2 {
    private Tree tree;

    @FXML
    private Button avlButton;

    @FXML
    private Button bstButton;

    @FXML
    private Button cbbstButton;

    @FXML
    private Button genericTreeButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button quitButton;

    @FXML
    void confirmQuit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("Press OK to quit, or press Cancel to return.");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void openAVL(ActionEvent event) throws IOException {
        tree = new AVLTree();
        loadTreeMenu(event, tree);
    }

    @FXML
    void openBST(ActionEvent event) throws IOException {
        tree = new BinarySearchTree();
        loadTreeMenu(event, tree);
    }

    @FXML
    void openHelp(ActionEvent event) throws IOException {
        final String BST_FXML_FILE_PATH = "/ui/view/HelpMenu.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(BST_FXML_FILE_PATH));
        HelpController helpController = new HelpController();
        fxmlLoader.setController(helpController);
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.setTitle("Help Menu");
        stage.show();
    }

    @FXML
    void openCBBST(ActionEvent event) throws IOException {
        tree = new CompleteBalanceBinarySearchTree();
        loadTreeMenu(event, tree);
    }

    @FXML
    void openGenericTree(ActionEvent event) throws IOException {
        tree = new GenericTree();
        loadTreeMenu(event, tree);
    }

    void loadTreeMenu(ActionEvent event, Tree tree) throws IOException {
        final String BST_FXML_FILE_PATH = "/ui/view/TreeMenu.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(BST_FXML_FILE_PATH));
        ControllerV2 controllerV2 = new ControllerV2(tree);
        fxmlLoader.setController(controllerV2);
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.setTitle(getClass(tree));
        stage.show();
    }

    public static String getClass(Tree tree) {
        if (tree instanceof GenericTree) {
            return "Generic Tree";
        }
        else if (tree instanceof AVLTree) {
            return "AVL Tree";
        }
        else if (tree instanceof BinarySearchTree) {
            return "Binary Search Tree";
        }
        else {
            return "Complete Balance Binary Search Tree";
        }
    }

}
