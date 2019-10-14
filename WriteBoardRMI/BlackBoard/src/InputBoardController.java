import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.Naming;

public class InputBoardController {
    @FXML
    private Stage thisStage;
    @FXML
    public Button loginButton;
    @FXML
    private TextField userName;
    @FXML
    private TextField ipAddr;
    @FXML
    private TextField port;

    private String userColor;

    public InputBoardController() {

        thisStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inputBoard.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("BubbleGum-Board");

            this.initialize();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public void initialize() {
        loginButton.setOnAction(actionEvent -> loginButtonAction());
    }


    @FXML
    private void loginButtonAction() {
        // CREATE NEW USER ON SERVER

        userColor = "Yellow";
        try {
            BlackBoardInterface c = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/WriteBoardService");
            c.newUser(getUserName(), userColor);

        } catch (Exception e) {
            System.out.println(e);
        }
        BoardSelectionController boardSelectionController = new BoardSelectionController(this);
        boardSelectionController.showStage();

    }

    public String getUserColor() {
        return userColor;
    }

    public String getUserName() {
        return userName.getText();
    }

    public String getIpAddr() {
        return ipAddr.getText();
    }

    public String getPort() {
        return port.getText();
    }

}
