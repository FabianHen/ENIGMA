import java.util.Scanner;

/**
 * The Main Class.
 * @author F. Henßler
 */
public class Main {
    private static final String LONGLINE = "==================";
    private static final String SHORTLINE = LONGLINE.substring(0,LONGLINE.length()/2);

    /**
     * The Main method always returns the same question with six possible answers using a while loop.
     * In the form of a number input, the user can then choose one of the commands.
     * This is then carried out with the help of a switch case.
     */
    public static void main(String[] args) {
        Enigma enigma = new Enigma();

        boolean keepgoing = true;
        while (keepgoing) {
            System.out.println("What's your plan?");
            System.out.println("Press 1 to encrypt a message");
            System.out.println("Press 2 to decrypt a message");
            System.out.println("Press 3 to enter a ringposition ");
            System.out.println("Press 4 to set a plug");
            System.out.println("Press 5 to see/remove plugs");
            System.out.println("Press 6 to exit");
            System.out.println(SHORTLINE + (enigma.getRingPosition()[0]+1) + "/" + (enigma.getRingPosition()[1]+1) + "/"
                    + (enigma.getRingPosition()[2]+1) + SHORTLINE);
            Scanner in = new Scanner(System.in);
            String input = in.nextLine();
            int action = input.charAt(0) - 48;
            switch (action) {
                case 1 -> {
                    System.out.println("What do you want to encrypt? (Only A-Z, no numbers)");
                    String message = in.nextLine();
                    String messageNew = enigma.useOnMessage(message, false);
                    System.out.println("Your Message was encrypted into:" + messageNew);
                    System.out.print("With the Ringposition:");
                    System.out.println((enigma.getRingPositionLastMessage()[0]+1) + "/" +
                            (enigma.getRingPositionLastMessage()[1]+1) + "/" + (enigma.getRingPositionLastMessage()[2]+1));
                    System.out.println(LONGLINE);
                }
                case 2 -> {
                    System.out.println("What do you want to decrypt? (Only A-Z, no numbers)");
                    String messageToDecrypt = in.nextLine();
                    String messageDecrypted = enigma.useOnMessage(messageToDecrypt, true);
                    System.out.println("Your Message was decrypted into:" + messageDecrypted);
                    System.out.println(LONGLINE);
                }
                case 3 -> {
                    int[] ringpositionUpdate = new int[3];
                    int position;
                    System.out.println("Set first wheel to (number): ");
                    try {
                        position = in.nextInt();
                    }catch (Exception e) {
                        System.out.println("Invalid input");
                        in.nextLine();
                        System.out.println(LONGLINE);
                        break;
                    }
                    ringpositionUpdate[0] = position;
                    System.out.println("Set second wheel to (number): ");
                    try {
                        position = in.nextInt();
                    }catch (Exception e) {
                        System.out.println("Invalid input");
                        in.nextLine();
                        System.out.println(LONGLINE);
                        break;
                    }
                    ringpositionUpdate[1] = position;
                    System.out.println("Set third wheel to (number):  ");
                    try {
                        position = in.nextInt();
                    }catch (Exception e) {
                        System.out.println("Invalid input");
                        in.nextLine();
                        System.out.println(LONGLINE);
                        break;
                    }
                    ringpositionUpdate[2] = position;
                    enigma.setRollersTo(ringpositionUpdate);
                    System.out.println("Rings have been set to " + ringpositionUpdate[0] + "/" + ringpositionUpdate[1] +
                            "/" + ringpositionUpdate[2]);
                    System.out.println(LONGLINE);
                }
                case 4 -> {
                    String plugFrom;
                    String plugTo;
                    System.out.println("Setting a plug from (letter A-Z)");
                    do {
                        plugFrom = in.nextLine();
                        System.out.println(plugFrom);
                    }while(!enigma.checkForLetter(plugFrom));
                    if(!enigma.isPlugOccupied(plugFrom)) {
                        System.out.println("to (letter A-Z) ");
                        do {
                            plugTo = in.nextLine();
                            System.out.println(plugTo);
                        }while(!enigma.checkForLetter(plugTo));
                        if(!enigma.isPlugOccupied(plugTo) && !plugFrom.equals(plugTo)){
                        enigma.setPlug(plugFrom.charAt(0), plugTo.charAt(0));
                        System.out.println("Plug has been set");
                        }
                        else{
                            System.out.println("This plug is already used, press 5 to see set plugs");
                        }
                    }
                    else{
                        System.out.println("This plug is already used, press 5 to see set plugs");
                    }
                    System.out.println(SHORTLINE + (enigma.getRingPosition()[0]+1) + "/"+ (enigma.getRingPosition()[1]+1) + "/"
                            + (enigma.getRingPosition()[2]+1) + SHORTLINE);
                }
                case 5 -> {
                    String answer;
                    char[][] plugs = enigma.getPlugs();
                    int length = enigma.findFirstUnusedPlug();
                    if(length == 11) length = 10;
                    System.out.println("Used plugs:");
                    for(int i = 0; i < length; i++){
                        System.out.println((i+1)+". " + plugs[i][0] + "-" +plugs[i][1]);
                    }
                    System.out.println("unused PLugs: " + (10-length));
                    System.out.println("Do you want to remove any? (Y/N)");
                    answer = in.nextLine();
                    if(answer.equalsIgnoreCase("Y")){
                        System.out.println("Which one? (number)");
                        int remove;
                        remove = in.nextInt();
                        enigma.removePlug(remove);
                    }
                    System.out.println(SHORTLINE + (enigma.getRingPosition()[0]+1) + "/"+ (enigma.getRingPosition()[1]+1) + "/"
                            + (enigma.getRingPosition()[2]+1) + SHORTLINE);
                }
                case 6 -> {
                    System.out.println("OK. Exiting...");
                    keepgoing = false;
                }
                default -> System.out.print("");
            }
        }
    }
}
