package ec.edu.ups.poo.util;

import java.util.Currency;
import java.util.Locale;

public class Idioma {
    private static MensajeInternacionalizacionHandler handler = new MensajeInternacionalizacionHandler("es", "EC");

    public static void setIdioma(String lang, String country) {
        handler.setLenguaje(lang, country);
    }

    public static String get(String key) {
        return handler.get(key);
    }

    public static MensajeInternacionalizacionHandler getHandler() {
        return handler;
    }

    public static Locale getLocale() {
        return handler.getLocale();
    }

    public static Currency getCurrency() {
        return Currency.getInstance(getLocale());
    }
}