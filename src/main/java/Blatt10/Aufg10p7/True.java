package Blatt10.Aufg10p7;

public class True extends Condition {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
