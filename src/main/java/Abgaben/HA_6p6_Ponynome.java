public class HA_6p6_Ponynome extends MiniJava {

  public static void main(String[] args) {

    double[] coeffs = new double[4];
    for (int i = 0; i < coeffs.length; i++) {
      coeffs[i] = readDouble(coeffs.length - i + ". Koeffizient:");
    }
    String res = "";
    boolean finite = true;
    int[] ns = new int[0];
    if (coeffs[0] == 0) {          // a2*x^2+a1*x+a0 => nur eine quadratische Gleichung
      if (coeffs[1] == 0) {        // a1*x+a0 => Gerade
        if (coeffs[2] == 0) {      // a0 => Konstante
          if (coeffs[3] == 0) {    // unendlich viele Nullstellen
            res = "Das Polynom hat unendlich viele Nullstellen";
            finite = false;
          }
        } else {                   // Gerade mit Steigung ungleich 0
          int[] interv = findIntervalRecursive(coeffs, -2, 2, 10);
          ns = new int[]{findRootRecursive(coeffs, interv[0], interv[1])};
        }
      } else {                     // quadratische Gleichung die keine Gerade ist
        double[] redCoeffs = new double[]{coeffs[1], coeffs[2], coeffs[3]};
        ns = quadraticFormula(redCoeffs);
      }
    } else {                       // ganz normaler Polynom dritten Grades
      int[] interv = findIntervalRecursive(coeffs, -2, 2, 10);
      int r0 = findRootRecursive(coeffs, interv[0], interv[1]);
      coeffs = hornerShema(coeffs, r0);
      int[] rs = quadraticFormula(coeffs);
      ns = new int[rs.length + 1];
      ns[0] = r0;
      for (int i = 0; i < rs.length; i++) {
        ns[i + 1] = rs[i];
      }
    }

    if (finite) {
      res = "Das Polynom hat " + ns.length + " Nullstelle" + (ns.length != 1 ? "n: " : ": ");
    }
    for (int i = 0; i < ns.length; i++) {
      res += ns[i];
      if (i < ns.length - 1) {
        res += ", ";
      }
    }
    write(res);

    //test();
  }

  /*
  public static void test() {
    System.out.println(Arrays.toString(quadraticFormula(new double[]{0, 4, 5})));
    System.out.println(quadraticFormula(new double[]{0, 4, 5}).length);
    System.out.println(Arrays.toString(hornerShema(new double[]{0.5, 5, -33.5, -308}, 8)));
    System.out.println(
        Arrays.toString(findIntervalRecursive(new double[]{0.5, 5, -33.5, -308}, -2, 2, 10)));
    System.out.println(findRootRecursive(new double[]{0.5, 5, -33.5, -308}, -20, 20));

  }
  */

  public static int[] quadraticFormula(double[] coefficients) {
    int x1;
    int x2;
    double a0 = coefficients[2];
    double a1 = coefficients[1];
    double a2 = coefficients[0];

    if (a2 == 0) {
      return new int[0];
    }

    double sqrt = (a1 * a1) - (4 * a2 * a0);

    // Wenn die Zahl unter der Wurzel negativ ist, gibt es keine Lösung
    if (sqrt < 0) {
      return new int[0];
    }

    sqrt = Math.sqrt(sqrt);

    x1 = (int) ((-a1 + sqrt) / (2 * a2));
    x2 = (int) ((-a1 - sqrt) / (2 * a2));

    /* Doppelte Nullstellen werden doppelt zurückgegeben
    if (x1 == x2) {
      return new int[]{x1};
    }
    */

    return new int[]{x1, x2};
  }

  public static double[] hornerShema(double[] coefficients, int x0) {
    double[] coeff = new double[3];
    coeff[0] = coefficients[0];
    for (int i = 1; i < coeff.length; i++) {
      coeff[i] = coefficients[i] + coeff[i - 1] * x0;
    }
    return coeff;
  }

  public static double calculateY(double[] coefficients, int x) {
    double res = 0;
    for (int i = coefficients.length - 1; i >= 0; i--) {
      res += coefficients[i] * Math.pow(x, (coefficients.length - 1) - i);
    }
    return res;
  }

  public static int[] findIntervalRecursive(double[] coefficients, int a, int b, int factor) {
    double yA = calculateY(coefficients, a);
    double yB = calculateY(coefficients, b);

    // Wenn f(a) und f(b) das gleiche Vorzeichen haben (außer 0) erweitere die Intervallgrenzen
    // und versuchs weiter ansonsten gib das jetzige Interval zurück
    return ((yA < 0 && yB < 0) || (yA > 0 && yB > 0)) ?
        findIntervalRecursive(coefficients, a * factor, b * factor, factor) :
        new int[]{a, b};
  }

  public static int findRootRecursive(double[] coefficients, int a, int b) {
    int m = (a + b) / 2;
    double fA = calculateY(coefficients, a);
    double fM = calculateY(coefficients, m);
    double fB = calculateY(coefficients, b);

    if (fA == 0) {
      return a;
    }
    if (fM == 0) {
      return m;
    }
    if (fB == 0) {
      return b;
    }

    // Wenn f(a) und f(m) das gleich Vorzeichen haben (außer 0), dann nimm [m, b] als neues Intervall
    // sonst nimm [a, m]
    if ((fA < 0 && fM < 0) || fA > 0 && fM > 0) {
      return findRootRecursive(coefficients, m, b);
    }
    return findRootRecursive(coefficients, a, m);

  }

}
