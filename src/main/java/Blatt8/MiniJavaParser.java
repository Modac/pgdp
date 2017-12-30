package Blatt8;

import java.util.ArrayList;
import java.util.Scanner;

public class MiniJavaParser {

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

  public static String[] lex(String program) {
    ArrayList<String> res = new ArrayList<>();

    StringBuilder token = new StringBuilder();

    for (int i = 0; i < program.length(); i++) {
      char cT = program.charAt(i);

      if (isAlphaNumeric(cT)) {
        token.append(cT);
      } else if (cT == ' ' && token.length() > 0) {
        res.add(token.toString());
        token.setLength(0);
      } else if (cT != ' ' && cT != '\n') {
        if (token.length() > 0) {
          res.add(token.toString());
          token.setLength(0);
        }

        char nextC = i + 1 < program.length() ? program.charAt(i + 1) : '\u0003';

        if ((nextC == '=' && ((cT == '=') || (cT == '!') || (cT == '<') || (cT == '>'))) ||
            (cT == '&' && nextC == '&') ||
            (cT == '|' && nextC == '|')) {
          res.add("" + cT + nextC);
          i++;
        } else {
          res.add("" + cT);
        }


      }
    }
    return res.toArray(new String[res.size()]);
  }

  private static boolean isUppercaseLetter(char character) {
    return ('A' <= character) && (character <= 'Z');
  }

  private static boolean isLowercaseLetter(char character) {
    return ('a' <= character) && (character <= 'z');
  }

  private static boolean isLetter(char character) {
    return isUppercaseLetter(character) || isLowercaseLetter(character);
  }

  private static boolean isLetter(String character) {
    return character.length() > 0 && isLetter(character.charAt(0));
  }

  private static boolean isNumber(char character) {
    return ('0' <= character) && (character <= '9');
  }

  private static boolean isNumber(String character) {
    return character.length() > 0 && isNumber(character.charAt(0));
  }

  private static boolean isAlphaNumeric(char character) {
    return isLetter(character) || isNumber(character);
  }

  public static int parseNumber(String[] program, int from) {
    if (from >= program.length) {
      return -1;
    }

    if(!isNumber(program[from].charAt(0))) {
      return -1;
    }

    for (int i = 1; i < program[from].length(); i++) {
      if(!isNumber(program[from].charAt(i))) {
        return -1;
      }
    }

    return from+1;

  }

  public static int parseName(String[] program, int from) {
    if (from >= program.length) {
      return -1;
    }

    if(!isLetter(program[from].charAt(0))) {
      return -1;
    }

    for (int i = 1; i < program[from].length(); i++) {
      if(!isAlphaNumeric(program[from].charAt(i))) {
        return -1;
      }
    }

    return from+1;
  }

  public static int parseType(String[] program, int from) {
    if (from < program.length && program[from].equals("int")) {
      return from + 1;
    }
    return -1;
  }

  public static int parseDecl(String[] program, int from) {

    if ((from = parseType(program, from)) < 0) {
      return -1;
    }

    if ((from = parseName(program, from)) < 0) {
      return -1;
    }

    while (from < program.length && program[from].equals(",")) {
      from++;
      if ((from = parseName(program, from)) < 0) {
        return -1;
      }
    }

    return checkToken(program, from, ";");

  }

  public static int parseUnop(String[] program, int from) {
    if (((from = checkToken(program, from, "-")) > 0)) {
      return from;
    }
    return -1;
  }

  public static int parseBinop(String[] program, int from) {
    int fromO = from;

    if ((from = checkToken(program, fromO, "-")) > 0 ||
        (from = checkToken(program, fromO, "+")) > 0 ||
        (from = checkToken(program, fromO, "*")) > 0 ||
        (from = checkToken(program, fromO, "/")) > 0 ||
        (from = checkToken(program, fromO, "%")) > 0) {
      return from;
    }

    return -1;
  }

  public static int parseComp(String[] program, int from) {
    int fromO = from;

    if ((from = checkToken(program, fromO, "==")) > 0 ||
        (from = checkToken(program, fromO, "!=")) > 0 ||
        (from = checkToken(program, fromO, "<=")) > 0 ||
        (from = checkToken(program, fromO, "<")) > 0 ||
        (from = checkToken(program, fromO, ">=")) > 0 ||
        (from = checkToken(program, fromO, ">")) > 0) {
      return from;
    }

    return -1;
  }

  public static int parseExpression(String[] program, int from) {
    int fromO = from;

    if ((from = parseNumber(program, from)) > 0) {
      return from;
    }

    from = fromO;

    if ((from = parseName(program, from)) > 0) {
      return from;
    }

    from = fromO;

    if ((from = checkToken(program, from, "(")) > 0 &&
        (from = parseExpression(program, from)) > 0 &&
        (from = checkToken(program, from, ")")) > 0) {
      return from;
    }

    from = fromO;

    if ((from = parseUnop(program, from)) > 0 &&
        (from = parseExpression(program, from)) > 0) {
      return from;
    }

    from = fromO;

    if ((from = parseExpression(program, from)) > 0 &&
        (from = parseBinop(program, from)) > 0 &&
        (from = parseExpression(program, from)) > 0) {
      return from;
    }

    return -1;
  }

