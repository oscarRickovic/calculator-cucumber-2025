package calculator;

import visitor.Visitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Operation is an abstract class that represents arithmetic operations,
 * which are a special kind of Expressions, just like numbers are.
 *
 * @see Expression
 * @see MyNumber
 */
public abstract class Operation implements Expression
{
	/**
	 * The list of expressions passed as an argument to the arithmetic operation
	 */
	public List<Expression> args;

	/**
	 * The character used to represent the arithmetic operation (e.g. "+", "*")
	 */
	protected String symbol;

	/**
	 * The neutral element of the operation (e.g. 1 for *, 0 for +)
	 */
	protected int neutral;

	/**
	 * The notation used to render operations as strings.
	 * By default, the infix notation will be used.
	 */
	public Notation notation = Notation.INFIX;

	/** It is not allowed to construct an operation with a null list of expressions.
	 * Note that it is allowed to have an EMPTY list of arguments.
	 *
	 * @param elist    The list of expressions passed as argument to the arithmetic operation
	 * @throws IllegalConstruction    Exception thrown if a null list of expressions is passed as argument
	 */
	protected Operation(List<Expression> elist) throws IllegalConstruction {
		this(elist, null);
	}

	/** To construct an operation with a list of expressions as arguments,
	 * as well as the Notation used to represent the operation.
	 *
	 * @param elist    The list of expressions passed as argument to the arithmetic operation
	 * @param n     The notation to be used to represent the operation
	 * @throws IllegalConstruction    Exception thrown if a null list of expressions is passed as argument
	 */
	protected Operation(List<Expression> elist, Notation n) throws IllegalConstruction {
		operationConstructor(elist, n);
	}

	protected Operation(Notation n, Expression... elements) throws IllegalConstruction {
		operationConstructor(Arrays.asList(elements), n);
	}

	protected void operationConstructor(List<Expression> elist, Notation n) throws IllegalConstruction {
		if (n != null) notation = n;
		if (elist == null) {
			throw new IllegalConstruction();
		} else {
			args = new ArrayList<>(elist);
			handleNotationNestedConflict();
		}
	}

	/**
	 * getter method to return the number of arguments of an arithmetic operation.
	 *
	 * @return The number of arguments of the arithmetic operation.
	 */
	public List<Expression> getArgs() {
		return args;
	}

	/**
	 * Abstract method representing the actual binary arithmetic operation to compute
	 * @param l    first argument of the binary operation
	 * @param r    second argument of the binary operation
	 * @return    result of computing the binary operation
	 */
	public abstract int op(int l, int r);

	/**
	 * Performs the operation on two numbers, supporting both real and complex numbers.
	 *
	 * @param l    first operand
	 * @param r    second operand
	 * @return     the result as a Number (could be ComplexNumber)
	 * @throws Exception if an error occurs
	 */
	public abstract Number op(Number l, Number r) throws Exception;

	/**
	 * Performs the operation on two complex numbers.
	 * This method handles operations when at least one operand is a ComplexNumber.
	 *
	 * @param l first operand
	 * @param r second operand
	 * @return the result as a ComplexNumber
	 */
	public ComplexNumber op(ComplexNumber l, ComplexNumber r) throws Exception {
		if (this instanceof Plus) {
			return l.add(r);
		} else if (this instanceof Minus) {
			return l.subtract(r);
		} else if (this instanceof Times) {
			return l.multiply(r);
		} else if (this instanceof Divides) {
			return l.divide(r);
		} else {
			throw new UnsupportedOperationException("Operation not supported for ComplexNumbers.");
		}
	}

	/** Add more parameters to the existing list of parameters
	 *
	 * @param params    The list of parameters to be added
	 */
	public void addMoreParams(List<Expression> params) {
		args.addAll(params);
	}

	/**
	 * Accept method to implement the visitor design pattern to traverse arithmetic expressions.
	 * Each operation will delegate the visitor to each of its arguments expressions,
	 * and will then pass itself to the visitor object to get processed by the visitor object.
	 *
	 * @param v    The visitor object
	 */
	public void accept(Visitor v) {
		for (Expression a : args) {
			a.accept(v);
		}
		v.visit(this);
	}

	/**
	 * Count the depth of an arithmetic expression recursively.
	 *
	 * @return The depth of the arithmetic expression being traversed
	 */
	public final int countDepth() {
		return 1 + args.stream().mapToInt(Expression::countDepth).max().getAsInt();
	}

	/**
	 * Count the number of operations contained in an arithmetic expression recursively.
	 *
	 * @return The number of operations contained in an arithmetic expression being traversed
	 */
	public final int countOps() {
		return 1 + args.stream().mapToInt(Expression::countOps).reduce(Integer::sum).getAsInt();
	}

	public final int countNbs() {
		return args.stream().mapToInt(Expression::countNbs).reduce(Integer::sum).getAsInt();
	}

	@Override
	public final String toString() {
		return toString(notation);
	}

	public final String toString(Notation n) {
		Stream<String> s = args.stream().map(Object::toString);
		return switch (n) {
			case INFIX -> "( " + s.reduce((s1, s2) -> s1 + " " + symbol + " " + s2).get() + " )";
			case PREFIX -> symbol + " " + "(" + s.reduce((s1, s2) -> s1 + ", " + s2).get() + ")";
			case POSTFIX -> "(" + s.reduce((s1, s2) -> s1 + ", " + s2).get() + ")" + " " + symbol;
		};
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (this == o) return true;
		if (getClass() != o.getClass()) return false;

		Operation other = (Operation) o;
		return this.args.equals(other.getArgs());
	}

	@Override
	public int hashCode() {
		int result = 5, prime = 31;
		result = prime * result + neutral;
		result = prime * result + symbol.hashCode();
		result = prime * result + args.hashCode();
		return result;
	}

	public void handleNotationNestedConflict() {
		for (Expression e : this.args) {
			if (e instanceof Operation op && op.notation != this.notation) {
				op.notation = (this.notation == null ? Notation.INFIX : this.notation);
				op.handleNotationNestedConflict();
			}
		}
	}
}