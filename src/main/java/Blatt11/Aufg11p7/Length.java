package Blatt11.Aufg11p7;

public class Length extends Expression {

  private Expression paramExpr;

  public Length(Expression paramExpr) {
    this.paramExpr = paramExpr;
  }

  public Expression getParamExpr() {
    return paramExpr;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
