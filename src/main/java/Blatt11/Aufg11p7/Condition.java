package Blatt11.Aufg11p7;

public class Condition extends ProgramPart {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
