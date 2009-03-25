package deriv;

import static deriv.Expr.*;

class Derivative {
	public static Expr take(Expr expr, Var var) {
		DerivativeVisitor visitor = new DerivativeVisitor(var);
		expr.accept(visitor);
		return visitor.result;
	}
	
	static class DerivativeVisitor implements Visitor {
		private Var var;
		Expr result;

		public DerivativeVisitor(Var var) {
			this.var = var;
		}

		public void visitVal(int value) {
			result = val(0);
		}

		public void visitVar(String name) {
			if (var.name != name) {
				result = val(0);
			} else {
				result = val(1);
			}
		}

		public void visitMul(Expr u, Expr v) {
			result = plus(mul(take(u, var), v), mul(u, take(v, var)));
		}

		public void visitDiv(Expr u, Expr v) {
			result = div(minus(mul(take(u, var), v), mul(u, take(v, var))), mul(v, v));
		}

		public void visitPlus(Expr left, Expr right) {
			result = plus(take(left, var), take(right, var));
		}

		public void visitMinus(Expr left, Expr right) {
			result = minus(take(left, var), take(right, var));
		}
		
		public void visitPow(Expr base, Expr power) {
			if (power.containsVar(var)) {
				throw new IllegalArgumentException("don't know how to take derivatives of powers containing vars");
			}
			result = mul(mul(power, pow(base, minus(power, val(1)))), take(base, var));
		}

		public void visitLn(Expr arg) {
			result = mul(div(val(1), arg), take(arg, var));			
		}

		public void visitExp(Expr arg) {
			result = mul(exp(arg), take(arg, var));			
		}
	}
}