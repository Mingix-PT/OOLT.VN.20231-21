package ui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tree.type.CompleteBalanceBinarySearchTree;
import ui.controller.v1.MenuController;

public class Main extends Application {
    private CompleteBalanceBinarySearchTree tree;

    @Override
    public void start(Stage stage) throws Exception {
        final String BST_FXML_FILE_PATH = "/ui/view/BST.fxml";
        final String MENU_FXML_FILE_PATH = "/ui/view/Menu.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(MENU_FXML_FILE_PATH));
        MenuController menuController = new MenuController();
        fxmlLoader.setController(menuController);
//        CBBSTController cbBSTController = new CBBSTController();
//        fxmlLoader.setController(cbBSTController);
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Tree Visualisation");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
