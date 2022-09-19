package com.example.juliasetgui;

import com.example.juliasetgui.julia_set.domain.JuliaSetConfig;
import com.example.juliasetgui.julia_set.view_controller.JuliaSetViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField maxIterations;
    @FXML
    private TextField cx;
    @FXML
    private TextField cy;

    @FXML
    protected void onGenerateButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("julia-set-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Integer.parseInt(widthTextField.getText()), Integer.parseInt(heightTextField.getText()));
        JuliaSetViewController controller = fxmlLoader.getController();
        var config = new JuliaSetConfig(
                Integer.parseInt(widthTextField.getText()),
                Integer.parseInt(heightTextField.getText()),
                Integer.parseInt(maxIterations.getText()),
                Float.parseFloat(cx.getText()),
                Float.parseFloat(cy.getText()));
        controller.setJuliaSetConfig(config);
        Stage stage = new Stage();
        stage.setTitle("Julia Set");
        stage.setScene(scene);
        stage.show();
    }
}