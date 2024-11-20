package contrasena;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.IllegalFormatConversionException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author carlovas practica 1 PROGRAMACION
 */
public class Contrasena {

    // Colores para que se vea mas bonito por consola lo he sacado de StackOverflow,
    // pasarlo por argumento seria muy tedioso
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    static final String reset_color = "\u001B[0m";
    static final String verde = "\u001B[32m";
    static final String rojo = "\u001B[31m";
    static final String amarillo = "\u001B[33m";
    static final String azul = "\u001B[34m";

    // Metodo para obtener el usuario codificado
    public String getSecretUser(Contrasena app, Scanner sc, String userInput) {
        try {
            // Usuario encontiptado(userEncripted)
            String userEncripted = "";

            // Indice del ultimo espacio del siguiente conjunto de espacios (indexSpace)
            int indexSpace = 0;
            // Siempre que el metodo no devuelva -1
            while ((indexSpace = lastSpace(userInput, indexSpace)) != -1) {
                // Obtenemos los dos char desde el indice encontrado, y lo añadimos
                // Lo mismo para el segundo apellido y lo añadimos en la posicion 0
                String part = userInput.substring(indexSpace, indexSpace + 2);
                userEncripted += part;
            }

            // Obtenemos los 3 primeros del nombre y lo añadimos al final
            return userEncripted + userInput.substring(0, 3);
        } catch (StringIndexOutOfBoundsException e) {
            // Si no puede acceder a las posiciones porque no existe por ejemplo Carlos V S
            // pasamos la excepcion al padre
            throw new StringIndexOutOfBoundsException();
        }
    }

    // Metodo que se utiliza prar buscar el ultimo espacio del conjunto de espacios
    // adyacentes, esto se hace por que entre
    // nombre y apellido puede haber mas de 1 espacios por ejemplo "Carlos (5
    // espacios) Vasquez tambien es valido en mi implementacion
    public int lastSpace(String userInput, int indexSpace) {
        // Empezamos buscando el conjunto de espacios desde el introducido por parametro
        // Siempre que exista osea != -1 y que el siguiente sea un espacio avanzamos el
        // indice
        indexSpace = userInput.indexOf(' ', indexSpace);
        while (indexSpace != -1 && userInput.charAt(indexSpace) == ' ')
            indexSpace++;
        return indexSpace;
    }

    // Contraseña tipo 1 dados
    public String contrasenaMecanica(int lengthPas) {
        // Elementos para la contraseña cualquier letra o numero (elements)
        String elements = "abcdefghijklmnopqrstuvwxyz0123456789";
        // Caracteres especiales de ecnima de los digitos (special)
        String special = "!\"·$%&/()=?¿";
        // Resultado de haber cifrado la contraseña (contrasenaCifrada)
        String contrasenaCifrada = "";

        // Creamos el objeto de random (random)
        Random random = new Random();

        // Mientras la longitud de la contraseña sea menor que la longitud desada
        while (lengthPas > contrasenaCifrada.length()) {
            // Generamos el cifrado de la contrasena, para ello obtenemos dos numeros
            // aleatorios representado un par x, y como minimo 0 maximo 6
            int x = random.nextInt(6); // x se mueve horizontamente en la tabla "lanzamos dado"
            int y = random.nextInt(6); // y se mueve verticalmente en la tabla "lanzamos dado"

            // Char obtenido de haberlo cifrado, su posicion sera y*6+x
            // porque cada 1y es una nueva fila excepto la primera que es 6*0=0, es decir
            // x2, y2, es 6*2+2=14, el peor de las casos es 5*6+5=35='9'
            char c = elements.charAt((y * 6) + x);

            // Verificamos si es un dijito, si no "tiramos una moneda" por asi decirlo con
            // 50% posibilidaes cara o cruz si es cara se pone en mayuscula
            if (Character.isDigit(c))
                c = special.charAt(Integer.parseInt(String.valueOf(c)) % special.length());
            else if (random.nextInt(1) == 1)
                c = Character.toUpperCase(c);
            // Lo anadimos a la contrasena cifrada
            contrasenaCifrada += c;
        }
        // Devolvemos la contrasena cifrada
        return contrasenaCifrada;
    }

