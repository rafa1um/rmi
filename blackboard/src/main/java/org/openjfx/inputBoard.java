package org.openjfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class inputBoard {

    public static void inputBoardWindow() {
        Stage window = new Stage();
        window.setTitle("yay");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //Hostname Label
        Label hostname_l = new Label("Hostname");
        GridPane.setConstraints(hostname_l, 0, 0);

        //Hostname Input
        TextField hostname_i = new TextField();
        GridPane.setConstraints(hostname_i, 1, 0);

        //Porta Label
        Label port_l = new Label("Porta");
        GridPane.setConstraints(port_l, 0, 1);

        //Porta Input
        TextField port_i = new TextField();
        GridPane.setConstraints(port_i, 1, 1);

        //Quadro Label
        Label board_l = new Label("Quadro");
        GridPane.setConstraints(board_l, 0, 2);

        //Quadro Input
        TextField board_i = new TextField();
        GridPane.setConstraints(board_i, 1, 2);

        //Botao Enviar
        Button sendButton = new Button("Enviar");
        GridPane.setConstraints(sendButton, 1, 3);

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println(hostname_i.getText());
                System.out.println(port_i.getText());
                System.out.println(board_i.getText());
            }
        };

        sendButton.setOnAction(event);


        grid.getChildren().addAll(hostname_l, hostname_i, port_l, port_i, board_l, board_i, sendButton);

        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);


        window.show();


    }

}
