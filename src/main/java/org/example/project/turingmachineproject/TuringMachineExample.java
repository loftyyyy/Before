package org.example.project.turingmachineproject;

import java.util.*;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class TuringMachineExample extends Application {

    private List<StackPane> tapeCells = new ArrayList<>();
    private Polygon head = new Polygon();
    private int currentIndex = 0;

    @Override
    public void start(Stage primaryStage) {
        HBox tapePane = new HBox(0); // Tape container
        Pane tapeContainer = new Pane(tapePane); // Tape and head container

        char[] tapeSymbols = {'1', '1', '1', '+', '1', '1'};
        StackPane emptyCell = createTapeCell(' ');
        tapeCells.add(createTapeCell(' '));
        tapePane.getChildren().add(emptyCell);

        for (char symbol : tapeSymbols) {
            StackPane cell = createTapeCell(symbol);
            tapeCells.add(cell);
            tapePane.getChildren().add(cell);
        }
        StackPane endEmptyCell = createTapeCell(' ');
        tapeCells.add(createTapeCell(' '));
        tapePane.getChildren().add(endEmptyCell);

        // Create the head (triangle) and place it above the first tape cell
        head.getPoints().addAll(0.0, 0.0, 10.0, 20.0, -10.0, 20.0);
        head.setFill(Color.RED);
        tapeContainer.getChildren().add(head);
        head.setTranslateX(tapeCells.get(0).getLayoutX());
        head.setTranslateY(-40);

        // Scene setup
        Scene scene = new Scene(tapeContainer, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Move the head to the right
        moveHead(3); // Move to the fourth cell (index 3)
    }

    private StackPane createTapeCell(char symbol) {
        StackPane cell = new StackPane();
        Rectangle rect = new Rectangle(40, 40);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        Label label = new Label(Character.toString(symbol));
        cell.getChildren().addAll(rect, label);
        return cell;
    }

    private void moveHead(int newIndex) {
        double newPositionX = tapeCells.get(newIndex).getLayoutX();
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.5), head);
        move.setToX(newPositionX);
        move.play();
        currentIndex = newIndex;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
