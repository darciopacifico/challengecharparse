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
        analyzeCharSequence("zzzzzzzzzzzEzzzzzzzz");
    }

    /**
     * Analyze the char sequence
     * @param charSeq
     */
    private static void analyzeCharSequence(String charSeq) {
        Iterator<Character> itChars = CharTypeUtils.toListChar(charSeq).iterator();

        Optional<Character> optChar = getSpecialVowel(itChars);

        if(optChar.isPresent())
            System.out.println("O caracter que coincide com as regras é: " + optChar.get());
        else
            System.out.println("Nao foi encontrado nenhum caracter que coincida com as regras!");
    }

    /**
     * Transpose the stream, signaling every read chars as one transition to the state machine.
     * @return optional char
     * @param itChars
     */
    public static Optional<Character> getSpecialVowel(Iterator<Character> itChars){
        CharStateMachine internalState = CharStateMachine.initialState;

        while(itChars.hasNext())
            internalState = internalState.signal(itChars.next());

        return internalState.getResult();
    }

}
