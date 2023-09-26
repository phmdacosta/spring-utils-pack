package com.pedrocosta.springutils.test;

import com.pedrocosta.springutils.output.Messages;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagesTest {

    @Test
    public void testGetMessages_withoutParameter() throws Exception {
        String expectedLabel1 = "Test Label";
        String actualLabel1 = Messages.get("test.label");
        assertEquals(expectedLabel1, actualLabel1);

        String expectedMessage = "Test message";
        String actualMessage = Messages.get("test.message");
        assertEquals(expectedMessage, actualMessage);

        String expectedLabel2 = "Test Label 2";
        String actualLabel2 = Messages.get("test.label2");
        assertEquals(expectedLabel2, actualLabel2);
    }

    @Test
    public void testGetMessages_withParameter() throws Exception {
        String param = "Parameter";

        String expectedLabel1 = "Test Label Parameter";
        String actualLabel1 = Messages.get("test.label.param", param);
        assertEquals(expectedLabel1, actualLabel1);

        String expectedMessage = "Test message Parameter";
        String actualMessage = Messages.get("test.message.param", param);
        assertEquals(expectedMessage, actualMessage);

        String expectedLabel2 = "Test Label 2 Parameter";
        String actualLabel2 = Messages.get("test.label2.param", param);
        assertEquals(expectedLabel2, actualLabel2);
    }
}
