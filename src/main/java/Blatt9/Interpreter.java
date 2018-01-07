package Blatt9;

import Libraries.MiniJava;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.annotation.Generated;


@Generated("Ignore Duplicate Code, because of the Files in Abgaben")
public class Interpreter extends MiniJava {

  public static final int NOP = 0;
  public static final int ADD = 1;
  public static final int SUB = 2;
  public static final int MUL = 3;
  public static final int MOD = 4;
  public static final int LDI = 5;
  public static final int LDS = 6;
  public static final int STS = 7;
  public static final int JUMP = 8;
  public static final int JE = 9;
  public static final int JNE = 10;
  public static final int JLT = 11;
  public static final int IN = 12;
  public static final int OUT = 13;
  public static final int CALL = 14;
  public static final int RETURN = 15;
  public static final int HALT = 16;
  public static final int ALLOC = 17;

  public static void main(String[] args) {
    System.out.println("Rückgabe: " + execute(parse(readProgramConsole())));
  }

  static void error(String message) {
    throw new RuntimeException(message);
  }

  public static String readProgramConsole() {
    @SuppressWarnings("resource")
    Scanner sin = new Scanner(System.in);
    StringBuilder builder = new StringBuilder();
    while (true) {
      String nextLine = sin.nextLine();
      if (nextLine.equals("")) {
        nextLine = sin.nextLine();
        if (nextLine.equals("")) {
          break;
        }
      }
      if (nextLine.startsWith("//")) {
        continue;
      }
      builder.append(nextLine);
      builder.append('\n');
    }
    return builder.toString();
  }

  private static Map<String, Integer> labelMap = new HashMap<>();

  private static int[] stack = new int[128];
  private static int sp = -1;
  private static int fp = sp;

  public static int execute(int[] program) {

    // Für Alloc
    int prevOpCode = -1;

    for (int i = 0; i < program.length; i++) {
      int opcode = program[i] >> 16;
      int imm = (short) (program[i] & 0xFFFF);

      // System.out.println(opcode + ", " + Integer.toBinaryString(imm));

      switch (opcode) {
        case ADD:
          add();
          break;
        case SUB:
          sub();
          break;
        case MUL:
          mul();
          break;
        case MOD:
          mod();
          break;
        case LDI:
          ldi(imm);
          break;
        case LDS:
          lds(imm);
          break;
        case STS:
          sts(imm);
          break;
        case JUMP:
          i = jump(imm) - 1;
          break;
        case JE:
          // i+1 wirkt dem - 1 entgegen und das - 1 wirkt dem i++ von der for Schleife entgegen
          i = je(imm, i+1) - 1;
          break;
        case JNE:
          i = jne(imm, i+1) - 1;
          break;
        case JLT:
          i = jlt(imm, i+1) - 1;
          break;
        case IN:
          in();
          break;
        case OUT:
          out();
          break;
        case CALL:
          i = call(imm, i+1) - 1;
          break;
        case RETURN:
          i = returnIns(imm) - 1;
          break;
        case HALT:
          //halt();
          return pop();
        case ALLOC:
          alloc(imm, prevOpCode);
          break;
        default:
          // Do nothing
      }

      if (!inRange(i + 1, program.length)) {
        error("Ungültiger Sprung zu Index: " + i);
      }

      prevOpCode = opcode;
    }
    error("Das Programm wurde nicht durch HALT beendet");
    return -1;
  }

  private static void alloc(int imm, int prevOpCode) {
    if (prevOpCode != CALL && prevOpCode != -1) {
      error("Alloc kann nur am Anfang einer Funktion oder des Programms stehen");
    }
    for (int i = 0; i < imm; i++) {
      push(0);
    }
  }

  private static int returnIns(int imm) {
    // Hol den Rückgabewert
    int rV = pop();

    // Entferne alle Argumente und lokalen Variablen entfernen
    for (int i = 0; i < imm; i++) {
      pop();
    }

    // Rücksprungadresse sichern
    int rA = pop();

    // Frame-Pointer wiederherstellen
    fp = pop();

    // Rückgabewert wieder auf den Stack
    push(rV);

    return rA;
  }

  private static int call(int imm, int n) {
    // Funktionsadresse sichern
    int f = pop();

    if (imm < 0) {
      error("Keine negative Anzahl an Argumenten");
    }

    // Argumente sichern
    int[] p = new int[imm];
    for (int i = imm - 1; i >= 0; i--) {
      p[i] = pop();
    }

    // Frame-Pointer auf den Stack
    push(fp);
    // Rücksprungadresse auch
    push(n);

    // Argumente wieder drauf
    for (int i = 0; i < imm; i++) {
      push(p[i]);
    }

    fp = sp;

    return f;
  }

  private static int jlt(int imm, int i) {
    return (pop() < pop()) ? imm : i;
  }

  private static int jne(int imm, int i) {
    return (pop() != pop()) ? imm : i;
  }

  private static int je(int imm, int i) {
    return (pop() == pop()) ? imm : i;
  }

  private static int jump(int imm) {
    return imm;
  }

  private static void sts(int imm) {
    if (!inRange(fp + imm, sp + 1)) {
      error("Ungültiger Frame-Zellen Index: " + (fp + imm));
    }
    stack[fp + imm] = pop();
  }

  private static boolean inRange(int i, int bound) {
    return i >= 0 && i < bound;
  }

  private static void lds(int imm) {
    if (!inRange(fp + imm, sp + 1)) {
      error("Ungültiger Frame-Zellen Index: " + (fp + imm));
    }
    push(stack[fp + imm]);
  }

