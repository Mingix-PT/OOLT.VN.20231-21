package ui.controller.ultility;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class TreeNodeController {

    @FXML
    private HBox hBox;
    @FXML
    private Label treeNodeKey;

    public void setKey(int key) {
        treeNodeKey.setText(String.valueOf(key));
    }

    public void highlight() {
        hBox.setStyle("-fx-background-color: #ff0000");
        treeNodeKey.setStyle("-fx-text-fill: #ff0000");
    }
}
