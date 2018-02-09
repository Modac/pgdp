package Blatt10.Aufg10p7;

import Blatt11.Aufg11p7.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import javax.annotation.Generated;

@Generated("Double code")
public class MiniJavaParser {

  private String[] program;
  private int from;

  public MiniJavaParser(String program) {
    this.program = lex(program);
  }

  public String[] getProgram() {
    return program;
  }

  public int getFrom() {
    return from;
  }

  public String readProgramConsole() {
    @SuppressWarnings("resource")
    Scanner sin = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (true) {
      String nextLine = sin.nextLine();
      if (nextLine.equals("")) {
        nextLine = sin.nextLine();
        if (nextLine.equals("")) {
          break;
        }
      }
      if (nextLine.startsWith("//")) {
        continue;
      }
      builder.append(nextLine);
      builder.append('\n');
    }
    return builder.toString();
  }

  public String[] lex(String program) {
    ArrayList<String> res = new ArrayList<>();

    StringBuilder token = new StringBuilder();

    for (int i = 0; i < program.length(); i++) {
      char cT = program.charAt(i);

      if (isAlphaNumeric(cT)) {
        token.append(cT);
      } else if ((cT == ' ' || cT == '\n' || cT == '\t') && token.length() > 0) {
        res.add(token.toString());
        token.setLength(0);
      } else if (cT != ' ' && cT != '\n' && cT != '\t') {
        if (token.length() > 0) {
          res.add(token.toString());
          token.setLength(0);
        }

        char nextC = i + 1 < program.length() ? program.charAt(i + 1) : '\u0003';

        if ((nextC == '=' && ((cT == '=') || (cT == '!') || (cT == '<') || (cT == '>'))) ||
            (cT == '&' && nextC == '&') ||
            (cT == '|' && nextC == '|')) {
          res.add("" + cT + nextC);
          i++;
        } else {
          res.add("" + cT);
        }


      }
    }
    return res.toArray(new String[res.size()]);
  }

  private boolean isUppercaseLetter(char character) {
    return ('A' <= character) && (character <= 'Z');
  }

  private boolean isLowercaseLetter(char character) {
    return ('a' <= character) && (character <= 'z');
  }

  private boolean isLetter(char character) {
    return isUppercaseLetter(character) || isLowercaseLetter(character);
  }

  private boolean isLetter(String character) {
    return character.length() > 0 && isLetter(character.charAt(0));
  }

  private boolean isNumber(char character) {
    return ('0' <= character) && (character <= '9');
  }

  private boolean isNumber(String character) {
    return character.length() > 0 && isNumber(character.charAt(0));
  }

  private boolean isAlphaNumeric(char character) {
    return isLetter(character) || isNumber(character);
  }

  public Number parseNumber() {
    if (from >= program.length) {
      from = -1;
      return null;
    }

    if (!isNumber(program[from].charAt(0))) {
      from = -1;
      return null;
    }

    for (int i = 1; i < program[from].length(); i++) {
      if (!isNumber(program[from].charAt(i))) {
        from = -1;
        return null;
      }
    }

    return new Number(Integer.parseInt(program[from++]));

  }

  public String parseName() {
    if (from < 0 || from >= program.length) {
      from = -1;
      return null;
    }

    String name = "";

    if (!isLetter(program[from].charAt(0))) {
      from = -1;
      return null;
    }

    for (int i = 1; i < program[from].length(); i++) {
      if (!isAlphaNumeric(program[from].charAt(i))) {
        from = -1;
        return null;
      }
    }

    return program[from++];
  }

  public Type parseType() {
    if (from < 0 || from < program.length && program[from].equals("int")) {
      int fromO = from;
      checkToken("[");
      checkToken("]");
      if(from > 0){
        return Type.IntA;
      }
      from = fromO+1;
      return Type.Int;
    }

    from = -1;
    return null;
  }

