package org.openjfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.*;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        inputBoard input_board = new inputBoard();
//        input_board.inputBoardWindow();
        drawBoard draw = new drawBoard();
        draw.drawBoardWindow();

    }
}