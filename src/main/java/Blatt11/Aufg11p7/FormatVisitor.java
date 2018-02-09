package Blatt11.Aufg11p7;

public class FormatVisitor extends Visitor {

  private StringBuilder program;

  private int indentLvl;

  public FormatVisitor() {
    this.program = new StringBuilder();
    indentLvl = 0;
  }

  @Override
  public void visit(Program program) {

    for (int i = 0; i < program.getFunctions().length; i++) {
      program.getFunctions()[i].accept(this);
      if (i + 1 < program.getFunctions().length) {
        newLine();
        newLine();
      }
    }
  }

  @Override
  public void visit(Function function) {
    indentLvl = 0;

    program.append("int ").append(function.getName()).append("(");
    for (int i = 0; i < function.getParameters().length; i++) {
      program.append("int ").append(function.getParameters()[i]);
      if (i < function.getParameters().length - 1) {
        program.append(", ");
      }
    }
    program.append(") {\n");

    indentLvl++;

    for (Declaration d : function.getDeclarations()) {
      indent();
      d.accept(this);
      newLine();
    }

    for (Statement s : function.getStatements()) {
      indent();
      s.accept(this);
      newLine();
    }

    indentLvl--;

    indent();
    program.append("}");

  }

  @Override
  public void visit(Declaration declaration) {
    program.append("int ");

    for (int i = 0; i < declaration.getNames().length; i++) {
      program.append(declaration.getNames()[i]);
      if (i < declaration.getNames().length - 1) {
        program.append(", ");
      }
    }

    semiColon();
  }

  ///////////////////////////////////////////////// Statement ANFANG

  @Override
  public void visit(Statement statement) {
    semiColon();
  }

  /////////////
  @Override
  public void visit(Assignment assignment) {
    program.append(assignment.getName()).append(" = ");
    assignment.getExpression().accept(this);
    semiColon();
  }

  @Override
  public void visit(Composite composite) {
    program.append("{\n");

    indentLvl++;

    for (int i = 0; i < composite.getStatements().length; i++) {
      indent();
      composite.getStatements()[i].accept(this);
      newLine();
    }

    indentLvl--;

    indent();
    program.append("}");
  }

  @Override
  public void visit(IfThen ifThen) {
    program.append("if (");

    ifThen.getCond().accept(this);

    program.append(") ");

    ifThen.getThenBranch().accept(this);

  }

  @Override
  public void visit(IfThenElse ifThenElse) {

    program.append("if (");

    ifThenElse.getCond().accept(this);

    program.append(") ");

    ifThenElse.getThenBranch().accept(this);

    program.append(" else ");

    ifThenElse.getElseBranch().accept(this);
  }

  @Override
  public void visit(While aWhile) {
    program.append("while (");

    aWhile.getCond().accept(this);

    program.append(") ");

    aWhile.getBody().accept(this);
  }

  @Override
  public void visit(Read read) {
    program.append(read.getName()).append(" = read();");
  }

  @Override
  public void visit(Write write) {
    program.append("write(");
    write.getExpression().accept(this);
    program.append(");");
  }

  @Override
  public void visit(Return aReturn) {
    program.append("return ");
    aReturn.getExpression().accept(this);
    semiColon();
  }

  ///////////////////////////////////////////////// Statement ENDE

  ///////////////////////////////////////////////// Expression ANFANG

  @Override
  public void visit(Expression expression) {

  }

  /////////////
  @Override
  public void visit(Variable variable) {
    program.append(variable.getName());
  }

  @Override
  public void visit(Number number) {
    program.append(number.getValue());
  }

  @Override
  public void visit(Binary binary) {
    int bN = bracketsNeeded(binary, binary.getLhs(), binary.getRhs());

    boolean lB = (bN & 2) > 0;

    if (lB) {
      program.append("(");
    }
    binary.getLhs().accept(this);
    if (lB) {
      program.append(")");
    }

    program.append(" ").append(binary.getOperator().toString()).append(" ");

    boolean rB = (bN & 1) > 0;

    if (rB) {
      program.append("(");
    }
    binary.getRhs().accept(this);
    if (rB) {
      program.append(")");
    }
  }

  @Override
  public void visit(Unary unary) {
    program.append(unary.getOperator().toString());
    program.append("(");
    unary.getOperand().accept(this);
    program.append(")");
  }

  @Override
  public void visit(Call call) {
    program.append(call.getFunctionName()).append("(");

    for (int i = 0; i < call.getArguments().length; i++) {
      call.getArguments()[i].accept(this);
      if (i < call.getArguments().length - 1) {
        program.append(", ");
      }
    }

    program.append(")");

  }

