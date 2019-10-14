import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class BoardSelectionController {

    @FXML
    private Stage thisStage;
    @FXML
    private InputBoardController inputBoardController;
    @FXML
    public ListView<String> boardList;
    @FXML
    public Button voltarButton;
    @FXML
    public Button newBoardButton;
    @FXML
    public Button entrarButton;
    @FXML
    private String userName;
    @FXML
    private String ipAddr;
    @FXML
    private String port;
    @FXML
    private String userColor;
    @FXML
    private String selectedBoard = null;
    @FXML
    private List<String> avBoards = new ArrayList<String>();

    public BoardSelectionController(InputBoardController inputBoardController) {

        userColor = "pink";
        this.inputBoardController = inputBoardController;
        thisStage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("boardSelection.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("BubbleGum-Board");

        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshBoardList();
        new Thread(refresh).start();
        initialize();
    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public String getUserColor() {
        return userColor;
    }

    public String getUserName() {
        return userName;
    }

    public String getSelectedBoard() {
        return selectedBoard;
    }

    public void initialize() {
        voltarButton.setOnAction(actionEvent -> voltarButtonAction());
        newBoardButton.setOnAction(actionEvent -> newBoardButtonAction());
        entrarButton.setOnAction(actionEvent -> entrarButtonAction());
        userName = inputBoardController.getUserName();
        ipAddr = inputBoardController.getIpAddr();
        port = inputBoardController.getPort();
    }

    @FXML
    private void voltarButtonAction() {
        Stage curStage = (Stage) voltarButton.getScene().getWindow();
        curStage.close();
    }

    @FXML
    private void newBoardButtonAction() {
        try {
            BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/BlackBoard");
            bs.newBoard();
            refreshBoardList();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void entrarButtonAction() {
        // OPEN BOARD
        try {
            selectedBoard = boardList.getSelectionModel().getSelectedItem();
            BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/BlackBoard");
            bs.enterBoard(selectedBoard, userName);
        } catch (Exception e) {
            System.out.println(e);
        }
        BoardController boardController = new BoardController(this);
        boardController.showStage();

    }

    Task<Void> refresh = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        refreshBoardList();
                    }
                });
            }
        }
    };

    private void refreshBoardList() {
        try {
            BlackBoardInterface bs = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/BlackBoard");
            avBoards = bs.getAvailableBoards();
            if (boardList.getItems().size() != avBoards.size()) {
                boardList.getItems().clear();
                boardList.getItems().addAll(avBoards);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
