import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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

        // Draw the node
        gc.fillOval(x - 15, y - 15, 30, 30);
        gc.setFill(Color.WHITE);
        gc.fillText(Integer.toString(node.value), x - 5, y + 5);
        gc.setFill(Color.BLACK);

        // Calculate position and draw children
        final double childY = y + 50;
        int numChildren = node.children.size();
        for (int i = 0; i < numChildren; i++) {
            double childX = x - (horizontalSpace / 2 * (numChildren - 1)) + (horizontalSpace * i);
            // Draw line to the child node
            gc.strokeLine(x, y, childX, childY);
            // Recursively draw the child node
            drawNode(gc, node.children.get(i), childX, childY, horizontalSpace / 2);
        }
    }

    // Call this method to redraw the tree if it's been modified
    public void refresh() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        drawTree();
    }
}
