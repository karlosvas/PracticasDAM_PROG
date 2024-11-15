package contrasena;

import java.security.SecureRandom;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.IllegalFormatConversionException;

/**
 * @author carlovas practica 1 PROGRAMACION
 */
public class Contrasena {
    // Metodo para obtener el username
    public String getUser(String userInput) {
        // Obtenemos los 3 primeros del nombre
        String name = userInput.substring(0, 3);

        // Indice de el ultimo espacio del siguiente conjunto de espacios (indexSpace)
        // Y obtenemos los dos char desde el indice encontrado
        int indexSpace = lastSpace(userInput, 0);
        String lastname1 = userInput.substring(indexSpace, indexSpace + 2);

        // Lo mismo para el segundo apellido
        indexSpace = lastSpace(userInput, indexSpace);
        String lastname2 = userInput.substring(indexSpace, indexSpace + 2);
        return lastname2 + lastname1 + name;
    }

    // Buscamos el ultimo espacio del conjunto de espacios esto se hace por si entre
    // nombre y apellido hay 5 espacios por ejemplo
    public int lastSpace(String userInput, int indexSpace) {
        // Empezamos buscando el conjunto de espacios desde el introducido por parametro
        indexSpace = userInput.indexOf(' ', indexSpace);
        // Quitamos todos los adyacentes
        while (userInput.charAt(indexSpace) == ' ')
            indexSpace++;
        return indexSpace;
    }

    // Contraseña tipo 1 dados
    public String contrasenaMecanica(int lengthCM) {
        // Elementos para la contraseña
        String elements = "abcdefghijklmnopqrstuvwxyz0123456789";
        // Caracteres especiales
        String special = "!\"·$%&/()=?¿";
        // Resultado de haber cifrado la contraseña
        String contrasenaCifrada = "";

        // Creamos el objeto de random
        Random random = new Random();

        // Mientras la longitud de la contraseña sea menor que la longitud desada
        while (lengthCM < contrasenaCifrada.length()) {
            // Generamos el cifrado de la contrasena para ello obtenemos dos numeros
            // aleatorios representado un par x, y como minimo 1 maximo 6
            int x = random.nextInt(6) + 1; // x se mueve horizontamente +1 porque minimo es 1
            int y = random.nextInt(6) + 1; // y para represental verticalmente +1 porque minimo es 1

            // Char obtenido de haberlo cifrado, su posicion sera y*longitud de las filas +
            // x, porque cada 6 es una nueva fila
            char c = elements.charAt((y * 6) + x);

            // Verificamos si es un dijito si no "tiramos una moneda" por asi decirlo con
            // 50% posibilidaes cara o cruz
            // So es cara se pone en mayuscula
            if (Character.isDigit(c))
                c = special.charAt(Integer.parseInt(String.valueOf(c)) % special.length());
            else if (random.nextInt(1) == 1)
                c = Character.toUpperCase(c);
            // Lo añadimos a las soluciones
            contrasenaCifrada += c;
        }
        return contrasenaCifrada;
    }

    // Contraseña tipo 2
    // Metodo para generar una contraseñas seguras
    public String contrasenaSegura(int lengthCM) {
        // Definir los caracteres permitidos
        String caracteresPermitidos = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";
        // Crear una instancia de SecureRandom
        SecureRandom secureRandom = new SecureRandom();
        // Crear un StringBuilder para construir la contraseña
        String contrasena = "";

        // Generar la contraseña seleccionando caracteres aleatorios
        while (contrasena.length() < lengthCM) {
            int index = secureRandom.nextInt(caracteresPermitidos.length());
            contrasena += caracteresPermitidos.charAt(index);
        }
        // Devolver la contraseña generada
        return contrasena.toString();
    }

