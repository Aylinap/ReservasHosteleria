import java.time.DayOfWeek;
import java.time.LocalDate;

// obtener la fecha de hoy: listar

/*UTILIZANDO java.time.LocalDate */
public class CalendarioLocalDate {

    public static void main(String[] args) {
        CalendarioLocalDate c = new CalendarioLocalDate();
        c.mostrarFechaActual();

        // Elegir una fecha para la reserva:
        // no se si la fecha se puede guardar como un arreglo o no, asi no hay que ir
        // cambiandola manual

        c.printCalendario(5, 2024);
        
    }

    // printear el calendario pasandole los parametros mes y año
    public void printCalendario(int m, int a) {
        int dias = 1;
        LocalDate ld = LocalDate.of(a, m, 1);
        int dow = ld.getDayOfWeek().getValue();// obtener día de la semana del primer día del mes
        int max = ld.lengthOfMonth();// obtiene el maximo de días.

        // se podrá parsear la fecha de ingles a español?-> buscar....
        // falta cambiar los dias de lunes a viernes, que muestre solo los días hábiles?

        for (int i = 1; i < 8; i++) {// Imprime encabezado del enum DayOfWeek

            System.out.print("||" + DayOfWeek.of(i).toString().substring(0, 2) + " |");

        }

        boolean empezo = false;

        for (int i = 1; i < 7; i++) {// semanas por mes
            System.out.println();

            for (int j = 1; j < 8; j++) {// día por semana
                if (j == dow) {
                    empezo = true;
                }
                if (!empezo || dias > max) {
                    System.out.print("| XX |");
                } else if (empezo) {
                    System.out.print("| " + (dias < 10 ? ("0" + dias) : ("" + dias)) + " |");
                    dias++;
                }
            }
        }

    }

    // obtener la fecha de hoy: listar

    public void mostrarFechaActual() {
        LocalDate hoy = LocalDate.now();
        System.out.println("");
        System.out.println("----- ¿Qué día quieres reservar? -----");
        System.out.println("La fecha de hoy es: " + hoy);
        System.out.println("");
    }

    // aqui mismo un metodo que haga elegir un dia de la semana????
    // o solo lo uso para listar la fecha y la persona lo ingrese manual y ya fue

    /* CREAR FECHA ESPECIFICA por ejemplo introducida por el teclado */
    /*
     * LocalDate fecha_especifica = LocalDate.of(2024,01,12) - año, mes, dia-
     * System.out.println("La fecha creada es: " + fecha_especifica);
     */
}
