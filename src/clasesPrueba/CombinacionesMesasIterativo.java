package clasesPrueba;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinacionesMesasIterativo {

    public static void main(String[] args) {
        int[] mesas = { 4, 5, 6, 7, 8, 9, 12 };
        int capacidadMesa = 4;

        // llama al metodo para hacer la combinacion
        List<int[]> combinaciones = generarCombinaciones(mesas);

        // print
        for (int[] combinacion : combinaciones) {
            if (combinacion.length > 0) {
                int capacidadTotal = combinacion.length * capacidadMesa;
                System.out.print("Combinación: " + Arrays.toString(combinacion));
                System.out.println(", Capacidad total: " + capacidadTotal + " personas");
            }
        }
    }

    // metodo para combinar, quiero que me devuelva un arreglo de combinaciones por
    // cantidadmesa
    public static ArrayList<int[]> generarCombinaciones(int[] mesas) {
        ArrayList<int[]> combinaciones = new ArrayList<>(); // arreglo de combinaciones
        int mesascantidad = mesas.length; // el arreglo de las mesas
        // https://stackoverflow.com/questions/30004456/what-does-the-symbol-mean-in-java
        for (int i = 1; i < (1 << mesascantidad); i++) {

            // arreglo para guardar la combinacion actual
            List<Integer> combinacion = new ArrayList<>();
            // itera sobre ellas y las guarda si la mesa esta incluida en la combinacion
            // actual
            for (int j = 0; j < mesascantidad; j++) {
                if ((i & (1 << j)) != 0) {
                    combinacion.add(mesas[j]);
                }
            }
            // parseo de la combinacion de ArrayList<Integer> a int[]
            // https://aadlakha.hashnode.dev/how-to-convert-listlessintegergreater-to-int-in-java-using-streams
            combinaciones.add(combinacion.stream().mapToInt(Integer::intValue).toArray());
        }

        return combinaciones;
    }
}
