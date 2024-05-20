package clasesPrueba;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MesaDao;

public class MesaGestor2 {
    private Map<Integer, Integer> capacidadMesas = new HashMap<>();
    private Map<Integer, String> salasMesas = new HashMap<>();
    private MesaDao mesaDao;

    public MesaGestor2() {
        capacidadMesas.put(1, 2);
        capacidadMesas.put(10, 4);
        capacidadMesas.put(11, 6);
        capacidadMesas.put(2, 4);
        capacidadMesas.put(3, 4);
        capacidadMesas.put(4, 4);
        capacidadMesas.put(5, 4);
        capacidadMesas.put(6, 4);
        capacidadMesas.put(7, 4);
        capacidadMesas.put(8, 4);
        capacidadMesas.put(9, 4);
        capacidadMesas.put(12, 4);

        salasMesas.put(1, "Sala 1");
        salasMesas.put(2, "Sala 1");
        salasMesas.put(3, "Sala 1");
        salasMesas.put(4, "Sala 2");
        salasMesas.put(5, "Sala 2");
        salasMesas.put(6, "Sala 2");
        salasMesas.put(7, "Sala 2");
        salasMesas.put(8, "Sala 2");
        salasMesas.put(9, "Sala 2");
        salasMesas.put(10, "Sala 2");
        salasMesas.put(11, "Sala 2");
        salasMesas.put(12, "Sala 2");
    }

    public void asignarMesa(int numComensales) {
        // primero verificar que mesas estan disponibles:

        mesasDisponibles();
        // Verificar si el número de comensales coincide exactamente con la capacidad de
        // una mesa prioritaria
        if (capacidadMesas.containsValue(numComensales)
                && (numComensales == 2 || numComensales == 4 || numComensales == 6)) {
            for (Map.Entry<Integer, Integer> entry : capacidadMesas.entrySet()) {
                int mesa = entry.getKey();
                int capacidad = entry.getValue();
                if (capacidad == numComensales && (mesa == 1 || mesa == 10 || mesa == 11)) {
                    System.out.println(
                            "Se asignó la mesa prioritaria " + mesa + " para " + numComensales + " comensales.");
                    return;
                }
            }
        }

        List<List<Integer>> combinaciones = generarCombinaciones(
                capacidadMesas.keySet().toArray(new Integer[0]), numComensales);

        // elimina combinaciones para salas diferentes
        combinaciones.removeIf(c -> {
            String sala = null;
            for (int mesa : c) {
                if (sala == null) {
                    sala = salasMesas.get(mesa);
                } else if (!sala.equals(salasMesas.get(mesa))) {
                    return true;
                }
            }
            return false;
        });

        // eliminar combinaciones con capacidad total menor que el número de comensales
        combinaciones.removeIf(c -> calcularCapacidadTotal(c) < numComensales);

        // no combinar 1, 10, 11
        combinaciones.removeIf(c -> c.contains(1) || c.contains(10) || c.contains(11));

        // sino hay combinaciones disponibles
        if (combinaciones.isEmpty()) {
            System.out.println("No hay mesas disponibles para " + numComensales + " comensales.");
            return;
        }

        // si la capacidad es mayor que el número de comensales, se asigna la mesa con
        // menor capacidad posible
        List<Integer> combinacionSeleccionada = combinaciones.get(0);
        int capacidadCombinacionSeleccionada = calcularCapacidadTotal(combinacionSeleccionada);
        for (List<Integer> combinacion : combinaciones) {
            int capacidad = calcularCapacidadTotal(combinacion);
            if (capacidad < capacidadCombinacionSeleccionada) {
                combinacionSeleccionada = combinacion;
                capacidadCombinacionSeleccionada = capacidad;
            }
        }

        // print
        System.out.print("Combinación: ");
        for (int mesa : combinacionSeleccionada) {
            System.out.print(mesa + " (" + capacidadMesas.get(mesa) + " personas), ");
        }
        System.out.println("para " + numComensales + " comensales.");
    }

    private List<List<Integer>> generarCombinaciones(Integer[] mesas, int numComensales) {
        List<List<Integer>> combinaciones = new ArrayList<>();

        // combinacion normal
        generarCombinacionesAux(mesas, 0, new ArrayList<>(), combinaciones, numComensales);

        return combinaciones;
    }

    private void generarCombinacionesAux(Integer[] mesas, int indice, List<Integer> combinacionParcial,
            List<List<Integer>> combinaciones, int numComensales) {
        int capacidadTotal = calcularCapacidadTotal(combinacionParcial);
        if (capacidadTotal >= numComensales) {
            combinaciones.add(new ArrayList<>(combinacionParcial));
        }

        if (capacidadTotal >= numComensales || indice == mesas.length) {
            return;
        }

        // si la mesa actual puede ser añadida a la combinacion
        boolean puedeAgregar = true;
        int mesaActual = mesas[indice];
        for (int mesa : combinacionParcial) {
            if (mesa == 1 || mesa == 10 || mesa == 11) {
                puedeAgregar = false;
                break;
            } else if ((mesa == 2 || mesa == 3) && (mesaActual == 2 || mesaActual == 3)) {
                puedeAgregar = true;
                break;
            }
        }

        // incluir la mesa actual en la combinación si es posible
        if (puedeAgregar) {
            combinacionParcial.add(mesaActual);
            generarCombinacionesAux(mesas, indice + 1, combinacionParcial, combinaciones, numComensales);
            combinacionParcial.remove(combinacionParcial.size() - 1);
        }

        // no incluir la mesa actual en la combinación
        generarCombinacionesAux(mesas, indice + 1, combinacionParcial, combinaciones, numComensales);
    }

    private int calcularCapacidadTotal(List<Integer> mesas) {
        int capacidadTotal = 0;
        for (int mesa : mesas) {
            capacidadTotal += capacidadMesas.get(mesa);
        }
        return capacidadTotal;
    }

    ///////////////////////////////////////////////////////////////////////

    // metodo para ver si esta disponible

    public void mesasDisponibles() throws SQLException {
        mesaDao.obtenerMesasDisponibles();
    }
}
