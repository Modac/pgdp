package Blatt9.Aufg9p6;

public class Function extends ProgramPart{

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

  public String getName(){
    return name;
  }

  public String[] getParameters(){
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
}
