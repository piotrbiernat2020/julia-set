module com.example.juliasetgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires jocl;
    requires java.desktop;

    exports com.example.juliasetgui;
    opens com.example.juliasetgui to javafx.fxml;
    exports com.example.juliasetgui.julia_set.view_controller;
    opens com.example.juliasetgui.julia_set.view_controller to javafx.fxml;
}