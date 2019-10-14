import javafx.scene.shape.Line;
import javafx.scene.paint.Paint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class BlackBoard extends UnicastRemoteObject implements BlackBoardInterface {

    public int board_count = 0;
    List<Board> boards = new ArrayList<Board>();

    protected BlackBoard() throws RemoteException {
        super();
    }

    public String newUser(String name, String color) throws RemoteException {
        User user = new User(name, color);
        System.out.println("Login " + name);
        return "Login " + name ;
    }

    public void newBoard() {
        Board board = new Board("BG-Board " + Integer.toString(board_count));
        boards.add(board);
        System.out.println("BG-Board " + Integer.toString(board_count) + " criado!");
        board_count++;

    }

    public void enterBoard(String boardName, String userName) {
        Board board = null;
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getBoard_name().compareTo(boardName) != 1) {
                board = boards.get(i);
            }
        }
        if (board != null) {
            board.addUser(userName, "blue");
            System.out.println(userName + " entrou em " + boardName);
        }
    }

    public void drawLine(String boardName, double x1, double y1, double x2, double y2, String color) throws RemoteException{
        Line l = new Line();
        l.setStartX(x1);
        l.setStartY(y1);
        l.setEndX(x2);
        l.setEndY(y2);
        l.setStroke(Paint.valueOf(color));
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getBoard_name().compareTo(boardName) == 0) {
                boards.get(i).addLine(l);
                System.out.println(boards.get(i).getBoard_name() + " desenhado");
            }
        }
    }

    public int getBoardLineLen(int boardPosition){
        return boards.get(boardPosition).getSizeBoardPoints();

    }

    public int getBoardPosition(String boardName) throws RemoteException {
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getBoard_name().compareTo(boardName) == 0) {
                return i;
            }
        }
        return -1;  // caso de erro
    }

    public List<String> getAvailableBoards() {
        List<String> saida = new ArrayList<String>();
        for (int i = 0; i < boards.size(); i++) {
            saida.add(boards.get(i).getBoard_name());
        }
        return saida;
    }

    public Boolean checkUpdate(int boardPosition, int user_size) throws RemoteException {
        System.out.println(boards.get(boardPosition).getBoard_name() + "com tamanho"
                + boards.get(boardPosition).getSizeBoardPoints());  // só pra testar
        return (boards.get(boardPosition).getSizeBoardPoints() != user_size);
    }

    public String getPoint(int boardPosition, int n){
        List<Line> l = boards.get(boardPosition).getBoardPoints();
        Line line = l.get(n);
        double x1 = line.getStartX();
        double y1 = line.getStartY();
        double x2 = line.getEndX();
        double y2 = line.getEndY();
        String color = line.getStroke().toString();

        String theLine = Double.toString(x1) +
                " " +
                Double.toString(y1) +
                " " +
                Double.toString(x2) +
                " " +
                Double.toString(y2) +
                " " +
                color;
        System.out.println(theLine);
        return theLine;
    }

    public void leaveBoard(String boardName, String userName) {
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getBoard_name().compareTo(boardName) == 0) {
                if (boards.get(i).getBoard_users().size() == 1) {
                    boards.remove(i);
                    System.out.println(userName + " deixou " + boardName);
                    break;
                }
                for (int j = 0; j < boards.get(i).getBoard_users().size(); j++) {
                    if (boards.get(i).getBoard_users().get(j).getUser_name().compareTo(userName) == 0) {
                        boards.get(i).getBoard_users().remove(j);
                        System.out.println(userName + " saiu do " + boardName);
                    }
                }
            }
        }
    }

    public String generateUserColor(int boardNumber){
        return boards.get(boardNumber).popColor().toString();
    }

    public void wbAdminConsulta() {
        String path = "./wbadmin_log.txt";
        File arq = new File(path);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(arq));
            for (int i = 0; i < boards.size(); i++) {
                writer.write("BG-Board: " + boards.get(i).getBoard_name());
                writer.newLine();
                for (int j = 0; j < boards.get(i).getBoard_users().size(); j++) {
                    writer.write(boards.get(i).getBoard_users().get(j).getUser_name());
                    writer.newLine();
                }
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Registry registry = null;

        /* inicia rmi */
        try {
            /* tenta iniciar o registro */
            registry = LocateRegistry.createRegistry(3030);

        } catch (RemoteException e) {
            /* se não conseguiu criar vê se está rodando */
            try {
                registry = LocateRegistry.getRegistry();
            } catch (RemoteException e2) {
                /* não conseguiu nem criar e nem há rodando, sai do programa */
                System.err.println("Registro não pode ser inicializado");
                System.exit(0);
            }
        }
        try {

            BlackBoard obj = new BlackBoard();

            Naming.rebind("rmi://127.0.0.1:3030/BlackBoard", (Remote) obj);
            System.out.println("BlackBoard rodando...");
        } catch (Exception e) {
            System.out.println("Ocorreu uma exceção no servidor");
            e.printStackTrace();
        }

    }

}