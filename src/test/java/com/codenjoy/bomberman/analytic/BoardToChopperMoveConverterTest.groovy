package com.codenjoy.bomberman.analytic

import com.codenjoy.bomberman.Element
import org.apache.commons.io.FileUtils
import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.bomberman.Element.*
import static com.codenjoy.bomberman.TestUtils.board

/**
 * Created by szelenin on 4/7/14.
 */
class BoardToChopperMoveConverterTest extends Specification {
    public static final int DEFAULT_WIDTH = 9
    private def tmpDir
    private File tmpFile

    def setup() {
        tmpDir = new File(FileUtils.getTempDirectory(), 'test')
        FileUtils.deleteDirectory(tmpDir)

        tmpFile = new File(tmpDir, "out.csv")
    }

    @Unroll
    def "convert board states"(def boardStrings, def expectedLines) {
        def converter = new BoardToChopperMoveConverter(tmpFile.getAbsolutePath())
        boardStrings.each { it -> converter.processState("$it") }

        expect:
        def lines = FileUtils.readLines(tmpFile)
        if (expectedLines.empty) {
            assert lines.size() == 1
        } else {
            int i = 0
            expectedLines.each { line ->
                verifyLine(lines[i + 1], expectedLines[i]);
                i++
            }
        }
        where:
        boardStrings                                                 | expectedLines
        ['&'(5,5)]| []
        ['&'(5, 5)]                                                  | []
        ['&'(5, 5), '&'(5, 5 + 1)]                                   | []
        ['&'(5, 5), '&'(5, 5 + 1), '&'(5, 5)]                        | ['D,0,0,0,0,U']
        ['&'(5, 5), '&'(5 + 1, 5), '&'(5, 5)]                        | ['R,0,0,0,0,L']
        ['&'(5, 5), '&'(5 + 1, 5), '&'(5 + 1, 5)]                    | ['R,0,0,0,0,S']
        ['&'(5, 5), '&'(5 + 1, 5), '&'(5 + 1, 5 + 1), '&'(5, 5 + 1)] | ['R,0,0,0,0,D', 'D,0,0,0,0,L']
        //2 choppers
        ['&'(6, 6, 3, 3), '&'(6, 5, 3, 4), '&'(6, 4, 3, 5)] | ['U,0,0,0,0,U', 'D,0,0,0,0,D']
        //obstacles
        //Previous,UpOccupied,RightOccupied,DownOccupied,LeftOccupied,Next
        ['&'(6, 4)._(WALL, 6, 2), '&'(6, 3)._(WALL, 6, 2), '&'(6, 4)._(WALL, 6, 2)] | ['U,W,0,0,0,D']
        ['&'(2, 2)._(WALL, 4, 2), '&'(3, 2)._(WALL, 4, 2), '&'(2, 2)._(WALL, 4, 2)] | ['R,0,W,0,0,L']
        ['&'(2, 2)._(WALL, 2, 4), '&'(2, 3)._(WALL, 2, 4), '&'(2, 2)._(WALL, 2, 4)] | ['D,0,0,W,0,U']
        ['&'(4, 2)._(WALL, 2, 2), '&'(3, 2)._(WALL, 2, 2), '&'(4, 2)._(WALL, 2, 2)] | ['L,0,0,0,W,R']
        ['&'(4, 2)._(DESTROY_WALL, 2, 2), '&'(3, 2)._(DESTROY_WALL, 2, 2), '&'(4, 2)._(DESTROY_WALL, 2, 2)] | ['L,0,0,0,W,R']

        // special cases when previous move cannot be uniquely defined
        ['&'(4, 2)._(MEAT_CHOPPER, 2, 2), '&'(3, 2)._(MEAT_CHOPPER, 2, 2), '&'(4, 2)._(MEAT_CHOPPER, 2, 2)] | ['S,0,C,0,0,S', 'L,0,0,0,C,R']
    }


    def '&'(int ... xy) {
        new SWrapper(board(DEFAULT_WIDTH, MEAT_CHOPPER, xy))
    }

    def verifyLine(String line, String expectedLine) {
        assert line == expectedLine
    }

    def verifyLine2() {
        assert false
    }
}
