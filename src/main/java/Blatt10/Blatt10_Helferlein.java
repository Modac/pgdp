package Blatt10;

public class Blatt10_Helferlein {

  static class A {
    void m(A a) { System.out.println("A.A+"); }
    void m(B b) { System.out.println("A.B+"); }
    void m(C c) { System.out.println("A.C+"); }
  }
  static class B extends A {
    void m(A a) { System.out.println("B.A+"); }
    void m(B b) { System.out.println("B.B+"); }
    void m(C c) { System.out.println("B.C+"); }
  }
  static class C extends B { }

  public static void main(String[] args) {
    A a = new A();
    B b = new B();
    C c = new C();

    // a.m((A) b); // Statement 1  // A.A+
    // a.m((B) c); // Statement 2  // A.B+
    // a.m(c); // Statement 3  // A.C+
    // b.m(a); // Statement 4  // B.A+
    // b.m((A) b); // Statement 5  // B.A+
    // b.m((A) c); // Statement 6  // B.A+
    // b.m((B) a); // Statement 7  // Exception
    // b.m(c); // Statement 8  // B.C+
  }
}
