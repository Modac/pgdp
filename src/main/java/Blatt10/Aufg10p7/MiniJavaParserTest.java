package Blatt10.Aufg10p7;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

public class MiniJavaParserTest {

  public static void lexTestBig() {

    String program = "int sum, n, i;\n"
        + "n = read();\n"
        + "while (n < 0) {\n"
        + "n = read();\n"
        + "}\n"
        + "\n"
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
        + "\n"
        + "write(sum);";

    String[] expectedLex = {
        "int", "sum", ",", "n", ",", "i", ";",
        "n", "=", "read", "(", ")", ";",
        "while", "(", "n", "<", "0", ")", "{",
        "n", "=", "read", "(", ")", ";",
        "}",
        "sum", "=", "0", ";",
        "i", "=", "0", ";",
        "while", "(", "i", "<", "n", ")", "{",
        "{",
        "if", "(", "i", "%", "3", "==", "0", "||", "i", "%", "7", "==", "0", ")", "{",
        "sum", "=", "sum", "+", "i", ";",
        "if", "(", "i", "%", "3", "==", "0", "||", "i", "%", "7", "==", "0", ")", "{",
        "sum", "=", "sum", "+", "i", ";",
        "}",
        "else",
        "sum", "=", "99", ";",
        "}",
        "i", "=", "i", "+", "1", ";",
        "}",
        "}",
        "write", "(", "sum", ")", ";",
    };

    MiniJavaParser mjp = new MiniJavaParser(program);

    String[] actualLex = mjp.getProgram();

    System.out.println(Arrays.toString(expectedLex));
    System.out.println(Arrays.toString(actualLex));

    assertArrayEquals(expectedLex, actualLex);
  }

  public static void testParser(){
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
        + "if (i > 4 &&  3 == 0 || i % 7 == 0) {\n"
        + "sum = sum + i;\n"
        + "} else\n"
        + "sum = 99;\n"
        + "}\n"
        + "i = i + 1;\n"
        + "}\n"
        + "}\n"
        + "write(sum);";

    // [int, sum, ,, n, ,, i, ;, n, =, read, (, ), ;, while, (, n, <, 0, ), {, n, =, read, (, ), ;, }]

    MiniJavaParser mjp = new MiniJavaParser(tP1);

    mjp.parseProgram();

    System.out.println(mjp.getFrom() + " | " + mjp.getProgram().length);

  }

  public static void main(String[] args) {
    lexTestBig();

    testParser();
  }

}
