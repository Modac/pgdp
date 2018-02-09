package Blatt11.Aufg11p7;

public class ArrayAccess extends Expression{

  private Expression nameExpr;
  private Expression indexExpr;

  public ArrayAccess(Expression nameExpr, Expression indexExpr) {
    this.nameExpr = nameExpr;
    this.indexExpr = indexExpr;
  }

  public Expression getNameExpr() {
    return nameExpr;
  }

  public Expression getIndexExpr() {
    return indexExpr;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
