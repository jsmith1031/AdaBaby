

public class Parser {
	public static final int _EOF = 0;
	public static final int _ident = 1;
	public static final int _intLit = 2;
	public static final int _floatLit = 3;
	public static final int _charLit = 4;
	public static final int _stringLit = 5;
	public static final int _subtype = 6;
	public static final int _record = 7;
	public static final int _range = 8;
	public static final int _is = 9;
	public static final int _loop = 10;
	public static final int _while = 11;
	public static final int _for = 12;
	public static final int _begin = 13;
	public static final int _end = 14;
	public static final int _procedure = 15;
	public static final int _function = 16;
	public static final int _package = 17;
	public static final int _use = 18;
	public static final int _with = 19;
	public static final int _of = 20;
	public static final int _array = 21;
	public static final int _integer = 22;
	public static final int _string = 23;
	public static final int _boolean = 24;
	public static final int _float = 25;
	public static final int _null = 26;
	public static final int _char = 27;
	public static final int _colon = 28;
	public static final int _comma = 29;
	public static final int _dot = 30;
	public static final int _self = 31;
	public static final int _lbrace = 32;
	public static final int _lbrack = 33;
	public static final int _lpar = 34;
	public static final int _mult = 35;
	public static final int _and = 36;
	public static final int _not = 37;
	public static final int _minus = 38;
	public static final int _plus = 39;
	public static final int _rbrace = 40;
	public static final int _rbrack = 41;
	public static final int _rpar = 42;
	public static final int _tilde = 43;
	public static final int maxT = 48;

	static final boolean _T = true;
	static final boolean _x = false;
	static final int minErrDist = 2;

	public Token t;    // last recognized token
	public Token la;   // lookahead token
	int errDist = minErrDist;
	
	public Scanner scanner;
	public Errors errors;

	Token peek(int n) {
  scanner.ResetPeek();
  Token x = la;
  while (n > 0) { x = scanner.Peek(); n--; }
  return x;
}




class ExprKind {
	static final int NONE     =  0;
	static final int CONDEXPR = 17;
	static final int APPLY    = 25;
	static final int NEWCLASS = 26;
	static final int NEWARRAY = 27;
	static final int PARENS   = 28;
	static final int ASSIGN   = 29;
	static final int TYPECAST = 30;
	static final int TYPETEST = 31;
	static final int SELECT   = 33;
	static final int IDENT    = 34;
	static final int LITERAL  = 35;
	static final int POS      = 41;
	static final int NEG      = 42;
	static final int NOT      = 43;
	static final int COMPL    = 44;
	static final int PREINC   = 45;
	static final int PREDEC   = 46;
	static final int POSTINC  = 47;
	static final int POSTDEC  = 48;
	static final int BINARY   = 50;
}



	public Parser(Scanner scanner) {
		this.scanner = scanner;
		errors = new Errors();
	}

	void SynErr (int n) {
		if (errDist >= minErrDist) errors.SynErr(la.line, la.col, n);
		errDist = 0;
	}

	public void SemErr (String msg) {
		if (errDist >= minErrDist) errors.SemErr(t.line, t.col, msg);
		errDist = 0;
	}
	
	void Get () {
		for (;;) {
			t = la;
			la = scanner.Scan();
			if (la.kind <= maxT) {
				++errDist;
				break;
			}

			la = t;
		}
	}
	
	void Expect (int n) {
		if (la.kind==n) Get(); else { SynErr(n); }
	}
	
	boolean StartOf (int s) {
		return set[s][la.kind];
	}
	
	void ExpectWeak (int n, int follow) {
		if (la.kind == n) Get();
		else {
			SynErr(n);
			while (!StartOf(follow)) Get();
		}
	}
	
	boolean WeakSeparator (int n, int syFol, int repFol) {
		int kind = la.kind;
		if (kind == n) { Get(); return true; }
		else if (StartOf(repFol)) return false;
		else {
			SynErr(n);
			while (!(set[syFol][kind] || set[repFol][kind] || set[0][kind])) {
				Get();
				kind = la.kind;
			}
			return StartOf(syFol);
		}
	}
	
	void ada() {
		while (StartOf(1)) {
			if (la.kind == 2 || la.kind == 46 || la.kind == 47) {
				expr();
				System.out.println("expr = " + t.val); 
			} else {
				init();
			}
		}
		System.out.println("init = " + t.val); 
	}

	void expr() {
		Term();
		System.out.println("Term = " + t.val); 
		while (la.kind == 38 || la.kind == 39) {
			AddOp();
			System.out.println("Addop = " + t.val); 
			Term();
			System.out.println("Term = " + t.val); 
		}
	}

	void init() {
		Expect(1);
		System.out.println("ident = " + t.val); 
		while (la.kind == 29) {
			Get();
			System.out.println("comma = " + t.val); 
			Expect(1);
			System.out.println("ident = " + t.val); 
		}
		Expect(28);
		System.out.println("semiColon = " + t.val); 
		type();
		System.out.println("type = " + t.val); 
		if (la.kind == 44 || la.kind == 45) {
			if (la.kind == 44) {
				Get();
				System.out.println("; = " + t.val); 
			} else {
				assign();
			}
		}
		System.out.println("assign := "); 
	}

