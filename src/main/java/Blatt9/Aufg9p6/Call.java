package Blatt9.Aufg9p6;

public class Call extends Expression{

  private String functionName;
  private Expression[] arguments;

  public Call(String functionName, Expression[] arguments) {
    this.functionName = functionName;
    this.arguments = arguments;
  }

  public String getFunctionName() {
    return functionName;
  }

  public Expression[] getArguments() {
    return arguments;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
