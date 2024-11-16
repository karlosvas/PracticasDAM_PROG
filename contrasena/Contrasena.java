package contrasena;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.IllegalFormatConversionException;

/**
 * @author carlovas practica 1 PROGRAMACION
 */
public class Contrasena {

    // Metodo para obtener el username
    public String getUser(String userInput) {
        // Si no puede acceder a las posiciones porque no existe por ejemplo Carlos V S,
        // da StringIndexOutOfBoundsException
        try {
            // Obtenemos los 3 primeros del nombre y lo añadimos
            String partName = userInput.substring(0, 3);

            // Indice de el ultimo espacio del siguiente conjunto de espacios (indexSpace)
            // Obtenemos los dos char desde el indice encontrado, y lo añadimos en la
            // posicion 0
            int indexSpace = lastSpace(userInput, 0);
            String partLastOne = userInput.substring(indexSpace, indexSpace + 2);

            // Lo mismo para el segundo apellido y lo añadimos en la posicion 0
            indexSpace = lastSpace(userInput, indexSpace);
            String partLastTwo = userInput.substring(indexSpace, indexSpace + 2);

            // Devolvemos el nombre con el formato correcto
            return partLastOne + partLastTwo + partName;
        } catch (StringIndexOutOfBoundsException e) {
            // Pasamos la excepcion al padre
            throw new StringIndexOutOfBoundsException();
        }
    }

    // Buscamos el ultimo espacio del conjunto de espacios esto se hace por si entre
    // nombre y apellido hay 5 espacios por ejemplo "Carlos (5 espacios) Vasquez
    // tambien es valido"
    public int lastSpace(String userInput, int indexSpace) {
        // Empezamos buscando el conjunto de espacios desde el introducido por parametro
        // No va a dar error porque ya nos aseguramos que hay 3 palabras
        indexSpace = userInput.indexOf(' ', indexSpace);
        // Quitamos todos los adyacentes a el y devolbemos la posicion del ultimo
        while (userInput.charAt(indexSpace) == ' ')
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
            // aleatorios representado un par x, y como minimo 0 maximo 5
            int x = random.nextInt(6); // x se mueve horizontamente
            int y = random.nextInt(6); // y se mueve verticalmente

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
        // Definir los caracteres permitidos en la contraseña (caracteresPermitidos)
        String caracteresPermitidos = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";
        // Crear una instancia de SecureRandom (secureRandom)
        SecureRandom secureRandom = new SecureRandom();
        // Contrasena generada y cifrada (contrasena)
        String contrasenaCifrada = "";

        // Generar la contraseña seleccionando caracteres aleatorios
        while (lengthPas > contrasenaCifrada.length()) {
            // Generamos un numero aleatorio entre 0 y la longitud de los caracteres
            // permitidos y lo añadimos a la contraseña
            int index = secureRandom.nextInt(caracteresPermitidos.length());
            contrasenaCifrada += caracteresPermitidos.charAt(index);
        }

        // Devolver la contraseña generada
        return contrasenaCifrada;
    }

