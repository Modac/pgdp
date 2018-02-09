package Blatt11.Aufg11p6;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeGenerationVisitor extends Visitor {

  private String program;
  private Map<String, Integer> varMap;
  private int cmds;
  private int specialLabelCounter;
  private int returnInt;

  private List<Function> functions;
  private int currFunc;


  public CodeGenerationVisitor() {
    this.program = "";
    cmds = 0;
    specialLabelCounter = 0;
    returnInt = 0;
    varMap = new HashMap<>();
    functions = new LinkedList<>();
    currFunc = -1;
  }

  @Override
  public void visit(Program program) {


    // Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // System.out.println(gson.toJson(program));

    program.accept(new FunctionVisitor());

    //addCmd("LDI -1");
    addCmd("LDI main");
    addCmd("CALL 0");
    addCmd("HALT");
    for (Function func : program.getFunctions()) {
      func.accept(this);
    }
  }

  @Override
  public void visit(Function function) {
    returnInt = 0;

    currFunc = functions.indexOf(function);

    varMap = new HashMap<>();

    for (Declaration decl : function.getDeclarations()) {
      returnInt += decl.getNames().length;
      decl.accept(this);
    }

    for (int i = 0; i < function.getParameters().length; i++) {
      if (varMap.containsKey(function.getParameters()[i])) {
        throw new RuntimeException("Doppelte Vraiablendeklarierung");
      }
      // der erste Parameter hat den Index -#parameters+1
      // der zweite den Index -#parameters+2
      // der letzt den Index -#parameters+#parameters = 0
      varMap.put(function.getParameters()[i], -(function.getParameters().length - 1) + i);
    }

    addLabel(function.getName());

    // returnInt ist die Anzahl der lokalen Variablen
    addCmd("ALLOC " + returnInt);

    returnInt += function.getParameters().length;

    for (Statement st : function.getStatements()) {
      st.accept(this);
    }
  }

  @Override
  public void visit(Declaration declaration) {
    for (String name : declaration.getNames()) {
      if (varMap.containsKey(name)) {
        throw new RuntimeException("Doppelte Vraiablendeklarierung");
      }
      varMap.put(name, varMap.size() + 1);
    }
  }

  ///////////////////////////////////////////////// Statement ANFANG
  @Override
  public void visit(Statement statement) {
    // wird eh nicht aufgerufen
  }

  /////////////

  @Override
  public void visit(Assignment assignment) {
    if (!varMap.containsKey(assignment.getName())) {
      throw new RuntimeException("Variable " + assignment.getName() + " wurde nicht deklariert.");
    }
    assignment.getExpression().accept(this);
    addCmd("STS " + varMap.get(assignment.getName()));
  }

  @Override
  public void visit(Composite composite) {
    for (Statement st : composite.getStatements()) {
      st.accept(this);
    }
  }

  @Override
  public void visit(IfThen ifThen) {
    ifThen.getCond().accept(this);

    addCmd("NOT");
    int labelCounter = specialLabelCounter++;
    addCmd("JUMP ifende" + labelCounter);

    ifThen.getThenBranch().accept(this);

    addLabel("ifende" + labelCounter);
  }

  @Override
  public void visit(IfThenElse ifThenElse) {
    ifThenElse.getCond().accept(this);

    addCmd("NOT");
    int labelCounter = specialLabelCounter++;
    addCmd("JUMP else" + labelCounter);

    ifThenElse.getThenBranch().accept(this);
    addCmd("LDI -1");
    addCmd("JUMP ifende" + labelCounter);

    addLabel("else" + labelCounter);
    ifThenElse.getElseBranch().accept(this);

    addLabel("ifende" + labelCounter);
  }

  @Override
  public void visit(While aWhile) {
    int labelCounter = specialLabelCounter++;
    addLabel("while" + labelCounter);

    aWhile.getCond().accept(this);
    addCmd("NOT");
    addCmd("JUMP whileende" + labelCounter);

    aWhile.getBody().accept(this);

    addCmd("LDI -1");
    addCmd("JUMP while" + labelCounter);

    addLabel("whileende" + labelCounter);
  }

  @Override
  public void visit(Read read) {
    if (!varMap.containsKey(read.getName())) {
      throw new RuntimeException("Variable " + read.getName() + " wurde nicht deklariert.");
    }
    addCmd("IN");
    addCmd("STS " + varMap.get(read.getName()));
  }

  @Override
  public void visit(Write write) {
    write.getExpression().accept(this);
    addCmd("OUT");
  }

  @Override
  public void visit(Return aReturn) {
    aReturn.getExpression().accept(this);

    addCmd("RETURN " + returnInt);
    //returnInt = 0;
  }

  ///////////////////////////////////////////////// Statement ENDE


  ///////////////////////////////////////////////// Expression ANFANG
  @Override
  public void visit(Expression expression) {
    // wird eh nicht aufgerufen
  }

  /////////////

  @Override
  public void visit(Variable variable) {
    if (!varMap.containsKey(variable.getName())) {
      throw new RuntimeException("Variable " + variable.getName() + " wurde nicht deklariert.");
    }
    addCmd("LDS " + varMap.get(variable.getName()));
  }

  @Override
  public void visit(Number number) {
    addCmd("LDI " + number.getValue());
  }

  @Override
  public void visit(Binary binary) {
    binary.getRhs().accept(this);
    binary.getLhs().accept(this);

    switch (binary.getOperator()) {
      case Minus:
        addCmd("SUB");
        break;
      case Modulo:
        addCmd("MOD");
        break;
      case MultiplicationOperator:
        addCmd("MUL");
        break;
      case Plus:
        addCmd("ADD");
        break;
      case DivisionOperator:
        addCmd("DIV");
        break;
    }
  }

  @Override
  public void visit(Unary unary) {

    // Minus als einziger unary, und '- expr' entspricht '0 - expr' als binary

    new Binary(new Number(0), Binop.Minus, unary.getOperand()).accept(this);
  }

  @Override
  public void visit(Call call) {

    for (int i = 0; i < call.getArguments().length; i++) {
      call.getArguments()[i].accept(this);
    }

    Function dummyFunc = new Function(call.getFunctionName(), new String[call.getArguments().length],
        new Declaration[0], new Statement[0]);

    if(!functions.contains(dummyFunc)){
      throw new RuntimeException("Invalid Method Call");
    }

    addCmd("LDI " + call.getFunctionName());

    // Platz im Programm machen für TailCallOptimization
    if(functions.indexOf(dummyFunc) == currFunc){
      for (int i = 1; i < call.getArguments().length; i++) {
        addCmd("NOP");
      }
    }

    addCmd("CALL " + call.getArguments().length);
  }

  ///////////////////////////////////////////////// Expression ENDE


  ///////////////////////////////////////////////// Condition ANFANG
  @Override
  public void visit(Condition condition) {
    // wird eh nicht aufgerufen
  }

  /////////////

  @Override
  public void visit(True aTrue) {
    addCmd("LDI -1");
  }

  @Override
  public void visit(False aFalse) {
    addCmd("LDI 0");
  }

  @Override
  public void visit(BinaryCondition binaryCondition) {
    binaryCondition.getRhs().accept(this);
    binaryCondition.getLhs().accept(this);

    switch (binaryCondition.getOperator()) {
      case Or:
        addCmd("OR");
        break;
      case And:
        addCmd("AND");
        break;
    }
  }

  @Override
  public void visit(Comparison comparison) {
    comparison.getRhs().accept(this);
    comparison.getLhs().accept(this);

    switch (comparison.getOperator()) {
      case Equals:
        addCmd("EQ");
        break;
      case NotEquals:
        addCmd("EQ");
        addCmd("NOT");
        break;
      case Less:
        addCmd("LT");
        break;
      case LessEqual:
        addCmd("LE");
        break;
      case Greater:
        addCmd("LE");
        addCmd("NOT");
        break;
      case GreaterEqual:
        addCmd("LT");
        addCmd("NOT");
        break;
    }
  }

  @Override
  public void visit(UnaryCondition unaryCondition) {
    unaryCondition.getOperand().accept(this);

    switch (unaryCondition.getOperator()) {
      case Not:
        addCmd("NOT");
        break;
    }
  }

  ///////////////////////////////////////////////// Condition ENDE

  private void addCmd(String cmd) {
    program += cmd + "\n";
    cmds++;
  }

  private void addLabel(String label) {
    program += label + ":\n";
  }

  public int[] getProgram() {
    //System.out.println(program + "\n-------------------------\n");
    return Interpreter.parse(program);
  }


  public static void main(String[] args) {

    CodeGenerationVisitor cgv = new CodeGenerationVisitor();

    new Call("func1",
        new Expression[]{
            new Number(22),
            new Binary(
                new Binary(new Number(4), Binop.Plus, new Number(29)),
                Binop.DivisionOperator,
                new Binary(new Number(9), Binop.Minus, new Number(6)))
        }).accept(cgv);

    System.out.println(cgv.program);

  }

  private class FunctionVisitor extends Visitor {

    @Override
    public void visit(Program program) {
      for (Function func : program.getFunctions()) {
        func.accept(this);
      }
    }

    @Override
    public void visit(Function function) {
      if (functions.contains(function)) {
        throw new RuntimeException("Doppelte Funktionsdeklarierung");
      }
      functions.add(function);
    }
  }


}
