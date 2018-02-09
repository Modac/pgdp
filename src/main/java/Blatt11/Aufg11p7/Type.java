package Blatt11.Aufg11p7;

public enum Type {
  Int, IntA;

  @Override
  public String toString() {
    switch (this) {
      case Int:
        return "int";
      case IntA:
        return "int[]";
      default:
        return "";
    }
  }
}
