import javafx.application.Application;
import javafx.stage.Stage;

import java.rmi.Naming;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        InputBoardController inputBoardController = new InputBoardController();

        inputBoardController.showStage();
    }

    public static void main(String[] args) {
        launch(args);
        try {
            BlackBoardInterface c = (BlackBoardInterface) Naming.lookup("rmi://127.0.0.1:3030/WriteBoardService");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}