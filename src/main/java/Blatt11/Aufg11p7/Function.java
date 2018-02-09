package Blatt11.Aufg11p7;

public class Function extends ProgramPart {

  private Type type;
  private String name;
  private Parameter[] parameters;
  private Declaration[] declarations;
  private Statement[] statements;

  public Function(Type type, String name, Parameter[] parameters, Declaration[] declarations,
      Statement[] statements) {
    this.type = type;
    this.name = name;
    this.parameters = parameters;
    this.declarations = declarations;
    this.statements = statements;
  }

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public Parameter[] getParameters() {
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
    String res = type + " " + name + "(";

    for (int i = 0; i < parameters.length; i++) {
      if (i != 0) {
        res += ", ";
      }
      res += parameters[i].getType() + " " + parameters[i].getName();
    }
    return res += ")";
  }
}
