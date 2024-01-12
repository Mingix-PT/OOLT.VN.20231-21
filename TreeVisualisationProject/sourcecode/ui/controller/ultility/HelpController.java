package ui.controller.ultility;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.*;
import javafx.stage.Stage;
import ui.controller.v1.MenuController;
import ui.controller.v2.MenuControllerV2;

import java.io.IOException;

import static javafx.scene.paint.Color.*;

public class HelpController {

    @FXML
    private Button backButton;
    @FXML
    private TextFlow textFlow;

    @FXML
    private void backToMainMenu(ActionEvent event) {
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
    private void initialize() {
        String textString = "This is a help page for the application.\n" +
                "\n" +
                "The application is a tool for visualizing the operations of various tree data structures.\n" +
                "\n" +
                "The application supports the following tree data structures:\n" +
                "\n" +
                "\t1. Generic Tree\n" +
                "\t2. Binary Search Tree\n" +
                "\t3. AVL Tree\n" +
                "\t4. Complete Balance Binary Search Tree\n" +
                "\n" +
                "The application supports the following operations:\n" +
                "\n" +
                "\t1. Insert\n" +
                "\t2. Delete\n" +
                "\t3. Search\n" +
                "\t4. Traverse (DFS & BFS)\n" +
                "\t5. Update (only for Generic Tree)\n" +
                "\t6. Randomly generate a tree with a given height\n" +
                "\n" +
                "The application supports the following features:\n" +
                "\n" +
                "\t1. Changing the speed of the visualization.\n" +
                "\t2. Undo the operation.\n" +
                "\t3. Redo the operation.\n" +
                "\n" +
                "Help Button to open this help page.\n" +
                "Quit Button to quit the application.\n" +
                "Back Button on every left corner to go back to the main menu.\n";
        Text text = new Text(textString);
        text.setFill(LIGHTGREEN);
        text.setFont(Font.font("Verdana", 16));
        textFlow.getChildren().add(text);
    }
}
