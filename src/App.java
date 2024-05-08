import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(scanner);

        Usuario admin = new Usuario(true);

        Usuario cliente = new Usuario(false);

        menu.ejecutarMenu(admin);
        menu.ejecutarMenu(cliente);
    }
}