  public Declaration parseDecl() {
    if (from < 0) {
      return null;
    }

    // <type> <name> (, <name> )* ;

    List<String> names = new LinkedList<>();

    // <type> <name>
    parseType();
    names.add(parseName());
    if (from < 0) {
      return null;
    }

    // (, <name> )*
    while (from < program.length && program[from].equals(",")) {
      // Wenn es ein Komma gibt muss auch ein name folgen
      from++;
      names.add(parseName());
      if (from < 0) {
        return null;
      }
    }

    // ;
    checkToken(";");
    return new Declaration(names.toArray(new String[]{}));

  }

  public Unop parseUnop() {
    if (from < 0) {
      return null;
    }

    // -

    checkToken("-");
    if (from > 0) {
      return Unop.Minus;
    }
    from = -1;
    return null;
  }

  public Binop parseBinop() {
    if (from < 0) {
      return null;
    }

    // - | + | * | / | %

    int fromO = from;

    checkToken("-");
    if (from > 0) {
      return Binop.Minus;
    }

    from = fromO;

    checkToken("+");
    if (from > 0) {
      return Binop.Plus;
    }

    from = fromO;

    checkToken("*");
    if (from > 0) {
      return Binop.MultiplicationOperator;
    }

    from = fromO;

    checkToken("/");
    if (from > 0) {
      return Binop.DivisionOperator;
    }

    from = fromO;

    checkToken("%");
    if (from > 0) {
      return Binop.Modulo;
    }

    from = -1;
    return null;
  }

  public Comp parseComp() {
    if (from < 0) {
      return null;
    }

    // == | != | <= | < | >= | >

    int fromO = from;

    checkToken("==");
    if (from > 0) {
      return Comp.Equals;
    }

    from = fromO;

    checkToken("!=");
    if (from > 0) {
      return Comp.NotEquals;
    }

    from = fromO;

    checkToken("<=");
    if (from > 0) {
      return Comp.LessEqual;
    }

    from = fromO;

    checkToken("<");
    if (from > 0) {
      return Comp.Less;
    }

    from = fromO;

    checkToken(">=");
    if (from > 0) {
      return Comp.GreaterEqual;
    }

    from = fromO;

    checkToken(">");
    if (from > 0) {
      return Comp.Greater;
    }

    from = -1;
    return null;
  }

  public Expression parseExpression() {
    if (from < 0) {
      return null;
    }

    // originales from speichern, falls ein Test -1 zurückgibt
    int fromO = from;
    int fromF = from;
    boolean isExpr = false;
    Expression expr = null;

    // <number>
    Number num = parseNumber();
    if ((from > 0)) {
      isExpr = true;
      fromF = from;
      expr = num;
    }
    from = fromO;

    // <name>
    // <name> ( [e | <expr>(, <expr>)*] )
    String name = parseName();
    if (!isExpr && from > 0) {
      int fromN = from;
      List<Expression> exprs = new LinkedList<>();
      checkToken("(");
      if (from > 0) {
        int fromT = from;
        exprs.add(parseExpression());

        if (from < 0) {
          from = fromT;
        } else {
          while (from < program.length && program[from].equals(",")) {
            // Wenn es ein Komma gibt muss auch eine expression folgen
            from++;
            exprs.add(parseExpression());
            if (from < 0) {
              break;
            }
          }
        }

        checkToken(")");
        if (from > 0) {
          isExpr = true;
          fromF = from;
          exprs.removeIf(Objects::isNull);
          expr = new Call(name, exprs.toArray(new Expression[]{}));
        }
      } else {
        expr = new Variable(name);
        isExpr = true;
        fromF = fromN;
      }
    }

    from = fromO;

    // (<expr>)
    checkToken("(");
    Expression expr1 = parseExpression();
    checkToken(")");
    if (!isExpr && from > 0) {
      isExpr = true;
      fromF = from;
      expr = expr1;
    }

    from = fromO;

    // <unop> <expr>
    Unary unary = new Unary(parseUnop(), parseExpression());
    if (!isExpr && from > 0) {
      isExpr = true;
      fromF = from;
      expr = unary;
    }

    // <expr> <binop> <expr>
    // Wenn es eine Expression ist kann auch ein binop und eine weitere Expression folgen
    if (isExpr) {
      from = fromF;
      Binop binop = parseBinop();
      if (from > 0) {
        Expression expr2 = parseExpression();
        if (from > 0) {
          return new Binary(expr, binop, expr2);
        } else {
          from = fromF;
          return expr;
        }
      } else {
        from = fromF;
        return expr;
      }
    }

    from = -1;
    return null;
  }

