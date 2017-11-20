public class HA_3p7_Feldverarbeitung extends MiniJava {

    public static void main(String[] args) {
        int n = read("Gewünschte Größe des Arrays:");
        while (n < 2) {
            n = read("Das Array muss mindestens 2 Elemente enthalten. Gewünschte Größe:");
        }

        int[] array = new int[n];
        int i = 0;
        while (i < n) {
            array[i] = read("Bitte gib das " + (i + 1) + "-te Element ein:");
            i++;
        }

        // erste Teilaufgabe
        i = 0;
        int summe = 0;
        // Bilde die Summe der Elemente an geraden Indices
        while (i < n) {
            summe += array[i];
            i += 2;
        }

        // Ziehe alle Elemente an ungeraden Indices ab
        i = 1;
        while (i < n) {
            summe -= array[i];
            i += 2;
        }
        write("Ausgabe der ersten Teilaufgabe: " + summe);


        // zweite Teilaufgabe
        int max = 0;
        int lastMax = max;
        i = 0;
        while (i < n) {
            if(array[i] > max){
                lastMax = max;
                max = array[i];
            // Falls das aktuelle Element nicht das größte ist, aber größer als das bisherige zweitgrößte
            } else if(array[i] > lastMax){
                lastMax = array[i];
            }
            i++;
        }
        write("Zweitgrößtes Element: " + lastMax);


        // dritte Teilaufgabe
        i = 0;
        while (i<(n-1)){
            array[i] += array[i+1];
            writeLineConsole(i + ": " + array[i]);
            writeLineConsole((i+1) + ": " + array[i+1]);
            i+=2;
        }
        if(i==(n-1)){
            writeLineConsole(i + ": " + array[i]);
        }

    }
}
