package Blatt11.Aufg11p6;

public class Number extends Expression {

  private int value;

  public Number(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
