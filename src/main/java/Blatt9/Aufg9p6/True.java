package Blatt9.Aufg9p6;

public class True extends Condition{

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
