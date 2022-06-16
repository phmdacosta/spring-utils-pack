package com.pedrocosta.springutils.output;

import com.pedrocosta.springutils.AppProperties;
import com.pedrocosta.springutils.ArrayUtils;
import com.pedrocosta.springutils.output.utils.Defaults;
import com.pedrocosta.springutils.output.utils.LocaleUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Handle request message locale.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public class MessageLocaleResolver extends AcceptHeaderLocaleResolver {

    public MessageLocaleResolver() {
        initSupportedLocales();
        initDefaultLocale();
    }

    private void initDefaultLocale() {
        setDefaultLocale(Defaults.LOCALE);
    }

    private void initSupportedLocales() {
        List<Locale> supported = getSupportedLocaleFromString(
                AppProperties.get("spring.messages.supported.locales"));
        if (supported != null && !supported.isEmpty()) {
            setSupportedLocales(supported);
        }
    }

    private List<Locale> getSupportedLocaleFromString(String supportLocales) {
        String[] supportedLocalesStr = supportLocales != null
                ? supportLocales.split(",") : new String[0];
        if (!ArrayUtils.isEmpty(supportedLocalesStr)) {
            return Arrays.stream(supportedLocalesStr)
                    .map(LocaleUtils::fromString).collect(Collectors.toList());
        }
        return null;
    }
}
