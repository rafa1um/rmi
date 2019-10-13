import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Line;

public class WriteBoardImple extends UnicastRemoteObject implements WriteBoard
{

    public int board_count  = 0;
    List<Board> boards = new ArrayList<Board>();

    protected WriteBoardImple() throws RemoteException
    {
        super();
    }

    public String newUser(String name, String color) throws RemoteException
    {
        User user = new User(name, color);
        System.out.println("User " + name + " logged in!");
        return "User " + name + " created!";
    }

    public void newBoard() {
        Board board = new Board("Quadro " + Integer.toString(board_count));
        boards.add(board);
        System.out.println("Board " + Integer.toString(board_count) + " criado!");
        board_count++;

    }

    public void enterBoard(String boardName, String userName){
        Board board = null;
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getBoard_name().compareTo(boardName) != 1){
                board = boards.get(i);
            }
        }
        if (board != null){
            board.addUser(userName, "blue");
            System.out.println(userName + " entrou no " + boardName);
        }
    }
    public void drawLine(String boardName, double x1, double y1, double x2, double y2) throws RemoteException{
        Line l = new Line();
        l.setStartX(x1);
        l.setStartY(y1);
        l.setEndX(x2);
        l.setEndY(y2);
        for(int i = 0; i < boards.size(); i++){
            if(boards.get(i).getBoard_name().compareTo(boardName) == 0){
                boards.get(i).addLine(l);
                System.out.println(boards.get(i).getBoard_name() + " desenhado");
            }
        }
    }
    public int getBoardPosition(String boardName) throws RemoteException{
        for(int i = 0; i < boards.size(); i++){
            if(boards.get(i).getBoard_name().compareTo(boardName) == 0){
               return i;
            }
        }
        return -1;  // caso de erro
    }

    public List<String> getAvailableBoards()
    {
        List<String> saida = new ArrayList<String>();
        for (int i = 0; i < boards.size(); i++) {
            saida.add(boards.get(i).getBoard_name());
        }
        return saida;
    }
    public Boolean checkUpdate(int boardPosition, int user_size) throws RemoteException{
        System.out.println(boards.get(boardPosition).getBoard_name() + "com tamanho"
        + boards.get(boardPosition).getSizeBoardPoints());  // só pra testar
        return(boards.get(boardPosition).getSizeBoardPoints() != user_size);
    }

    public List<Line> getPoints(int boardPosition){
        return boards.get(boardPosition).getBoardPoints();
    }

    public void leaveBoard(String boardName, String userName) {
        for (int i = 0; i < boards.size(); i++) {
            if (boards.get(i).getBoard_name().compareTo(boardName) == 0) {
                if (boards.get(i).getBoard_users().size() == 1){
                    boards.remove(i);
                    System.out.println("Usuário " + userName + " saiu do " + boardName + " e o quadro foi excluido.");
                    break;
                }
                for (int j = 0; j < boards.get(i).getBoard_users().size(); j++) {
                    if (boards.get(i).getBoard_users().get(j).getUser_name().compareTo(userName) == 0) {
                        boards.get(i).getBoard_users().remove(j);
                        System.out.println("Usuário " + userName + " saiu do " + boardName);
                    }
                }
            }
        }
    }

    public void wbAdminConsulta()
    {
        String path = "./wbadmin_log.txt";
        File arq = new File(path);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(arq));
            for (int i = 0; i < boards.size(); i++) {
                writer.write("Quadro: " + boards.get(i).getBoard_name());
                writer.newLine();
                for (int j = 0; j < boards.get(i).getBoard_users().size(); j++) {
                    writer.write(boards.get(i).getBoard_users().get(j).getUser_name());
                    writer.newLine();
                    }
                writer.newLine();
                }
            writer.flush();
            writer.close();
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
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

            WriteBoardImple obj = new WriteBoardImple();

            Naming.rebind("rmi://127.0.0.1:3030/WriteBoardService", (Remote) obj);
            System.out.println("WriteBoardImple foi criado e registrado");
        }

        catch(Exception e) {
            System.out.println("Ocorreu uma exceção no servidor");
            e.printStackTrace();
        }

    }

}