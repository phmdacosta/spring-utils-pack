package com.pedrocosta.springutils.output;

import com.pedrocosta.springutils.AppProperties;
import com.pedrocosta.springutils.ArrayUtils;
import com.pedrocosta.springutils.output.utils.Defaults;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MessageLocaleResolver extends AcceptHeaderLocaleResolver {

    public MessageLocaleResolver() {
        initSupportedLocales();
        initDefaultLocale();
    }

    private void initDefaultLocale() {
        Locale defaultLocale = getLocaleFromString(AppProperties.get("spring.messages.locale"));
        setDefaultLocale(defaultLocale);
    }

    private void initSupportedLocales() {
        List<Locale> supported = getSupportedLocaleFromString(
                AppProperties.get("spring.messages.supported.locales"));
        if (supported != null && !supported.isEmpty()) {
            setSupportedLocales(supported);
        }
    }

    private Locale getLocaleFromString(String localeStr) {
        if (localeStr != null && !localeStr.isEmpty()) {
            String[] locInfo = localeStr.split("_");
            if (locInfo.length > 1) {
                if (locInfo.length > 2) {
                    return new Locale(locInfo[0], locInfo[1], locInfo[2]);
                }
                return new Locale(locInfo[0], locInfo[1]);
            }
            return new Locale(localeStr);
        }
        return Defaults.LOCALE;
    }

    private List<Locale> getSupportedLocaleFromString(String supportLocales) {
        String[] supportedLocalesStr = supportLocales != null
                ? supportLocales.split(",") : new String[0];
        if (!ArrayUtils.isEmpty(supportedLocalesStr)) {
            return Arrays.stream(supportedLocalesStr)
                    .map(this::getLocaleFromString).collect(Collectors.toList());
        }
        return null;
    }
}
