package org.openjfx;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.ArcType;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

public class drawBoard {

    public void drawBoardWindow() {

        Stage window = new Stage();
        window.setTitle("yay");

        Group root = new Group();
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawLines(gc);
        root.getChildren().add(canvas);
        window.setScene(new Scene(root));
        window.show();

    }

    private void drawLines(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 6, 6);
        gc.strokeLine(90, 50, 2, 2);

    }
}
