package controllers.helpers;

import java.util.regex.Pattern;

/**
 * A helper class to perform some common operations on strings.
 */
public class StringHelper {

    /**
     * Returns whether specified string is numeric.
     *
     * @param string The string to be checked.
     * @return Returns whether string is numeric.
     */
    public boolean isNumeric(String string) {
        return Pattern.matches("\\s*[0-9]+(\\.[0-9]+)?\\s*", string);
    }

    /**
     * Returns whether all strings in array are numeric.
     *
     * @param strings The array of strings to be checked.
     * @return Returns whether strings are numeric.
     */
    public boolean isNumeric(String[] strings) {
        boolean satisfied = true;
        for (String string : strings) satisfied = satisfied && isNumeric(string);
        return satisfied;
    }

    /**
     * Returns whether specified string is purely alphabetic.
     *
     * @param string The string to be checked.
     * @return Returns whether specified string is purely alphabetic.
     */
    public boolean isAlpha(String string) {
        return Pattern.matches("(\\s*[a-zA-Z]+\\s*)+", string);
    }

    /**
     * Returns whether all strings in array are purely alphabetic.
     *
     * @param strings The array of strings to be checked.
     * @return Returns whether strings are purely alphabetic.
     */
    public boolean isAlpha(String[] strings) {
        boolean satisfied = true;
        for (String string : strings) satisfied = satisfied && isAlpha(string);
        return satisfied;
    }

    /**
     * Capitalizes first letter of string.
     *
     * @param string The string to capitalize.
     * @return Returns the string with first letter capitalized.
     */
    public String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