    // Contrasena tipo 3
    // Metodo para generar una contrasena innovadora
    public String contrasenaInovadora(int lengthPas, String userInput, Scanner sc, String ANSI_RED, String ANSI_RESET) {
        // Contrasena cifrada final (contrasenaCifrada)
        String contrasenaCifrada = "";
        // DNI del usuario (DNI)
        String DNI = "";

        // Generar la contraseña de longuitud lengthPas y lo ciframos
        for (int i = 0; i < lengthPas; i++) {
            // Difernetes casos segun la posicion de la contraseña
            switch (i + 1) {
                case 1:
                    // Primera letra del nombre en mayusculas
                    contrasenaCifrada += userInput.charAt(0);
                    break;
                case 2:
                    // Saltamos a la posicion del ultimo espacio del primer grupo de espacios, osea
                    // comienzo del primer apellido (space)
                    int space = lastSpace(userInput, 0);
                    // Agregamos el ultimo caracater del primer apellido, que sera el siguiente
                    // espacio -1
                    int index = userInput.indexOf(' ', space) - 1;
                    if (index >= 0 && index < userInput.length()) {
                        contrasenaCifrada += userInput.charAt(index);
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
                            // que haber 8 gracias a {} indicamos que queremos numeros del 1-9 utilziado \d,
                            // y \ porque si pongo una lo toma como escape de caracter
                            // para escapar el caracter
                            if (!DNI.matches("[1-9]{8}")) {
                                DNI = "";
                                throw new InputMismatchException();
                            }
                            // Si llego aqui es que es valido añadimos las dos ultimas cifras de los numeros
                            // o una segun corresponda
                            if (lengthPas == 4)
                                contrasenaCifrada += DNI.substring(DNI.length() - 1);
                            else
                                contrasenaCifrada += DNI.substring(DNI.length() - 2);
                        } catch (InputMismatchException e) {
                            System.out.printf(ANSI_RED
                                    + "El formato no es correcto recuerda que deve de ser una cadena de logitud 8"
                                    + ANSI_RESET + "%n");
                        }
                    }
                    i++; // Pasamos directamente al 5 (3+1+(1 del bucle))
                    break;
                case 5:
                    // Letra del digito de control de DNI en mayusculas, algoritmo modulo 23
                    // Creamos un string con las letras posibles del DNI cada una asociada al resto
                    // de dividirlo entre 26 las letras del DNI
                    String codString = "TRWAGMYFPDXBNJZSQVHLCKE";
                    char codeNumerDNI = codString.charAt(Integer.parseInt(DNI) % codString.length());
                    contrasenaCifrada += codeNumerDNI;
                    break;
                case 6:
                    // Obtener la fecha de nacimiento con un formato valido
                    // Creamos un objeto de DateTimeFormatter con el formato de fecha
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    boolean validYear = false;
                    while (!validYear) {
                        try {
                            System.out.printf("%nIntroduce tu fecha de nacimiento dd/MM/yyyy: ");
                            // El usuario introduce su año de nacimiento
                            String yearInput = sc.nextLine().trim();

                            // Verificamos el formato anadiendolo a un objeto de LocalDate y pasandole el
                            // formato, obtenemos el string del año
                            LocalDate date = LocalDate.parse(yearInput, formatter);
                            String year = String.valueOf(date.getYear());
                            // Si llego aqui y no fue al catch es que es valido añadimos las dos ultimos
                            // digitos, o una segun corresponda
                            validYear = true;
                            if (lengthPas == 7)
                                contrasenaCifrada += year.substring(year.length() - 1);
                            else
                                contrasenaCifrada += year.substring(year.length() - 2);
                        } catch (DateTimeParseException | InputMismatchException | IllegalFormatConversionException e) {
                            System.out.println(ANSI_RED +
                                    "Error el año de nacimiento deve tener el formato de dd/mm/aaaa" + ANSI_RESET);
                        }
                    }
                    i++; // Pasamos directamente a logitud 8, 6+1+(1 del bucle)
                    break;
                case 8:
                    // Si la longitud de la contraseña es de 8, añadimos un caracter especial
                    // aleatorio
                    String special = "!\"·$%&/()=?¿";
                    contrasenaCifrada += special.charAt((int) (Math.random() * special.length()));
                    break;
            }

        }
        // Devolvemos la contrasena cifrada
        return contrasenaCifrada;
    }

    public String levelOfPasword(String contrasena, String ANSI_RED, String ANSI_RESET, String ANSI_YELLOW,
            String ANSI_GREEN) {
        // Si la longitud de la contraseña es de 4 a 6 es poco segura
        int l = contrasena.length();
        if (l >= 4 && l <= 6)
            return ANSI_RED + "Poco segura" + ANSI_RESET;

        // Posibles simbolos que puede tener la contrasena (simbolos)
        String simbolos = "[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?]";

        // Referencia ya se que es en local pero para que veas que no me lo invento que
        // lo vi en la documentacion:
        // file:///home/karlosvas/Documentos/jdk-21.0.5_doc-all/docs/api/java.base/java/util/regex/Pattern.html

        // Creamos el patron para buscar los simbolos, creas un pattern con ello creas
        // un matcher que verifica si son iguales
        Pattern pattern = Pattern.compile(simbolos);
        // A partir de ella puedes llamar a find para ver si hay alguna subsecuencia que
        // coincida
        Matcher matcher = pattern.matcher(contrasena);
        // Ternaria para mostrar si tiene algun simbolo es muy segura si no es solo
        // segura
        return (matcher.find()) ? ANSI_GREEN + "Muy segura" + ANSI_RESET
                : ANSI_YELLOW + "Segura" + ANSI_RESET;
    }

    public int getLenghtPassword(Scanner sc, String ANSI_RED, String ANSI_RESET) {
        // Longitud de la contrasena deada por el usuario (lengthPas)
        int lengthPas = -1;
        // Mientras la longitud no sea valida
        while (lengthPas < 4 || lengthPas > 8) {
            try {
                System.out.printf("%nComo de larga quieres que sea tu contrasena: ");
                // Pedimos la longitud de la contrasena al usuario
                lengthPas = sc.nextInt();
                // Si la longitud no esta en el rango lanzamos un error
                if (lengthPas < 4 || lengthPas > 8)
                    throw new InputMismatchException();
            } catch (InputMismatchException e) {
                System.out.printf(ANSI_RED + "La contrasena deve de ser de longitud entre (4,8)%n" + ANSI_RESET);
                sc.nextLine(); // Limpiamos el buffer
            }
        }
        sc.nextLine(); // Limpiamos el buffer de entrada porque luego utilizamos nextLine
        // se esto por en cin.ignore() en c++ ademas que creo que lo dimos en clase
        return lengthPas;
    }

    public static void main(String[] args) {
        // Colores para que se vea mas bonito por consola
        // Lo he sacado de StackOverflow
        // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_YELLOW = "\u001B[33m";

        // Creamos el objeto de entrada del usuario
        Scanner sc = new Scanner(System.in);
        // Creamos el objeto app para llamar a los metodos
        Contrasena app = new Contrasena();

        // Nombre de usuario despues del formateo (username)
        String username = "";
        // Nombre introducido por el usuario (userInput)
        String userInput = "";

        System.out.printf(
                ANSI_GREEN + "%n//////////////////// BIENVENIDO A LA APP ////////////////////%n" + ANSI_RESET);
        // Mientras el usuario no introduzca un nombre valido preguntamos
        while (userInput.isBlank() || userInput.split("\\s+").length != 3) {
            System.out.printf("%nIntruoduzca su nombre primer_apellido segundo_apellido: ");
            try {
                // El usuario introduce el nombre con sus apellidos (userInput)
                // Le quitamos los espacios
                userInput = sc.nextLine().trim();
                // Si esta vacio o si hay mas de tres palabras lanzamos un error, \\s es
                // cualquier espacio + es todo lo que le sigue
                if (userInput.isBlank() || userInput.split("\\s+").length != 3)
                    throw new InputMismatchException();

                // Generalmente accederia a las posiciones del split para obtener el nombre y
                // apellidos pero al no poder utilizar array utilizo el metodo getUser
                // Se lo pasamos en minusculas
                username = app.getUser(userInput.toLowerCase());
            } catch (InputMismatchException | StringIndexOutOfBoundsException e) {
                System.out.printf(ANSI_RED + "Error de formato intentelo de nuevo%n" + ANSI_RESET);
                userInput = "";
            }
        }

        // Contraseñas
        // Pedir longitud solo una vez?, bolean no deja null
        Boolean unique = null;
        while (unique == null) {
            System.out.println(
                    "Quieres generar las 3 contrasenas con la misma longitud?" + ANSI_YELLOW + "(s/n)" + ANSI_RESET);
            try {
                // Leer la entrada del usuario y convertirla a minúsculas
                String input = sc.nextLine().toLowerCase();

                // Validar la entrada del usuario
                if (input.equals("s")) {
                    unique = true;
                    break;
                } else if (input.equals("n")) {
                    unique = false;
                    break;
                } else {
                    System.out.printf(ANSI_RED + "Entrada no válida. Por favor, introduce 's' o 'n'" + ANSI_RESET);
                }
            } catch (Exception e) {
                System.out.println(ANSI_RED + "Ocurrio un error inesperado" + ANSI_RESET);
            }
        }

        int lengthPas = app.getLenghtPassword(sc, ANSI_RED, ANSI_RESET);
        // Contrasena final tipo1 (contrasenaMecanica)
        String contrasenaMecanica = app.contrasenaMecanica(lengthPas);

        // Contrasena final tipo2 (contrasenaSegura)
        // Algoritmos con Secure Random
        if (!unique)
            lengthPas = app.getLenghtPassword(sc, ANSI_RED, ANSI_RESET);
        String contrasenaSegura = app.contrasenaSegura(lengthPas);

        // Contraseña tipo3 (contrasenaInovadora)
        // Idea inovadora de contrasena
        if (!unique)
            lengthPas = app.getLenghtPassword(sc, ANSI_RED, ANSI_RESET);
        String contrasenaInovadora = app.contrasenaInovadora(lengthPas, userInput, sc, ANSI_RED, ANSI_RESET);

        System.out
                .printf(ANSI_GREEN + "%n/////////////////////// SOLUCIONES /////////////////////////%n" + ANSI_RESET);
        // Mostramos los resultados \t es un tabulador
        System.out.printf("\t\tGenerando contrasenas%n");
        // Creamos una instancia de random para hacer la carga
        Random random = new Random();

        // Hacemos una carga hasta 100% dando saltos de entre 1 y 5, \r es un retorno de
        // en la linea de comandos osea
        // que se sobreescribe la anterior linea
        for (int i = 0; i < 100; i += random.nextInt(5)) {
            System.out.printf(ANSI_GREEN + "\r\t\t----------%d%%----------" + ANSI_RESET, i);
            try {
                // Hacemos una pausa de 0 a 500 milisegundos
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException e) {
                System.out.printf(ANSI_RED + "Error en la carga" + ANSI_RESET);
            }
        }
        // Mostramos los resultados
        System.out.printf(ANSI_GREEN + "\r\t\t----------%d%%----------" + ANSI_RESET, 100);
        System.out.printf(
                "%n\tUsername: %s%n\tContraseña tipo 1: %s = %s%n\tContraseña tipo 2: %s = %s%n\tContraseña tipo 3: %s = %s%n",
                username, contrasenaMecanica, app.levelOfPasword(contrasenaMecanica, ANSI_RED, ANSI_RESET, ANSI_YELLOW,
                        ANSI_GREEN),
                contrasenaSegura,
                app.levelOfPasword(contrasenaSegura, ANSI_RED, ANSI_RESET, ANSI_YELLOW,
                        ANSI_GREEN),
                contrasenaInovadora, app.levelOfPasword(contrasenaInovadora, ANSI_RED, ANSI_RESET, ANSI_YELLOW,
                        ANSI_GREEN));
        System.out.printf(ANSI_GREEN + "\t\t-----------------------%n"
                + ANSI_RESET);
    }
}