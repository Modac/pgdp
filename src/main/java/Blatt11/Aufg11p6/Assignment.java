package Blatt11.Aufg11p6;

public class Assignment extends Statement {

  private String name;
  private Expression expression;

  public Assignment(String name, Expression expression) {
    this.name = name;
    this.expression = expression;
  }

  public String getName() {
    return name;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
