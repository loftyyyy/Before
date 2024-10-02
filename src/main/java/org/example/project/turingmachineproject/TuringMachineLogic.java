package org.example.project.turingmachineproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TuringMachineLogic {
    private List<StackPane> tapeCells = new ArrayList<>();
    private String cellString = "";
    private int currentIndex = 0;
    private HBox tape;
    private boolean isValid = true;
    private ScrollPane scrollPane;
    private Timeline timeline;
    private Diagram stateDiagram;
    private TransitionLines transitionLines;
    private Pane diagram;

    public TuringMachineLogic(HBox tape, ScrollPane scrollPane, Pane diagram, Diagram stateDiagram) {
        this.tape = tape;
        this.scrollPane = scrollPane;
        this.stateDiagram = stateDiagram;
        this.diagram = diagram;

        transitionLines = new TransitionLines(diagram, 30);


        this.diagram.getChildren().addAll(stateDiagram.getState("q0"),stateDiagram.getState("q1"), stateDiagram.getState("q2"), stateDiagram.getState("q3"), stateDiagram.getState("q4"));

        //Initial Line
        transitionLines.addInitialStateArrow(stateDiagram.getState("q0"));

        // Linear Line
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q0"),stateDiagram.getState("q1"),"B, 0 / R");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q1"),stateDiagram.getState("q2"),"B, B / L");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q2"),stateDiagram.getState("q3"),"0, B / L");
        transitionLines.addArrowBetweenStates(stateDiagram.getState("q3"),stateDiagram.getState("q4"),"0, B / L");

        //Loop Line
        transitionLines.addLoopArrow(stateDiagram.getState("q0"),30,50,"0, 0 / R",20000);
        transitionLines.addLoopArrow(stateDiagram.getState("q1"),30,50,"0, 0 / R",20000);







    }


    public void createInputTape(String input, String input2) {
        char[] inputChars = input.toCharArray();
        char[] input2Chars = input2.toCharArray();

        // Generate Empty Cells
        StackPane emptyCell = createTapeCell('B');
        cellString += "B";
        tapeCells.add(emptyCell);
        tape.getChildren().add(emptyCell);

        for (char s : inputChars) {
            cellString += Character.toString(s);
            StackPane cell = createTapeCell(s);
            tapeCells.add(cell);
            tape.getChildren().add(cell);
        }
        StackPane separator3 = createTapeCell('B');
        tapeCells.add(separator3);
        cellString += "B";
        tape.getChildren().add(separator3);

        for (char s : input2Chars) {
            StackPane cell = createTapeCell(s);
            cellString += Character.toString(s);
            tapeCells.add(cell);
            tape.getChildren().add(cell);
        }
        StackPane separator2 = createTapeCell('B');
        cellString += "B";
        tapeCells.add(separator2);
        tape.getChildren().add(separator2);

    }

    public StackPane createTapeCell(char symbol) {
        StackPane cell = new StackPane();
        Rectangle rect = new Rectangle(90, 90);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        rect.setStrokeType(StrokeType.OUTSIDE);
        rect.setStrokeWidth(1);
        Label label = new Label(Character.toString(symbol));
        cell.getChildren().addAll(rect, label);
        return cell;
    }

    // Automatically move the cursor to the next cell in a given interval
    public void startAutomaticCursorMovement() {
//        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
//            moveCursorRight();
//        }));
//        timeline.setCycleCount(tapeCells.size());
//        timeline.play();

        runSimulation(cellString);
    }

    public void stopAutomaticCursorMovement() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public String getNextState(String input) {
        String currentState = stateDiagram.getCurrentState();

        if (currentState.equals("q0")) {
            if (input.equals("1")) {
                // Stay in q0 and move right
                moveCursorRight(); // Move cursor
                return "q0";
            } else if (input.equals("B")) {
                // Write 1, move right, transition to q1
                writeToCurrentCell('1', currentIndex);
                moveCursorRight(); // Move cursor
                return "q1";
            }
        } else if (currentState.equals("q1")) {
            if (input.equals("1")) {
                // Stay in q1 and move right
                moveCursorRight(); // Move cursor
                return "q1";
            } else if (input.equals("B")) {
                // Stay in q1, move left, transition to q2
                moveCursorLeft(); // Move cursor
                return "q2";
            }
        } else if (currentState.equals("q2")) {
            if (input.equals("1")) {
                // Write B, move left, transition to q3
                writeToCurrentCell('B', currentIndex);
                moveCursorLeft(); // Move cursor
                return "q3";
            }
        } else if (currentState.equals("q3")) {
            if (input.equals("1")) {
                // Stay in q3 and move left
                moveCursorLeft(); // Move cursor
                return "q3";
            } else if (input.equals("B")) {
                // Stay in q3, move right, transition to q4
                moveCursorRight(); // Move cursor
                return "q4";
            }
        }

        return null; // Invalid state/input
    }
    private void moveCursorLeft() {
        if (currentIndex > 0) {
            // Reset the border of the current cell
            StackPane currentCell = tapeCells.get(currentIndex);
            Rectangle currentRect = (Rectangle) currentCell.getChildren().get(0);
            currentRect.setStroke(Color.BLACK);  // Reset to default black border
            currentRect.setStrokeWidth(1);

            // Move the cursor left
            currentIndex--;

            // Highlight the new current cell
            StackPane newCurrentCell = tapeCells.get(currentIndex);
            Rectangle newCurrentRect = (Rectangle) newCurrentCell.getChildren().get(0);
            newCurrentRect.setStroke(Color.RED);  // Change border color to red to highlight
            newCurrentRect.setStrokeWidth(2);

            // Scroll to the new index
            scrollToCurrentIndex();

            // Optionally, update the tape if needed for Turing Machine logic
            String currentInput = readCurrentInput();
            // Logic to handle the read current input can be placed here if necessary
        }
    }


    private void moveCursorRight() {
        // Reset the border of the previous cell
        if (currentIndex > 0) {
            StackPane prevCell = tapeCells.get(currentIndex - 1);
            Rectangle prevRect = (Rectangle) prevCell.getChildren().get(0);
            prevRect.setStroke(Color.BLACK);  // Reset to default black border
            prevRect.setStrokeWidth(1);
        }

        // Highlight the current cell
        StackPane currentCell = tapeCells.get(currentIndex);
        Rectangle currentRect = (Rectangle) currentCell.getChildren().get(0);
        currentRect.setStroke(Color.RED);  // Change border color to red to highlight
        currentRect.setStrokeWidth(2);

        executeLogicAndMoveCursor();



        // Scroll to the current index
        scrollToCurrentIndex();

        // Increment the index and stop if it's the last cell
        if (currentIndex < tapeCells.size() - 1) {
            currentIndex++;
        } else {
            stopAutomaticCursorMovement();
        }
    }

    private void executeLogicAndMoveCursor(){



//        int indexToUpdate = this.currentIndex;
//        String currentInput = readCurrentInput();
//
//        if(readCurrentInput().equals("1")){
//            writeToCurrentCell('0', currentIndex);
//        }



    }

    private String readCurrentInput() {
        StackPane currentCell = tapeCells.get(currentIndex);
        Label currentLabel = (Label) currentCell.getChildren().get(1);  // Label is at index 1 in StackPane
        return currentLabel.getText();
    }

    public void writeToCurrentCell(char newSymbol, int index) {
        Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            StackPane currentCell = tapeCells.get(index);
            Label currentLabel = (Label) currentCell.getChildren().get(1);  // Label is at index 1 in StackPane
            currentLabel.setText(Character.toString(newSymbol));  // Update the symbol in the current cell
            System.out.println("Wrote " + newSymbol + " to the current cell.");
        }));
        delayTimeline.setCycleCount(1);  // Run only once
        delayTimeline.play();

    }

    private void scrollToCurrentIndex() {
        double totalWidth = tape.getWidth();
        double cellWidth = 90;  // Assuming the width of each cell is 90
        double scrollPos = (currentIndex * cellWidth) / totalWidth;
        scrollPane.setHvalue(scrollPos);  // Scroll horizontally to the current index
    }


    public void runSimulation(String input) {
        if (timeline != null) {
            timeline.stop();
            timeline.getKeyFrames().clear();  // Ensure previous timeline is cleared
        }

        timeline = new Timeline();
        stateDiagram.resetToStartState();

//        if (!textfield.getText().isEmpty()) {
            // Optionally uncomment this if you want to validate input before proceeding
            // if (!isValidInput(input)) {
            //     label.setText("Invalid characters in input.");
            //     return; // Stop simulation if input is invalid
            // }

            // Loop through the input string and animate state transitions
            for (int i = 0; i < input.length(); i++) {
                char currentInput = input.charAt(i);
                int step = i;

                KeyFrame frame = new KeyFrame(Duration.seconds(step + 1), event -> {
                    if (isValid) {  // Check if input is still valid
                        String nextState = getNextState(Character.toString(currentInput));

                        if (nextState != null) {
                            StateNode currentStateNode = stateDiagram.getState(stateDiagram.getCurrentState());
                            currentStateNode.resetColor(true); // Reset current state's color

                            stateDiagram.setCurrentState(nextState); // Move to next state
                            StateNode nextStateNode = stateDiagram.getState(nextState);
                            nextStateNode.highlight(true); // Highlight the next state
                        } else {
                            isValid = false;  // Stop further processing
                            timeline.stop();
                        }
                    }
                });

                timeline.getKeyFrames().add(frame);
            }

//            KeyFrame finalStep = new KeyFrame(Duration.seconds(input.length() + 1), event -> {
//                if (isValid) {  // Only check if input is valid
//                    if (automaton.getCurrentState().equals(automaton.getFinalState())) {
//                        label.setText("String Accepted!");
//                    } else {
//                        label.setText("String Rejected.");
//                    }
//                }
//            });
//
//            timeline.getKeyFrames().add(finalStep);
            timeline.play();
//        } else {
//            label.setText("Invalid Input");
        }
}

