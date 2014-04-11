package com.codenjoy.bomberman.analytic

import org.apache.commons.io.FileUtils
import spock.lang.Specification
import spock.lang.Unroll

import static com.codenjoy.bomberman.Element.*
import static com.codenjoy.bomberman.TestUtils.board

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
        ['&'(5, 5)]                                                  | []
        ['&'(5, 5), '&'(5, 5 + 1)]                                   | []
        ['&'(5, 5), '&'(5, 5 + 1), '&'(5, 5)]                        | ['D,0,0,0,0,U']
        ['&'(5, 5), '&'(5 + 1, 5), '&'(5, 5)]                        | ['R,0,0,0,0,L']
        ['&'(5, 5), '&'(5 + 1, 5), '&'(5 + 1, 5)]                    | ['R,0,0,0,0,S']
        ['&'(5, 5), '&'(5 + 1, 5), '&'(5 + 1, 5 + 1), '&'(5, 5 + 1)] | ['R,0,0,0,0,D', 'D,0,0,0,0,L']
    }

    def '&'(int x, int y) {
        board(x, y, 9, MEAT_CHOPPER)
    }


    def verifyLine(String line, String expectedLine) {
        assert line == expectedLine
    }

    def verifyLine2() {
        assert false
    }

}
