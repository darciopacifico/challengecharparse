package br.com.avaliacao.parser;

import java.util.Iterator;
import java.util.Optional;

/**
 * Created by darcio on 8/22/16.
 *
 * Client for the character StateMachine
 */
public class StreamParser {

    /**
     * Analyze the stream and optionally return the wanted character
     *
     * @param charSeq
     * @return
     */
    public static Optional<Character> parseAsStream(String charSeq) {
        if(charSeq==null) throw new IllegalArgumentException("Charseq is null!");

        Iterator<Character> itChars = CharTypeUtils.toListChar(charSeq).iterator();

        return getSpecialVowel(itChars);
    }

    /**
     * Transpose the stream, signaling every read chars as one transition to the state machine.
     * @return optional char
     * @param itChars
     */
    private static Optional<Character> getSpecialVowel(Iterator<Character> itChars){
        CharStateMachine internalState = CharStateMachine.initialState;

        while(itChars.hasNext())
            internalState = internalState.signal(itChars.next());

        return internalState.getResult();
    }

}
