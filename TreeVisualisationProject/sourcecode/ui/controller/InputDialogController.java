package ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InputDialogController {

    @FXML
    private Button btnConfirm;

    @FXML
    private Label lbInput;

    @FXML
    private TextField tfInput;

    @FXML
    void checkInput(ActionEvent event) {

    }

    void setLabel(String label) {
        lbInput.setText(label);
    }

}
