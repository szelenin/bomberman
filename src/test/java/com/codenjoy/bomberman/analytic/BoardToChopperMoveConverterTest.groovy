package com.codenjoy.bomberman.analytic

import com.codenjoy.bomberman.Element
import com.codenjoy.bomberman.TestUtils
import org.apache.commons.io.FileUtils
import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.bomberman.Element.*
import static com.codenjoy.bomberman.TestUtils.board
import static com.codenjoy.bomberman.TestUtils.createBoardWithElementAt

/**
 * Created by szelenin on 4/7/14.
 */
class BoardToChopperMoveConverterTest extends Specification {
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
        boardStrings.each { it -> converter.processState(it) }

        expect:
        def lines = FileUtils.readLines(tmpFile)
        int i = 0
        lines.each { line -> verifyLine2(line, expectedLines[i]); i++}

        where:
        boardStrings                                    | expectedLines
        ['&'(5, 5)] | ['NA,0,0,0,0,NA']
        ['&'(5, 5), '&'(5, 5 + 1)] | ['NA,0,0,0,0,NA', 'D,0,0,0,0,NA']
    }

    def '&'(int x, int y){
        board(x, y, 9, MEAT_CHOPPER)
    }

    def verifyLine(String line, String previousMove, int isUpOccupied, int isDownOccupied, int isLeftOccupied, int isRightOccupied, String nextMove) {
        def split = line.split('\\,')
        split[0] == previousMove &&
                split[1] as int == isUpOccupied &&
                split[2] as int == isDownOccupied &&
                split[3] as int == isLeftOccupied &&
                split[4] as int == isRightOccupied &&
                split[5] == nextMove
    }

    def verifyLine2(String line, String expectedLine) {
        assert line == expectedLine
    }

    def verifyLine2() {
        assert false
    }

}
