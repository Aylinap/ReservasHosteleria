import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(scanner);
        // usuario administrador
        Usuario admin = new Usuario(true);
        // usuario cliente
        Usuario cliente = new Usuario(false);

        menu.ejecutarMenu(admin);
        menu.ejecutarMenu(cliente);
    }
}
