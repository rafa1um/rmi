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

    void drawLine(String boardName, double x1, double y1, double x2, double y2, String color) throws RemoteException;

    List<String> getAvailableBoards() throws RemoteException;

    Boolean checkUpdate(int boardPosition, int user_size) throws RemoteException;

    String getPoint(int boardPosition, int n) throws RemoteException;

    int getBoardPosition(String boardName) throws RemoteException;

    int getBoardLineLen(int boardPosition) throws RemoteException;

    void leaveBoard(String boardName, String userName) throws RemoteException;

    String generateUserColor(int boardNumber) throws RemoteException;

    void wbAdminConsulta() throws RemoteException;
}