  public Bbinop parseBbinop() {
    if (from < 0) {
      return null;
    }

    int fromO = from;

    // && | ||

    checkToken("&&");
    if (from > 0) {
      return Bbinop.And;
    }

    from = fromO;

    checkToken("||");
    if (from > 0) {
      return Bbinop.Or;
    }

    from = -1;
    return null;
  }

  public Bunop parseBunop() {
    if (from < 0) {
      return null;
    }

    // !

    checkToken("!");
    if ((from > 0)) {
      return Bunop.Not;
    }

    from = -1;
    return null;
  }

  public Condition parseCondition() {
    if (from < 0) {
      return null;
    }

    int fromO = from;
    int fromF = from;
    boolean isCond = false;
    Condition cond = null;

    // true

    checkToken("true");
    if (from > 0) {
      isCond = true;
      fromF = from;
      cond = new True();
    }

    from = fromO;

    // false
    checkToken("false");
    if (!isCond && from > 0) {
      isCond = true;
      fromF = from;
      cond = new False();
    }

    from = fromO;

    // ( <cond> )
    checkToken("(");
    Condition cond2 = parseCondition();
    checkToken(")");
    if (!isCond && from > 0) {
      isCond = true;
      fromF = from;
      cond = cond2;
    }

    from = fromO;

    // <expr> <comp> <expr>
    Comparison comp = new Comparison(parseExpression(), parseComp(), parseExpression());
    if (!isCond && from > 0) {
      isCond = true;
      fromF = from;
      cond = comp;
    }

    from = fromO;

    // <bunop> ( <cond> )
    Bunop bunop = parseBunop();
    checkToken("(");
    UnaryCondition unCond = new UnaryCondition(bunop, parseCondition());
    checkToken(")");
    if (!isCond && from > 0) {
      isCond = true;
      fromF = from;
      cond = unCond;
    }

    // <cond> <bbinop> <cond>
    if (isCond) {
      from = fromF;
      Bbinop bbinop = parseBbinop();
      if (from > 0) {
        Condition cond3 = parseCondition();
        if (from > 0) {
          return new BinaryCondition(cond, bbinop, cond3);
        } else {
          from = fromF;
          return cond;
        }
      } else {
        from = fromF;
        return cond;
      }
    }

    from = -1;
    return null;
  }

