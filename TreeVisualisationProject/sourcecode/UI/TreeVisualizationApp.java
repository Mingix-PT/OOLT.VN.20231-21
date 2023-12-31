import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.util.Optional;

public class TreeVisualizationApp extends Application {
    private VBox toolPanel;
    private BorderPane root;
    private ComboBox<String> treeComboBox;
    private VBox comboBoxContainer;
    private MenuButton changeStructureButton;
    private Label currentStructureLabel;

    private GenericTree tree;
    private TreeCanvas treeCanvas;

    @Override
    public void start(Stage primaryStage) {
        // Khởi tạo GenericTree và TreeCanvas với kích thước mong muốn
        tree = new GenericTree();
        treeCanvas = new TreeCanvas(tree, 1200, 800);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(treeCanvas);
        scrollPane.setPrefSize(1200, 800); // Đặt kích thước ưa thích cho ScrollPane

        root = new BorderPane();

        // Khởi tạo MenuBar và các Menu
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> handleAbout());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        root.setTop(menuBar);

        root.setCenter(scrollPane); // Đặt ScrollPane chứa treeCanvas vào giữa BorderPane

        // Khởi tạo ComboBox và Label cho việc chọn cấu trúc dữ liệu
        treeComboBox = new ComboBox<>();
        String[] structures = {"Generic Tree", "Binary Search Tree (BST)", "Adelson-Velskii Landis Tree (AVL)", "Balanced Binary Tree"};
        treeComboBox.getItems().addAll(structures);
        Label selectLabel = new Label("Chọn cấu trúc dữ liệu:");
        comboBoxContainer = new VBox(10, selectLabel, treeComboBox);
        comboBoxContainer.setAlignment(Pos.TOP_CENTER);

        StackPane centerPane = new StackPane(comboBoxContainer);
        centerPane.setAlignment(Pos.CENTER);
        
        root.setCenter(centerPane);

        initializeToolPanel(structures);

        // Khởi tạo Label cho cấu trúc dữ liệu hiện tại nhưng không đặt văn bản mặc định
        currentStructureLabel = new Label("");
        currentStructureLabel.setAlignment(Pos.CENTER);
        HBox currentStructureContainer = new HBox(currentStructureLabel);
        currentStructureContainer.setAlignment(Pos.CENTER);
        
        VBox topContainer = new VBox(menuBar, currentStructureContainer);
        root.setTop(topContainer);

        primaryStage.setScene(new Scene(root, 1200, 800)); // Đặt kích thước Scene phù hợp với ScrollPane và Canvas
        primaryStage.show();

    // Set ComboBox action
    treeComboBox.setOnAction(e -> {
        String selectedStructure = treeComboBox.getValue();
        if (selectedStructure != null) {
            comboBoxContainer.setVisible(false);  // Ẩn comboBoxContainer
            handleStructureChange(selectedStructure);  // Tùy chỉnh toolPanel dựa trên cấu trúc được chọn
            root.setCenter(treeCanvas);  // Đặt treeCanvas làm trung tâm để hiển thị cây
            updateCurrentStructureLabel(selectedStructure);  // Cập nhật label
            treeCanvas.refresh();  // Làm mới treeCanvas để vẽ cây mới
        }
    });

        primaryStage.show();
        // Modify exitItem's action event
        exitItem.setOnAction(e -> confirmAndExit(primaryStage));

