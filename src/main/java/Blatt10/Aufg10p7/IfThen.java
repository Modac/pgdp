package Blatt10.Aufg10p7;

public class IfThen extends Statement {

  private Condition cond;
  private Statement thenBranch;

  public IfThen(Condition cond, Statement thenBranch) {
    this.cond = cond;
    this.thenBranch = thenBranch;
  }

  public Condition getCond() {
    return cond;
  }

  public Statement getThenBranch() {
    return thenBranch;
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

}
