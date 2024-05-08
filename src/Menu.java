import java.util.Scanner;

public class Menu {
    private Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = new Scanner(System.in);
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
        System.out.println("2. Mostrar reserva por nombre");
        System.out.println("3. Eliminar todas las reservas");
        System.out.println("4. Ver disponibilidad de las mesas");
        System.out.println("5. Ver cuántas mesas están ocupadas");
        System.out.println("6. Ver mesas reservadas pero no ocupadas");
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
                // realizar una reserva
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
                    // ver disponibilidad de las mesas(ocupada-disponible-reservada)
                    break;
                case 5:
                    // ver mesas ocupadas
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
}
