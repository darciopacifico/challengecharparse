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
     * Main method for the test.
     * @param args
     */
    public static void main(String[] args) {

        analyzeCharSequence("aAbBABacfe");
        analyzeCharSequence("aAbBABacfu");
        analyzeCharSequence("aAbBABacf");
        analyzeCharSequence("zzzzzzzzzzzzzzzzzzzz");
        analyzeCharSequence("");

        analyzeCharSequence("aA+++bBAB&&&acfe");
        analyzeCharSequence("aAbBABacfE");

    }

    /**
     * Analyze the char sequence
     * @param charSeq
     */
    public static void analyzeCharSequence(String charSeq) {
        Optional<Character> optChar = parseAsStream(charSeq);

        if(optChar.isPresent())
            System.out.println("O caracter que coincide com as regras é: " + optChar.get());
        else
            System.out.println("Nao foi encontrado nenhum caracter que coincida com as regras!");
    }

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
