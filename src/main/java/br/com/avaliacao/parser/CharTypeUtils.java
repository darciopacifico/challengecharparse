package br.com.avaliacao.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utils class to check char type
 * Created by darcio on 8/23/16.
 */
public class CharTypeUtils {

    //ignorando caracteres acentuados por simplicidade. A lógica seria a mesma
    final static Set<Character> setVowels = toSetChar("aeiouAEIOU");
    final static Set<Character> setConsonants = toSetChar("bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ");

    public static boolean isVowel(char c){return setVowels.contains(c);}
    public static boolean isConsanant(char c){return setConsonants.contains(c);}

    /**
     * Transform a string into a Set o Chars.
     *
     * @param chars
     * @return
     */
    public static Set<Character> toSetChar(String chars) {
        Set<Character> setChars = new HashSet<>(chars.length());

        char[] arrChar = chars.toCharArray();
        for (int i = 0; i < arrChar.length; i++)
            setChars.add(arrChar[i]);
        return setChars;
    }



    /**
     * Transform a string into a List o Chars.
     *
     * @param chars
     * @return
     */
    public static List<Character> toListChar(String chars) {
        List<Character> listChars = new ArrayList<>(chars.length());

        char[] arrChar = chars.toCharArray();
        for (int i = 0; i < arrChar.length; i++)
            listChars.add(arrChar[i]);
        return listChars;
    }

}
