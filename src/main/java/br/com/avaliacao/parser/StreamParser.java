package br.com.avaliacao.parser;

import javax.swing.text.html.Option;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

        //emulates a char stream
        Stream<Character> charStream = CharTypeUtils.toListChar(charSeq).stream();

        return getSpecialVowel(charStream);
    }

    /**
     * Transpose the stream, signaling every read chars as one transition to the state machine.
     * @return optional char
     * @param charStream char stream
     */
    private static Optional<Character> getSpecialVowel(Stream<Character> charStream ){
        CharacterConsumer consumer = new CharacterConsumer();

        charStream.forEach(consumer);

        return consumer.getResult();
    }


    /**
     * Consumer for the char stream. Wraps the CharStateMachine instance.
     */
    private static class CharacterConsumer implements Consumer<Character> {
        CharStateMachine internalState = CharStateMachine.initialState;

        @Override
        public void accept(Character character) {internalState = internalState.signal(character);}

        public Optional<Character> getResult(){return internalState.getResult();}
    }
}
