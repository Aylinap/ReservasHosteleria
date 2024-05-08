public class Usuario {
    private String user;
    private String pass;
    private boolean asist_V;
    // podria colocarle en el menu, todas las opciones de reservar y luego al final
    // poner "gestor de reservas" ahi que pida el usuario
    // si es administrador en ese caso?? podria ser una forma o simplemente pedirlo
    // al inicio de la aplicación tambien. ----> revisar

    public Usuario(String user, String pass, boolean asist_V) {
        this.user = user;
        this.pass = pass;
        this.asist_V = asist_V;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public boolean isAsist_V() {
        return asist_V;
    }
}
