import com.codenjoy.bomberman.*;
import com.codenjoy.bomberman.Action;
import com.codenjoy.bomberman.utils.*;
import com.codenjoy.bomberman.utils.Point;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private int counter = 1;

    public BoardReplay() throws IOException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(34 * 24, 34 * 24);
        setVisible(true);
        BoardPanel pane = new BoardPanel();
        add(pane);

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


    private class BoardPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(buffer, 0, 0, null);
        }

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    public void drawBoard(String boardString, int boardSize) throws IOException {
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
        File file = new File("board" + counter + ".gif");
        counter++;
        ImageIO.write(buffer, "gif", file);
    }
}
