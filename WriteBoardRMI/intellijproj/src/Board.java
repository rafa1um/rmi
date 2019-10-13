import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Line;

public class Board {
    private String board_name;
    private List <User> board_users;
    private List <Line> board_points;
    public Board(String name) {
        board_name = name;
        board_users = new ArrayList<User>();
        board_points = new ArrayList<Line>();
    }

    public String getBoard_name()
    {
        return board_name;
    }

    public List<User> getBoard_users() { return board_users; }

    public void addUser(String username, String user_color) {
        User user = new User(username, user_color);
        board_users.add(user);
        System.out.println("Usuario " + username + " adicionado ao quadro.");
    }
    public void addLine(Line l){
        board_points.add(l);
    }

    public int getSizeBoardPoints(){return board_points.size();}

    public List<Line> getBoardPoints() {return board_points;}
}
