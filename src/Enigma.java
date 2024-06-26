import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Enigma class, in which messages can be both encrypted and decrypted.
 * @author F. Henßler
 */
public class Enigma {

    private final ArrayList<Character[][]> roller1 = new ArrayList<>();
    private final ArrayList<Character[][]> roller2 = new ArrayList<>();
    private final ArrayList<Character[][]> roller3 = new ArrayList<>();
    private final ArrayList<Character> letters = new ArrayList<>();
    private final ArrayList<Character[]> reversingRoller = new ArrayList<>();
    private int[] ringPositionLastMessage;
    private final char[][] plugs;

    public Enigma(){
        createRollers();
        List<Character> letterList = Arrays.asList('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q',
                'R', 'S','T','U','V','W','X','Y','Z');
        letters.addAll(letterList);
        ringPositionLastMessage = new int [3];
        plugs = new char[10][2];
    }

    /**
     * A given message is encrypted letter by letter using the "encryptLetter" method. As with the original Enigma,
     * the letter is passed through the board twice. If the parameter "decrypt" decides that a message is decrypted and
     * not decrypted, the last used ring position is automatically used.
     * @param message the message to be encrypted
     * @param decrypt decides whether to encrypt or decrypt the message
     * @return the encrypted message as a string
     */
    public String useOnMessage(String message, boolean decrypt){
        if(decrypt){
            setRollersTo(ringPositionLastMessage);
        }
        else {
            ringPositionLastMessage = getRingPosition();
        }

        String encryptedMessage = "";
        message = message.toUpperCase();
        for(int i= 0; i <= message.length() - 1;i++){
            if(letterToNumber(message.charAt(i)) < 30) {
                char encryptedLetter = putLetterThroughPlugs(encryptLetter(putLetterThroughPlugs(message.charAt(i))));
                encryptedMessage = encryptedMessage + encryptedLetter;
            }
        }
        return encryptedMessage;
    }

    /**
     * Passes through the individual reels with a letter and then returns the encrypted letter.
     * @param pChar the character to be encoded
     * @return the encoded character
     */
    private char encryptLetter(char pChar){
        int next = letterToNumber(pChar);
        char searched;

        searched = roller1.get(next)[1][1];
        next = searchInRoller(0, searched, roller1);
        searched = roller2.get(next)[1][1];
        next = searchInRoller(0, searched, roller2);
        searched = roller3.get(next)[1][1];
        next = searchInRoller(0, searched, roller3);
        searched = reversingRoller.get(next)[1];
        for(int i=0; i<26; i++){
            if(reversingRoller.get(i)[0] == searched){
                next = i;
                break;
            }
        }
        searched = roller3.get(next)[0][1];
        next = searchInRoller(1, searched, roller3);
        searched = roller2.get(next)[0][1];
        next = searchInRoller(1, searched, roller2);
        searched = roller1.get(next)[0][1];
        next = searchInRoller(1, searched, roller1);
        spinWhole();
        return letters.get(next);
    }

    /**
     * Finds a letter in a roller and returns the position of that letter.
     *
     * @param side on which side of the roller should be searched
     * @param searched which character is searched
     * @param roller the roller in which the letter is searched
     * @return at what height the searched character in the roller is
     */
    public int searchInRoller(int side, char searched, ArrayList<Character[][]> roller){
        for(int i=0; i<26; i++){
            if(roller.get(i)[side][0] == searched){
                return i;
            }
        }
        return 0;
    }

    /**
     * Spin the entire reel, starting with 'roller1'. Rollers 2 and 3 rotate with 'q' and 'e'.
     */
    private void spinWhole(){
        if(roller1.get(0)[0][0] == 'q'){
            if(roller2.get(0)[0][0] == 'e'){
                Character[][] help3;
                help3 = roller3.get(0);
                roller3.remove(0);
                roller3.add(help3);
            }
            Character[][] help2;
            help2 = roller2.get(0);
            roller2.remove(0);
            roller2.add(help2);
        }
        Character[][] help1;
        help1 = roller1.get(0);
        roller1.remove(0);
        roller1.add(help1);
    }

    /**
     * Rotates the first roller one time. This deletes the first entry of the list and appends it to the back.
     */
    private void spinRoller1(){
        Character[][] help1;
        help1 = roller1.get(0);
        roller1.add(help1);
        roller1.remove(0);
    }
    /**
     * Rotates the second roller one time. This deletes the first entry of the list and appends it to the back.
     */
    private void spinRoller2(){
        Character[][] help2;
        help2 = roller2.get(0);
        roller2.remove(0);
        roller2.add(help2);
    }
    /**
     * Rotates the third roller one time. This deletes the first entry of the list and appends it to the back.
     */
    private void spinRoller3(){
        Character[][] help3;
        help3 = roller3.get(0);
        roller3.remove(0);
        roller3.add(help3);
    }

