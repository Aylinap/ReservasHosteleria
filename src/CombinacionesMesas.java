import java.util.ArrayList;
import java.util.List;

public class CombinacionesMesas {

    public static void main(String[] args) {
        int[] mesas = { 4, 5, 6, 7, 8, 9, 12 };
        int capacidadMesa = 4;

        List<List<Integer>> combinaciones = generarCombinaciones(mesas, capacidadMesa);

        // Imprimir las combinaciones y su capacidad total
        for (List<Integer> combinacion : combinaciones) {
            int capacidadTotal = combinacion.size() * capacidadMesa;
            System.out.println("Combinación: " + combinacion + ", Capacidad total: " + capacidadTotal + " personas");
        }
    }

    public static List<List<Integer>> generarCombinaciones(int[] mesas, int capacidadMesa) {
        List<List<Integer>> combinaciones = new ArrayList<>();

        for (int i = 1; i <= mesas.length; i++) {
            generarCombinacionesAux(mesas, 0, i, new ArrayList<>(), combinaciones);
        }

        return combinaciones;
    }

    public static void generarCombinacionesAux(int[] mesas, int indice, int tamaño, List<Integer> combinacionParcial,
            List<List<Integer>> combinaciones) {
        if (tamaño == 0) {
            combinaciones.add(new ArrayList<>(combinacionParcial));
            return;
        }

        if (indice == mesas.length) {
            return;
        }

        // Incluir la mesa actual en la combinación
        combinacionParcial.add(mesas[indice]);
        generarCombinacionesAux(mesas, indice + 1, tamaño - 1, combinacionParcial, combinaciones);
        combinacionParcial.remove(combinacionParcial.size() - 1);

        // No incluir la mesa actual en la combinación
        generarCombinacionesAux(mesas, indice + 1, tamaño, combinacionParcial, combinaciones);
    }
}