	void type() {
		if (la.kind == 22) {
			Get();
		} else if (la.kind == 23) {
			Get();
		} else if (la.kind == 24) {
			Get();
		} else if (la.kind == 25) {
			Get();
		} else if (la.kind == 27) {
			Get();
		} else SynErr(49);
	}

	void assign() {
		Expect(45);
		expr();
		System.out.println("expr  " + t.val); 
		Expect(44);
		System.out.println("; = " + t.val); 
	}

	void Term() {
		Factor();
		while (la.kind == 35) {
			MulOp();
			Factor();
		}
	}

	void AddOp() {
		if (la.kind == 39) {
			Get();
		} else if (la.kind == 38) {
			Get();
		} else SynErr(50);
		System.out.println("AddOp = " + t.val); 
	}

	void Factor() {
		if (la.kind == 2) {
			Get();
		} else if (la.kind == 46) {
			Get();
		} else if (la.kind == 47) {
			Get();
		} else SynErr(51);
	}

	void MulOp() {
		Expect(35);
		System.out.println("mul = " + t.val); 
	}



	public void Parse() {
		la = new Token();
		la.val = "";		
		Get();
		ada();
		Expect(0);

	}

	private static final boolean[][] set = {
		{_T,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x},
		{_x,_T,_T,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_x,_x, _x,_x,_T,_T, _x,_x}

	};
} // end Parser


class Errors {
	public int count = 0;                                    // number of errors detected
	public java.io.PrintStream errorStream = System.out;     // error messages go to this stream
	public String errMsgFormat = "-- line {0} col {1}: {2}"; // 0=line, 1=column, 2=text
	
	protected void printMsg(int line, int column, String msg) {
		StringBuffer b = new StringBuffer(errMsgFormat);
		int pos = b.indexOf("{0}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, line); }
		pos = b.indexOf("{1}");
		if (pos >= 0) { b.delete(pos, pos+3); b.insert(pos, column); }
		pos = b.indexOf("{2}");
		if (pos >= 0) b.replace(pos, pos+3, msg);
		errorStream.println(b.toString());
	}
	
	public void SynErr (int line, int col, int n) {
		String s;
		switch (n) {
			case 0: s = "EOF expected"; break;
			case 1: s = "ident expected"; break;
			case 2: s = "intLit expected"; break;
			case 3: s = "floatLit expected"; break;
			case 4: s = "charLit expected"; break;
			case 5: s = "stringLit expected"; break;
			case 6: s = "subtype expected"; break;
			case 7: s = "record expected"; break;
			case 8: s = "range expected"; break;
			case 9: s = "is expected"; break;
			case 10: s = "loop expected"; break;
			case 11: s = "while expected"; break;
			case 12: s = "for expected"; break;
			case 13: s = "begin expected"; break;
			case 14: s = "end expected"; break;
			case 15: s = "procedure expected"; break;
			case 16: s = "function expected"; break;
			case 17: s = "package expected"; break;
			case 18: s = "use expected"; break;
			case 19: s = "with expected"; break;
			case 20: s = "of expected"; break;
			case 21: s = "array expected"; break;
			case 22: s = "integer expected"; break;
			case 23: s = "string expected"; break;
			case 24: s = "boolean expected"; break;
			case 25: s = "float expected"; break;
			case 26: s = "null expected"; break;
			case 27: s = "char expected"; break;
			case 28: s = "colon expected"; break;
			case 29: s = "comma expected"; break;
			case 30: s = "dot expected"; break;
			case 31: s = "self expected"; break;
			case 32: s = "lbrace expected"; break;
			case 33: s = "lbrack expected"; break;
			case 34: s = "lpar expected"; break;
			case 35: s = "mult expected"; break;
			case 36: s = "and expected"; break;
			case 37: s = "not expected"; break;
			case 38: s = "minus expected"; break;
			case 39: s = "plus expected"; break;
			case 40: s = "rbrace expected"; break;
			case 41: s = "rbrack expected"; break;
			case 42: s = "rpar expected"; break;
			case 43: s = "tilde expected"; break;
			case 44: s = "\";\" expected"; break;
			case 45: s = "\":=\" expected"; break;
			case 46: s = "\"true\" expected"; break;
			case 47: s = "\"false\" expected"; break;
			case 48: s = "??? expected"; break;
			case 49: s = "invalid type"; break;
			case 50: s = "invalid AddOp"; break;
			case 51: s = "invalid Factor"; break;
			default: s = "error " + n; break;
		}
		printMsg(line, col, s);
		count++;
	}

	public void SemErr (int line, int col, String s) {	
		printMsg(line, col, s);
		count++;
	}
	
	public void SemErr (String s) {
		errorStream.println(s);
		count++;
	}
	
	public void Warning (int line, int col, String s) {	
		printMsg(line, col, s);
	}
	
	public void Warning (String s) {
		errorStream.println(s);
	}
} // Errors


class FatalError extends RuntimeException {
	public static final long serialVersionUID = 1L;
	public FatalError(String s) { super(s); }
}
