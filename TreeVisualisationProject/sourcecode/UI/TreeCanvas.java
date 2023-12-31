import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TreeCanvas extends Canvas {
    private GenericTree tree;

    public TreeCanvas(GenericTree tree) {
        this.tree = tree;
        drawTree();
    }
    // Trong lớp TreeCanvas
public void reset() {
    getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight()); // Xóa Canvas
    tree.reset(); // Reset cấu trúc dữ liệu cây
}


    private void drawTree() {
        if (tree.root == null) return;

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        drawNode(gc, tree.root, getWidth() / 2, 50, getWidth() / 4);
    }

private void drawNode(GraphicsContext gc, Node node, double x, double y, double horizontalSpace) {
    if (node == null) return;

    // Kích thước của vòng tròn
    double radius = 20;

    // Vẽ vòng tròn với viền đậm
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(2); // Độ đậm của viền
    gc.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);

    // Văn bản nằm ở giữa vòng tròn
    String text = Integer.toString(node.value);
    gc.setFill(Color.BLACK);
    // Giả định font size là 12 để tính toán
    gc.setFont(new Font(12));
    double textWidth = text.length() * 6; // Ước lượng chiều rộng của văn bản
    double textHeight = 12; // Ước lượng chiều cao của văn bản
    gc.fillText(text, x - textWidth / 2, y + textHeight / 4);

    // Tính toán vị trí và vẽ các nút con
    final double childY = y + 80; // Tăng khoảng cách giữa các nút
    int numChildren = node.children.size();
    for (int i = 0; i < numChildren; i++) {
        double childX = x - (horizontalSpace / 2 * (numChildren - 1)) + (horizontalSpace * i);

        // Tính tọa độ kết thúc của đường nối sao cho nó chỉ đến viền của đường tròn
        double lineEndX = childX;
        double lineEndY = childY - radius; // Điều chỉnh để đường nối chỉ đến viền trên cùng của đường tròn dưới

        // Vẽ đường nối đến viền của nút con
        gc.strokeLine(x, y + radius, lineEndX, lineEndY);

        // Vẽ đệ quy nút con
        drawNode(gc, node.children.get(i), childX, childY, horizontalSpace / 2);
    }
}


    // Call this method to redraw the tree if it's been modified
    public void refresh() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        drawTree();
    }
}
