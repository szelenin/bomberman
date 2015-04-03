package com.codenjoy.minesweeper;

/**
 * User: oleksandr.baglai
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Element {

    MINESWEEPER('☺'),
    BOMB('☻'),
    DESTROYED_BOMB('x'),
    DEAD_MINESWEEPER('Ѡ'),

    BOMBS_1('1'),
    BOMBS_2('2'),
    BOMBS_3('3'),
    BOMBS_4('4'),
    BOMBS_5('5'),
    BOMBS_6('6'),
    BOMBS_7('7'),
    BOMBS_8('8'),
    WALL('☼'),
    SPACE(' '), HIDDEN('*'), FLAG('‼');

    private char ch;

    Element(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such Elment for " + ch);
    }

    public static void main(String[] args) {
        System.out.println("Element.values().length = " + Element.values().length);
    }
}