    /**
     * Sets the reels of the Enigma to a certain position.
     * @param pRingPosition the desired roll position
     */
    public void setRollersTo(int[] pRingPosition){
        if (pRingPosition.length == 3) {

            char posRoller1 = Character.toLowerCase(letters.get(pRingPosition[0]));
            char posRoller2 = Character.toLowerCase(letters.get(pRingPosition[1]));
            char posRoller3 = Character.toLowerCase(letters.get(pRingPosition[2]));

            while (!roller1.get(0)[0][0].equals(posRoller1)) {
                spinRoller1();
            }
            while (!roller2.get(0)[0][0].equals(posRoller2)) {
                spinRoller2();
            }
            while (!roller3.get(0)[0][0].equals(posRoller3)) {
                spinRoller3();
            }
        }
    }

    /**
     * Returns the current reel position as numbers from right to left.
     * @return the current roller position as array
     */
    public int[] getRingPosition(){
        int [] ringPosition = new int [3];
        ringPosition[2]= letterToNumber(Character.toUpperCase(roller1.get(0)[0][0]));
        ringPosition[1]= letterToNumber(Character.toUpperCase(roller2.get(0)[0][0]));
        ringPosition[0]= letterToNumber(Character.toUpperCase(roller3.get(0)[0][0]));
        return ringPosition;
    }

    /** @return ringPositionLastMessage */
    public int[] getRingPositionLastMessage(){
        return ringPositionLastMessage;
    }

    /**
     * Converts a given letter to another letter if it appears in the plug array.
     * @param pChar the letters to be converted
     * @return the converted letter
     */
    public char putLetterThroughPlugs(char pChar){
        int unused = findFirstUnusedPlug();
        for (int i = 0; i < unused; i++) {
         if(plugs[i][0] == pChar){
             return plugs[i][1];
         }
         else if(plugs[i][1] == pChar){
             return plugs[i][0];
         }
        }
        return pChar;
    }

    /**
     * Fills the first empty space of the plug array with given letters
     * @param plug1 Plug 1 of the cable
     * @param plug2 Plug 2 of the cable
     */
    public void setPlug(char plug1, char plug2){
       int plugSpot = findFirstUnusedPlug();
        if(plugSpot != 11){
            plugs[plugSpot][0] = plug1;
            plugs[plugSpot][1] = plug2;
        }
    }

    /**
     * Searches for a string in the plug array. If it exists, true is returned.
     * @param searched the searched string
     * @return if Plug is occupied
     */
    public boolean isPlugOccupied(String searched){
        for (char[] plug : plugs) {
            if (plug[0] == searched.charAt(0) || plug[1] == searched.charAt(0)) {
                return true;
            }
        }
        return false;

    }

    /**
     * sets any index of the plug array to 0 and sorts it backwards
     * @param index the index to be removed
     */
    public void removePlug(int index){
        plugs[index-1][0] = 0;
        plugs[index-1][1] = 0;
        for(int i = index-1; i< plugs.length; i++){
            if(i == plugs.length -1){
                plugs[i][0] = 0;
                plugs[i][1] = 0;
            }
            else {
                plugs[i][0] = plugs[i + 1][0];
                plugs[i][1] = plugs[i + 1][1];
            }
        }

    }
    /** @return the first empty index of the plug array*/
    public int findFirstUnusedPlug() {
        for (int i = 0; i < plugs.length; i++) {
            if (plugs[i][0] == 0) {
                return i;
            }
        }
        return 11;
    }

    /**
     * Getter for 'plugs'.
     * @return 'plugs'
     */
    public char[][] getPlugs(){
        return plugs;
    }

