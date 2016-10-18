import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.Prompter;
/**
 * Created by nicolasjhampton on 10/16/16.
 */
public class LeagueManager {
    public static void main(String[] args) {
        Player[] players = Players.load();
        Prompter prompter = new Prompter(players);
        prompter.startScreen();
        prompter.menu();
    }
}