        // Handle window close request
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            confirmAndExit(primaryStage);
        });
    }

    private void initializeToolPanel(String[] structures) {
        toolPanel = new VBox(10);
        toolPanel.setAlignment(Pos.CENTER_LEFT);

        // Tạo và thêm MenuButton vào toolPanel
        changeStructureButton = new MenuButton("Đổi cấu trúc dữ liệu");
        for (String structure : structures) {
            MenuItem structureItem = new MenuItem(structure);
            structureItem.setOnAction(e -> handleStructureChange(structure));
            changeStructureButton.getItems().add(structureItem);
        }
        toolPanel.getChildren().add(changeStructureButton);

        // Thêm các nút khác vào toolPanel
        Button createButton = new Button("Create");
        Button insertButton = new Button("Insert");
        insertButton.setOnAction(e -> showInsertDialog());
        Button deleteButton = new Button("Delete");
        Button updateButton = new Button("Update");
        Button traverseButton = new Button("Traverse");
        Button searchButton = new Button("Search");
        toolPanel.getChildren().addAll(createButton, insertButton, deleteButton, updateButton, traverseButton, searchButton);

        toolPanel.setVisible(false);
    }

    private void handleStructureChange(String structure) {
        updateCurrentStructureLabel(structure); // Cập nhật label khi đổi cấu trúc
        System.out.println("Selected: " + structure);
    
        treeCanvas.reset(); // Reset TreeCanvas và GenericTree

        // Ẩn comboBoxContainer
        comboBoxContainer.setVisible(false);
    
        // Tùy chỉnh và hiển thị toolPanel dựa trên cấu trúc dữ liệu mới
        switch (structure) {
            case "Generic Tree":
                setupToolPanelForGenericTree();
                break;
            case "Binary Search Tree (BST)":
                setupToolPanelForBST();
                break;
            case "Adelson-Velskii Landis Tree (AVL)":
                setupToolPanelForAVL();
                break;
            case "Balanced Binary Tree":
                setupToolPanelForBalancedBinaryTree();
                break;
            default:
                // Xử lý trường hợp không có cấu trúc dữ liệu được chọn
                break;
        }
    
        // Hiển thị toolPanel
        root.setLeft(toolPanel);
        toolPanel.setVisible(true);
        root.setCenter(treeCanvas); // Đặt lại treeCanvas làm trung tâm để hiển thị trạng thái mới của cây
    }
    private void updateCurrentStructureLabel(String structure) {
        currentStructureLabel.setText("Cấu trúc dữ liệu hiện tại: " + structure);
    }

    private void setupToolPanelForGenericTree() {
        // Cấu hình toolPanel cho Generic Tree
        // Thêm/Xóa các nút cần thiết
    }
    
    private void setupToolPanelForBST() {
        // Cấu hình toolPanel cho Binary Search Tree
        // Thêm/Xóa các nút cần thiết
    }
    
    private void setupToolPanelForAVL() {
        // Cấu hình toolPanel cho Adelson-Velskii Landis Tree
        // Thêm/Xóa các nút cần thiết
    }
    
    private void setupToolPanelForBalancedBinaryTree() {
        // Cấu hình toolPanel cho Balanced Binary Tree
        // Thêm/Xóa các nút cần thiết
    }
    

    private void handleAbout() {
        // Handle About menu option
    }

    private void confirmAndExit(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }
private void showInsertDialog() {
    // Tạo một dialog mới
    Dialog<Pair<String, String>> dialog = new Dialog<>();
    dialog.setTitle("Insert Node");

    // Thêm nút OK và Cancel
    ButtonType insertButtonType = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(insertButtonType, ButtonType.CANCEL);

    // Tạo form nhập liệu
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    TextField parentValueField = new TextField();
    parentValueField.setPromptText("Parent Node Value");
    TextField childValueField = new TextField();
    childValueField.setPromptText("New Node Value");

    grid.add(new Label("Parent Node Value:"), 0, 0);
    grid.add(parentValueField, 1, 0);
    grid.add(new Label("New Node Value:"), 0, 1);
    grid.add(childValueField, 1, 1);

    dialog.getDialogPane().setContent(grid);

    // Convert the result to a pair when the insert button is clicked
    dialog.setResultConverter(dialogButton -> {
        if (dialogButton == insertButtonType) {
            return new Pair<>(parentValueField.getText(), childValueField.getText());
        }
        return null;
    });

    // Show the dialog and capture the result
    Optional<Pair<String, String>> result = dialog.showAndWait();

    result.ifPresent(parentChildPair -> {
        try {
            int parentValue = Integer.parseInt(parentChildPair.getKey());
            int childValue = Integer.parseInt(parentChildPair.getValue());
            tree.insert(parentValue, childValue);
            treeCanvas.refresh(); // Refresh the canvas to show the new node
        } catch (NumberFormatException ex) {
            showAlert("Invalid input", "Please enter valid integers for parent and child values.");
        }
    });
}

private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
    public static void main(String[] args) {
        launch(args);
    }
}
