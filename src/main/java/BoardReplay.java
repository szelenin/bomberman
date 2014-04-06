import com.codenjoy.bomberman.*;
import com.codenjoy.bomberman.Action;
import com.codenjoy.bomberman.utils.*;
import com.codenjoy.bomberman.utils.Point;
import com.sun.org.apache.bcel.internal.generic.BIPUSH;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.codenjoy.bomberman.Element.*;

/**
 * Created by szelenin on 4/4/14.
 */
public class BoardReplay extends JFrame {
    private final BufferedImage[] images = new BufferedImage[values().length];
    private Map<Element, String> elementToFile = new HashMap<Element, String>();
    private BufferedImage buffer;

    public BoardReplay() throws IOException {
        elementToFile.put(BOMB_BOMBERMAN, "bomb_bomberman.png");
        elementToFile.put(BOMB_TIMER_1, "bomb_one.png");
        elementToFile.put(BOMB_TIMER_2, "bomb_two.png");
        elementToFile.put(BOMB_TIMER_3, "bomb_three.png");
        elementToFile.put(BOMB_TIMER_4, "bomb_four.png");
        elementToFile.put(BOMB_TIMER_5, "bomb_five.png");
        elementToFile.put(BOMBERMAN, "bomberman.png");
        elementToFile.put(DEAD_BOMBERMAN, "dead_bomberman.png");
        elementToFile.put(OTHER_BOMBERMAN, "other_bomberman.png");
        elementToFile.put(OTHER_BOMB_BOMBERMAN, "other_bomb_bomberman.png");
        elementToFile.put(OTHER_DEAD_BOMBERMAN, "other_dead_bomberman.png");
        elementToFile.put(BOOM, "boom.png");
        elementToFile.put(WALL, "wall.png");
        elementToFile.put(DESTROY_WALL, "destroy_wall.png");
        elementToFile.put(DESTROYED_WALL, "destroyed_wall.png");
        elementToFile.put(MEAT_CHOPPER, "meat_chopper.png");
        elementToFile.put(DEAD_MEAT_CHOPPER, "dead_meat_chopper.png");
        elementToFile.put(SPACE, "empty.png");
        for (int i = 0; i < Element.values().length; i++) {
            images[i] = ImageIO.read(BoardReplay.class.getResourceAsStream(elementToFile.get(Element.values()[i])));
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        BoardReplay boardReplay = buildFrame();
//        boardReplay.setSize(640, 480);
        boardReplay.setVisible(true);

        BoardPanel pane = boardReplay.new BoardPanel();
        boardReplay.add(pane);
        String boardString = "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼#&#####           #           #☼☼ ☼#☼#☼#☼#☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼#☼ ☼☼&  # #   #                  &  ☼☼ ☼ ☼#☼#☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼☼   ##    ♥#                    ☼☼ ☼ ☼#☼#☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼ #    #                        ☼☼#☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼#☼☼# #    #      #              # ☼☼#☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼#☼ ☼#☼#☼ ☼ ☼ ☼☼ ##        #    ☺       #   # #☼☼ ☼ ☼ ☼ ☼♥☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼#☼ ☼☼      #            &  #        ☼☼#☼ ☼ ☼#☼ ☼ ☼#☼ ☼ ☼#☼ ☼ ☼#☼ ☼ ☼ ☼☼                     #         ☼☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼#☼☼#          &     #             ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼               #         #     ☼☼#☼#☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼ #    &                      # ☼☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼☼ ##                         #  ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼&☼ ☼ ☼#☼ ☼ ☼ ☼#☼☼           #  &    ##        ##☼☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼ ☼ ☼ ☼ ☼#☼#☼#☼ ☼☼#      #              &    #   ☼☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼#☼☼     # # ### #               #♥☼☼ ☼#☼ ☼ ☼ ☼#☼#☼ ☼ ☼ ☼&☼ ☼ ☼ ☼ ☼ ☼☼#          #         #       # ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";
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

    private class BoardPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(buffer, 0, 0, null);
        }

    }

    private static BoardReplay buildFrame() throws IOException {
        BoardReplay frame = new BoardReplay();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(34 * 24, 34 * 24);
        frame.setVisible(true);
        return frame;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    private void drawBoard(String boardString, int boardSize) {
        int spriteWidth = images[0].getWidth();
        int spriteHeight = images[0].getHeight();
        buffer = new BufferedImage(spriteWidth * (boardSize + 1), spriteHeight * (boardSize + 1), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = buffer.getGraphics();
        StringBuilder sb = new StringBuilder(boardString);
        LengthToXY toXY = new LengthToXY(boardSize);
        for (int i = 0; i < sb.length(); i++) {
            Element element = Element.valueOf(sb.charAt(i));
            Point xy = toXY.getXY(i);
            graphics.drawImage(images[element.ordinal()], (xy.getX() + 1) * spriteWidth, (xy.getY() +1)* spriteHeight, this);
        }
    }
}
