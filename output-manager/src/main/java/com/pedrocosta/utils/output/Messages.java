package com.pedrocosta.utils.output;

import com.pedrocosta.utils.AppProperties;
import com.pedrocosta.utils.output.utils.Defaults;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

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
    private static ResourceBundleMessageSource source;

    /**
     * Get message by its key in properties file.
     *
     * @param key   Key name of message in properties file
     * @param args  Arguments to include in message
     * @return String with message
     */
    public static String get(String key, String ... args) {
        return get(getDefaultLocale(), key, args);
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
        return getSource().getMessage(key, args, locale);
    }

    /**
     * Get resource object.
     * It uses the property <i>spring.messages.basename</i> in
     * <b>application.properties</b> file to look up for basename.
     * If property <i>spring.messages.basename</i> not set, it uses
     * @return {@link ResourceBundleMessageSource} object.
     */
    private static ResourceBundleMessageSource getSource() {
        return getSource(AppProperties.get("spring.messages.basename"));
    }

    /**
     * Get resource object.
     * @param baseNames Path and name of properties files inside
     *                  <i>resources</i> folder. If there is no file to set,
     *                  use {@link Messages#getSource()} instead.
     * @return {@link ResourceBundleMessageSource} object.
     */
    private static ResourceBundleMessageSource getSource(String ... baseNames) {
        if (source == null) {
            source = new ResourceBundleMessageSource();
            if (baseNames != null) {
                source.setBasenames(baseNames);
            }
            source.setUseCodeAsDefaultMessage(true);
            source.setDefaultLocale(getDefaultLocale());
        }
        return source;
    }

    /**
     * Get app's default locale set in application.properties file.
     * If it is not set, the default locale is {@link Locale#ENGLISH}.
     * @return {@link Locale} object with default locale.
     */
    private static Locale getDefaultLocale() {
        String localProp = AppProperties.get("spring.messages.locale");
        if (localProp == null || localProp.isEmpty()) {
            return Defaults.LOCALE;
        }
        String[] locInfo = localProp.split("_");
        if (locInfo.length > 1) {
            if (locInfo.length > 2) {
                return new Locale(locInfo[0], locInfo[1], locInfo[2]);
            }
            return new Locale(locInfo[0], locInfo[1]);
        }
        return new Locale(localProp);
    }
}
