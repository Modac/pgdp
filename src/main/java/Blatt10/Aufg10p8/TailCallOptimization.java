package Blatt10.Aufg10p8;

import Blatt10.Aufg10p7.Interpreter;

public class TailCallOptimization {

  public static void optimize(int[] program) {

    int funcStart = 0;
    boolean isPosEndRec = false;
    boolean isEndRec = false;
    int endRecStart = 0;
    int params = 0;

    for (int i = 0; i < program.length; i++) {
      int opCode = Interpreter.getOpcodeFromInstruction(program[i]);
      int imm = Interpreter.getImmFromInstruction(program[i]);

      if (opCode == Interpreter.NOP) {
        continue;
      }

      if (opCode == Interpreter.ALLOC) {
        funcStart = i;
        continue;
      }

      if (opCode == Interpreter.LDI && imm == funcStart && !isPosEndRec) {
        isPosEndRec = true;
        endRecStart = i;
        //funcStart = imm;
      } else if (opCode == Interpreter.CALL && isPosEndRec) {
        params = imm;
      } else if (opCode == Interpreter.RETURN && isPosEndRec) {
        resolveEndRec(program, funcStart, endRecStart, params);
      } else {
        isPosEndRec = false;
      }


    }

    System.out.println("\nOptimized: \n");

    System.out.println(Interpreter.programToString(program));

  }

  private static void resolveEndRec(int[] program, int funcStart, int endRecStart, int params) {

    // Ersetzte die Parameter unterhalb des Frame-Pointers mit den Argumenten oben auf dem Stack
    for (int i = endRecStart; i < endRecStart + params; i++) {
      program[i] = Interpreter.commandToInstruction("STS " + (endRecStart-i));
    }

    // Springe zum Anfang der Funktion aber rufe ALLOC nicht nochmal auf
    program[endRecStart + params] = Interpreter.commandToInstruction("LDI -1");
    program[endRecStart + params + 1] = Interpreter.commandToInstruction("JUMP " + (funcStart+1));

    // Ersetze alle noch nicht ersetzten Befehle von der EndRekursion mit NOP
    for (int i = endRecStart + params + 2; i < endRecStart + 3; i++) {
      program[i] = Interpreter.commandToInstruction("NOP");
    }

  }
}
