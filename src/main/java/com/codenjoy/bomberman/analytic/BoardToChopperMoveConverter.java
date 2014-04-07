package com.codenjoy.bomberman.analytic;

import com.codenjoy.bomberman.GameState;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by szelenin on 4/7/14.
 */

public class BoardToChopperMoveConverter {
    private File outFile;

    public BoardToChopperMoveConverter(String outFilePath) {
        this.outFile = new File(outFilePath);
    }

    public void processState(String boardString) throws IOException {
        FileUtils.writeStringToFile(outFile, "NA,0,0,0,0,NA\n");
    }
}
