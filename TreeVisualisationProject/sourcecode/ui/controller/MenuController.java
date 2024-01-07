package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.*;

import java.io.IOException;

public class MenuController {

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
        final String BST_FXML_FILE_PATH = "/ui/view/BST.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(BST_FXML_FILE_PATH));
        AVLController avlController = new AVLController();
        fxmlLoader.setController(avlController);
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.setTitle("AVL Tree");
        stage.show();
    }

    @FXML
    void openBST(ActionEvent event) throws IOException {
        final String BST_FXML_FILE_PATH = "/ui/view/BST.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(BST_FXML_FILE_PATH));
        BSTController bstController = new BSTController();
        fxmlLoader.setController(bstController);
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.setTitle("Binary Search Tree");
        stage.show();
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
        final String BST_FXML_FILE_PATH = "/ui/view/BST.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(BST_FXML_FILE_PATH));
        CBBSTController cbBSTController = new CBBSTController();
        fxmlLoader.setController(cbBSTController);
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.setTitle("Complete Balance Binary Search Tree");
        stage.show();
    }

    @FXML
    void openGenericTree(ActionEvent event) throws IOException {
        final String BST_FXML_FILE_PATH = "/ui/view/BST.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(BST_FXML_FILE_PATH));
        GenericTreeController genericTreeController = new GenericTreeController();
        fxmlLoader.setController(genericTreeController);
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(parent));
        stage.setTitle("Generic Tree");
        stage.show();
    }

}
