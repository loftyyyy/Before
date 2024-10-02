package org.example.project.turingmachineproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.scene.layout.Pane;

public class TuringMachineAdditionController {
    @FXML
    private HBox TapeCells;

    @FXML
    private Label result;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private MFXTextField inputField1;
    @FXML
    private MFXTextField inputField2;

    @FXML
    private MFXButton startButton;

    @FXML
    private Pane diagramPane;
    private TuringMachineLogic turingMachineLogic;
    private Diagram stateDiagram;

    @FXML
    public void initialize() {
        stateDiagram = new Diagram();
        turingMachineLogic = new TuringMachineLogic(TapeCells, scrollPane, diagramPane, stateDiagram);

    }

    @FXML
    public void generateTape() {
        turingMachineLogic.createInputTape(inputField1.getText(), inputField2.getText());
    }

    @FXML
    public void startAutomaticMovement() {
        turingMachineLogic.startAutomaticCursorMovement();
    }

    @FXML
    public void stopAutomaticMovement() {
        turingMachineLogic.stopAutomaticCursorMovement();
    }
}
