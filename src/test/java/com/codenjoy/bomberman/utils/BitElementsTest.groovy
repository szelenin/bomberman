package com.codenjoy.bomberman.utils

import spock.lang.Specification

/**
 * Created by szelenin on 2/18/14.
 */
class BitElementsTest extends Specification {
    def "getBit range"() {
        BitElements elements = new BitElements(1)
        when:
        elements.setBit(1)
        then:
        thrown(IllegalArgumentException)
    }

    def "setBit"() {
        BitElements elements = new BitElements(2)
        when:
        elements.setBit(0)
        then:
        assert elements.getBit(0)
        assert !elements.getBit(1)
    }

    def "setBit higher byte"() {
        BitElements elements = new BitElements(2 * 8 + 3)
        when:
        elements.setBit(2 * 8 + 3 - 1)
        then:
        assert elements.getBit(2 * 8 + 3 - 1)
        assert !elements.getBit(2 * 8)
    }

}
