package calculator.StaticClasses.Parsers;

import calculator.MyComplexNumber;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for parsing string expressions into MyComplexNumber objects.
 * Supports various formats of complex number representation.
 */
public class ComplexNumberParser {

    // Regex patterns for complex number formats
    private static final String COMPLEX_NUMBER_REGEX = 
            "^\\s*" +  // Start of string with optional whitespace
            "([-+]?\\d*\\.?\\d*)" +  // Real part (optional sign, numbers, optional decimal)
            "(?:" +  // Start non-capturing group for imaginary part
            "([-+])" +  // Sign of imaginary part
            "(\\d*\\.?\\d*)" +  // Magnitude of imaginary part
            "i\\s*" +  // 'i' followed by optional whitespace
            ")?" +  // End non-capturing group (optional)
            "$";  // End of string

    private static final Pattern COMPLEX_PATTERN = Pattern.compile(COMPLEX_NUMBER_REGEX);
    
    /**
     * Parse a string into a complex number.
     * Supports formats like: "3+4i", "-2-3i", "5", "i", "-i", "3i", etc.
     * 
     * @param input The string to parse
     * @return A MyComplexNumber object representing the parsed complex number
     * @throws IllegalArgumentException If the input does not represent a valid complex number
     */
    public static MyComplexNumber parse(String input) {
        // Handle special case for just "i" (1i)
        if (input.trim().equals("i")) {
            return new MyComplexNumber(0, 1);
        }
        
        // Handle special case for just "-i" (-1i)
        if (input.trim().equals("-i")) {
            return new MyComplexNumber(0, -1);
        }
        
        // Replace whitespace for consistent parsing
        String cleanInput = input.replaceAll("\\s+", "");
        
        // Try with standard regex pattern first
        Matcher matcher = COMPLEX_PATTERN.matcher(cleanInput);
        
        if (matcher.matches()) {
            String realStr = matcher.group(1);
            String imagSign = matcher.group(2);  // Sign of imaginary part
            String imagMagnitude = matcher.group(3);  // Magnitude of imaginary part
            
            // Parse real part (default to 0 if missing)
            double real = (realStr.isEmpty() || realStr.equals("+") || realStr.equals("-")) ? 
                        0 : Double.parseDouble(realStr);
            
            // If there's no imaginary part
            if (imagSign == null) {
                return new MyComplexNumber(real, 0);
            }
            
            // Parse imaginary part
            double imag;
            if (imagMagnitude.isEmpty()) {
                imag = 1.0;  // Default to 1 if only "i" or "+i" or "-i"
            } else {
                imag = Double.parseDouble(imagMagnitude);
            }
            
            imag = imagSign.equals("-") ? -imag : imag;
            
            return new MyComplexNumber(real, imag);
        }
        
        // Handle just imaginary part without real part
        if (cleanInput.endsWith("i")) {
            String imagPartStr = cleanInput.substring(0, cleanInput.length() - 1);
            
            // Empty string means just "i"
            if (imagPartStr.isEmpty()) {
                return new MyComplexNumber(0, 1);
            }
            
            try {
                double imagPart = Double.parseDouble(imagPartStr);
                return new MyComplexNumber(0, imagPart);
            } catch (NumberFormatException e) {
                // Not a valid number
            }
        }
        
        // Try parsing as just a real number
        try {
            double real = Double.parseDouble(cleanInput);
            return new MyComplexNumber(real, 0);
        } catch (NumberFormatException e) {
            // Not a valid number
        }
        
        throw new IllegalArgumentException("Invalid complex number format: " + input);
    }
    
    /**
     * Test method to validate parsing of various complex number formats.
     */
    public static void main(String[] args) {
        String[] testExpressions = {
            "3+4i",
            "-2-3i",
            "5",
            "i",
            "-i",
            "3i",
            "3.5-2.7i",
            "+4-i",
            "0+0i",
            "  2.5 + 3.8i  "
        };
        
        for (String expr : testExpressions) {
            try {
                MyComplexNumber result = parse(expr);
                System.out.println("Input: " + expr + " => " + result);
            } catch (Exception e) {
                System.out.println("Error parsing " + expr + ": " + e.getMessage());
            }
        }
    }
}