package Blatt9.Aufg9p6;

public class Program extends ProgramPart{

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
