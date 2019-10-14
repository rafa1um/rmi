import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class BoardController {
    @FXML

    public Button voltarButton;
    private BoardSelectionController boardSelectionController;
    private Stage thisStage;
    private double px, py;
    private Boolean already_clicked = false;  //usado no drawLine
    private String boardName;
    private int user_size = 0;  // Numero de linhas que tem no quadro
    private Boolean break_thread = false;  //Para parar a thread;
    private int board_position;  //Pega a posição do quadro no servido
    @FXML
    private AnchorPane apMain;

    public void initialize() {
        voltarButton.setOnAction(actionEvent -> voltarButtonAction());
    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public Line drawLine(double x, double y) {
        String color = boardSelectionController.getUserColor();
        Line line = new Line();
        line.setStartX(x);
        line.setStartY(y);
        line.setEndX(px);
        line.setEndY(py);
        line.setStroke(Color.web(color));
        apMain.getChildren().add(line);
        return line;
    }

    public BoardController(BoardSelectionController boardSelectionController) {

        // LOAD LINES

        this.boardSelectionController = boardSelectionController;
        thisStage = new Stage();
        boardName = boardSelectionController.getSelectedBoard();
        System.out.println(boardName);


        try {
            BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/WriteBoardService");
            board_position = bs.getBoardPosition(boardName);
            new Thread(() -> {
                while (!break_thread) {
                    try {
                        if (bs.checkUpdate(board_position, user_size)) {
                            // List<Line> l =  bs.getPoints(board_position);
                            //System.out.println(l.size());
                            System.out.println("Percorrer lista e desenhar size " + user_size);
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();
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
                                    l.getEndY());
                            user_size++;
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        initialize();
    }

    @FXML
    private void voltarButtonAction() {
        try {
            BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/WriteBoardService");
            bs.leaveBoard(boardName, boardSelectionController.getUserName());
        } catch (Exception e) {
            System.out.println(e);
        }
        break_thread = true;
        Stage curStage = (Stage) voltarButton.getScene().getWindow();
        curStage.close();
    }
}
