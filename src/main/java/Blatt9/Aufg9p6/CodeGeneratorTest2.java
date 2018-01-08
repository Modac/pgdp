package Blatt9.Aufg9p6;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CodeGeneratorTest2 {

  public void testProgram(Program program, int expected) {
    CodeGenerationVisitor cgv = new CodeGenerationVisitor();
    program.accept(cgv);
    int retVal = Interpreter.execute(cgv.getProgram());
    assertEquals(expected, retVal);
  }

  @Test
  public void testExpression() {
    Program p = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Unary(Unop.Minus, new Number(10))
            )})
    });
    testProgram(p, -10);
    Program p2 = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Binary(new Number(13), Binop.Plus, new Number(10))
            )})
    });
    testProgram(p2, 23);
    Program p3 = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Binary(new Number(13), Binop.MultiplicationOperator, new Number(10))
            )})
    });
    testProgram(p3, 130);
    Program p4 = new Program(new Function[]{
        new Function("main",
            new String[0],
            new Declaration[0],
            new Statement[]{new Return(
                new Binary(
                    new Binary(new Number(1), Binop.Plus, new Number(2)),
                    Binop.Plus,
                    new Binary(new Number(1), Binop.Plus, new Number(2)))
            )})
    });
    testProgram(p4, 6);
  }

  @Test
  public void testCondition() {
    Program p1 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new BinaryCondition(new True(), Bbinop.And, new False()),
                new Return(new Unary(Unop.Minus, new Number(1))),
                new Return(new Number(0))
            )})
    });
    testProgram(p1, 0);
    Program p2 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new BinaryCondition(new True(), Bbinop.Or, new False()),
                new Return(new Unary(Unop.Minus, new Number(1))),
                new Return(new Number(0))
            )})
    });
    testProgram(p2, -1);
    Program p3 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new UnaryCondition(Bunop.Not, new False()),
                new Return(new Unary(Unop.Minus, new Number(1))),
                new Return(new Number(0))
            )})
    });
    testProgram(p3, -1);
    Program p4 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new Comparison(new Number(5), Comp.Less, new Number(10)),
                new Return(new Unary(Unop.Minus, new Number(1))),
                new Return(new Number(0))
            )})
    });
    testProgram(p4, -1);
    Program p5 = new Program(new Function[]{
        new Function(
            "main",
            new String[0],
            new Declaration[0],
            new Statement[]{new IfThenElse(
                new Comparison(new Number(5), Comp.Greater, new Number(10)),
                new Return(new Unary(Unop.Minus, new Number(1))),
                new Return(new Number(0))
            )})
    });
    testProgram(p5, 0);
  }

  @Test
  public void testIfThen() {
    Function f = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{
            new IfThen(
                new True(),
                new Assignment("testProgram", new Number(12))
            ),
            new Return(new Variable("testProgram"))
        });
    testProgram(new Program(new Function[]{f}), 12);
  }

  @Test
  public void testIfThenElse() {
    Function f2 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{
            new IfThenElse(
                new True(),
                new Assignment("testProgram", new Number(12)),
                new Assignment("testProgram", new Number(20))
            ),
            new Return(new Variable("testProgram"))
        });
    Function f3 = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{
            new IfThenElse(
                new False(),
                new Assignment("testProgram", new Number(12)),
                new Assignment("testProgram", new Number(20))
            ),
            new Return(new Variable("testProgram"))
        });
    testProgram(new Program(new Function[]{f2}), 12);
    testProgram(new Program(new Function[]{f3}), 20);

  }

  @Test
  public void testVariables() {
    Function f = new Function("main", new String[0],
        new Declaration[]{new Declaration(new String[]{"testProgram"})},
        new Statement[]{new Assignment("testProgram", new Number(123)),
            new Return(new Variable("testProgram"))});
    Program p = new Program(new Function[]{f});
    testProgram(p, 123);
  }

  @Test
  public void testWhile() {
    Function f = new Function("main", new String[0],
        new Declaration[]{new Declaration(new String[]{"a", "b"})},
        new Statement[]{
            new Assignment("a", new Number(5)),
            new Assignment("b", new Number(0)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Binary(new Variable("b"), Binop.Plus, new Number(5))),
                    new Assignment("a", new Binary(new Variable("a"), Binop.Minus, new Number(1)))
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }

  @Test(expected = RuntimeException.class)
  public void testVariableNotDeclared() {
    Function f = new Function("main", new String[0],
        new Declaration[0],
        new Statement[]{new Assignment("a", new Number(5)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Binary(new Variable("b"), Binop.Plus, new Number(5))),
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }

  /*
    @Test(expected = RuntimeException.class)
    public void testVariableNotDefined() {
      Function f = new Function("main",
          new String[0],
          new Declaration[]{new Declaration(new String[]{"a"})},
          new Statement[]{
              new Return(new Variable("a"))
          });
      Program p = new Program(new Function[]{f});
      testProgram(p, 25);
    }
  */

  @Test(expected = RuntimeException.class)
  public void testNegativeNumber() {
    Function f = new Function("main",
        new String[0],
        new Declaration[]{new Declaration(new String[]{"a"})},
        new Statement[]{
            new Assignment("a", new Number(-1)),
            new Return(new Variable("a"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 0);
  }

  @Test(expected = RuntimeException.class)
  public void testFunctionNotDeclared() {
    Function f = new Function("main", new String[0],
        new Declaration[]{new Declaration(new String[]{"a", "b"})},
        new Statement[]{new Assignment("a", new Number(5)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Call("undefined", new Expression[0])),
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }

  @Test(expected = RuntimeException.class)
  public void testNoMainFunction() {
    Function f = new Function("testProgram", new String[0],
        new Declaration[]{new Declaration(new String[]{"a", "b"})},
        new Statement[]{new Assignment("a", new Number(5)),
            new While(new Comparison(new Variable("a"), Comp.Greater, new Number(0)),
                new Composite(new Statement[]{
                    new Assignment("b", new Number(10)),
                })
            ),
            new Return(new Variable("b"))
        });
    Program p = new Program(new Function[]{f});
    testProgram(p, 25);
  }
}
