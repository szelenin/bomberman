package com.codenjoy.bomberman

import spock.lang.Specification

import static com.codenjoy.bomberman.TestUtils.*

/**
 * Created by szelenin on 3/25/14.
 */
class AstarBombChoppersAgentTest extends Specification {
    def "boom chopper"() {
        String board = setElement(9, 4, 4, Element.MEAT_CHOPPER.char, createBoardWithBomberAt(5, 5, 9).chars)
        def currentState = new GameState(board, true)
        def agent = new AstarBombChoppersAgent()
        int iteration = 0
        when:
        while (!isDeadChopper(currentState) && iteration <=  5+2) {
            Action action = agent.getAction(currentState)
            currentState = currentState.generateSuccessor(action)
            println "$currentState"
            iteration ++
        }

        then:
        assert iteration <=  5+2
    }

    def "dead bomber initial state"() {
        def startState = new GameState("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼###    #♥# ##          #   #   ☼☼ ☼ ☼#☼ ☼ ☼ ☼&☼Ѡ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼☼           & H҉҉҉҉ ### ##  #   ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼҉☼ ☼ ☼#☼ ☼#☼ ☼ ☼ ☼☼      &       H                ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼       # ####       #    # # # ☼☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼#☼ ☼ ☼☼  #                  #         ☼☼&☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼        ## #         #    &  # ☼☼ ☼ ☼ ☼&☼ ☼&☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼☼ #       #      #              ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼&☼ ☼ ☼#☼☼          #    #  # #          ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼ ☼ ☼ ☼ ☼☼     #         ## #         #  ☼☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼♥☼ ☼ ☼ ☼ ☼ ☼ ☼☼  #              ##    ##  ##  ☼☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼#☼ ☼ ☼ ☼#☼ ☼#☼ ☼☼                           # ##☼☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼#☼#☼ ☼#☼ ☼ ☼#☼☼   #         #       #         ☼☼ ☼ ☼#☼ ☼ ☼ ☼#☼ ☼ ☼ ☼#☼#☼ ☼ ☼ ☼ ☼☼         #   #    ## #         ☼☼ ☼ ☼#☼ ☼ ☼&☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼#☼☼              #      ##        ☼☼ ☼#☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼#☼☼       #  #           #       #☼☼ ☼ ☼#☼ ☼#☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼#☼ ☼ ☼ ☼☼    #     &           #        ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼")
        def agent = new AstarBombChoppersAgent()


        when:
        def action = agent.getAction(startState)

        then:
        assert action != null
    }

    public boolean isDeadChopper(GameState state) {
        List<ElementState> choppers = state.getChoppers();
        for (ElementState chopper : choppers) {
            if (chopper.isDead()) {
                return true;
            }
        }
        return false;
    }

}
