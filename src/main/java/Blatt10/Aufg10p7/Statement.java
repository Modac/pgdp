package Blatt10.Aufg10p7;

public class Statement extends ProgramPart {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
