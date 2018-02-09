package Blatt10.Aufg10p7;

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