  ///////////////////////////////////////////////// Expression ENDE

  ///////////////////////////////////////////////// Condition ANFANG

  @Override
  public void visit(Condition condition) {

  }

  /////////////
  @Override
  public void visit(True aTrue) {
    program.append("true");
  }

  @Override
  public void visit(False aFalse) {
    program.append("false");
  }

  @Override
  public void visit(BinaryCondition binaryCondition) {
    binaryCondition.getLhs().accept(this);
    program.append(" ").append(binaryCondition.getOperator().toString()).append(" ");
    if (bracketsNeeded(binaryCondition, binaryCondition.getRhs())) {
      program.append("(");
      binaryCondition.getRhs().accept(this);
      program.append(")");
    } else {
      binaryCondition.getRhs().accept(this);
    }
  }

  @Override
  public void visit(Comparison comparison) {
    comparison.getLhs().accept(this);
    program.append(" ").append(comparison.getOperator().toString()).append(" ");
    comparison.getRhs().accept(this);
  }

  @Override
  public void visit(UnaryCondition unaryCondition) {
    program.append(unaryCondition.getOperator().toString()).append("(");
    unaryCondition.getOperand().accept(this);
    program.append(")");
  }

  ///////////////////////////////////////////////// Condition ENDE

  private void indent() {
    for (int i = 0; i < indentLvl; i++) {
      program.append("  ");
    }
  }

  private void newLine() {
    program.append("\n");
  }

  private void semiColon() {
    program.append(";");
  }

  private int bracketsNeeded(Expression expr, Expression... subExprs) {
    if (expr instanceof Call ||
        expr instanceof Number ||
        expr instanceof Variable) {
      return 0;
    }
    if (expr instanceof Unary) {
      return 1;
    }
    if (expr instanceof Binary) {
      if (subExprs.length < 2) {
        return -1;
      }
      Binary bin = (Binary) expr;

      int res = 0;

      if (subExprs[0] instanceof Binary) {
        Binary lSub = (Binary) subExprs[0];

        if (bin.getOperator() == Binop.MultiplicationOperator ||
            bin.getOperator() == Binop.DivisionOperator) {
          if (lSub.getOperator() == Binop.Plus ||
              lSub.getOperator() == Binop.Minus) {
            res |= 2;
          }
        } else if (bin.getOperator() == Binop.Modulo) {
          if (lSub.getOperator() == Binop.Minus) {
            res |= 2;
          }
        }
      }

      if (subExprs[1] instanceof Binary) {
        Binary rSub = (Binary) subExprs[1];

        if (bin.getOperator() == Binop.Minus ||
            bin.getOperator() == Binop.MultiplicationOperator) {
          if (rSub.getOperator() == Binop.Plus ||
              rSub.getOperator() == Binop.Minus) {
            res |= 1;
          }
        } else if (bin.getOperator() == Binop.DivisionOperator ||
            bin.getOperator() == Binop.Modulo) {
          res |= 1;
        }
      }

      return res;
    }
    return -1;
  }

  private boolean bracketsNeeded(BinaryCondition binC, Condition rCon) {
    if (rCon instanceof BinaryCondition) {
      BinaryCondition rBinC = (BinaryCondition) rCon;

      if (binC.getOperator() == Bbinop.And &&
          rBinC.getOperator() == Bbinop.Or) {
        return true;
      }
    }

    return false;
  }


  public String getResult() {
    System.out.println(program.toString());
    return program.toString();
  }

  public static void main(String[] args) {
    Program p1 =
        new Program(
            new Function[]{
                new Function(Type.Int,
                    "main",
                    new Parameter[0],
                    new Declaration[0],
                    new Statement[]{
                        new IfThenElse(
                            new Comparison(new Number(3), Comp.Equals, new Number(0)),
                            new Composite(new Statement[]{
                                new Write(new Number(6)),
                                new Read("testVar")
                            }),
                            new Composite(new Statement[]{
                                new Write(new Number(3)),
                                new Read("testVar2"),
                                new IfThenElse(
                                    new Comparison(new Number(2), Comp.Equals, new Number(0)),
                                    new Composite(new Statement[]{
                                        new Write(new Number(1)),
                                        new Read("testVar3")
                                    }),
                                    new Composite(new Statement[]{
                                        new Write(new Number(8)),
                                        new Read("testVar4")
                                    })
                                )
                            })
                        )
                    })
            }
        );

    FormatVisitor fv = new FormatVisitor();

    p1.accept(fv);

    System.out.println(fv.getResult());
  }


}
