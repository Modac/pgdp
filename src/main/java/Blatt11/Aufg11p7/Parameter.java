package Blatt11.Aufg11p7;

public class Parameter extends ProgramPart {

  private Type type;
  private String name;
  private int index;

  public Parameter(Type type, String name, int index) {
    this.type = type;
    this.name = name;
    this.index = index;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public int getIndex() {
    return index;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
