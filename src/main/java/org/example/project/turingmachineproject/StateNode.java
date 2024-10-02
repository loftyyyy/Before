package org.example.project.turingmachineproject;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class StateNode extends StackPane {
    Circle innerCircle;
    Circle outerCircle;
    Text label;

    public StateNode(boolean isFinalState, String stateName, double x, double y) {
        outerCircle = new Circle(30);
        outerCircle.setFill(Color.LIGHTBLUE);
        outerCircle.setStroke(Color.BLACK);

        label = new Text(stateName);
        setTranslateX(x);
        setTranslateY(y);

        if (isFinalState) {
            innerCircle = new Circle(25);
            innerCircle.setFill(Color.LIGHTBLUE);
            innerCircle.setStroke(Color.BLACK);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(outerCircle, innerCircle);
            getChildren().addAll(stackPane, label);
        } else {
            getChildren().addAll(outerCircle, label);
        }
    }

    public void highlight(boolean isFinalState) {
        outerCircle.setFill(Color.YELLOW);
        if (isFinalState && innerCircle != null) {
            innerCircle.setFill(Color.YELLOW);
        }
    }

    public void resetColor(boolean isFinalState) {
        outerCircle.setFill(Color.LIGHTBLUE);
        if (isFinalState && innerCircle != null) {
            innerCircle.setFill(Color.LIGHTBLUE);
        }
    }
}
