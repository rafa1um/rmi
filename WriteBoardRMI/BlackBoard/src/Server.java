import java.rmi.Naming;

/*
Classname: HelloServerDemo
Purpose: The RMI server.
*/
public class Server {

    Server() {
        try {
            BlackBoardInterface c = new BlackBoard();
            Naming.rebind("rmi://127.0.0.1:3030/WriteBoardService", c);
            System.out.println("Conectado!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}