    /**
     * Specifies the position of a letter of the alphabet. Where 'A' is 0 and not 1.
     * @param a the letter to be converted
     * @return the position in the alphabet -1
     */
    public int letterToNumber(char a){
        return switch (a) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 6;
            case 'H' -> 7;
            case 'I' -> 8;
            case 'J' -> 9;
            case 'K' -> 10;
            case 'L' -> 11;
            case 'M' -> 12;
            case 'N' -> 13;
            case 'O' -> 14;
            case 'P' -> 15;
            case 'Q' -> 16;
            case 'R' -> 17;
            case 'S' -> 18;
            case 'T' -> 19;
            case 'U' -> 20;
            case 'V' -> 21;
            case 'W' -> 22;
            case 'X' -> 23;
            case 'Y' -> 24;
            case 'Z' -> 25;
            default -> 30;
        };
    }

    /**
     * Creates 3 predefined reels in the following pattern:
     * {'place', 'link to the right'} {'place', 'link to the left'}
     */
    public void createRollers(){
        roller1.add(new Character[][]{{'a', 'q'},{'a', 'j'}});
        roller1.add(new Character[][]{{'b', 'j'},{'b', 'd'}});
        roller1.add(new Character[][]{{'c', 's'},{'c', 'h'}});
        roller1.add(new Character[][]{{'d', 'b'},{'d', 'o'}});
        roller1.add(new Character[][]{{'e', 't'},{'e', 's'}});
        roller1.add(new Character[][]{{'f', 'x'},{'f', 'u'}});
        roller1.add(new Character[][]{{'g', 'z'},{'g', 'n'}});
        roller1.add(new Character[][]{{'h', 'c'},{'h', 'v'}});
        roller1.add(new Character[][]{{'i', 'n'},{'i', 'w'}});
        roller1.add(new Character[][]{{'j', 'a'},{'j', 'b'}});
        roller1.add(new Character[][]{{'k', 'y'},{'k', 'm'}});
        roller1.add(new Character[][]{{'l', 'o'},{'l', 'x'}});
        roller1.add(new Character[][]{{'m', 'k'},{'m', 'y'}});
        roller1.add(new Character[][]{{'n', 'g'},{'n', 'i'}});
        roller1.add(new Character[][]{{'o', 'd'},{'o', 'l'}});
        roller1.add(new Character[][]{{'p', 'v'},{'p', 'q'}});
        roller1.add(new Character[][]{{'q', 'p'},{'q', 'a'}});
        roller1.add(new Character[][]{{'r', 'w'},{'r', 't'}});
        roller1.add(new Character[][]{{'s', 'e'},{'s', 'c'}});
        roller1.add(new Character[][]{{'t', 'r'},{'t', 'e'}});
        roller1.add(new Character[][]{{'u', 'f'},{'u', 'z'}});
        roller1.add(new Character[][]{{'v', 'h'},{'v', 'p'}});
        roller1.add(new Character[][]{{'w', 'i'},{'w', 'r'}});
        roller1.add(new Character[][]{{'x', 'l'},{'x', 'f'}});
        roller1.add(new Character[][]{{'y', 'm'},{'y', 'k'}});
        roller1.add(new Character[][]{{'z', 'u'},{'z', 'g'}});

        roller2.add(new Character[][]{{'a', 'f'},{'a', 'e'}});
        roller2.add(new Character[][]{{'b', 's'},{'b', 'p'}});
        roller2.add(new Character[][]{{'c', 'h'},{'c', 'i'}});
        roller2.add(new Character[][]{{'d', 'o'},{'d', 's'}});
        roller2.add(new Character[][]{{'e', 'a'},{'e', 'v'}});
        roller2.add(new Character[][]{{'f', 'l'},{'f', 'a'}});
        roller2.add(new Character[][]{{'g', 'q'},{'g', 'y'}});
        roller2.add(new Character[][]{{'h', 'x'},{'h', 'c'}});
        roller2.add(new Character[][]{{'i', 'c'},{'i', 'x'}});
        roller2.add(new Character[][]{{'j', 'u'},{'j', 'l'}});
        roller2.add(new Character[][]{{'k', 'z'},{'k', 'r'}});
        roller2.add(new Character[][]{{'l', 'j'},{'l', 'f'}});
        roller2.add(new Character[][]{{'m', 'v'},{'m', 'o'}});
        roller2.add(new Character[][]{{'n', 'w'},{'n', 't'}});
        roller2.add(new Character[][]{{'o', 'm'},{'o', 'd'}});
        roller2.add(new Character[][]{{'p', 'b'},{'p', 'z'}});
        roller2.add(new Character[][]{{'q', 'y'},{'q', 'g'}});
        roller2.add(new Character[][]{{'r', 'k'},{'r', 'u'}});
        roller2.add(new Character[][]{{'s', 'd'},{'s', 'b'}});
        roller2.add(new Character[][]{{'t', 'n'},{'t', 'w'}});
        roller2.add(new Character[][]{{'u', 'r'},{'u', 'j'}});
        roller2.add(new Character[][]{{'v', 'e'},{'v', 'm'}});
        roller2.add(new Character[][]{{'w', 't'},{'w', 'n'}});
        roller2.add(new Character[][]{{'x', 'i'},{'x', 'h'}});
        roller2.add(new Character[][]{{'y', 'g'},{'y', 'q'}});
        roller2.add(new Character[][]{{'z', 'p'},{'z', 'k'}});

        roller3.add(new Character[][]{{'a', 'd'},{'a', 'd'}});
        roller3.add(new Character[][]{{'b', 'p'},{'b', 'g'}});
        roller3.add(new Character[][]{{'c', 'v'},{'c', 'e'}});
        roller3.add(new Character[][]{{'d', 'a'},{'d', 'a'}});
        roller3.add(new Character[][]{{'e', 'c'},{'e', 'h'}});
        roller3.add(new Character[][]{{'f', 'h'},{'f', 'i'}});
        roller3.add(new Character[][]{{'g', 'b'},{'g', 'k'}});
        roller3.add(new Character[][]{{'h', 'e'},{'h', 'f'}});
        roller3.add(new Character[][]{{'i', 'f'},{'i', 'n'}});
        roller3.add(new Character[][]{{'j', 's'},{'j', 'r'}});
        roller3.add(new Character[][]{{'k', 'g'},{'k', 'w'}});
        roller3.add(new Character[][]{{'l', 'z'},{'l', 'o'}});
        roller3.add(new Character[][]{{'m', 'w'},{'m', 't'}});
        roller3.add(new Character[][]{{'n', 'i'},{'n', 'p'}});
        roller3.add(new Character[][]{{'o', 'l'},{'o', 'x'}});
        roller3.add(new Character[][]{{'p', 'n'},{'p', 'b'}});
        roller3.add(new Character[][]{{'q', 'y'},{'q', 's'}});
        roller3.add(new Character[][]{{'r', 'j'},{'r', 'y'}});
        roller3.add(new Character[][]{{'s', 'q'},{'s', 'j'}});
        roller3.add(new Character[][]{{'t', 'm'},{'t', 'z'}});
        roller3.add(new Character[][]{{'u', 'x'},{'u', 'v'}});
        roller3.add(new Character[][]{{'v', 'u'},{'v', 'c'}});
        roller3.add(new Character[][]{{'w', 'k'},{'w', 'm'}});
        roller3.add(new Character[][]{{'x', 'o'},{'x', 'u'}});
        roller3.add(new Character[][]{{'y', 'r'},{'y', 'q'}});
        roller3.add(new Character[][]{{'z', 't'},{'z', 'l'}});

        reversingRoller.add(new Character[]{'a', 'l'});
        reversingRoller.add(new Character[]{'b', 'u'});
        reversingRoller.add(new Character[]{'c', 'z'});
        reversingRoller.add(new Character[]{'d', 'm'});
        reversingRoller.add(new Character[]{'e', 'i'});
        reversingRoller.add(new Character[]{'f', 'x'});
        reversingRoller.add(new Character[]{'g', 'q'});
        reversingRoller.add(new Character[]{'h', 'o'});
        reversingRoller.add(new Character[]{'i', 'e'});
        reversingRoller.add(new Character[]{'j', 's'});
        reversingRoller.add(new Character[]{'k', 'v'});
        reversingRoller.add(new Character[]{'l', 'a'});
        reversingRoller.add(new Character[]{'m', 'd'});
        reversingRoller.add(new Character[]{'n', 'y'});
        reversingRoller.add(new Character[]{'o', 'h'});
        reversingRoller.add(new Character[]{'p', 't'});
        reversingRoller.add(new Character[]{'q', 'g'});
        reversingRoller.add(new Character[]{'r', 'w'});
        reversingRoller.add(new Character[]{'s', 'j'});
        reversingRoller.add(new Character[]{'t', 'p'});
        reversingRoller.add(new Character[]{'u', 'b'});
        reversingRoller.add(new Character[]{'v', 'k'});
        reversingRoller.add(new Character[]{'w', 'r'});
        reversingRoller.add(new Character[]{'x', 'f'});
        reversingRoller.add(new Character[]{'y', 'n'});
        reversingRoller.add(new Character[]{'z', 'c'});
    }

    public boolean checkForLetter(String input){
        if(input == null || input.length() != 1) return false;
        return (input.toUpperCase().charAt(0) >= 65 && input.toUpperCase().charAt(0) <= 90);
    }
}