  public Statement parseStatement() {
    if (from < 0) {
      return null;
    }

    int fromO = from;

    // ;
    checkToken(";");
    if (from > 0) {
      return new Statement();
    }

    from = fromO;

    // { <stmt>* }
    checkToken("{");
    if (from > 0) {

      List<Statement> statements = new LinkedList<>();

      do {
        int fromT = from;
        checkToken("}");
        if (from > 0) {
          return new Composite(statements.toArray(new Statement[]{}));
        }
        from = fromT;
        statements.add(parseStatement());
      } while (from > 0);

      from = -1;
      return null;

    }

    from = fromO;

    // <name> = <expr>;
    String varName = parseName();
    if (from > 0) {
      checkToken("=");
      if (from > 0) {

        int fromO1 = from;

        // 'read' kann auch als Expression gelten
        Expression expr = parseExpression();
        checkToken(";");
        if (from > 0) {
          return new Assignment(varName, expr);
        }

        // <name> = read();
        from = fromO1;
        checkToken("read");
        checkToken("(");
        checkToken(")");
        checkToken(";");
        return new Read(varName);

      }
    }

    from = fromO;

    // write( <expr> );
    checkToken("write");
    checkToken("(");
    Expression expr = parseExpression();
    checkToken(")");
    checkToken(";");
    if (from > 0) {
      return new Write(expr);
    }

    from = fromO;

    // if( <cond> ) <stmt>
    // if( <cond> ) <stmt> else <stmt>
    checkToken("if");
    checkToken("(");
    Condition cond = parseCondition();
    checkToken(")");
    Statement thenBranch = parseStatement();
    if (from > 0) {
      int fromO1 = from;
      // Schauen ob noch ein else <stmt> danach kommt
      checkToken("else");
      Statement elseBranch = parseStatement();
      if (from < 0) {
        // Wenn nicht gibt das from von nach dem if zurück
        from = fromO1;
        return new IfThen(cond, thenBranch);
      }
      return new IfThenElse(cond, thenBranch, elseBranch);

    }

    from = fromO;

    // while( <cond> ) <stmt>
    checkToken("while");
    checkToken("(");
    cond = parseCondition();
    checkToken(")");
    thenBranch = parseStatement();
    if (from > 0) {
      return new While(cond, thenBranch);
    }

    from = fromO;

    // return <expr>;
    checkToken("return");
    expr = parseExpression();
    checkToken(";");
    if (from > 0) {
      return new Return(expr);
    }

    return null;
  }

  public void checkToken(String token) {
    if (from < 0 || from >= program.length || !program[from++].equals(token)) {
      from = -1;
      return;
    }
    return;
  }

  public Function parseFunction() {

    // <type> <name> ( <params> ) { <decl>* <stmt>* }

    // <type> <name> ( <params> ) {
    parseType();
    String name = parseName();
    checkToken("(");
    String[] params = parseParams();
    checkToken(")");
    checkToken("{");

    List<Declaration> decls = new LinkedList<>();

    int prevN = from;
    while (from >= 0) {
      prevN = from;
      decls.add(parseDecl());
    }

    decls.removeIf(Objects::isNull);

    from = prevN;

    List<Statement> statemnts = new LinkedList<>();

    while (from < program.length) {
      prevN = from;
      statemnts.add(parseStatement());
      if (from < 0) {
        from = prevN;
        break;
      }
    }
    statemnts.removeIf(Objects::isNull);

    checkToken("}");
    if(from > 0 ){
      return new Function(name, params, decls.toArray(new Declaration[]{}),
          statemnts.toArray(new Statement[]{}));
    }
    return null;
  }

  public String[] parseParams() {

    // e | (<type> <name>)(, <type> <name>)*

    int fromO = from;

    List<String> params = new LinkedList<>();

    // (<type> <name>)
    parseType();
    params.add(parseName());
    if (from < 0) {
      from = fromO;
      return new String[]{};
    }

    // (, <type> <name> )*
    while (from < program.length && program[from].equals(",")) {
      // Wenn es ein Komma gibt muss auch ein name folgen
      from++;
      parseType();
      params.add(parseName());
      if (from < 0) {
        return null;
      }
    }

    return params.toArray(new String[]{});
  }

  public Program parseProgram() {
    from = 0;

    List<Function> funcs = new LinkedList<>();

    while (from < program.length) {
      funcs.add(parseFunction());
      if (from < 0) {
        return null;
      }
    }

    return new Program(funcs.toArray(new Function[]{}));
  }

  public Program parse() {
    return parseProgram();
  }


  public static void main(String[] args) {
    MiniJavaParser mjp = new MiniJavaParser("int main() {int[] a; a = new int[ 3+2];if ( 3 == 0);}");

    System.out.println(Arrays.toString(mjp.program));
    /*
    Program p = mjp.parse();

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(gson.toJson(p));
    */
  }
}