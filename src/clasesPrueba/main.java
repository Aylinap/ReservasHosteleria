package clasesPrueba;

public class main {

    public static void main(String[] args) {
        MesaGestor mesaManager = new MesaGestor();

        mesaManager.asignarMesa(2); // Prueba con 2 comensales prioritaria
        mesaManager.asignarMesa(3); // Prueba con 4 comensales prioritaria
        mesaManager.asignarMesa(6); // Prueba con 6 comensales prioritaria
        mesaManager.asignarMesa(6);
        mesaManager.asignarMesa(4);
        mesaManager.asignarMesa(8); // Prueba con 8 comensales no prioritaria pero si combinada
        mesaManager.asignarMesa(10); // Prueba con 10 comensales no prioritaria pero si combinada
    }

}
