import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.time.LocalTime;

public class MesaGestor {
    private ClienteDao clienteDao;
    private MesaDao mesaDao;
    private ReservaDao reservaDao;

    public MesaGestor(MesaDao mesaDao, ClienteDao clienteDao) {
        this.mesaDao = new MesaDao();
        this.reservaDao = new ReservaDao();
        this.clienteDao = new ClienteDao();
    }

    // mostrar estado de las mesas, desde la tabla mesa
    public void mostrarEstadoMesa() throws SQLException {
        List<Mesa> mesas_estados = mesaDao.obtenerTodasLasMesas();

        for (Mesa mesa : mesas_estados) {
            String estado = mesa.getEstado();
            System.out.println("\n Mesa número : " + mesa.getNumero_mesa() + "\n Capacidad : " + mesa.getCapacidad()
                    + "\n Estado : " + estado + "\n Sala : " + mesa.getSala());
        }
    }

    // crear una reserva y mesa asignada, se le pasa como parametros los datos de
    // RESERVA
    // el método lo primero que busca son las mesas disponibles, luego juega con
    // eso.
    public ReservaAsignada asignarMesaYCrearReserva(int idCliente, java.sql.Date diaReserva, LocalTime horaReserva,
            int numComensales, String descripcion) throws SQLException {
        List<Mesa> mesasDisponibles = mesaDao.obtenerMesasDisponiblesSinComensales();

        // print para comprobar la cantidad de mesas disponibles y la capacidad de cada
        // una(borrar)
        System.out.println("Mesas disponibles:");
        for (Mesa mesa : mesasDisponibles) {
            System.out.println("Mesa " + mesa.getNumero_mesa() + " - Capacidad: " + mesa.getCapacidad());
        }

        // intenta asignar una mesa prioritaria si el numero de comensales es igual a
        // la capacidad de la mesa
        for (Mesa mesa : mesasDisponibles) {
            if (mesa.getPrioritaria().equals("si") && mesa.getCapacidad() == numComensales) {
                // print para saber que funciona el metodo(borrar)
                System.out.println("Asignando mesa prioritaria...");
                mesaDao.actualizarEstadoMesa(mesa.getNumero_mesa(), "ocupada");
                // se agrega al arreglo de mesa reservada aunque sea solo 1
                List<Integer> mesasReservadas = new ArrayList<>();
                mesasReservadas.add(mesa.getNumero_mesa());
                // se crea la reserva
                Reserva reserva = new Reserva(mesa.getNumero_mesa(), idCliente, diaReserva, horaReserva, numComensales,
                        descripcion);
                // se inserta en la bbdd
                reservaDao.insertaReservaNueva(reserva, mesasReservadas);
                // print para saber que mesa se le asigno si es una sola
                System.out.println("¡Mesa asignada! Mesa número: " + mesa.getNumero_mesa());
                // crea una reservamesa mesa se la pasa al constructor de reservaAsignada
                // falta probar si es una mesa con comensales justos.
                return new ReservaAsignada(reserva, mesasReservadas);
            }
        }

        // si hay mas de 6 comensales(capacidad maxima)
        if (numComensales > 6) {
            // print para comprobar que funciona (borrar quizas)
            System.out.println("Buscando combinaciones de mesas...");
            // creo un arreglo de mesas combinables y que no sean prioritarias
            List<Mesa> mesasCombinables = new ArrayList<>();
            for (Mesa mesa : mesasDisponibles) {
                if (!mesa.getPrioritaria().equals("si")) {
                    mesasCombinables.add(mesa);
                }
            }

            // print mesas, solo para buscar el error
            System.out.println("Mesas combinables:");
            for (Mesa mesa : mesasCombinables) {
                System.out.println("Mesa " + mesa.getNumero_mesa() + " - Capacidad: " + mesa.getCapacidad());
            }
            // se itera por cada mesa candidata posible combinacion
            for (Mesa mesa : mesasCombinables) {
                // traigo la info de la bbdd tabla combinaciones, se guarda en un arreglo
                List<Integer> posiblesCombinaciones = mesaDao.obtenerMesasCombinables(mesa.getNumero_mesa());
                // creo un arreglo de combinaciones actuales
                List<Mesa> combinacionActual = new ArrayList<>();
                // se le agrega la mesa
                combinacionActual.add(mesa);
                // guardamos la capacidad total de la mesa que se guardo
                int totalCapacidad = mesa.getCapacidad();

                // itera sobre todas las mesas que se pueden combinar traidas desde la bbdd
                for (Integer numeroMesaCombinable : posiblesCombinaciones) {
                    // itera sobre las posibles mesas que se pueden combinar
                    for (Mesa posibleMesa : mesasCombinables) {
                        // aqui se compara la mesa actual se puede combinar o está dentro del arreglo de
                        // mesascombinables.
                        if (posibleMesa.getNumero_mesa() == numeroMesaCombinable
                                && !combinacionActual.contains(posibleMesa)) {
                            // si la combinacion es posible se agrega al arreglo de combinacion actual
                            combinacionActual.add(posibleMesa);
                            // suma las capacidades de las dos mesas y si
                            totalCapacidad += posibleMesa.getCapacidad();
                            // la suma caen los numeroes de comensales indicados
                            if (totalCapacidad >= numComensales) {
                                // print para verificar si encontro mesa(borrar)
                                System.out.println("Mesa combinada encontrada:");
                                for (Mesa m : combinacionActual) {
                                    System.out.println(
                                            "Mesa " + m.getNumero_mesa() + " - Capacidad: " + m.getCapacidad());
                                }
                                // lista de mesas reservadas
                                List<Integer> mesasReservadas = new ArrayList<>();
                                // itera sobre ellas apra cambiar el estado de las mesas y guardar el numero de
                                // mesa.
                                for (Mesa m : combinacionActual) {
                                    mesaDao.actualizarEstadoMesa(m.getNumero_mesa(), "ocupada");
                                    mesasReservadas.add(m.getNumero_mesa());
                                }

                                // guardo una reserva con la mesa pero me esta guardando las dos mesas
                                // combinadas con id de reserva disitinto?? buscar el error !!
                                Mesa mesaPrincipal = combinacionActual.get(0);
                                // obtengo el primer numero del arreglo de combinacion actual lo dejo como mesa
                                // priniciapl y lo paso a reserva.
                                Reserva reserva = new Reserva(mesaPrincipal.getNumero_mesa(), idCliente, diaReserva,
                                        horaReserva, numComensales, descripcion);
                                reservaDao.insertaReservaNueva(reserva, mesasReservadas);

                                System.out.println(

                                        "¡Mesa asignada combinando! Mesa principal: " + mesaPrincipal.getNumero_mesa());

                                return new ReservaAsignada(reserva, mesasReservadas);
                            }
                        }
                    }
                }
            }
        }

        // si hay alguna cosa que no funcione retorna null
        System.out.println("No se pudo asignar una mesa (en metood asignarMesaYCrearReserva)");
        return null;
    }

    // filtra si las mesas son prioritarias o no, pasandole el arreglo de todas las
    // mesas
    public List<Mesa> filtrarMesasPrioritarias(List<Mesa> todasMesas) {
        List<Mesa> mesasPrioritarias = new ArrayList<>();
        for (Mesa mesa : todasMesas) {
            if (mesa.getPrioritaria().equals("si")) {
                mesasPrioritarias.add(mesa);
            }
        }
        return mesasPrioritarias;
    }

}
