module org.example.project.turingmachineproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires MaterialFX;


    opens org.example.project.turingmachineproject to javafx.fxml;
    exports org.example.project.turingmachineproject;
}