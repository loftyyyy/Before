package org.example.project.turingmachineproject;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;

public class TransitionLines {
    private Pane pane;
    private int radius;

    public TransitionLines(Pane pane, int radius){
        this.pane = pane;
        this.radius = radius;
    }




    public void addInitialStateArrow(StateNode initialState) {

        // Get the center coordinates of the initial state node
        double centerX = initialState.getTranslateX() + radius; // Center X
        double centerY = initialState.getTranslateY() + radius; // Center Y

        // Define the starting point for the arrow to be on the left of the initial state node
        double startX = centerX - 100; // Start 100 pixels to the left of the state
        double startY = centerY;       // Same Y-coordinate as the center of the state

        // Calculate the angle between the starting point and the center of the circle
        double angle = Math.atan2(centerY - startY, centerX - startX);

        // Adjust the end point to be at the edge of the circle, not the center
        double endX = centerX - radius * Math.cos(angle);
        double endY = centerY - radius * Math.sin(angle);

        // Create the arrow line that points to the edge of the circle
        Arrow arrow = new Arrow(startX, startY, endX, endY);
        pane.getChildren().addAll(arrow.getLine(), arrow);

        // Now create the arrowhead
        double directionX = endX - startX;
        double directionY = endY - startY;
        double length = Math.sqrt(directionX * directionX + directionY * directionY);

        // Normalize the direction vector for the arrowhead
        double unitArrowX = directionX / length;
        double unitArrowY = directionY / length;

        // Add the arrowhead at the end of the arrow (at the edge of the circle)
        addArrowhead(endX, endY, unitArrowX, unitArrowY);
    }


    public void addArrowBetweenStates(StateNode source, StateNode target, String labelText) {
        double startX = source.getTranslateX() + 30; // Outer circle radius is 30
        double startY = source.getTranslateY() + 30;
        double endX = target.getTranslateX() + 30;
        double endY = target.getTranslateY() + 30;

        double angle = Math.atan2(endY - startY, endX - startX);

        // Adjust for the radius of the outer circle
        double startAdjustX = startX + 30 * Math.cos(angle); // Adjust by radius
        double startAdjustY = startY + 30 * Math.sin(angle);
        double endAdjustX = endX - 30 * Math.cos(angle);     // Adjust by radius
        double endAdjustY = endY - 30 * Math.sin(angle);

        // Create the arrow
        Arrow arrow = new Arrow(startAdjustX, startAdjustY, endAdjustX, endAdjustY);
        pane.getChildren().addAll(arrow.getLine(), arrow);

        // Calculate the midpoint of the line (for the label)
        double midX = (startAdjustX + endAdjustX) / 2;
        double midY = (startAdjustY + endAdjustY) / 2;

        // Create a label at the midpoint
        Label label = new Label(labelText);
        label.setStyle("-fx-padding: 2px; -fx-font-weight: bold; -fx-font-size: 15");

        // Add the label to the pane first, so we can calculate its size
        pane.getChildren().add(label);

        // Use Platform.runLater to ensure proper layout after rendering
        Platform.runLater(() -> {
            // Get label bounds after rendering
            Bounds labelBounds = label.getBoundsInParent();
            double labelWidth = labelBounds.getWidth();
            double labelHeight = labelBounds.getHeight();

            // Center the label horizontally at the midpoint, place slightly above the line
            label.setLayoutX(midX - labelWidth / 2);
            label.setLayoutY(midY - labelHeight - 5); // Adjust 5px above the line
        });
    }


