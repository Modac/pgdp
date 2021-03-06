package Blatt11.Aufg11p6;

public class Declaration extends ProgramPart {

  private String[] names;

  public Declaration(String[] names) {
    this.names = names;
  }

  public String[] getNames() {
    return names;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
