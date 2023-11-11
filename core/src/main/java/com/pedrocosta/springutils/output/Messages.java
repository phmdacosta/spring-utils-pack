package com.pedrocosta.springutils.output;

import com.pedrocosta.springutils.AppProperties;
import com.pedrocosta.springutils.ArrayUtils;
import com.pedrocosta.springutils.output.utils.Defaults;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Class that centralizes the retrieval of
 * system messages in the properties file.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
@Component
public final class Messages {
    private static ReloadableResourceBundleMessageSource source;
    private static final Object locker = new Object();

    /**
     * Get message by its key in properties file.
     *
     * @param key   Key name of message in properties file
     * @param args  Arguments to include in message
     * @return String with message
     */
    public static String get(String key, String ... args) {
        return get(getLocale(), key, args);
    }

    /**
     * Get message by its key in properties file.
     *
     * @param locale    Locale of message
     * @param key       Key name of message in properties file
     * @param args      Arguments to include in message
     * @return String with message
     */
    public static String get(Locale locale, String key, String ... args) {
        ReloadableResourceBundleMessageSource source = getSource();
        String msg = source.getMessage(key, args, locale);
        return msg.equals(key) ? source.getMessage(key, args, Defaults.LOCALE) : msg;
    }

    /**
     * Get resource object.
     * It uses the property <i>spring.messages.basename</i> in
     * <b>application.properties</b> file to look up for basename.
     * If property <i>spring.messages.basename</i> not set, it uses
     * @return {@link ResourceBundleMessageSource} object.
     */
    public static ReloadableResourceBundleMessageSource getSource() {
        String basenamesVal = AppProperties.get("spring.messages.basename");
        String[] basenamesArr = new String[0];
        if (basenamesVal != null) {
            basenamesArr = basenamesVal.split(",");
        }
        return getSource(basenamesArr);
    }

    /**
     * Get resource object.
     * @param baseNames Path and name of properties files inside
     *                  <i>resources</i> folder. If there is no file to set,
     *                  use {@link Messages#getSource()} instead.
     * @return {@link ReloadableResourceBundleMessageSource} object.
     */
    private static ReloadableResourceBundleMessageSource getSource(String ... baseNames) {
        synchronized (locker) {
            if (source == null) {
                source = new ReloadableResourceBundleMessageSource();
                source.setDefaultEncoding(StandardCharsets.UTF_8.name());
                source.setUseCodeAsDefaultMessage(true);
                if (!ArrayUtils.isEmpty(baseNames)) {
                    source.setBasenames(baseNames);
                }
            }
            return source;
        }
    }

    private static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
