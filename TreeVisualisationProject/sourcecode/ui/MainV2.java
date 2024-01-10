package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.ultility.MenuController;
import ui.controller.v2.MenuControllerV2;

public class MainV2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final String MENU_FXML_FILE_PATH = "/ui/view/Menu.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(MENU_FXML_FILE_PATH));
        MenuControllerV2 menuController = new MenuControllerV2();
        fxmlLoader.setController(menuController);
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Tree Visualisation Application");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
