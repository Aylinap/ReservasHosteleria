import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private MesaDao mesaDao;
   

    public Menu(Scanner scanner) {
        this.scanner = new Scanner(System.in);
    }

    public Menu(MesaDao mesaDao) {
        this.mesaDao = mesaDao;

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
                    System.out.println("Acceso denegado. Debe ser administrador para acceder al Gestor de Reservas.");
                }
                break;
            default:
                System.out.println("Opción no válida. Por favor, seleccione una opción del menú.");
        }
    }

    public void accederGestorReservas() {
        System.out.println("Por favor, inicie sesión como administrador para acceder al Gestor de Reservas.");
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
                    System.out.println("Opción no válida. Por favor, seleccione una opción válida.");
            }
        } else {
            System.out.println("Credenciales incorrectas. Acceso denegado.");
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
            System.out.println("Opción inválida. Por favor, seleccione un número válido para la reserva.");
            return null;
        }

        return horarios[opcion - 1];
    }

    public void añadirReserva() {
        System.out.println("\n--- Añadir Reserva ---");

        System.out.println("Ingrese el nombre del cliente: ");
        String nombreCliente = scanner.nextLine();

        System.out.println("¿Cuántas personas vienen contigo?");
        int numeroComensales = scanner.nextInt();
        scanner.nextLine();
        // el numero de la mesa deberia ser automatico depende de la cantidad de
        // comensales.
        System.out.println("Ingrese el número de mesa: ");
        int numeroMesa = scanner.nextInt();
        scanner.nextLine();

        // numero sala tiene que asignarse automatico o depende de la cantidad de
        // comensales.
        System.out.println("Ingrese el número de sala: ");
        int numeroSala = scanner.nextInt();
        scanner.nextLine();

        // 
        System.out.println("Ingrese la fecha de reserva Lunes a viernes(yyyy-MM-dd): ");
        String fechaReservaStr = scanner.nextLine();
        java.sql.Date fechaReserva = java.sql.Date.valueOf(fechaReservaStr);

        // puedo hacer que el horario sea submenu ingresado como opcion y que no sea
        // ingresado manualmente
        seleccionarHorario();

        System.out.println("Ingrese la hora de reserva (HH:mm): ");
        String horaReservaStr = scanner.nextLine();
        LocalTime horaReserva = LocalTime.parse(horaReservaStr);

        System.out.println("¿Desea agregar algún comentario especial?: ");
        String descripcion = scanner.nextLine(); 

        // borre mesa del constructor, por ahora no se lo paso como parametro, luego ver
        // como se lo paso al hacer la logica de la asignacion de la mesa
        Reserva reservaNueva = new Reserva(nombreCliente, fechaReserva, horaReserva, numeroComensales, descripcion);

        try {
            ReservaDao reservaDao = new ReservaDao();
            reservaDao.insertarReserva(reservaNueva);
            System.out.println("Reserva añadida correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al añadir la reserva: " + e.getMessage());
        }
    }
 }
