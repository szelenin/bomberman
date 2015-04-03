import com.codenjoy.bomberman.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by szelenin on 6/25/2014.
 */
public class ReplayAStarSearch {
    public static void main(String[] args) throws IOException, InterruptedException {
        BoardReplay boardReplay = new BoardReplay();

        String boardString = "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼### &#  ##       ##      #    &☼☼#☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼☼♥## #     # #  #            #  ☼☼#☼ ☼#☼&☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼  #   #                        ☼☼ ☼#☼ ☼ ☼ ☼ ☼ ☼&☼ ☼ ☼ ☼#☼ ☼ ☼ ☼#☼☼    # ##           #           ☼☼ ☼#☼#☼ ☼ ☼ ☼♥☼ ☼#☼#☼ ☼ ☼ ☼ ☼ ☼ ☼☼##                     ♥       ☼☼#☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼☼ #                   #         ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼          ## ### #          #  ☼☼ ☼ ☼ ☼ ☼&☼#☼#☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼☼ #     #       #         #     ☼☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼              #     #          ☼☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼☼ &                  #      ####☼☼#☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼#   #    #       #   # #       ☼☼ ☼ ☼#☼ ☼ ☼#☼ ☼♥☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼☼      #  #       ☺         ## #☼☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼☼    #                          ☼☼ ☼#☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼&☼ ☼☼    &                          ☼☼ ☼ ☼ ☼#☼ ☼ ☼&☼ ☼ ☼ ☼ ☼#☼ ☼#☼ ☼ ☼☼ #                 #      #  ##☼☼ ☼ ☼#☼#☼ ☼ ☼ ☼#☼#☼ ☼&☼ ☼#☼ ☼ ☼ ☼☼       #   #     ##   ##  #    ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";
        boardReplay.drawBoard(boardString, 33);

        GameState state = new GameState(boardString);
        List<Action> actions = new AstarSearch(new NearestChopperToBombHeuristic()).search(new Problem(state));
        for (Action action : actions) {
            Thread.sleep(200);
            System.out.println("action = " + action);
            state = state.generateSuccessor(action);
            boardReplay.drawBoard(state.toString().replaceAll("\\n", ""), 33);
            boardReplay.repaint();
        }

    }
}