    // Contraseña tipo 2
    // Metodo para generar una contraseñas seguras con SecureRandom
    public String contrasenaSegura(int lengthPas) {
        // Definir los caracteres permitidos en la contraseña (charAllowed)
        String charAllowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";
        // Crear una instancia de SecureRandom (secureRandom)
        SecureRandom secureRandom = new SecureRandom();
        // Contrasena generada y cifrada (encriptedPassword)
        String encriptedPassword = "";

        // Generar la contraseña seleccionando caracteres aleatorios
        while (lengthPas > encriptedPassword.length()) {
            // Generamos un numero aleatorio entre 0 y la longitud de los caracteres
            // permitidos y lo añadimos a la contraseña
            int index = secureRandom.nextInt(charAllowed.length());
            encriptedPassword += charAllowed.charAt(index);
        }

        // Devolver la contraseña generada
        return encriptedPassword;
    }

    // Contrasena tipo 3
    // Metodo para generar una contrasena innovadora (contrasenaInovadora)
    public String contrasenaInovadora(int lengthPas, String userInput, Scanner sc) {
        // Contrasena cifrada final (contrasenaCifrada)
        String encriptedPassword = "";
        // DNI del usuario (DNI)
        String DNI = "";

        // Generar la contraseña de longuitud lengthPas y lo ciframos
        for (int i = 0; i < lengthPas; i++) {
            // Difernetes casos segun la posicion de la contraseña
            switch (i + 1) {
                case 1:
                    // Primera letra del nombre en mayusculas
                    encriptedPassword += userInput.charAt(0);
                    break;
                case 2:
                    // Saltamos a la posicion del ultimo espacio del primer grupo de espacios, osea
                    // comienzo del primer apellido (space)
                    int space = lastSpace(userInput, 0);
                    // Agregamos el ultimo caracater del primer apellido, que sera el siguiente
                    // espacio -1 ya nos aseguramos antes al formatear el input que se pudiera
                    // acceder
                    // pero por si acaso lo verificamos
                    int index = userInput.indexOf(' ', space) - 1;
                    if (index >= 0 && index < userInput.length())
                        encriptedPassword += userInput.charAt(index);
                    else {
                        System.out.println(rojo
                                + "Ocurrio un error inseperado no se pudo acceder a la ultima letra de el primer apellido"
                                + reset_color + "%n");
                        encriptedPassword += "?";
                    }
                    break;
                case 3:
                    // Si no se encuentra uno valido sera el string vacio
                    while (DNI.isBlank()) {
                        try {
                            // Dos ultimas cifras del DNI
                            System.out.printf("%nIntroduce tu DNI sin el digito final: ");
                            // El usaurio introduce sus numeros de DNI
                            DNI = sc.nextLine();
                            // Utilizando ragex revisamos que sea valido, dice que obligatoriamente tiene
                            // que haber 8 gracias a {}, depues indicamos que queremos numeros del 0-9
                            // utilziado \d,
                            if (!DNI.matches("\\d{8}")) {
                                DNI = "";
                                throw new InputMismatchException();
                            }
                            // Si llego aqui es que es valido añadimos las dos ultimas cifras de los numeros
                            // o una segun corresponda solo una
                            if (lengthPas == 4)
                                encriptedPassword += DNI.substring(DNI.length() - 1);
                            else
                                encriptedPassword += DNI.substring(DNI.length() - 2);
                        } catch (InputMismatchException e) {
                            System.out.printf(rojo
                                    + "El formato no es correcto recuerda que deve de ser una cadena de logitud 8 y positivo"
                                    + reset_color);
                        }
                    }
                    i++; // Pasamos directamente al 5 (3+1+(1 del bucle))
                    break;
                case 5:
                    // Letra del digito de control de DNI en mayusculas, algoritmo modulo 23
                    // Creamos un string con las letras posibles del DNI cada una asociada al resto
                    // de dividirlo entre 26 las letras del DNI
                    String controlDNI = "TRWAGMYFPDXBNJZSQVHLCKE";
                    char codeNumerDNI = controlDNI.charAt(Integer.parseInt(DNI) % controlDNI.length());
                    encriptedPassword += codeNumerDNI;
                    break;
                case 6:
                    // Obtener la fecha de nacimiento con un formato valido
                    // Creamos un objeto de DateTimeFormatter con el formato de fecha
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    // Siempre que no sea valido el formato de fecha
                    boolean validYear = false;
                    while (!validYear) {
                        try {
                            System.out.printf("%nIntroduce tu fecha de nacimiento dd/mm/yyyy: ");
                            // El usuario introduce su año de nacimiento
                            String yearInput = sc.nextLine().trim();

                            // Verificamos el formato anadiendolo a un objeto de LocalDate y pasandole el
                            // formato, obtenemos el string del año
                            LocalDate date = LocalDate.parse(yearInput, formatter);
                            String year = String.valueOf(date.getYear());
                            // Si llego aqui y no fue al catch es que es valido añadimos las dos ultimos
                            // digitos, o una segun corresponda uno
                            validYear = true;
                            if (lengthPas == 7)
                                encriptedPassword += year.substring(year.length() - 1);
                            else
                                encriptedPassword += year.substring(year.length() - 2);
                        } catch (DateTimeParseException | InputMismatchException | IllegalFormatConversionException e) {
                            System.out.println(rojo
                                    + "Error el año de nacimiento deve tener el formato de dd/mm/aaaa" + reset_color);
                        }
                    }
                    i++; // Pasamos directamente a logitud 8, 6+1+(1 del bucle)
                    break;
                case 8:
                    // Si la longitud de la contraseña es de 8, añadimos un caracter especial
                    // aleatorio de los de encima del teclado
                    String special = "!\"·$%&/()=?¿";
                    encriptedPassword += special.charAt((int) (Math.random() * special.length()));
                    break;
            }

        }
        // Devolvemos la contrasena cifrada
        return encriptedPassword;
    }

