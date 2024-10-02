package org.example.project.turingmachineproject;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Arrow extends Polygon {
    private Line line;

    public Arrow(double startX, double startY, double endX, double endY) {
        // Create the main line of the arrow
        line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.BLACK);

        double arrowLength = 10; // Length of the arrowhead
        double arrowWidth = 5;   // Width of the arrowhead

        double angle = Math.atan2(endY - startY, endX - startX);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        // Coordinates for arrowhead points
        getPoints().addAll(
                endX, endY,
                endX - arrowLength * cos + arrowWidth * sin, endY - arrowLength * sin - arrowWidth * cos,
                endX - arrowLength * cos - arrowWidth * sin, endY - arrowLength * sin + arrowWidth * cos
        );

        setFill(Color.BLACK);
    }

    public Line getLine() {
        return line;
    }
}
