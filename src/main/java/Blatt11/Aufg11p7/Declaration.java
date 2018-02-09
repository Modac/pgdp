package Blatt11.Aufg11p7;

public class Declaration extends ProgramPart {

  private Type type;
  private String[] names;

  public Declaration(Type type, String[] names) {
    this.type = type;
    this.names = names;
  }

  public Type getType() {
    return type;
  }

  public String[] getNames() {
    return names;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
