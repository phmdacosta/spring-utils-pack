package com.pedrocosta.springutils.output;

import org.junit.jupiter.api.Test;

import java.util.Locale;

public class MessagesTest {

    @Test
    public void testGetMessage_defaultLocale() {
        assert "This is a test label 21.".equals(
                Messages.get("test.message", "21"));
    }

    @Test
    public void testGetMessage_withLocale_pt_BR() {
        assert "Isso é uma label de teste 21.".equals(
                Messages.get(new Locale("pt", "BR"),
                        "test.message", "21"));
    }
}
