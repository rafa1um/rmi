import java.rmi.*;
import java.util.List;

import javafx.scene.shape.Line;

/*
Classname: WriteBoard
Comment: The remote interface. 
*/
public interface BlackBoardInterface extends Remote {
    String newUser(String name, String color) throws RemoteException;

    void newBoard() throws RemoteException;

    void enterBoard(String boardName, String userName) throws RemoteException;

    void drawLine(String boardName, double x1, double y1, double x2, double y2) throws RemoteException;

    List<String> getAvailableBoards() throws RemoteException;

    Boolean checkUpdate(int boardPosition, int user_size) throws RemoteException;

    List<Line> getPoints(int boardPosition) throws RemoteException;

    int getBoardPosition(String boardName) throws RemoteException;

    void leaveBoard(String boardName, String userName) throws RemoteException;

    void wbAdminConsulta() throws RemoteException;
}