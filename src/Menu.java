import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private MesaDao mesaDao;
    private ClienteDao clienteDao;
    private MesaGestor mesaGestor;

    public Menu(Scanner scanner) {
        this.scanner = new Scanner(System.in);
        this.mesaDao = new MesaDao();
        this.clienteDao = new ClienteDao();
        this.mesaGestor = new MesaGestor(mesaDao, clienteDao);
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
        System.out.print("Ingresa una opción: ");

    }

    public void mostrarSubMenuGestorReservas() {
        System.out.println("Submenú del Gestor de Reservas");
        System.out.println("1. Mostrar todas las reservas");
        System.out.println("2. Buscar reserva por nombre");
        System.out.println("3. Eliminar todas las reservas");
        System.out.println("4. Eliminar una reserva");
        System.out.println("5. Ver disponibilidad de las mesas");
        System.out.println("6. Ver cuántas mesas están ocupadas");
        System.out.println("7. Ver mesas reservadas pero no ocupadas");
        System.out.println("8. Marcar mesa disponible nuevamente");
        System.out.print("Ingresa una opción: ");

    }

    // parametro que le paso es el usuario
    public void ejecutarMenu(Usuario usuario) {

        mostrarMenuPrincipal();
        // declaras la variable y la guardas
        int opcion = scanner.nextInt();
        scanner.nextLine(); // limpia el scanner

        switch (opcion) {
            case 1:
                añadirReserva();
                break;
            case 2:
                // mostrar reserva por nombre
                break;
            case 3:
                // cancelar una reserva
                break;
            case 4:
                // modificar una reserva
                break;
            case 5:
                if (usuario != null && usuario.isEsAdmin()) {
                    accederGestorReservas();
                } else {
                    System.out.println("Debe ser administrador para acceder al Gestor de Reservas.");
                }
                break;
            default:
                System.out.println("Por favor, seleccione una opción del menú.");
        }
    }

    public void accederGestorReservas() {
        System.out.println("Por favor, inicia sesión como administrador para acceder al Gestor de Reservas.");
        System.out.print("Usuario: ");
        String user = scanner.nextLine();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine();

        if (user.equals("admin") && pass.equals("admin123")) {
            // claves para ingresar al gestor se puede modificar para
            // que sea tomado desde otro lado o desde la base de
            // datos tambien
            mostrarSubMenuGestorReservas();
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el scanner

            // sub menu si es administrador
            switch (opcion) {
                case 1:
                    // mostrar todas las reservas
                    break;
                case 2:
                    // mostrar reserva por nombre
                    break;
                case 3:
                    // eliminar todas o una reserva falta agregar otra opcion
                    break;
                case 4:

                    break;
                case 5:
                    break;
                case 6:
                    // ver mesas reservadas pero no ocupadas
                    break;
                default:
                    System.out.println("Por favor, seleccione una opción válida.");
            }
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }
    // falta que liste el horario disponibles para las reservas y los dias que solo
    // son los dias de la semana

    public void mostrarHorariosDisponibles() {
        System.out.println("--- Horarios disponibles para reservar ---");
        LocalTime[] horarios = Reserva.getHorariosReservas();
        for (int i = 0; i < horarios.length; i++) {
            System.out.println((i + 1) + ". " + horarios[i]);
        }
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
            System.out.println("Selecciona un número válido para la reserva.");
            return null;
        }

        return horarios[opcion - 1];
    }

    public void añadirReserva() {
        try {
            System.out.println("\n--- Añadir Reserva ---");

            System.out.println("Ingresa tu nombre: ");
            String nombreCliente = scanner.nextLine();

            System.out.println("Ingresa tu número de teléfono:");
            int telefono = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Ingresa tu e-mail:");
            String email = scanner.nextLine();

            Cliente cliente = new Cliente(0, nombreCliente, telefono, email);
            int idcliente = clienteDao.insertarCliente(cliente);

            mesaGestor.mostrarEstadoMesa();

            System.out.println("¿Cuántas personas vienen contigo?");
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

            System.out.println("¿Desea agregar algún comentario especial?: ");
            String descripcion = scanner.nextLine();

            ReservaAsignada reservaAsignada = mesaGestor.asignarMesaYCrearReserva(idcliente, fechaReserva, horaReserva,
                    numeroComensales, descripcion);

            if (reservaAsignada != null) {
                Reserva reservaNueva = reservaAsignada.getReserva();
                int numeroMesa = reservaAsignada.getNumeroMesa();

                try {
                    ReservaDao reservaDao = new ReservaDao();
                    reservaDao.insertaReservaNueva(reservaNueva, numeroMesa);
                    System.out.println("Reserva añadida correctamente.");
                } catch (SQLException e) {
                    System.out.println("Error al añadir la reserva: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al añadir la reserva: " + e.getMessage());
        }
    }

}