    // Contrasena tipo 3
    public String contrasenaInovadora(int lengthCM, String userInput, Scanner sc) {
        String contrasesena = "";
        String DNI = "";

        for (int i = 0; i < lengthCM; i++) {
            switch (i) {
                case 1:
                    // Primera letra del nombre en mayusculas
                    contrasesena += userInput.charAt(0);
                    break;
                case 2:
                    // Saltamos el ultimo espacio del primer grupo de espacios, osea comienzo del
                    // primer apellido
                    int space = lastSpace(userInput, 0);
                    // Agregamos el ultimo caracater del apellido, que sera el siguiente espacio -1
                    contrasesena += userInput.charAt(userInput.indexOf(' ', space) - 1);
                    break;
                case 3:
                    // Dos ultimas cifras del DNI
                    System.out.printf("Introduce tu DNI sin el digito final: ");
                    sc.nextLine(); // Limpiamos la entrada
                    // Si no se encuentra uno valido sera el string vacio
                    while (DNI.isBlank()) {
                        try {
                            // El usaurio introduce sus numeros de DNI
                            DNI = sc.nextLine().trim();
                            // Utilizando ragex revisamos que sea valido, dice que obligatoriamente tiene
                            // que haber 8 con {} numeros utilziado /d, y // para escapar el caracter
                            if (!DNI.matches("\\d{8}")) {
                                DNI = "";
                                throw new InputMismatchException();
                            }
                        } catch (InputMismatchException e) {
                            System.out.printf(
                                    "El formato no es correcto recuerda que deve de ser una cadena de logitud 8%n");
                        }
                    }
                    contrasesena += DNI.substring(DNI.length() - 2);
                    i++; // Pasamos directamente al 5
                    break;
                case 5:
                    // Letra del digito de control de DNI en mayusculas, algoritmo modulo 23
                    // Posibilidades de numeros, el algoritmo dice que el resto del numero entre 23
                    // da la posicion de al letra en esa sucesion
                    String codString = "TRWAGMYFPDXBNJZSQVHLCKE";
                    char codeNumerDNI = codString.charAt(Integer.parseInt(DNI) % codString.length());
                    contrasesena += codeNumerDNI;
                    break;
                case 6:
                    // Obtener la fehca de nacimiento con un formato valido
                    System.out.print("Introduce tu año de nacimiento: ");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
                    boolean validYear = false;
                    while (!validYear) {
                        try {
                            String yearInput = sc.nextLine().trim();
                            // Verifica el formato
                            Year.parse(yearInput, formatter);
                            validYear = true;
                            contrasesena += yearInput.substring(yearInput.length() - 2);
                        } catch (DateTimeParseException | InputMismatchException | IllegalFormatConversionException e) {
                            System.out.println(
                                    "Ha ocurrido un error con el año de nacimiento deve tener el formato de yyyy");
                        }
                    }
                    i++; // Pasamos directamente al 8
                    break;
                case 8:
                    String special = "!\"·$%&/()=?¿";
                    contrasesena += special.charAt((int) (Math.random() * special.length()));
                    break;
            }

        }
        return contrasesena;
    }

    public String levelOfPasword(String contrasena) {
        int l = contrasena.length();
        if (l >= 4 && l <= 6)
            return "Poco segura";
        // Si al contraseña tiene simbolos mes muy segura
        // Falta de implementar
        boolean simb = false;
        return (simb) ? "Muy segura" : "Segura";
    }

    public static void main(String[] args) {
        // Creamos el objeto de entrada del usuario
        Scanner sc = new Scanner(System.in);
        // Creamos el objeto app para llamar a los metodos
        Contrasena app = new Contrasena();

        // Nombre de usuario (username)
        String username = "";
        String userInput = ""; // La inicializamos fuera para poder acceder a ella posteriormente

        // Mientras el usuario no introduzca un nombre valido
        while (userInput.isBlank() || userInput.split("\\s+").length != 3) {
            System.out.printf("-----%nIntruoduzca su nombre primer_apellido segundo_apellido: ");
            try {
                // El usuario introduce el nombre con sus apellidos (userInput)
                userInput = sc.nextLine();
                // Si esta vacio o si hay mas de tres palabras lanzamos un error
                if (userInput.isBlank() || userInput.split("\\s+").length != 3)
                    throw new InputMismatchException();

                // Generalmente accederia a las posiciones del split para obtener el nombre y
                // apellidos pero al no poder utilizar array utilizo el metodo getUser
                // Se lo pasamos sin espacios y en minusculas
                username = app.getUser(userInput.trim().toLowerCase());
            } catch (InputMismatchException e) {
                System.out.printf("Error de formato%n");
            }
        }

        // Contraseñas
        System.out.print("Como de larga quieres que sea tu contrasena: ");

        // Longitud de la contrasena mecanica
        int lengthCM = -1;

        // Mientras la longitud no sea valida
        while (lengthCM < 0) {
            try {
                // Pedimos la longitud de la contrasena al usuario
                lengthCM = sc.nextInt();
                // Si la longitud no esta en el rango lanzamos un error
                if (lengthCM < 1 || lengthCM > 8)
                    throw new InputMismatchException();
            } catch (InputMismatchException e) {
                System.out.printf("La contrasena deve de ser de longitud entre (1,8)%n");
            }
        }

        // Contrasena final tipo1 (contrasenaMecanica)
        String contrasenaMecanica = app.contrasenaMecanica(lengthCM);

        // Contrasena final tipo2 (contrasenaSegura)
        // Algoritmos con Secure Random
        String contrasenaSegura = "";
        contrasenaSegura = app.contrasenaSegura(lengthCM);

        // Contraseña tipo3 (contrasenaInovadora)
        // Idea inovadora de contrasena
        String contrasenaInovadora = app.contrasenaInovadora(lengthCM, userInput, sc);

        System.out.printf(
                "////////////////////SOLUCIONES////////////////////%nUsername: %s%nContraseña tipo 1: %s = %s%nContraseña tipo 2: %s = %s%nContraseña tipo 3: %s = %s%n-----%n",
                username, contrasenaMecanica, app.levelOfPasword(contrasenaMecanica), contrasenaSegura,
                app.levelOfPasword(contrasenaSegura),
                contrasenaInovadora, app.levelOfPasword(contrasenaInovadora));
    }
}