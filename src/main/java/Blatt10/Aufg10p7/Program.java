package Blatt10.Aufg10p7;

public class Program extends ProgramPart {

  private Function[] functions;

  public Program(Function[] functions){
    this.functions = functions;
  }

  public Function[] getFunctions(){
    return functions;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
