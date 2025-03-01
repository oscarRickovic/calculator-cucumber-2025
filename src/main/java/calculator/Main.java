package calculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A very simple calculator in Java
 * University of Mons - UMONS
 * Software Engineering Lab
 * Faculty of Sciences
 *
 * @author tommens
 */
public class Main {

	/**
	 * This is the main method of the application.
	 * It provides examples of how to use it to construct and evaluate arithmetic expressions.
	 *
	 * @param args	Command-line parameters are not used in this version
	 */
	public static void main(String[] args) {

  	Expression e;
  	Calculator c = new Calculator();

	try{

		e = new MyNumber(8);
		c.print(e);
		c.eval(e);

	    List<Expression> params = new ArrayList<>();
	    Collections.addAll(params, new MyNumber(3), new MyNumber(4), new MyNumber(5));
	    e = new Plus(params,Notation.PREFIX);
		c.printExpressionDetails(e);
		c.eval(e);
	
		List<Expression> params2 = new ArrayList<>();
		Collections.addAll(params2, new MyNumber(5), new MyNumber(3));
		e = new Minus(params2, Notation.INFIX);
		c.print(e);
		c.eval(e);

		List<Expression> params3 = new ArrayList<>();
		Collections.addAll(params3, new Plus(params), new Minus(params2));
		e = new Times(params3);
		c.printExpressionDetails(e);
		c.eval(e);

		List<Expression> params4 = new ArrayList<>();
		Collections.addAll(params4, new Plus(params), new Minus(params2), new MyNumber(5));
		e = new Divides(params4,Notation.POSTFIX);
		c.print(e);
		c.eval(e);
		
		//  ( (3 + 2) . (5 + (4 / 2)) / 2 )
		
		e = new Divides(null, 
			new Times(null,
				new Plus(null, new MyNumber(3), new MyNumber(2)),
				new Plus(null,
					new MyNumber(5),
					new Divides(null, new MyNumber(4), new MyNumber(2))))
			, new MyNumber(2)
			
		);

		c.print(e);
		c.eval(e);

		// (4 . 3 . 2 . 5) / 5 + 1  = 25

		e = new Plus(null, 
			new Divides(null,
				new Times(null, 
				new MyNumber(4), new MyNumber(3), new MyNumber(2), new MyNumber(5)) 
				, new MyNumber(5)
			)
			, new MyNumber(1)
		);

		c.print(e);
		c.eval(e);
	}

	catch(IllegalConstruction exception) {
		System.out.println("cannot create operations without parameters");
		}
 	}

}
