package Blatt11.Aufg11p7;

public class Expression extends ProgramPart {

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
