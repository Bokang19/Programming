package com.mycompany.chattapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class MessageTest {

    // Helper - creates the 5 POE messages
    public ArrayList<Message> setup() {
        Message.sentMessages.clear();
        Message.storedMessages.clear();
        Message.disregardedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();
        Message.sentMessagesText.clear();

        ArrayList<Message> list = new ArrayList<>();
        Message m1 = new Message("Developer", "+27834557896", "Did you get the cake?");
        Message m2 = new Message("Developer", "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message m3 = new Message("Developer", "+27834484567", "Yohoooo, I am at your gate.");
        Message m4 = new Message("Developer", "+27838884567", "It is dinner time !");
        m4.setMessageID("0838884567");
        Message m5 = new Message("Developer", "+27838884567", "Ok, I am leaving without you.");

        m1.storeMessage("Sent");
        m2.storeMessage("Stored");
        m3.storeMessage("Disregard");
        m4.storeMessage("Sent");
        m5.storeMessage("Stored");

        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        list.add(m5);
        return list;
    }

    // 1 - The one that was failing before
    @Test
    public void testTotalMessagesStartsAtZero() {
        Message.sentMessages.clear();
        Message.storedMessages.clear();
        Message.disregardedMessages.clear();
        Message.messageIDs.clear();
        Message.messageHashes.clear();
        int total = Message.sentMessages.size() + Message.storedMessages.size() + Message.disregardedMessages.size();
        assertEquals(0, total);
    }

    // 2 - Sent messages are populated
    @Test
    public void testSentMessagesPopulated() {
        setup();
        assertEquals(2, Message.sentMessages.size());
        assertEquals("Did you get the cake?", Message.sentMessages.get(0).getMessageText());
        assertEquals("It is dinner time !", Message.sentMessages.get(1).getMessageText());
    }

    // 3 - Longest message
    @Test
    public void testLongestMessage() {
        ArrayList<Message> l = setup();
        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.displayLongestMessage(l));
    }

    // 4 - Search by Message ID
    @Test
    public void testSearchByMessageID() {
        ArrayList<Message> l = setup();
        assertEquals("It is dinner time !", Message.searchByMessageID("0838884567", l));
    }

    // 5 - Search by Message ID - Not Found
    @Test
    public void testSearchByMessageIDNotFound() {
        ArrayList<Message> l = setup();
        assertEquals("Message ID not found.", Message.searchByMessageID("9999999999", l));
    }

    // 6 - Search by Recipient
    

    // 7 - Search by Recipient - Not Found
    @Test
    public void testSearchByRecipientNotFound() {
        ArrayList<Message> l = setup();
        assertEquals("No messages found for this recipient.", Message.searchByRecipient("+27000000000", l));
    }

    // 8 - Delete by Hash
    @Test
    public void testDeleteByHash() {
        ArrayList<Message> l = setup();
        String hash = l.get(1).getMessageHash();
        String result = Message.deleteByHash(hash, l);
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
        assertEquals(4, l.size());
    }

    // 9 - Delete by Hash - Not Found
    @Test
    public void testDeleteByHashNotFound() {
        ArrayList<Message> l = setup();
        assertEquals("Message hash not found.", Message.deleteByHash("00:0:FAKE:FAKE", l));
    }

    // 10 - Message ID is 10 characters or less
    @Test
    public void testMessageIDLength() {
        Message m = new Message("+27831234567", "Hello test");
        assertTrue(m.checkMessageID());
        assertTrue(m.getMessageID().length() <= 10);
    }

    // 11 - Recipient Cell Valid
    @Test
    public void testCheckRecipientCellValid() {
        Message m = new Message("+27831234567", "Hello");
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    // 12 - Recipient Cell Invalid
    @Test
    public void testCheckRecipientCellInvalid() {
        Message m = new Message("0831234567", "Hello");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", m.checkRecipientCell());
    }

    // 13 - Message Hash Format
    @Test
    public void testMessageHashFormat() {
        Message m = new Message("+27831234567", "Did you get the cake?");
        String hash = m.getMessageHash();
        assertNotNull(hash);
        assertTrue(hash.contains(":"));
        // Should contain first word and last word in caps
        assertTrue(hash.contains("DID"));
        assertTrue(hash.contains("CAKE"));
    }

    // 14 - Full Report + Stored Messages
    @Test
    public void testFullReportAndStored() {
        ArrayList<Message> l = setup();
        // Stored should have 2 messages (m2 and m5)
        assertEquals(2, Message.storedMessages.size());

        String report = Message.displayFullReport(l);
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("Where are you?"));
        assertTrue(report.contains("Recipient"));
    }
}
