package com.codenjoy.bomberman.analytic

import com.codenjoy.bomberman.Element
import com.codenjoy.bomberman.TestUtils
import org.apache.commons.io.FileUtils
import spock.lang.Specification

/**
 * Created by szelenin on 4/7/14.
 */
class BoardToChopperMoveConverterTest extends Specification {
    private def tmpDir
    private File tmpFile

    def setup(){
        tmpDir = new File(FileUtils.getTempDirectory(), 'test')
        FileUtils.deleteDirectory(tmpDir)

        tmpFile = new File(tmpDir, "out.csv")
    }

    def "convert initial state"() {
        def board1 = TestUtils.createBoardWithElementAt(5, 5, 9, Element.MEAT_CHOPPER)
        def converter = new BoardToChopperMoveConverter(tmpFile.getAbsolutePath())

        when:
        converter.processState(board1)

        then:
        def lines = FileUtils.readLines(tmpFile)
        verifyLine(lines[0], "NA", 0,0,0,0, "NA")
    }

    def "convert simple"() {
        def board1 = TestUtils.createBoardWithElementAt(5, 5, 9, Element.MEAT_CHOPPER)
        def board2 = TestUtils.createBoardWithElementAt(5, 5 + 1, 9, Element.MEAT_CHOPPER)
        def board3 = TestUtils.createBoardWithElementAt(5, 5 + 2, 9, Element.MEAT_CHOPPER)

        def converter = new BoardToChopperMoveConverter('out.csv')
        when:
        converter.processState(board1)
        converter.processState(board2)
        converter.processState(board3)


        def lines = FileUtils.readLines(new File('out.csv'))

        then:
        verifyLine(lines[0], "NA", 0,0,0,0, "NA")
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

}
