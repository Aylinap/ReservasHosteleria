import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private MesaDao mesaDao;
    private ClienteDao clienteDao;
    private MesaGestor mesaGestor;
    private ReservaDao reservaDao;

    public Menu(Scanner scanner) {
        this.scanner = new Scanner(System.in);
        this.mesaDao = new MesaDao();
        this.clienteDao = new ClienteDao();
        this.mesaGestor = new MesaGestor(mesaDao, clienteDao);
        this.reservaDao = new ReservaDao();
    }

    public void mostrarMenuPrincipal() {
        System.out.println("");
        System.out.println(" ¡ Bienvenido, Listo para reservar con nosotros :) !");
        System.out.println("");
        System.out.println("1. Realizar una reserva");
        System.out.println("2. Mostrar reserva por nombre");
        System.out.println("3. Cancelar una reserva");
        System.out.println("4. Modificar una reserva");
        System.out.println("5. Acceder al Gestor de Reservas");
        System.out.println("0. Salir..");
        System.out.print("Ingresa una opción: ");

    }

    public void mostrarSubMenuGestorReservas() {
        System.out.println("Submenú del Gestor de Reservas");
        System.out.println("");
        System.out.println("1. Mostrar todas las reservas");
        System.out.println("2. Buscar reserva por nombre");
        System.out.println("3. Eliminar una reserva");
        System.out.println("4. Ver disponibilidad de las mesas");
        System.out.println("5. Ver cuántas mesas están ocupadas");
        System.out.println("6. Marcar todas las mesas disponible nuevamente");
        System.out.println("7. Volver al menú anterior:");
        System.out.println("0. Salir..");
        System.out.print("Ingresa una opción: ");

    }

    // parametro que le paso es el usuario
    public void ejecutarMenu(Usuario usuario) {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    añadirReserva();
                    break;
                case 2:
                    buscarReservaPorNombre();
                    break;
                case 3:
                    eliminarReserva();
                    break;
                case 4:
                    // Modificar una reserva
                    break;
                case 5:
                    if (usuario != null && usuario.isEsAdmin()) {
                        accederGestorReservas();
                    } else {
                        System.out.println("Debe ser administrador para acceder al Gestor de Reservas.");
                    }
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Por favor, seleccione una opción válida.");
            }
        } while (opcion != 0);

    }

    public void accederGestorReservas() {
        boolean credencialesCorrectas = false;

        do {
            System.out.println("Por favor, inicia sesión como administrador para acceder al Gestor de Reservas.");
            System.out.print("Usuario: ");
            String user = scanner.nextLine();
            System.out.print("Contraseña: ");
            String pass = scanner.nextLine();

            if (user.equals("admin") && pass.equals("admin123")) {
                // Claves para ingresar al gestor se pueden modificar para
                // que sean tomadas desde otro lugar o desde la base de
                // datos también
                mostrarSubMenuGestorReservas();
                int opcion = scanner.nextInt();
                scanner.nextLine();

                // Submenu si es administrador
                switch (opcion) {
                    case 1:
                        mostrarReservasTodas();
                        break;
                    case 2:
                        buscarReservaPorNombre();
                        break;
                    case 3:
                        eliminarReserva();
                        break;
                    case 4:
                        mostrarTodasMesasDisponibles();
                        break;
                    case 5:
                        mostrarTodasMesasOcupadas();
                        break;
                    case 6:
                        marcarTodasMesasDisponibles();

                        break;
                    case 7:
                        ejecutarMenu(null);
                    case 0:
                        System.out.println("Saliendo del programa..");
                        break;
                    default:
                        System.out.println("Por favor, selecciona una opción válida.");
                }
                credencialesCorrectas = true;
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
            }
        } while (!credencialesCorrectas);
    }
    // falta que liste el horario disponibles para las reservas y los dias que solo
    // son los dias de la semana

    public void mostrarHorariosDisponibles() {
        System.out.println("");
        System.out.println("--- Horarios disponibles para reservar ---");
        LocalTime[] horarios = Reserva.getHorariosReservas();
        for (int i = 0; i < horarios.length; i++) {
            System.out.println((i + 1) + ". " + horarios[i]);
        }
        System.out.println("");
    }

    // seleccionar horario tipo opcion asi el usuario no ingresa manualmente el
    // horario
    public LocalTime seleccionarHorario() {
        mostrarHorariosDisponibles();
        System.out.println("Selecciona un horario:");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        LocalTime[] horarios = Reserva.getHorariosReservas();
        if (opcion < 1 || opcion > horarios.length) {
            System.out.println("Selecciona un horario valido para la reserva");
            return null;
        }
        return horarios[opcion - 1];
    }

    // guardar desde el teclado la info del cliente
    public void añadirReserva() {
        try {
            System.out.println("\n---- Añadir Reserva ----");

            System.out.println("Ingresa tu nombre: ");
            String nombreCliente = scanner.nextLine();

            System.out.println("Ingresa tu número de teléfono:");
            int telefono = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Ingresa tu e-mail:");
            String email = scanner.nextLine();

            // se crea el objeto cliente con los datos obtenidos,
            Cliente cliente = new Cliente(0, nombreCliente, telefono, email);
            int idcliente = clienteDao.insertarCliente(cliente);

            System.out.println("----------------");
            System.out.println("ID del cliente generado: " + idcliente);
            System.out.println("----------------");

            System.out.println("¿Cuántas personas van a comer?");
            int numeroComensales = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Ingresa la fecha que quieres reservar (Lunes a viernes -> yyyy-MM-dd): ");
            String fechaReservaStr = scanner.nextLine();
            java.sql.Date fechaReserva = java.sql.Date.valueOf(fechaReservaStr);

            LocalTime horaReserva = seleccionarHorario();

            if (horaReserva == null) {
                System.out.println("No se puede seleccionar el horario de reserva.");
                return;
            }

            System.out.println("¿Deseas agregar algún comentario especial?: ");
            String descripcion = scanner.nextLine();

            ReservaAsignada reservaAsignada = mesaGestor.asignarMesaYCrearReserva(idcliente, fechaReserva, horaReserva,
                    numeroComensales, descripcion);

            if (reservaAsignada != null) {
                Reserva reservaNueva = reservaAsignada.getReserva();
                List<Integer> numerosMesa = reservaAsignada.getNumerosMesa();

                try {
                    System.out.println("=========================================");
                    System.out.println("Reservando con los siguientes detalles:");
                    System.out.println("ID Cliente: " + reservaNueva.getId_cliente());
                    System.out.println("Fecha Reserva: " + reservaNueva.getDiaReserva());
                    System.out.println("Hora Reserva: " + reservaNueva.getHoraReserva());
                    System.out.println("Número Comensales: " + reservaNueva.getNumero_comensales());
                    System.out.println("Descripción: " + reservaNueva.getDescripcion());

                    reservaDao.insertaReservaNueva(reservaNueva, numerosMesa);
                    System.out.println("Reserva añadida correctamente(menu).");
                } catch (SQLException e) {
                    System.out.println("Error al añadir la reserva(menu): " + e.getMessage());
                }
            } else {
                System.out.println("No se pudo asignar una mesa(menu).");
            }
        } catch (SQLException e) {
            System.out.println("Error al añadir la reserva (menu): " + e.getMessage());
        }
    }

    // mostrar todas las reservas en submenu de administrador
    public void mostrarReservasTodas() {
        try {
            reservaDao.obtenerTodasLasReservas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // mostrar las mesas ocupadas
    public void mostrarTodasMesasOcupadas() {
        try {
            mesaDao.mostrarTodasLasMesasOcupadas();
        } catch (SQLException e) {
            System.out.println("No hay mesas ocupadas");
        }
    }

    // metodo bsucar reserva por nombre, cliente:
    public void buscarReservaPorNombre() {
        try {
            System.out.print("Ingresa el nombre del cliente para buscar la reserva: ");
            String nombreCliente = scanner.nextLine();
            reservaDao.mostrarReservaPorNombre(nombreCliente);
        } catch (SQLException e) {
            System.out.println("No se ha encontrado reserva con ese nombre");
        }
    }

    // metodod mostrar mesas todas mesas disponibles
    public void mostrarTodasMesasDisponibles() {
        try {
            System.out.println("----Mesas disponibles----");
            mesaDao.mostrarTodasLasMesasDisponibles();

        } catch (SQLException e) {
            System.out.println(" No hay mesas disponibles");
        }
    }

    // borrar una reserva por nombre

    public void eliminarReserva() {

        System.out.println("\n---- Eliminar Reserva ----");
        System.out.print("Ingresa el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();

        try {
            ReservaDao reservaDao = new ReservaDao();
            List<Reserva> reservas = reservaDao.obtenerReservasPorNombre(nombreCliente);

            if (reservas.isEmpty()) {
                System.out.println("No se encontraron reservas para el cliente: " + nombreCliente);
                return;
            }

            System.out.println("\n -- Reservas encontradas con el nombre: " + nombreCliente);

            for (Reserva reserva : reservas) {
                System.out.println("ID de Reserva: " + reserva.getId_reserva() +
                        ", Día: " + reserva.getDiaReserva() +
                        ", Hora: " + reserva.getHoraReserva() +
                        ", Comensales: " + reserva.getNumero_comensales() +
                        ", Comentario: " + reserva.getDescripcion());
            }
            System.out.println("");
            System.out.print("\nIngresa el ID de la reserva que deseas eliminar: ");
            int idReserva = scanner.nextInt();
            scanner.nextLine();

            reservaDao.eliminarReservaPorId(idReserva);
            System.out.println("Reserva eliminada correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar la reserva: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // marcar todas las mesas disponibles
    public void marcarTodasMesasDisponibles() {
        try {

            mesaDao.marcarTodasLasMesasComoDisponibles();
            System.out.println("");
            System.out.println("Se han marcado todas las mesas como disponibles");

        } catch (SQLException e) {
            System.out.println("No se han podido marcar las mesas como 'disponibles' ");
            e.printStackTrace();
        }
    }
}
