/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.utiles;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author sebastian
 */
public class Utiles {

    //codigo para validar la cedula
    // Esta función servirá para validar la longitud de la cadena de texto
    private static Boolean stringLength(String string, int length) {
        if (string.length() == length) {
            return true;
        }
        return false;
    }

    // Esta función sumará y multiplicará los primeros 9 dígitos de la cédula
    // Aquí se multiplica los dígitos impares por 2 y los pares por 1
    // Se usa el siguiente patrón 2.1.2.1.2.1.2.1.2
    // Se retorna la resta de la decena superior de la suma menos la suma
    // Es decir si sale la suma de todos los dígitos 36 entonces la decena superior es 40
    // Por lo tanto debemos hacer lo siguiente (40-36)
    private static byte sumDigits(byte[] digits) {
        byte verifier;
        byte sum = 0;
        for (byte i = 0; i < digits.length; i = (byte) (i + 2)) {
            verifier = (byte) (digits[i] * 2);
            if (verifier > 9) {
                verifier = (byte) (verifier - 9);
            }
            sum = (byte) (sum + verifier);
        }
        for (byte i = 1; i < digits.length; i = (byte) (i + 2)) {
            verifier = (byte) (digits[i] * 1);
            sum = (byte) (sum + verifier);
        }
        return (byte) ((sum - (sum % 10) + 10) - sum);
    }

    // Función principal que hace uso de las anteriores funciones
    public static Boolean idCardEcuador(String idCard) {
        try {
            if (stringLength(idCard, 10)) {
                String[] data = idCard.split("");
                byte verifier = Byte.parseByte(data[0] + data[1]);
                byte[] digits = new byte[9];
                for (byte i = 0; i < 9; i++) {
                    digits[i] = Byte.parseByte(data[i]);
                }
                if (verifier >= 1 && verifier <= 24) {
                    verifier = digits[2];
                    if (verifier <= 6) {
                        if (sumDigits(digits) == Byte.parseByte(data[9])) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Field getField(Class clazz, String attribute) {
        Field field = null;
        //Field[] fields = clazz.getFields();//ver atributos declarados en la clase padre
        for (Field f : clazz.getSuperclass().getDeclaredFields()) {
            System.out.println(f.getName() + " " + f.getType().getName());
            if (f.getName().equalsIgnoreCase(attribute)) {
                field = f;
                break;
            }
        }
        for (Field f : clazz.getDeclaredFields()) {
            System.out.println(f.getName() + " " + f.getType().getName());
            if (f.getName().equalsIgnoreCase(attribute)) {
                field = f;
                break;
            }
        }
        return field;
    }

    public static String getDirPoject() {
        return System.getProperty("user.dir");
    }

    public static String getOS() {
        return System.getProperty("os.name");
    }

    public static void abrirNavegadorPredeterminadorWindows(String url) throws Exception {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
    }

    public static void abrirNavegadorPredeterminadorLinux(String url) throws Exception {
        Runtime.getRuntime().exec("xdg-open " + url);
    }

    public static void abrirNavegadorPredeterminadorMacOsx(String url) throws Exception {
        Runtime.getRuntime().exec("open " + url);
    }

    //PARA GUARDAR ARCHIVO EN UTILES
    public static void copiarArchivo(File origen, File destino) throws Exception {
        Files.copy(origen.toPath(),
                (destino).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }

    //EN UTILES EXTENSION
    public static String extension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }

    public static double coordGpsToKm(double lat1, double lon1, double lat2, double lon2) {
        double lat1rad = Math.toRadians(lat1);
        double lon1rad = Math.toRadians(lon1);
        double lat2rad = Math.toRadians(lat2);
        double lon2rad = Math.toRadians(lon2);

        double difLatitud = lat1rad - lat2rad;
        double difLongitud = lon1rad - lon2rad;

        double a = Math.pow(Math.sin(difLatitud / 2), 2)
                + Math.cos(lat1rad)
                * Math.cos(lat2rad)
                * Math.pow(Math.sin(difLongitud / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double radioTierraKm = 6378.0;
        double distancia = radioTierraKm * c;

        return distancia;
    }

    public static Double redondear(Double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}
