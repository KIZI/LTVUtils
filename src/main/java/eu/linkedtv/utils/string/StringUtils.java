package eu.linkedtv.utils.string;

public class StringUtils {
    /**
     * Method to get the substring of length n from the end of a string.
     * 
     * @param s
     *            - String from which substring to be extracted.
     * @param substringLength
     *            - Desired length of the substring.
     * @return lastCharacters
     */
    public static String reverseSubstring(String s, int substringLength) {
        int length = s.length();
        if (length <= substringLength) {
            return s;
        }
        int startIndex = length - substringLength;
        
        return s.substring(startIndex);
    }
}
