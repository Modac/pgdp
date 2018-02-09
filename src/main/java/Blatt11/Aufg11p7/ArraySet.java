package Blatt11.Aufg11p7;

public class ArraySet extends Statement{

  private String name;
  private Expression indexExpr;
  private Expression expression;

  public ArraySet(String name, Expression indexExpr, Expression expression) {
    this.name = name;
    this.indexExpr = indexExpr;
    this.expression = expression;
  }

  public String getName() {
    return name;
  }

  public Expression getIndexExpr() {
    return indexExpr;
  }

  public Expression getExpression() {
    return expression;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
