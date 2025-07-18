package ec.edu.ups.poo.util;

import ec.edu.ups.poo.exeptions.CedulaExeption;
import ec.edu.ups.poo.exeptions.ContraseñaExeption;

import java.text.MessageFormat;
import java.util.regex.Pattern;

/**
 * Esta clase proporciona m\u00e9todos est\u00e1ticos para validar datos de entrada
 * comunes en la aplicaci\u00f3n, como c\u00e9dulas ecuatorianas, contrase\u00f1as seguras,
 * y conversiones de tipos num\u00e9ricos. Lanza excepciones claras si la validaci\u00f3n falla.
 *
 * @author Tu Nombre del Estudiante
 * @version 1.0
 * @since 2023-01-01
 */
public class Validador {

    /**
     * Valida si una cadena no es nula ni est\u00e1 vac\u00eda (despu\u00e9s de recortar espacios).
     *
     * @param texto El texto a validar.
     * @param nombreCampoKey La clave de internacionalizaci\u00f3n del nombre del campo para el mensaje de error.
     * @throws IllegalArgumentException Si el texto es nulo o est\u00e1 vac\u00edo.
     */
    public static void validarNoVacio(String texto, String nombreCampoKey) throws IllegalArgumentException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format(Idioma.get("validacion.campo.vacio"), Idioma.get(nombreCampoKey)));
        }
    }

    /**
     * Valida si una cadena representa un n\u00famero entero.
     *
     * @param texto El texto a validar.
     * @param nombreCampoKey La clave de internacionalizaci\u00f3n del nombre del campo para el mensaje de error.
     * @return El valor entero si es v\u00e1lido.
     * @throws IllegalArgumentException Si el texto no es un n\u00famero entero v\u00e1lido.
     */
    public static int validarEntero(String texto, String nombreCampoKey) throws IllegalArgumentException {
        validarNoVacio(texto, nombreCampoKey);
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MessageFormat.format(Idioma.get("validacion.campo.numerico"), Idioma.get(nombreCampoKey)));
        }
    }

    /**
     * Valida si una cadena representa un n\u00famero decimal (double).
     *
     * @param texto El texto a validar.
     * @param nombreCampoKey La clave de internacionalizaci\u00f3n del nombre del campo para el mensaje de error.
     * @return El valor double si es v\u00e1lido.
     * @throws IllegalArgumentException Si el texto no es un n\u00famero decimal v\u00e1lido.
     */
    public static double validarDouble(String texto, String nombreCampoKey) throws IllegalArgumentException {
        validarNoVacio(texto, nombreCampoKey);
        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MessageFormat.format(Idioma.get("validacion.campo.decimal"), Idioma.get(nombreCampoKey)));
        }
    }

    /**
     * Valida una c\u00e9dula ecuatoriana seg\u00fan su formato y d\u00edgito verificador.
     * La c\u00e9dula debe tener 10 d\u00edgitos y pasar la l\u00f3gica de validaci\u00f3n de d\u00edgito verificador.
     *
     * @param cedula La cadena que representa la c\u00e9dula.
     * @throws CedulaExeption Si la c\u00e9dula no es v\u00e1lida por longitud, provincia o d\u00edgito verificador.
     */
    public static void validarCedulaEcuatoriana(String cedula) throws CedulaExeption {
        validarNoVacio(cedula, "registro.lbl.usuario");
        if (!cedula.matches("\\d{10}")) {
            throw new CedulaExeption(Idioma.get("validacion.cedula.longitudInvalida"));
        }

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if (provincia < 1 || provincia > 24) {
            throw new CedulaExeption(Idioma.get("validacion.cedula.provinciaInvalida"));
        }

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            int valor = digito * coeficientes[i];
            suma += (valor >= 10) ? (valor - 9) : valor;
        }

        int ultimoDigito = Character.getNumericValue(cedula.charAt(9));
        int digitoVerificador = (suma % 10 == 0) ? 0 : (10 - (suma % 10));

        if (ultimoDigito != digitoVerificador) {
            throw new CedulaExeption(Idioma.get("validacion.cedula.digitoVerificadorInvalido"));
        }
    }

    /**
     * Valida si una contrase\u00f1a cumple con los requisitos de seguridad.
     * Requisitos: M\u00ednimo 6 caracteres, al menos una letra may\u00fascula,
     * una min\u00fascula y uno de los caracteres especiales '@', '_' o '-'.
     *
     * @param password La contrase\u00f1a a validar.
     * @throws ContraseñaExeption Si la contrase\u00f1a no cumple alguno de los requisitos.
     */
    public static void validarContrasenaSegura(String password) throws ContraseñaExeption {
        validarNoVacio(password, "registro.lbl.contrasena");

        if (password.length() < 6) {
            throw new ContraseñaExeption(Idioma.get("validacion.contrasena.longitudMinima"));
        }
        Pattern mayuscula = Pattern.compile(".*[A-Z].*");
        if (!mayuscula.matcher(password).matches()) {
            throw new ContraseñaExeption(Idioma.get("validacion.contrasena.faltaMayuscula"));
        }
        Pattern minuscula = Pattern.compile(".*[a-z].*");
        if (!minuscula.matcher(password).matches()) {
            throw new ContraseñaExeption(Idioma.get("validacion.contrasena.faltaMinuscula"));
        }
        // Escapar el guion '-' en la expresi\u00f3n regular
        Pattern caracterEspecial = Pattern.compile(".*[@_\\-].*");
        if (!caracterEspecial.matcher(password).matches()) {
            throw new ContraseñaExeption(Idioma.get("validacion.contrasena.faltaCaracterEspecial"));
        }
    }
}