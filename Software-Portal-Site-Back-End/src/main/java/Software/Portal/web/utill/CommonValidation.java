package Software.Portal.web.utill;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class CommonValidation {

    private static final Pattern PATTERNNIC = Pattern.compile("^(\\d{9}[A-Za-z]|\\d{12})$");
    private static final Pattern PATTERNCONTACT = Pattern.compile("^\\d{10,15}$");


    public static boolean isNumberInRange(int number) {
        return number >= 10 && number <= 40;
    }


    public static boolean isValidInput(String input) {
        if (input == null) {
            return false;
        }
        return PATTERNNIC.matcher(input).matches();
    }

    public static boolean isValidNumber(String input) {
        if (input == null) {
            return false;
        }
        return PATTERNCONTACT.matcher(input).matches();
    }

        public static boolean stringNullValidation(String inputString) {
            return inputString == null || inputString.isEmpty();
        }

//    public  static int generateSixDigitRandomNumber() {
//        return ThreadLocalRandom.current().nextInt(100000, 1000000);
//    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  // Matches numbers with optional '-' and decimal
    }
    }