    public void addLoopArrow(StateNode state, double controlOffsetX, double controlOffsetY, String labelText, int spaceAbove) {

        // Get the center coordinates of the state node
        double centerX = state.getTranslateX() + radius; // Center X
        double centerY = state.getTranslateY() + radius; // Center Y

        // Define the starting and ending points for the loop (slightly off to the right and left of the top)
        double startX = centerX + radius / 2; // Start a little to the right of the top
        double startY = centerY - radius;     // Top position (Y is decreased by radius)
        double endX = centerX - radius / 2;   // End a little to the left of the top
        double endY = centerY - radius;       // Top position (Y is decreased by radius)

        // Calculate control points for a more rounded loop
        double controlX1 = centerX;           // Control point X to make the peak vertical
        double controlY1 = startY - controlOffsetY * 2.5; // Adjusted to make the peak rounder

        // Create the cubic curve for the loop arrow
        CubicCurve loopCurve = new CubicCurve();
        loopCurve.setStartX(startX);
        loopCurve.setStartY(startY);
        loopCurve.setControlX1(controlX1);
        loopCurve.setControlY1(controlY1);
        loopCurve.setControlX2(controlX1);  // Using the same control point for both
        loopCurve.setControlY2(controlY1);
        loopCurve.setEndX(endX);
        loopCurve.setEndY(endY);

        loopCurve.setStroke(Color.BLACK);
//        loopCurve.setStrokeWidth(2);
        loopCurve.setFill(null);

        // Add the loop curve to the pane
        pane.getChildren().add(loopCurve);

        // Add label at the peak of the loop (control point)
        Label label = new Label(labelText);
        label.setStyle("-fx-padding: 2px; -fx-font-weight: bold; -fx-font-size: 15");

        // Add the label to the pane first, so we can calculate its size
        pane.getChildren().add(label);

        // Use Platform.runLater to ensure proper layout after rendering
        Platform.runLater(() -> {
            // Get label bounds after rendering
            Bounds labelBounds = label.getBoundsInParent();
            double labelWidth = labelBounds.getWidth();
            double labelHeight = labelBounds.getHeight();

            // Center the label horizontally at the control point, adjust it closer to the peak
            label.setLayoutX(controlX1 - labelWidth / 2);
            label.setLayoutY(controlY1 - labelHeight / spaceAbove); // Reduce vertical offset to place it closer to the peak
        });

        // Calculate the direction vector for the arrowhead
        double directionX = endX - controlX1; // Difference in X
        double directionY = endY - controlY1; // Difference in Y

        // Normalize the direction vector to get the unit vector for the arrowhead
        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        double unitArrowX = directionX / length;
        double unitArrowY = directionY / length;

        // Create the arrowhead with the correct unit vector
        addArrowhead(endX, endY, unitArrowX, unitArrowY);
    }


    public void addLoopArrowBelow(StateNode state, double controlOffsetX, double controlOffsetY, String labelText, int spaceAbove) {

        // Get the center coordinates of the state node
        double centerX = state.getTranslateX() + radius; // Center X
        double centerY = state.getTranslateY() + radius; // Center Y

        // Define the starting and ending points for the loop (slightly off to the right and left of the bottom)
        double startX = centerX + radius / 2; // Start a little to the right of the bottom
        double startY = centerY + radius;     // Bottom position (Y is increased by radius)
        double endX = centerX - radius / 2;   // End a little to the left of the bottom
        double endY = centerY + radius;       // Bottom position (Y is increased by radius)

        // Calculate control points for a more rounded loop
        double controlX1 = centerX;           // Control point X to make the peak vertical
        double controlY1 = startY + controlOffsetY * 2.5; // Adjusted to make the bottom loop rounder

        // Create the cubic curve for the loop arrow
        CubicCurve loopCurve = new CubicCurve();
        loopCurve.setStartX(startX);
        loopCurve.setStartY(startY);
        loopCurve.setControlX1(controlX1);
        loopCurve.setControlY1(controlY1);
        loopCurve.setControlX2(controlX1);  // Using the same control point for both
        loopCurve.setControlY2(controlY1);
        loopCurve.setEndX(endX);
        loopCurve.setEndY(endY);

        loopCurve.setStroke(Color.BLACK);
        loopCurve.setFill(null);

        // Add the loop curve to the pane
        pane.getChildren().add(loopCurve);

        // Add label at the peak of the loop (control point)
        Label label = new Label(labelText);
        label.setStyle("-fx-padding: 2px; -fx-font-weight: bold; -fx-font-size: 15");

        // Add the label to the pane first, so we can calculate its size
        pane.getChildren().add(label);

        // Use Platform.runLater to ensure proper layout after rendering
        Platform.runLater(() -> {
            // Get label bounds after rendering
            Bounds labelBounds = label.getBoundsInParent();
            double labelWidth = labelBounds.getWidth();
            double labelHeight = labelBounds.getHeight();

            // Center the label horizontally at the control point, adjust it closer to the peak
            label.setLayoutX(controlX1 - labelWidth / 2);
            label.setLayoutY(controlY1 - labelHeight / spaceAbove); // Reduce vertical offset to place it closer to the peak
        });

        // Calculate the direction vector for the arrowhead
        double directionX = endX - controlX1; // Difference in X
        double directionY = endY - controlY1; // Difference in Y

        // Normalize the direction vector to get the unit vector for the arrowhead
        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        double unitArrowX = directionX / length;
        double unitArrowY = directionY / length;

        // Create the arrowhead with the correct unit vector
        addArrowhead(endX, endY, unitArrowX, unitArrowY);
    }


