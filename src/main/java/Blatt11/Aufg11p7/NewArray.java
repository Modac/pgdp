package Blatt11.Aufg11p7;

public class NewArray extends Expression{

  private Expression lengthExpr;

  public NewArray(Expression lengthExpr) {
    this.lengthExpr = lengthExpr;
  }

  public Expression getLengthExpr() {
    return lengthExpr;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
