package calculator;

import java.util.List;

/** This class represents the arithmetic sum operation "+".
 * The class extends an abstract superclass Operation.
 * Other subclasses of Operation represent other arithmetic operations.
 * @see Operation
 * @see Minus
 * @see Times
 * @see Divides
 */
public final class Plus extends Operation {

    protected static final List<String> SYMBOLES = List.of("+");

    /**
     * Class constructor specifying a number of Expressions to add.
     *
     * @param elist The list of Expressions to add
     * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
     */
    public Plus(List<Expression> elist) throws IllegalConstruction {
        this(elist, null);
    }

    /**
     * Class constructor specifying a number of Expressions to add,
     * as well as the Notation used to represent the operation.
     *
     * @param elist The list of Expressions to add
     * @param n The Notation to be used to represent the operation
     * @throws IllegalConstruction    If an empty list of expressions if passed as parameter
     */
    public Plus(List<Expression> elist, Notation n) throws IllegalConstruction {
        super(elist, n);
        symbol = "+";
        neutral = 0;
    }

    public Plus(Notation n, Expression... elist) throws IllegalConstruction {
        super(n, elist);
        symbol = "+";
        neutral = 0;
    }

    /**
     * The actual computation of the (binary) arithmetic addition of two integers
     * @param l The first integer
     * @param r The second integer that should be added to the first
     * @return The integer that is the result of the addition
     */
    public int op(int l, int r) {
        return (l + r);
    }

    /**
     * The actual computation of the (binary) arithmetic addition of two numbers,
     * handling both real and complex numbers.
     *
     * @param l The first number
     * @param r The second number that should be added to the first
     * @return The result of the addition as a Number (Integer, Double, or ComplexNumber)
     */
    public Number op(Number l, Number r) {
        // Si l'un des nombres est un nombre complexe, on effectue une addition complexe
        if (l instanceof ComplexNumber || r instanceof ComplexNumber) {
            ComplexNumber c1 = (l instanceof ComplexNumber) ? (ComplexNumber) l : new ComplexNumber(l.doubleValue(), 0);
            ComplexNumber c2 = (r instanceof ComplexNumber) ? (ComplexNumber) r : new ComplexNumber(r.doubleValue(), 0);
            return c1.add(c2);
        }

        // Addition classique pour les nombres réels
        if (l instanceof Double || r instanceof Double) {
            return l.doubleValue() + r.doubleValue();
        } else if (l instanceof Float || r instanceof Float) {
            return l.floatValue() + r.floatValue();
        } else if (l instanceof Long || r instanceof Long) {
            return l.longValue() + r.longValue();
        } else if (l instanceof Integer || r instanceof Integer) {
            return l.intValue() + r.intValue();
        } else if (l instanceof Short || r instanceof Short) {
            return l.shortValue() + r.shortValue();
        } else {
            return l.byteValue() + r.byteValue();
        }
    }
}