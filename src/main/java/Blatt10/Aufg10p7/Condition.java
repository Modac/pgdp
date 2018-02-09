package Blatt10.Aufg10p7;

public class Condition extends ProgramPart {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
