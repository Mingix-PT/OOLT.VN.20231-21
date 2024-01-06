package ui.test;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import ui.test.GenericTree.GenericTreeNode;

public class TreeVisualizationApp extends Application {
    private GenericTree tree;
    private VBox toolPanel;
    private BorderPane root;
    private ComboBox<String> treeComboBox;
    private VBox comboBoxContainer;
    private MenuButton changeStructureButton;
    private Label currentStructureLabel;
    private Canvas canvas;
    private GraphicsContext gc;

    @Override
    public void start(Stage primaryStage) {
        tree = new GenericTree(10);

        root = new BorderPane(); // Khởi tạo root trước khi sử dụng

        // Tạo canvas
        canvas = new Canvas(1200, 800);
        gc = canvas.getGraphicsContext2D();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvas); // Thêm canvas vào ScrollPane
        scrollPane.setPrefSize(1200, 800); // Đặt kích thước ưa thích cho ScrollPane

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

        root.setCenter(scrollPane); // Đặt ScrollPane vào giữa BorderPane

        // Khởi tạo ComboBox và Label cho việc chọn cấu trúc dữ liệu
        treeComboBox = new ComboBox<>();
        String[] structures = { "Generic Tree", "Binary Search Tree (BST)", "Adelson-Velskii Landis Tree (AVL)",
                "Balanced Binary Tree" };
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

        primaryStage.setScene(new Scene(root, 1200, 800)); // Đặt kích thước Scene
        primaryStage.show();

        updateTreeVisualization(); // Vẽ cây khi ứng dụng khởi chạy

        // Set ComboBox action
        treeComboBox.setOnAction(e -> {
            String selectedStructure = treeComboBox.getValue();
            if (selectedStructure != null) {
                comboBoxContainer.setVisible(false); // Ẩn comboBoxContainer
                handleStructureChange(selectedStructure); // Tùy chỉnh toolPanel dựa trên cấu trúc được chọn
                root.setCenter(canvas); // Đặt treeCanvas làm trung tâm để hiển thị câ
                updateCurrentStructureLabel(selectedStructure); // Cập nhật label
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                updateTreeVisualization();

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

        changeStructureButton = new MenuButton("Đổi cấu trúc dữ liệu");
        for (String structure : structures) {
            MenuItem structureItem = new MenuItem(structure);
            structureItem.setOnAction(e -> handleStructureChange(structure));
            changeStructureButton.getItems().add(structureItem);
        }

        // Tạo nút mới cho mỗi chức năng
        Button createButton = new Button("Create");
        Button insertButton = new Button("Insert");
        insertButton.setOnAction(e -> showInsertDialog());
        Button deleteButton = new Button("Delete");
        Button updateButton = new Button("Update");
        Button traverseButton = new Button("Traverse");
        Button searchButton = new Button("Search");

        // Thêm tất cả các nút vào toolPanel một lần
        toolPanel.getChildren().addAll(changeStructureButton, createButton, insertButton, deleteButton, updateButton,
                traverseButton, searchButton);
    }

    private void handleStructureChange(String structure) {
        updateCurrentStructureLabel(structure); // Cập nhật label khi đổi cấu trúc
        System.out.println("Selected: " + structure);

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Insert Node");
        dialog.setHeaderText("Insert a new node");
        dialog.setContentText("Please enter the parent key and new key (separated by space):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(keys -> {
            String[] parts = keys.split(" "); // Tách chuỗi bằng dấu cách
            if (parts.length == 2) { // Kiểm tra xem có đúng hai phần tử không
                try {
                    int parentKey = Integer.parseInt(parts[0].trim());
                    int newKey = Integer.parseInt(parts[1].trim());
                    boolean inserted = tree.insert(parentKey, newKey);
                    if (inserted) {
                        // Cập nhật và vẽ lại cây
                        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Xóa canvas
                        updateTreeVisualization(); // Update the tree visualization
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Insert Error",
                                "Could not insert the node. Please check the keys.");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid integer values.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid Format",
                        "Input does not match the required format. Please enter two integers separated by a space.");
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateTreeVisualization() {
        tree.printTree();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Clear the canvas
        drawTree(gc, tree.getRoot(), canvas.getWidth() / 2, 40, canvas.getWidth() / 4); // Redraw the tree
    }

    private void drawTree(GraphicsContext gc, GenericTreeNode node, double x, double y, double horizontalSpacing) {
        if (node == null) return;
    
        // Kích thước của vòng tròn
        double radius = 20;
    
        // Vẽ vòng tròn với viền đậm
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3); // Độ đậm của viền
        gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
    
        // Văn bản nằm ở giữa vòng tròn
        String text = Integer.toString(node.key);
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(17)); // Cài đặt font mới với kích thước đã tăng
        double textWidth = text.length() * 7; // Ước lượng chiều rộng của văn bản
        double textHeight = 20; // Ước lượng chiều cao của văn bản
        gc.fillText(text, x - textWidth / 2, y + textHeight / 4);
    
        // Tính toán vị trí và vẽ các nút con
        final double childY = y + 80; // Khoảng cách dọc giữa các nút
        int numChildren = node.children.size();
        
        // Tăng khoảng cách ngang giữa các nút con
        double newHorizontalSpace = horizontalSpacing * (numChildren + 1);
    
        for (int i = 0; i < numChildren; i++) {
            double childX = x - (newHorizontalSpace / 2) + newHorizontalSpace / (numChildren + 1) * (i + 1);
    
            // Tính tọa độ kết thúc của đường nối sao cho nó chỉ đến viền của đường tròn
            double lineEndX = childX;
            double lineEndY = childY - radius; // Điều chỉnh để đường nối chỉ đến viền trên cùng của đường tròn dưới
    
            // Vẽ đường nối đến viền của nút con
            gc.strokeLine(x, y + radius, lineEndX, lineEndY);
    
            // Vẽ đệ quy nút con
            drawTree(gc, node.children.get(i), childX, childY, horizontalSpacing / 2);
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}