package Blatt11.Aufg11p6;

public class Condition extends ProgramPart {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