  public static int parseBbinop(String[] program, int from) {
    int fromO = from;

    if ((from = checkToken(program, fromO, "&&")) > 0 ||
        (from = checkToken(program, fromO, "||")) > 0) {
      return from;
    }

    return -1;
  }

  public static int parseBunop(String[] program, int from) {
    if (((from = checkToken(program, from, "!")) > 0)) {
      return from;
    }
    return -1;
  }

  public static int parseCondition(String[] program, int from) {
    int fromO = from;

    if ((from = checkToken(program, from, "true")) > 0) {
      return from;
    }

    from = fromO;

    if ((from = checkToken(program, from, "false")) > 0) {
      return from;
    }

    from = fromO;

    if ((from = checkToken(program, from, "(")) > 0 &&
        (from = parseCondition(program, from)) > 0 &&
        (from = checkToken(program, from, ")")) > 0) {
      return from;
    }

    from = fromO;

    if ((from = parseExpression(program, from)) > 0 &&
        (from = parseComp(program, from)) > 0 &&
        (from = parseExpression(program, from)) > 0) {
      return from;
    }

    from = fromO;

    if ((from = parseBunop(program, from)) > 0 &&
        (from = checkToken(program, from, "(")) > 0 &&
        (from = parseCondition(program, from)) > 0 &&
        (from = checkToken(program, from, ")")) > 0) {
      return from;
    }

    from = fromO;

    if ((from = parseCondition(program, from)) > 0 &&
        (from = parseBinop(program, from)) > 0 &&
        (from = parseCondition(program, from)) > 0) {
      return from;
    }
    return -1;
  }

  public static int parseStatement(String[] program, int from) {
    int fromO = from;

    if ((from = checkToken(program, from, ";")) > 0) {
      return from;
    }

    from = fromO;

    if ((from = checkToken(program, from, "{")) > 0) {

      do {
        if (((checkToken(program, from, "}")) > 0)) {
          return from+1;
        }
      } while ((from = parseStatement(program, from)) > 0);

      return -1;

    }

    from = fromO;

    if ((from = parseName(program, from)) > 0) {
      if (((from = checkToken(program, from, "=")) > 0)) {

        int fromO1 = from;

        if ((from = parseExpression(program, from)) > 0) {
          if((from = checkToken(program, from, ";"))>0){
            return from;
          }
        }

        if (((fromO1 = checkToken(program, fromO1, "read")) > 0) &&
            ((fromO1 = checkToken(program, fromO1, "(")) > 0) &&
            ((fromO1 = checkToken(program, fromO1, ")")) > 0)) {
          return checkToken(program, fromO1, ";");
        } else {
          return -1;
        }

      }
    }

    from = fromO;

    if ((from = checkToken(program, from, "write")) > 0 &&
        (from = checkToken(program, from, "(")) > 0 &&
        (from = parseExpression(program, from)) > 0 &&
        (from = checkToken(program, from, ")")) > 0 &&
        (from = checkToken(program, from, ";")) > 0) {
      return from;
    }

    from = fromO;

    if ((from = checkToken(program, from, "if")) > 0 &&
        (from = checkToken(program, from, "(")) > 0 &&
        (from = parseCondition(program, from)) > 0 &&
        (from = checkToken(program, from, ")")) > 0 &&
        (from = parseStatement(program, from)) > 0) {
      int fromO1 = from;
      if ((from = checkToken(program, from, "else")) > 0 &&
          (from = parseStatement(program, from)) > 0) {
        return from;
      }
      return fromO1;

    }

    from = fromO;

    if ((from = checkToken(program, from, "while")) > 0 &&
        (from = checkToken(program, from, "(")) > 0 &&
        (from = parseCondition(program, from)) > 0 &&
        (from = checkToken(program, from, ")")) > 0 &&
        (from = parseStatement(program, from)) > 0) {
      return from;
    }

    return -1;
  }

  public static int checkToken(String[] program, int from, String token) {
    if (from > program.length || !program[from++].equals(token)) {
      return -1;
    }
    return from;
  }

  public static int parseProgram(String[] program) {
    int n = 0;
    int prevN = n;
    while (n >= 0) {
      prevN = n;
      n = parseDecl(program, n);
    }

    n = prevN;

    while (n < program.length) {
      n = parseStatement(program, n);
      if (n < 0) {
        return -1;
      }
    }

    return n;
  }

  public static void main(String[] args) {
    String tP = "int sum, n, i;\n"
        + "n = read();\n"
        + "while (n == 0) {\n"
        + "n = read();\n"
        + "}";

    String tP1 = "int sum, n, i;\n"
        + "n = read();\n"
        + "while (n < 0) {\n"
        + "n = read();\n"
        + "}\n"
        + "sum = 0;\n"
        + "i = 0;\n"
        + "while (i < n) {\n"
        + "{\n"
        + "if (i % 3 == 0 || i % 7 == 0) {\n"
        + "sum = sum + i;\n"
        + "if (i % 3 == 0 || i % 7 == 0) {\n"
        + "sum = sum + i;\n"
        + "} else\n"
        + "sum = 99;\n"
        + "}\n"
        + "i = i + 1;\n"
        + "}\n"
        + "}\n"
        + "write(sum);";

    // [int, sum, ,, n, ,, i, ;, n, =, read, (, ), ;, while, (, n, <, 0, ), {, n, =, read, (, ), ;, }]

    System.out.println(parseProgram(lex(tP1)));

  }
}