public class HA_7p6_SlowAndEvenSlowerSort {

  public static void main(String[] args) {
    int[] array0 = {1};
    int[] array1 = {1, 2};
    int[] array2 = {1, 2, 3};
    int[] array3 = {1, 2, 3, 4};
    int[] array4 = {1, 2, 3, 4, 5};
    int[] array5 = {1, 2, 3, 4, 5, 6};
    int[] array6 = {1, 2, 3, 4, 5, 6, 7};
    int[] arrayU = {6, 2, 4, 7};

    evenSlowerSort(arrayU);
    //System.out.println(Arrays.toString(arrayU));
  }

  public static void slowSort(int[] array) {
    int m = array.length / 2;
    //System.out.println(Arrays.toString(array));

    if (m <= 0) {
      return;
    }

    int[] fHalf = new int[m];
    int[] sHalf = new int[array.length - m];

    for (int i = 0; i < m; i++) {
      fHalf[i] = array[i];
    }
    for (int i = 0; i < sHalf.length; i++) {
      sHalf[i] = array[i + m];
    }
    slowSort(fHalf);
    slowSort(sHalf);

    int fHalfE = fHalf[fHalf.length - 1];
    int sHalfE = sHalf[sHalf.length - 1];
    int max = fHalfE > sHalfE ? fHalfE : sHalfE;

    putMaxAtEnd(array, max);

    int[] redArr = new int[array.length - 1];

    for (int i = 0; i < redArr.length; i++) {
      redArr[i] = array[i];
    }

    slowSort(redArr);

    for (int i = 0; i < redArr.length; i++) {
      array[i] = redArr[i];
    }


  }

  public static void evenSlowerSort(int[] array) {
    //System.out.println(Arrays.toString(array));

    if (array.length <= 1) {
      return;
    }

    int m = array.length / 3;

    int[] fHalf = new int[array.length == 2 ? 1 : m];
    int[] sHalf = new int[array.length == 2 ? 1 : m];

    for (int i = 0; i < fHalf.length; i++) {
      fHalf[i] = array[i];
    }
    for (int i = 0; i < sHalf.length; i++) {
      sHalf[i] = array[i + fHalf.length];
    }
    evenSlowerSort(fHalf);
    evenSlowerSort(sHalf);

    int[] tHalf = new int[0];

    if (array.length > 2) {
      tHalf = new int[array.length - fHalf.length - sHalf.length];

      for (int i = 0; i < tHalf.length; i++) {
        tHalf[i] = array[i + fHalf.length + sHalf.length];
      }

      evenSlowerSort(tHalf);
    }

    int fHalfE = fHalf[fHalf.length - 1];
    int sHalfE = sHalf[sHalf.length - 1];
    int sTMax = sHalfE;
    if (tHalf.length > 0) {
      int tHalfE = tHalf[tHalf.length - 1];
      sTMax = tHalfE > sHalfE ? tHalfE : sHalfE;
    }

    int max = fHalfE > sTMax ? fHalfE : sTMax;

    putMaxAtEnd(array, max);

    int[] redArr = new int[array.length - 1];

    for (int i = 0; i < redArr.length; i++) {
      redArr[i] = array[i];
    }

    evenSlowerSort(redArr);

    for (int i = 0; i < redArr.length; i++) {
      array[i] = redArr[i];
    }

  }

  private static void putMaxAtEnd(int[] array, int max) {
    boolean shift = false;
    for (int i = 0; i < array.length - 1; i++) {
      if (array[i] == max) {
        shift = true;
      }
      if (shift) {
        array[i] = array[i + 1];
      }
    }
    array[array.length - 1] = max;
  }

}
