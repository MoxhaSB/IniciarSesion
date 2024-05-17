import Interface.Sistema;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;
import java.util.Scanner;

public class SistemaImpl implements Sistema {

    /**
     * Scanner para leer datos
     */
    private Scanner read;
    private ArrayList<Usuario> lista;
    private Usuario usuarioActual;


    /**
     * Constructor
     */
    public SistemaImpl() throws IOException, NoSuchAlgorithmException {
        read = new Scanner(System.in);
        lista = new ListaUsuario().getLista();
        this.usuarioActual = null;
        readFile();
        menu();
    }


    @Override
    public String createHash(String contra) throws NoSuchAlgorithmException {
        MessageDigest crearHash = MessageDigest.getInstance("SHA-256");
        byte[] bytes = crearHash.digest(contra.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * bytes.length);

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public void signUp() throws NoSuchAlgorithmException <{

        this.usuarioActual = null;
        String nombre;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("                                                   \n" +
                ",---.          o     |                             \n" +
                "|---',---.,---..,---.|--- ,---.,---.,---.,---.,---.\n" +
                "|  \\ |---'|   ||`---.|    |    ,---||    `---.|---'\n" +
                "`   ``---'`---|``---'`---'`    `---^`    `---'`---'\n" +
                "          `---'                                    ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        while(true) {

            boolean nombreOcupado = false;

            System.out.println("\nIngrese su nombre de usuario: (0 para volver)");
            nombre = read.nextLine();

            if(nombre.equalsIgnoreCase("0")){
                return;
            }

            for (Usuario aux : lista) {
                if (aux.getNombre().equalsIgnoreCase(nombre)) {
                    System.out.println("\nEl nombre de usuario ya existe, intente otro.");
                    nombreOcupado = true;
                    break;
                }
            }
            if(!nombreOcupado){

                System.out.println("\nIngrese su contraseña: (0 para volver)");
                String contra = read.nextLine();

                if (contra.equalsIgnoreCase("0")) {
                    //no hacer nada

                }else{
                    System.out.println("\nConfirme su contraseña: ");
                    String confirmarContra = read.nextLine();

                    if(contra.equalsIgnoreCase(confirmarContra)){

                        //hashear la contraseña y crear el usuario
                        Usuario nuevo= new Usuario(nombre,createHash(contra));
                        this.usuarioActual = nuevo;
                        lista.add(nuevo);
                        System.out.println("\nSe ha creado su cuenta, bienvenido --> " + nombre);
                        //TODO COLOCAR EL MENU QUE QUIERA QUE SALGA UNA VEZ INICIADO SESIÓN
                        break;

                    }else{
                        System.out.println("\nLas contraseñas no son iguales, intente nuevamente.");
                    }
                }
            }
        }
    }

    @Override
    public void login() throws NoSuchAlgorithmException {
        //hacer null para evitar errores
        this.usuarioActual = null;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("|     o     o              ,---.          o          \n" +
                "|,---..,---..,---.,---.    `---.,---.,---..,---.,---.\n" +
                "||   |||    |,---||            ||---'`---.||   ||   |\n" +
                "``   '``---'``---^`        `---'`---'`---'``---'`   '\n" +
                "                                                     ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("\nIngrese su nombre de usuario: ");
        String nombre = read.nextLine();
        System.out.println("\nIngrese su contraseña: ");
        String contra = read.nextLine();

        for (Usuario aux : lista){
            if(aux.getNombre().equalsIgnoreCase(nombre) && aux.getContrasenia().equalsIgnoreCase(createHash(contra))){
                System.out.println("\nSe ha iniciado sesión como --> " + nombre);
                this.usuarioActual = aux;
                break;
            }
        }
        if(this.usuarioActual != null){
            //TODO PONER EL MENU QUE QUIERA
            System.out.println("\nmenuuuuuuuuuuuu");
        }else{
            System.out.println("\nUsuario o contraseñas incorrectos.");
        }
    }

    @Override
    public void menu() throws NoSuchAlgorithmException, IOException {
        while(true) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("                                                                                     \n" +
                    "`7MMpdMAo. `7Mb,od8  ,pW\"Wq.   .P\"Ybmmm `7Mb,od8  ,6\"Yb.  `7MMpMMMb.pMMMb.   ,6\"Yb.  \n" +
                    "  MM   `Wb   MM' \"' 6W'   `Wb :MI  I8     MM' \"' 8)   MM    MM    MM    MM  8)   MM  \n" +
                    "  MM    M8   MM     8M     M8  WmmmP\"     MM      ,pm9MM    MM    MM    MM   ,pm9MM  \n" +
                    "  MM   ,AP   MM     YA.   ,A9 8M          MM     8M   MM    MM    MM    MM  8M   MM  \n" +
                    "  MMbmmd'  .JMML.    `Ybmd9'   YMMMMMb  .JMML.   `Moo9^Yo..JMML  JMML  JMML.`Moo9^Yo.\n" +
                    "  MM                          6'     dP                                              \n" +
                    ".JMML.                        Ybmmmd'                                                \n");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            System.out.println("""
                    |1| Iniciar sesión
                    |2| Registrarse
                    |3| Cerrar programa
                    Ingrese su opción: 
                    """);
            switch (containText()) {
                case -1 -> System.out.println("\nIngrese un valor numérico.");
                case 1 -> login();
                case 2 -> signUp();
                case 3 -> {
                    writeFile();
                    System.out.println("\nCerrando programa.");
                    return;
                }
                default -> System.out.println("\nIngrese una opción válida.");
            }
        }

    }

    @Override
    public void readFile() throws FileNotFoundException {

        //bufferedReader para optimizacion en lectura de un fileReader
        try(BufferedReader file = new BufferedReader(new FileReader("Usuarios.txt"))){
            String linea;
            while((linea= file.readLine())!= null){
                String[] chain = linea.split(",");
                lista.add(new Usuario(chain[0],chain[1]));
            }
        }catch (Exception a){
            a.printStackTrace();
        }


    }

    @Override
    public void writeFile() throws IOException {
        try(BufferedWriter file = new BufferedWriter(new FileWriter("Usuarios.txt"))){
            String linea;
            for(Usuario aux : lista){
                file.write(aux.getNombre()+","+aux.getContrasenia());
                file.newLine();
            }
        }catch (IOException a){
            System.out.println(a);
        }
    }

    public int containText(){
        int e;
        try{
            e = Integer.parseInt(read.nextLine());
        }catch (IllegalArgumentException a){
            return -1;
        }
        return  e;
    }
}
