package calculator.StaticClasses;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class StaticHelpers {
    public static final Scanner INPUT = new Scanner(System.in);

    public static final Map<String, String> matchingBrackets = Map.of(")", "(", "]", "[", "}", "{");
    public static final Set<String> openingBrackets = Set.of("(", "[", "{");
    public static final Set<String> closingBrackets = Set.of(")", "]", "}");
}
