import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // llamo aqui a la clase menu
        Menu menu = new Menu(scanner);

        // usuario administrador
        Usuario admin = new Usuario(true);
        // usuario cliente
        Usuario cliente = new Usuario(false);

        // clase menu. metodo dentro del menu y se le pasa el usuario que pide el metodo
        // de menu
        menu.ejecutarMenu(admin);
        menu.ejecutarMenu(cliente);
    }
}
