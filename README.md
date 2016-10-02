> /**
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
