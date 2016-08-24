package br.com.avaliacao.parser;

import java.util.Optional;

/**
 * Created by darcio on 8/22/16.
 *
 * Nondeterministic Finite Automaton implementation for char transitions:
 *
 *                            +-------+
 *                            |       |
 *                        Consonant   |
 *                            |       |
 *                         +--+-------v--+
 *                         |             |
 *                  +------>  WaitVowel  +-----+
 *                  |      |             |     |
 *                  |      +-------------+     |
 *             Consonant                    Vowel
 *                  |                          |     +------+
 *                  |                          |     |      |
 *                  |                          |     |   Consonant
 *       +----------+----+                +----v-----+-+    |
 *       |  (INITIAL)    |                |            <----+
 *       | WaitConsonant |                |  WaitEnd   |
 *       |               |                |            +----+
 *       +------------+-^+                +---------^--+    |
 *           ^        | |     Same           |      |     Different
 *           |        | +-----Last-Vowel-----+      |     Vowel
 *         Vowel      |                             |       |
 *           |        |                             +-------+
 *           +--------+
 *
 * (Generics definitions was suppressed for simplicity)
 *
 * There are three possible states: two static states, with Optional.Empty as result, waitConsonant and waitVowel
 * and a third special state, that stores the vowel char in a special condition.
 *
 * The third state is the only one instantiated under demand, due to need to store the char. The other ones are static.
 *
 * At every signal there are only two transitions for the static states and three transitions for
 * the special "waitEnd" state:
 *  1. Consonant
 *  2. Different Vowel
 *  3. Same Vowel
 *
 * The state transitions are returned by the "signal(char)" method and should be hold by the StateMachine client.
 *
 * This class are stateless and thread safe.
 *
 * Asymptotic Analysis:
 * ---------------------
 * The performance is proportional to O(N) characters comparisons.
 * The memory usage is constant, O(1). The discarded WaitEndState states will be collected by the GC periodically.
 *
 */
public abstract class CharStateMachine {

    //possible intermediary states
    private static final CharStateMachine waitConsonant = new WaitConsonantState();
    private static final CharStateMachine waitVowel = new WaitVowelState();

    /**
     * Initial state! Apply the signal transitions (chars) and hold the next state!
     */
    public static final CharStateMachine initialState = waitConsonant;

    //contract for state machine
    public abstract CharStateMachine signal(Character input);
    public abstract Optional<Character> getResult();

    /**
     * Accessible only by its initialState attribute.
     * No need for constructors or factories.
     */
    private CharStateMachine(){}

    /**
     * Intermediary state to detect the vowel condition
     */
    private static class WaitConsonantState extends CharStateMachine {
        @Override
        public CharStateMachine signal(Character input) {
            if (CharTypeUtils.isConsonant(input))
                return waitVowel;
            else
                return this;
        }

        @Override
        public Optional<Character> getResult() {return Optional.empty();}
    }

    /**
     * Itermediary state to detect the vowel condition
     */
    private static class WaitVowelState extends CharStateMachine {
        @Override
        public CharStateMachine signal(Character input) {
            if (CharTypeUtils.isConsonant(input))
                return this;
            else
                return new WaitEndState(input);

        }

        @Override
        public Optional<Character> getResult() {return Optional.empty();}
    }


    /**
     * The state that hold the qualified vowel.
     */
    private class WaitEndState extends CharStateMachine {

        private Optional<Character> champChar = Optional.empty();

        public WaitEndState(Character champChar) {
            this.champChar = Optional.of(champChar);
        }

        @Override
        public CharStateMachine signal(Character input) {
            if (CharTypeUtils.isConsonant(input))
                return this;
            else

                if(input.toString().toUpperCase().equals(this.champChar.get().toString().toUpperCase()))
                    return waitConsonant;
                else
                    return this;

        }

        @Override
        public Optional<Character> getResult() {return this.champChar;}

    }
}