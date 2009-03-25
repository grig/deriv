package deriv;

import junit.framework.TestCase;
import static deriv.Expr.*;

public class ExprTest extends TestCase {
	private static Var X = var("x");
	private static Var Y = var("y");
	
	public void testShouldProvideSaneToString() {
		Expr expr = mul(plus(val(3), X), Y);
		assertEquals("((3 + x) * y)", expr.toString());
	}
	
	public void testDerivativeOfConstShouldBeZero() {
		assertEquals("0", Derivative.take(val(3), X).toString());
	}
	
	public void testDerivativeOfXByYShouldBeZero() {
		assertEquals("0", Derivative.take(X, Y).toString());
	}
	
	public void testDerivativeOfXByXShouldEqualOne() {
		assertEquals("1", Derivative.take(X, X).toString());
	}
	
	public void testShouldDeriveMultiplication() {
		assertEquals("((1 * y) + (x * 0))", Derivative.take(mul(X, Y), X).toString());
	}

	public void testShouldDeriveDivision() {
		assertEquals("(((1 * y) - (x * 0)) / (y * y))", Derivative.take(div(X, Y), X).toString());
	}

	public void testShouldDeriveAddition() {
		assertEquals("(1 + 0)", Derivative.take(plus(X, val(3)), X).toString());
	}

	public void testShouldDeriveSubtraction() {
		assertEquals("(1 - 0)", Derivative.take(minus(X, val(3)), X).toString());
	}
	
	public void testShouldDerivePowers() {
		assertEquals("((n * x^(n - 1)) * 1)", Derivative.take(pow(X, var("n")), X).toString());
	}
	
	public void testShouldFailToDerivePowersContainingVars() {
		try {
			Derivative.take(pow(val(3), X), X);
			fail("Expected an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}
	
	public void testShouldDeriveLogarithms() {
		assertEquals("((1 / x) * 1)", Derivative.take(ln(X), X).toString());
	}
	
	public void testShouldDeriveExponents() {
		assertEquals("(exp(x) * 1)", Derivative.take(exp(X), X).toString());
	}
}
