package Blatt11.Aufg11p6;

public class Function extends ProgramPart {

  private String name;
  private String[] parameters;
  private Declaration[] declarations;
  private Statement[] statements;

  public Function(String name, String[] parameters, Declaration[] declarations,
      Statement[] statements) {
    this.name = name;
    this.parameters = parameters;
    this.declarations = declarations;
    this.statements = statements;
  }

  public String getName() {
    return name;
  }

  public String[] getParameters() {
    return parameters;
  }

  public Declaration[] getDeclarations() {
    return declarations;
  }

  public Statement[] getStatements() {
    return statements;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Function)) {
      return false;
    }
    Function func = (Function) obj;
    return this.name.equals(func.name) && this.parameters.length == func.parameters.length;
  }

  @Override
  public String toString() {
    String res = name + "(";

    for (int i = 0; i < parameters.length; i++) {
      if (i != 0) {
        res += ", ";
      }
      res += "int " + parameters[i];
    }
    return res += ")";
  }
}
