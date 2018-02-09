package Blatt11.Aufg11p7;

public class Read extends Statement {

  private String name;

  public Read(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
