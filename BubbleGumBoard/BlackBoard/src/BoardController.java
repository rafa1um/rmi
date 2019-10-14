import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class BoardController {
    @FXML

    public Button voltarButton;
    public List<Line> drawn_lines;
    private BoardSelectionController boardSelectionController;
    private Stage thisStage;
    private double px, py;
    private Boolean already_clicked = false;  //usado no drawLine
    private String boardName;
    private int user_size = 0;  // Numero de linhas que tem no quadro
    private Boolean break_thread = false;  //Para parar a thread;
    private int board_position;  //Pega a posição do quadro no servido
    String color = generateUserColor();
    @FXML
    private AnchorPane apMain;
    Task<Void> update = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            while (!break_thread) {

                Platform.runLater(() -> {
                    try {
                        BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/BlackBoard");
                        updateLines(bs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                Thread.sleep(800);
            }
            return null;
        }
    };

    public BoardController(BoardSelectionController boardSelectionController) {

        // LOAD LINES

        this.boardSelectionController = boardSelectionController;
        thisStage = new Stage();
        boardName = boardSelectionController.getSelectedBoard();
        drawn_lines = new ArrayList<Line>();
        System.out.println(boardName);


        try {
            BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/BlackBoard");
            board_position = bs.getBoardPosition(boardName);
            Thread t = new Thread(update);
            t.setDaemon(true);
            t.start();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("board.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (already_clicked) {
                        Line l = drawLine(mouseEvent.getX(), mouseEvent.getY());  // essa funçao nao deve ser chamada
                        try {
                            bs.drawLine(boardName,
                                    l.getStartX(),
                                    l.getStartY(),
                                    l.getEndX(),
                                    l.getEndY(),
                                    l.getStroke().toString());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        already_clicked = false;
                    } else {
                        px = mouseEvent.getX();
                        py = mouseEvent.getY();
                        already_clicked = true;
                    }
                }
            });
            thisStage.setScene(scene);
            thisStage.setTitle("BubbleGum-Board");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        initialize();
    }

    public void initialize() {
        voltarButton.setOnAction(actionEvent -> voltarButtonAction());
    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public Line drawLine(double x, double y) {
        Line line = new Line();
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(px);
        line.setEndY(py);
        line.setStroke(Color.web(color));
        return line;
    }

    public String generateUserColor() {
        BlackBoardInterface bs = null;
        try {
            bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/BlackBoard");
            return bs.generateUserColor(board_position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateLines(BlackBoardInterface bs) throws RemoteException {
        if (bs.checkUpdate(board_position, user_size)) {
            int boardLineLen = bs.getBoardLineLen(board_position);
            for (int i = 0; i < boardLineLen; i++) {
                String l = bs.getPoint(board_position, i);
                Line line = drawLineGivenStr(l);
                if (!drawn_lines.contains(line)) {
                    apMain.getChildren().add(line);
                    user_size++;
                    drawn_lines.add(line);
                }
            }
        }
    }

    public Line drawLineGivenStr(String l) {
        String[] points = l.split(" ");
        Line line = new Line();
        line.setStartX(Double.parseDouble(points[0]));
        line.setStartY(Double.parseDouble(points[1]));
        line.setEndX(Double.parseDouble(points[2]));
        line.setEndY(Double.parseDouble(points[3]));
        line.setStroke(Paint.valueOf(points[4]));
        return line;
    }

    @FXML
    private void voltarButtonAction() {
        try {
            BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/BlackBoard");
            bs.leaveBoard(boardName, boardSelectionController.getUserName());
        } catch (Exception e) {
            System.out.println(e);
        }
        break_thread = true;
        Platform.exit();
        Stage curStage = (Stage) voltarButton.getScene().getWindow();
        curStage.close();
    }
}