    public void addBelowCurvedArrowBetweenStates(StateNode source, StateNode target, double controlOffsetX, double controlOffsetY, String labelText) {

        // Get the center coordinates of the source and target nodes
        double startX = source.getTranslateX() + radius; // Start at the bottom of the source circle
        double startY = source.getTranslateY() + radius; // Bottom of the circle (Y-coordinate is translateY + radius)
        double endX = target.getTranslateX() + radius; // End at the bottom of the target circle
        double endY = target.getTranslateY() + radius; // Bottom of the circle

        // Adjust the starting and ending Y-coordinates to be at the bottom of the circles
        startY += 34; // Add a small offset to place the start point below the bottom edge
        endY += 34; // Add a small offset to place the end point below the bottom edge

        // Calculate the vector from the source to the target
        double deltaX = endX - startX;
        double deltaY = endY - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Normalize the vector
        double unitX = deltaX / distance;
        double unitY = deltaY / distance;

        // Calculate control points for a smoother curve
        double controlX1 = (startX + endX) / 2 + controlOffsetX;
        double controlY1 = (startY + endY) / 2 + controlOffsetY;

        // Adjust the control point to create a convex curve below the line connecting the two states
        controlY1 += 30; // Adjust the control point downwards to create a convex effect

        // Create the cubic curve for the arrow body
        CubicCurve curve = new CubicCurve();
        curve.setStartX(startX);
        curve.setStartY(startY);
        curve.setControlX1(controlX1);
        curve.setControlY1(controlY1);
        curve.setControlX2(controlX1);  // Using the same control point for both
        curve.setControlY2(controlY1);
        curve.setEndX(endX);
        curve.setEndY(endY);

        curve.setStroke(Color.BLACK);
//        curve.setStrokeWidth(2);
        curve.setFill(null);

        // Add the curve to the pane
        pane.getChildren().add(curve);

        // Add label above the curve's peak (which is near the control point)
        Label label = new Label(labelText);
        label.setStyle("-fx-padding: 2px; -fx-font-weight: bold; -fx-font-size: 15");


        // Position the label slightly above the control point
        label.setLayoutX(controlX1 - label.getWidth() / 2); // Center the label horizontally
        label.setLayoutY(controlY1 - 30); // Adjust the vertical position (20 pixels above the control point)

        // Add the label to the pane
        pane.getChildren().add(label);


        // Calculate the direction vector from the last control point to the end point
        double directionX = endX - controlX1; // Difference in X
        double directionY = endY - controlY1; // Difference in Y

        // Normalize the direction vector to get the unit vector for the arrowhead
        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        double unitArrowX = directionX / length;
        double unitArrowY = directionY / length;

        // Now, create the arrowhead with the correct unit vector
        addArrowhead(endX, endY, unitArrowX, unitArrowY);
    }



