package br.com.avaliacao.parser;

import org.junit.Test;

import java.util.Optional;

import static br.com.avaliacao.parser.StreamParser.parseAsStream;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the StreamParser implementation
 */
public class StreamParserTest {

    /**
     * Regular tests
     */
    @Test
    public void testStateMachine(){

        assertEquals(parseAsStream("aAbBABacfe"), Optional.of('e'));
        assertEquals(parseAsStream("aAbBABacf"), Optional.empty());
        assertEquals(parseAsStream("aAbBABacfE"), Optional.of('E'));
        assertEquals(parseAsStream("aAbBABacfu"), Optional.of('u'));
        assertEquals(parseAsStream("aA+++bBAB&&&acfe"), Optional.of('e'));
        assertEquals(parseAsStream("aaaaaaa"), Optional.empty());
        assertEquals(parseAsStream("zzzzzzzzz"), Optional.empty());
        assertEquals(parseAsStream("zzzzzzzzzAzzzzzzBzzzzzC"), Optional.of('A'));
        assertEquals(parseAsStream(""), Optional.empty());


    }

    /**
     * Avoid NPE
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStateMachineNull(){

        assertEquals(parseAsStream(null), Optional.empty());

    }

}