  private static void out() {
    write(pop());
  }

  private static void in() {
    push(read("Eingabe:"));
  }

  private static void ldi(int imm) {
    push(imm);
  }

  private static void mod() {
    push(pop() % pop());
  }

  private static void mul() {
    push(pop() * pop());
  }

  private static void sub() {
    push(pop() - pop());
  }

  private static void add() {
    push(pop() + pop());
  }

  public static void push(int value) {
    sp++;
    if (sp >= stack.length) {
      error("Stack voll");
    }
    stack[sp] = value;
  }

  public static int pop() {
    if (sp < 0) {
      error("Stack leer");
    }
    return stack[sp--];
  }


  public static int[] parse(String textProgram) {

    String lines[] = textProgram.split("\\R+");
    String proLines[] = new String[lines.length];
    int proLinesN = 0;

    for (int i = 0; i < lines.length; i++) {

      if (lines[i].endsWith(":")) {
        boolean hasLetter = false;
        boolean hasSpecial = false;
        for (int j = 0; j < lines[i].length() - 1; j++) {
          char charT = lines[i].charAt(j);
          if ((('A' <= charT) && (charT <= 'Z')) ||   // Großbuchstabe oder
              (('a' <= charT) && (charT <= 'z'))) {   // Kleinbuchstabe
            hasLetter = true;
          } else if (!(('0' <= charT) && (charT <= '9'))) {   // Keine Zahl
            hasSpecial = true;
          }
        }
        String label = new StringBuilder(lines[i]).deleteCharAt(lines[i].length() - 1).toString();

        if (hasLetter && !hasSpecial) {
          if (labelMap.containsKey(label)) {
            error("Label " + label + " kommt mehrfach vor");
          } else {
            labelMap.put(label, proLinesN);
          }
        } else {
          error("Label " + label + " ist ungültig");
        }

      } else {
        String[] command = lines[i].split("\\s+");
        // System.out.println(Arrays.toString(command));
        if (command.length == 0 || getOpCode(command[0]) == -1) {
          error("Ungültiger Befehl: " + lines[i]);
        } else {
          proLines[proLinesN] = lines[i];
          proLinesN++;
        }
      }
    }
    int[] instrA = new int[proLinesN];

    for (int i = 0; i < instrA.length; i++) {
      instrA[i] = commandToInstruction(proLines[i]);
    }

    // System.out.println(labelMap.toString());
    // System.out.println(Arrays.toString(proLines));
    // System.out.println(Arrays.toString(instrA));

    return instrA;
  }

  private static int commandToInstruction(String command) {
    String[] commandA = command.split("\\s+");
    int opcode = getOpCode(commandA[0]);
    String immediate = getImmediate(command);

    // Wenn NOP ADD SUB MUL MOD IN OUT oder HALT und es ein Immediate gibt
    if ((opcode <= MOD || opcode == IN || opcode == HALT || opcode == OUT)
        && !immediate.equals("")) {
      error("Befehl " + command + " darf kein Immediate haben");
    // Wenn es ein Befehl ist, der ein Immediate braucht, aber keins da ist
    } else if (!(opcode <= MOD || opcode == IN || opcode == HALT || opcode == OUT)
        && immediate.equals("")) {
      error("Befehl " + command + " muss ein Immediate haben");
    }
    int immediateInt = 0;
    if (!immediate.equals("")) {
      // Ersetze labels durch die zugehörigen Indizes
      if (((opcode >= JUMP && opcode <= JLT) || opcode == LDI) && labelMap.containsKey(immediate)) {
        immediate = "" + labelMap.get(immediate);
      }

      try {
        immediateInt = Integer.parseInt(immediate);
      } catch (NumberFormatException nfe) {
        error("Immediate " + immediate + " ist keine gültige Zahl");
      }

      if (immediateInt > Short.MAX_VALUE || immediateInt < Short.MIN_VALUE) {
        error("Immediate " + immediate + " ist zu größ bzw. zu klein");
      }

    }
    // System.out.println(command + ": " + opcode + ", " + immediateInt + ", " + Integer.toHexString((opcode << 16) | (immediateInt & 0xFFFF)));
    return (opcode << 16) | (immediateInt & 0xFFFF);
  }

  private static String getImmediate(String command) {
    String[] commandA = command.split("\\s+");
    //System.out.println(Arrays.toString(commandA));
    if (commandA.length > 2) {
      error("Befehl " + command + " hat zu viele Immediates");
    } else if (commandA.length == 1) {
      return "";
    }
    return commandA[1];
  }

  private static int getOpCode(String mnemonic) {
    mnemonic = mnemonic.toUpperCase();
    switch (mnemonic) {
      case "NOP":
        return NOP;
      case "ADD":
        return ADD;
      case "SUB":
        return SUB;
      case "MUL":
        return MUL;
      case "MOD":
        return MOD;
      case "LDI":
        return LDI;
      case "LDS":
        return LDS;
      case "STS":
        return STS;
      case "JUMP":
        return JUMP;
      case "JE":
        return JE;
      case "JNE":
        return JNE;
      case "JLT":
        return JLT;
      case "IN":
        return IN;
      case "OUT":
        return OUT;
      case "CALL":
        return CALL;
      case "RETURN":
        return RETURN;
      case "HALT":
        return HALT;
      case "ALLOC":
        return ALLOC;
      default:
        return -1;
    }
  }


}