    // Method to add a curved arrow between two states, with the arrow starting and ending at the edges of the circles
    public void addCurvedArrowBetweenStates(StateNode source, StateNode target, double controlOffsetX, double controlOffsetY, String labelText) {

        // Get the center coordinates of the source and target nodes
        double startX = source.getTranslateX() + radius; // Start at the top of the source circle
        double startY = source.getTranslateY() - 2;      // Top of the circle
        double endX = target.getTranslateX() + radius;   // End at the top of the target circle
        double endY = target.getTranslateY() - 2;        // Top of the circle

        // Calculate the vector from the source to the target
        double deltaX = endX - startX;
        double deltaY = endY - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Normalize the vector
        double unitX = deltaX / distance;
        double unitY = deltaY / distance;

        // Calculate control points for a smoother curve
        double controlX1 = (startX + endX) / 2 + controlOffsetX;
        double controlY1 = (startY + endY) / 2 + controlOffsetY;

        // Create the cubic curve for the arrow body
        CubicCurve curve = new CubicCurve();
        curve.setStartX(startX);
        curve.setStartY(startY);
        curve.setControlX1(controlX1);
        curve.setControlY1(controlY1);
        curve.setControlX2(controlX1);  // Using the same control point for both
        curve.setControlY2(controlY1);
        curve.setEndX(endX);
        curve.setEndY(endY);

        curve.setStroke(Color.BLACK);
//        curve.setStrokeWidth(2);
        curve.setFill(null);

        // Add the curve to the pane
        pane.getChildren().add(curve);

        // Add label at the peak of the curve (control point)
        Label label = new Label(labelText);
        label.setStyle("-fx-padding: 2px; -fx-font-weight: bold; -fx-font-size: 15");

        // Add the label to the pane first, so we can calculate its size
        pane.getChildren().add(label);

        // Use Platform.runLater to ensure proper layout after rendering
        Platform.runLater(() -> {
            // Get label bounds after rendering
            Bounds labelBounds = label.getBoundsInParent();
            double labelWidth = labelBounds.getWidth();
            double labelHeight = labelBounds.getHeight();

            // Center the label horizontally at the control point, adjust it closer to the peak
            label.setLayoutX(controlX1 - labelWidth / 2);
            label.setLayoutY(controlY1 - labelHeight / 100); // Reduce the vertical offset, placing it closer to the peak
        });

        // Calculate the direction vector from the control point to the end point
        double directionX = endX - controlX1; // Difference in X
        double directionY = endY - controlY1; // Difference in Y

        // Normalize the direction vector to get the unit vector for the arrowhead
        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        double unitArrowX = directionX / length;
        double unitArrowY = directionY / length;

        // Now, create the arrowhead with the correct unit vector
        addArrowhead(endX, endY, unitArrowX, unitArrowY);
    }








    // Helper method to create an arrowhead at the end of the curve
    private void addArrowhead(double endX, double endY, double unitX, double unitY) {
        double arrowLength = 10; // Length of the arrowhead
        double arrowWidth = 5;   // Width of the arrowhead

        // Calculate the angle based on the unit vector
        double angle = Math.atan2(unitY, unitX);

        // Calculate the positions for the arrowhead points
        Polygon arrowHead = new Polygon();
        arrowHead.getPoints().addAll(
                endX, endY, // tip of the arrow
                endX - arrowLength * Math.cos(angle - Math.PI / 6), // Left point
                endY - arrowLength * Math.sin(angle - Math.PI / 6),
                endX - arrowLength * Math.cos(angle + Math.PI / 6), // Right point
                endY - arrowLength * Math.sin(angle + Math.PI / 6)
        );

        arrowHead.setFill(Color.BLACK);

        // Add the arrowhead to the pane
        pane.getChildren().add(arrowHead);
    }
}