    public String levelOfPasword(String contrasena) {
        // Si la longitud de la contraseña es de 4 a 6 es poco segura
        if (contrasena.length() >= 4 && contrasena.length() <= 6)
            return rojo + "Poco segura" + reset_color;

        // Posibles simbolos que puede tener la contrasena (simbolos)
        String simbolos = "[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?]";

        // Creamos el patron para buscar los simbolos, creas un pattern con ello creas
        // un matcher que verifica si son iguales (pattern)
        Pattern pattern = Pattern.compile(simbolos);
        // A partir de ella puedes llamar a find para ver si hay alguna subsecuencia que
        // coincida (matcher)
        Matcher matcher = pattern.matcher(contrasena);

        // Ternaria para mostrar si tiene algun simbolo es muy segura si no es solo
        // segura
        return (matcher.find()) ? verde + "Muy segura" + reset_color
                : amarillo + "Segura" + reset_color;
    }

    public int getLengthPassword(Scanner sc, Contrasena app, String op, int lengthPas) {
        System.out.printf("%n" + azul + "Cargando tipo " + op + reset_color);
        // Longitud de la contrasena deada por el usuario (lengthPas)
        // Mientras la longitud no sea valida
        while (lengthPas < 4 || lengthPas > 8) {
            try {
                System.out.printf("%nComo de larga quieres que sea tu contrasena: ");
                // Pedimos la longitud de la contrasena al usuario
                lengthPas = sc.nextInt();
                // Limpiamos el buffer de entrada
                sc.nextLine();
                // Si la longitud no esta en el rango, o esta en blanco lanzamos un error
                if (lengthPas < 4 || lengthPas > 8 || String.valueOf(lengthPas).isBlank())
                    throw new InputMismatchException();
            } catch (InputMismatchException e) {
                System.out.printf(rojo + "La contrasena deve de ser de longitud entre (4,8)" + reset_color);
            }
        }
        return lengthPas;
    }

