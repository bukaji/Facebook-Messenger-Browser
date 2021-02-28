package com.mycompany.messenger.browser;

import com.mycompany.messenger.browser.entity.Message;
import com.mycompany.messenger.browser.entity.Participant;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author bukja
 */
public class BrowserImpl implements IBrowser {

    private List<Participant> participants = new ArrayList();
    private List<Message> messages = new ArrayList();
    private Boolean isParsed = Boolean.FALSE;

    @Override
    public List<Participant> getParticipants() {
        checkParse();
        return participants;
    }

    @Override
    public List<Message> getMessages() {
        checkParse();
        return messages;
    }

    @Override
    public void parseJson(File file) {

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(file)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JSONObject jsonObject = (JSONObject) obj;
            parseObjects(jsonObject);
            isParsed = Boolean.TRUE;

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private void parseObjects(JSONObject jsonObject) {
        parseParticipants(jsonObject);
        parseMessages(jsonObject);
    }

    private void parseMessages(JSONObject jsonObject) {
        JSONArray messagesJson = (JSONArray) jsonObject.get("messages");

        for (Iterator it = messagesJson.iterator(); it.hasNext();) {
            JSONObject message = (JSONObject) it.next();

            String senderName = decode(message.get("sender_name").toString());
            Date timestampMs = new Date((Long) message.get("timestamp_ms"));

            Object contentObj = message.get("content");
            String content = decode(contentObj == null ? "" : contentObj.toString());
            String type = message.get("type").toString();
            Boolean isUnsent = Boolean.parseBoolean(message.get("is_unsent").toString());

            messages.add(new Message(senderName, timestampMs, content, type, isUnsent));
        }
    }

    private void parseParticipants(JSONObject jsonObject) {
        JSONArray participantsJson = (JSONArray) jsonObject.get("participants");

        for (Iterator it = participantsJson.iterator(); it.hasNext();) {
            JSONObject participant = (JSONObject) it.next();
            String name = decode(participant.get("name").toString());
            participants.add(new Participant(name));
        }
    }

    private String decode(String value) {
//        ą, ć, ę, ł, ń, ó, ś, ź, ż

        value = value.replaceAll("\\u00c4\\u0085", "ą");
        value = value.replaceAll("\\u00c4\\u0087", "ć");
        value = value.replaceAll("\\u00c4\\u0099", "ę");
        value = value.replaceAll("\\u00c5\\u0082", "ł");
        value = value.replaceAll("\\u00c5\\u0084", "ń");
        value = value.replaceAll("\\u00c3\\u00b3", "ó");
        value = value.replaceAll("\\u00c5\\u009b", "ś");
        value = value.replaceAll("\\u00c5\\u00ba", "ź");
        value = value.replaceAll("\\u00c5\\u00bc", "ż");

        value = value.replaceAll("\\u00c4\\u0084", "Ą");
        value = value.replaceAll("\\u00c4\\u0086", "Ć");
        value = value.replaceAll("\\u00c4\\u0098", "Ę");
        value = value.replaceAll("\\u00c5\\u0081", "Ł");
        value = value.replaceAll("\\u00c5\\u0083", "Ń");
        value = value.replaceAll("\\u00c3\\u00b2", "Ó");
        value = value.replaceAll("\\u00c5\\u009a", "Ś");
        value = value.replaceAll("\\u00c5\\u00ba", "Ź");
        value = value.replaceAll("\\u00c5\\u00bb", "Ż");

        value = value.replaceAll("\\u00f0\\u009f\\u0098\\u009e", "EMOTKA");
        return value;
    }

    private void checkParse() {
        if (!isParsed) {
            throw new RuntimeException("Żaden Plik nie zostal przetworzony");
        }
    }

    @Override
    public String[] filterMessages(String sender, Date fromDate, String text) {
        List<String> list = getMessages().stream()
                .sorted((m1, m2) -> m1.getTimestampMs().compareTo(m2.getTimestampMs()))
                .filter(m -> m.getSenderName().toLowerCase().contains(sender))
                .filter(m -> fromDate == null ? true : m.getTimestampMs().compareTo(fromDate) > 0)
                .filter(m -> m.getContent().toLowerCase().contains(text))
                .map(m -> m.toString())
                .collect(Collectors.toList());
        return list.toArray(new String[0]);
    }

}
