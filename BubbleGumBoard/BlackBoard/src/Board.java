import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private String board_name;
    private List<User> board_users;
    private List<Line> board_points;
    private List<Color> availableColors;

    public Board(String name) {
        board_name = name;
        board_users = new ArrayList<User>();
        board_points = new ArrayList<Line>();
        availableColors = new ArrayList<>();
        availableColors.add(Color.web("darkmagenta"));
        availableColors.add(Color.web("darksalmon"));
        availableColors.add(Color.web("darkorchid"));
        availableColors.add(Color.web("firebrick"));
        availableColors.add(Color.web("gold"));
        availableColors.add(Color.web("plum"));
        availableColors.add(Color.web("darkslategray"));
        availableColors.add(Color.web("powderblue"));
        availableColors.add(Color.web("deeppink"));
    }

    public Color popColor() {
        Color c = availableColors.get(availableColors.size() - 1);
        availableColors.remove(availableColors.size() - 1);
        return c;
    }

    public String getBoard_name() {
        return board_name;
    }

    public List<User> getBoard_users() {
        return board_users;
    }

    public void addUser(String username, String user_color) {
        User user = new User(username, user_color);
        board_users.add(user);
        System.out.println(username + " adicionado ao quadro.");
    }

    public void addLine(Line l) {
        board_points.add(l);
    }

    public int getSizeBoardPoints() {
        return board_points.size();
    }

    public List<Line> getBoardPoints() {
        return board_points;
    }
}