    public boolean showMenu(Scanner sc, Contrasena app) {
        // Longitud de las contraseñas si se comparten (lengthPas)
        int lengthPas = 0;
        // Nombre de usuario introducido por el usuario (userInput)
        String userInput = "";
        // Nombre con el formato desado (username)
        String username = "";
        // Formato correcto usuario
        // Mientras el usuario no introduzca un nombre valido preguntamos
        while (userInput.isBlank() || userInput.split("\\S+").length != 3) {
            try {
                System.out.printf("%nIntruoduzca su nombre primer_apellido segundo_apellido: ");
                // El usuario introduce el nombre con sus apellidos (userInput)
                // Le quitamos los espacios
                userInput = sc.nextLine().trim().toLowerCase();
                // Si esta vacio o si hay mas de tres palabras lanzamos un error, \S busca
                // cadenas que no contenganm espacios en blanco + es que deve de ser 1 o mas
                // veces, osea por cada
                // adyacencia de ellos
                if (userInput.isBlank() || userInput.split("\\S+").length != 3)
                    throw new InputMismatchException();
                // Generalmente accederia a las posiciones del split para obtener el nombre y
                // apellidos pero al no poder utilizar array utilizo el metodo getSecretUser
                // Se lo pasamos en minusculas
                username = getSecretUser(app, sc, userInput);
            } catch (InputMismatchException | StringIndexOutOfBoundsException e) {
                System.out.printf(rojo + "Error de formato intentelo de nuevo" + reset_color);
                userInput = "";
            }
        }

        // Pedir longitud solo una vez las contraseñas o no?
        // Siempre que no sea null, decidimos si quieremos que sea unico o no (isUnique)
        Boolean isUnique = null;
        while (isUnique == null) {
            try {
                System.out.printf(
                        "%nQuieres generar las 3 contrasenas con la misma longitud?" + azul + " (s/n) " + reset_color);
                // Leer la entrada del usuario y convertirla a minúsculas
                String input = sc.nextLine().toLowerCase();
                // Validar la entrada del usuario
                if (input.equals("s")) {
                    isUnique = true;
                    lengthPas = app.getLengthPassword(sc, app, "1,2,3", lengthPas);
                } else if (input.equals("n"))
                    isUnique = false;
                else
                    System.out.printf(rojo + "Entrada no valida, por favor, introduce 's' o 'n'" + reset_color);
            } catch (Exception e) {
                System.out.println(rojo + "Ocurrio un error inesperado" + reset_color);
            }
        }

        // Contraseña tipo 1,2,3
        String contrasenaMecanica = "", contrasenaSegura = "", contrasenaInovadora = "";
        // Opcion selecionada por el usuario (op)
        String op = "";
        while (op.isBlank()) {
            try {
                System.out.printf("%n" + azul + "╔═══════════════════════════════════════════════╗%n");
                System.out.printf(azul + "║    1- Crear contraseña Tipo1 (mecánica)       ║%n");
                System.out.printf(azul + "║    2- Crear contraseña Tipo2 (segura)         ║%n");
                System.out.printf(azul + "║    3- Crear contraseña Tipo3 (innovadora)     ║%n");
                System.out.printf(azul + "║    4- Crear todas (Tipo1,2,3)                 ║%n");
                System.out.printf(azul + "║    5- Salir de la App                         ║%n");
                System.out
                        .printf(azul + "╚═══════════════════════════════════════════════╝%nRespuesta: " + reset_color);
                op = sc.nextLine();

                // Convirtiendolo a entero verificamos que sea un numero, verificamos que este
                // en el rango
                if (Integer.parseInt(op) < 1 || Integer.parseInt(op) > 5)
                    throw new InputMismatchException();

                switch (op) {
                    // Contraseña tipo 1
                    case "1":
                        // Contrasena final tipo1 (contrasenaMecanica)
                        lengthPas = app.getLengthPassword(sc, app, op, lengthPas);
                        contrasenaMecanica = app.contrasenaMecanica(lengthPas);
                        break;
                    // Contraseña tipo 2
                    case "2":
                        // Contrasena final tipo2 (contrasenaSegura) algoritmos con Secure Random
                        lengthPas = app.getLengthPassword(sc, app, op, lengthPas);
                        contrasenaSegura = app.contrasenaSegura(lengthPas);
                        break;
                    // Contraseña tipo 3
                    case "3":
                        // Contraseña tipo3 (contrasenaInovadora)idea inovadora de contrasena
                        lengthPas = app.getLengthPassword(sc, app, op, lengthPas);
                        contrasenaInovadora = app.contrasenaInovadora(lengthPas, userInput, sc);
                        break;
                    // Todas a la vez
                    case "4":
                        // Si no es unica preguntamos una vez por cada una
                        // Si es unica ya sabemos la longitud
                        if (!isUnique) {
                            lengthPas = app.getLengthPassword(sc, app, "1", lengthPas);
                            contrasenaMecanica = app.contrasenaMecanica(lengthPas);

                            lengthPas = app.getLengthPassword(sc, app, "2", lengthPas);
                            contrasenaSegura = app.contrasenaSegura(lengthPas);

                            lengthPas = app.getLengthPassword(sc, app, "3", lengthPas);
                            contrasenaInovadora = app.contrasenaInovadora(lengthPas, userInput, sc);
                        } else {
                            contrasenaMecanica = app.contrasenaMecanica(lengthPas);
                            contrasenaSegura = app.contrasenaSegura(lengthPas);
                            contrasenaInovadora = app.contrasenaInovadora(lengthPas, userInput, sc);
                        }
                        break;
                    // Salir de el menu y termina el programa
                    case "5":
                        return false;
                }

                // Mostramos las soluciones
                app.showSolutions(app, username, contrasenaMecanica, contrasenaSegura, contrasenaInovadora);

                // Preguntamos si el usuario quiere volber a introducir sus datos
                Boolean tryAgain = null;
                while (tryAgain == null) {
                    try {
                        System.out.printf(
                                "%nQuieres introducir nuevos datos (nombre, logitud de contrasenas) seleciona \"s\", o volber al menu seleciona  \"n\" "
                                        + azul
                                        + "(s/n) " + reset_color);
                        // Leer la entrada del usuario y convertirla a minúsculas
                        String input = sc.nextLine().toLowerCase();
                        // Validar la entrada del usuario
                        // Volvemos a pedir los datos del usuario, por lo que terminara este y el
                        // siguiente bucle
                        if (input.equals("s"))
                            tryAgain = true;
                        else if (input.equals("n")) {
                            // Mostrar la interfaz de nuevo reiniciamos las contraseñas y la opcion
                            tryAgain = false;
                            op = contrasenaMecanica = contrasenaSegura = contrasenaInovadora = "";
                        } else
                            System.out
                                    .printf(verde + "Entrada no válida. Por favor, introduce 's' o 'n'" + reset_color);
                    } catch (Exception e) {
                        System.out.println(rojo + "Ocurrio un error inesperado" + reset_color);
                    }
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.printf(rojo + "Valor invalido, intentelo de nuevo" + reset_color);
                op = "";
            }
        }

        // Todo a salido bien y no queremos introducir los datos de vuelta, por l
        return true;
    }

    public void showSolutions(Contrasena app, String username, String contrasenaMecanica, String contrasenaSegura,
            String contrasenaInovadora) {
        System.out.printf("%n" + verde + "═════════════════════ SOLUCIONES ═════════════════════%n");

        // Creamos una instancia de random para hacer la carga
        Random random = new Random();
        // Hacemos una carga hasta 100% dando saltos de entre 1 y 5, en VS se ve mejor,
        // \r es un retorno de linea de comandos osea
        // que sobreescribe la anterior linea
        for (int i = 0; i < 100; i += random.nextInt(5)) {
            // Mostramos los resultados \t es un tabulador
            System.out.printf(verde + "\r\t\tGenerando contrasenas %d%%" + reset_color, i);
            try {
                // Hacemos una pausa de 0 a 500 milisegundos
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                System.out.printf(rojo + "Error en la carga" + reset_color);
            }
        }

        // Mostramos los resultados
        System.out.printf(verde + "\r\t\tGenerando contrasenas %d%%" + reset_color, 100);
        System.out.printf("%n\tUsername: %s%n", username);
        if (!contrasenaMecanica.equals(""))
            System.out.printf("\tContrasena Tipo 1: %s = %s%n", contrasenaMecanica,
                    app.levelOfPasword(contrasenaMecanica));
        if (!contrasenaSegura.equals(""))
            System.out.printf("\tContrasena Tipo 2: %s = %s%n", contrasenaSegura, app.levelOfPasword(contrasenaSegura));
        if (!contrasenaInovadora.equals(""))
            System.out.printf("\tContrasena Tipo 3: %s = %s%n", contrasenaInovadora,
                    app.levelOfPasword(contrasenaInovadora));
        System.out.printf(verde + "════════════════════════════════════════════════════%n" + reset_color);
    }

    public static void main(String[] args) {
        // Creamos el objeto de entrada del usuario
        Scanner sc = new Scanner(System.in);
        // Creamos el objeto app para llamar a los metodos
        Contrasena app = new Contrasena();

        System.out.printf(
                "%n" + verde + "════════════════ Bienvenido a la APP ═════════════════════%n" + reset_color);
        // Opcion del usuario (op)
        while (app.showMenu(sc, app))
            ;

        System.out.printf(
                "%n" + verde + "════════════════ Programa terminado ══════════════════════%n" + reset_color);
    }
}
