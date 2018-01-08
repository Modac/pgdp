package Blatt9.Aufg9p6;

public class Expression extends ProgramPart{

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
