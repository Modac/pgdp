package Blatt11.Aufg11p6;

public enum Bbinop {
  And, Or;

  @Override
  public String toString() {
    switch (this) {
      case And:
        return "&&";
      case Or:
        return "||";
      default:
        return "";
    }
  }
}
