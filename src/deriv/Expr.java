package deriv;

public abstract class Expr {
	public static Var var(String name) { return new Var(name); }
	public static Expr val(int val) { return new Val(val); }
	public static Expr plus(Expr left, Expr right) { return new Plus(left, right); }
	public static Expr minus(Expr left, Expr right) { return new Minus(left, right); }
	public static Expr mul(Expr left, Expr right) { return new Mul(left, right); }
	public static Expr div(Expr left, Expr right) { return new Div(left, right); }
	public static Expr pow(Expr base, Expr power) { return new Pow(base, power); }
	public static Expr ln(Expr arg) { return new Ln(arg); }
	public static Expr exp(Expr arg) { return new Exp(arg); }
	
	public abstract boolean containsVar(Var var);
	public abstract void accept(Visitor visitor);
}

interface Visitor {
	public void visitVal(int value);
	public void visitVar(String name);
	public void visitPlus(Expr left, Expr right);
	public void visitMul(Expr left, Expr right);
	public void visitPow(Expr base, Expr power);
	public void visitMinus(Expr left, Expr right);
	public void visitDiv(Expr left, Expr right);
	public void visitLn(Expr arg);
	public void visitExp(Expr arg);
}

class Val extends Expr {
	private int value;

	public Val(int value) {
		this.value = value;
	}
	
	public void accept(Visitor visitor) { visitor.visitVal(value); }
	public boolean containsVar(Var var) { return false; }
	public String toString() { return Integer.toString(value);	}
	
}

class Var extends Expr {
	String name;

	public Var(String name) {
		this.name = name;
	}

	public void accept(Visitor visitor) { visitor.visitVar(name); }
	public boolean containsVar(Var var) { return this.name.equals(var.name); }
	public String toString() { return name; } 
}

abstract class BinaryOp extends Expr {
	protected Expr left;
	protected Expr right;

	public BinaryOp(Expr left, Expr right) {
		this.left = left;
		this.right = right;
	}
	public boolean containsVar(Var var) { return left.containsVar(var) || right.containsVar(var); }
}

class Plus extends BinaryOp {
	public Plus(Expr left, Expr right) { super(left, right); }
	
	public void accept(Visitor visitor) { visitor.visitPlus(left, right); }
	public String toString() { return "(" + left + " + " + right + ")";	}
}

class Minus extends BinaryOp {
	public Minus(Expr left, Expr right) { super(left, right); }
	
	public void accept(Visitor visitor) { visitor.visitMinus(left, right); }
	public String toString() { return "(" + left + " - " + right + ")";	}
}

class Mul extends BinaryOp {
	public Mul(Expr left, Expr right) { super(left, right); }
	
	public void accept(Visitor visitor) { visitor.visitMul(left, right); }
	public String toString() { return "(" + left + " * " + right + ")"; }
}

class Div extends BinaryOp {
	public Div(Expr left, Expr right) { super(left, right); }
	
	public void accept(Visitor visitor) { visitor.visitDiv(left, right); }
	public String toString() { return "(" + left + " / " + right + ")"; }
}

class Pow extends BinaryOp {
	public Pow(Expr left, Expr right) { super(left, right); }
	public void accept(Visitor visitor) { visitor.visitPow(left, right); }
	public String toString() { return "" + left + "^" + right; }
}

abstract class UnaryFunc extends Expr {
	protected String name;
	protected Expr arg;

	public UnaryFunc(String name, Expr arg) {
		this.name = name;
		this.arg = arg;
	}
	
	public boolean containsVar(Var var) { return arg.containsVar(var); }
	public String toString() { return "" + name + "(" + arg + ")"; }
}

class Ln extends UnaryFunc {
	public Ln(Expr arg) {
		super("ln", arg);
	}
	public void accept(Visitor visitor) { visitor.visitLn(arg);	}
}

class Exp extends UnaryFunc {
	public Exp(Expr arg) {
		super("exp", arg);
	}
	public void accept(Visitor visitor) { visitor.visitExp(arg);	}
}