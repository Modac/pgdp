package Blatt11.Aufg11p6;

public class Compiler {
  public static int[] compile(String code){
    MiniJavaParser mjp = new MiniJavaParser(code);

    CodeGenerationVisitor cgv = new CodeGenerationVisitor();

    Program program = mjp.parse();
    if(program==null){
      throw new RuntimeException("Das Programm ist kein valides MiniJava Programm");
    }

    program.accept(cgv);

    return cgv.getProgram();
  }
}
