import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tree.*;
import ui.controller.BSTController;

public class Main extends Application {
    private BinarySearchTree bst;

    @Override
    public void start(Stage stage) throws Exception {
        final String BST_FXML_FILE_PATH = "/ui/view/BST.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(BST_FXML_FILE_PATH));
        BSTController bstController = new BSTController();
        fxmlLoader.setController(bstController);
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
