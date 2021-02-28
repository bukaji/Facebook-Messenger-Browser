package com.mycompany.messenger.browser;

import com.mycompany.messenger.browser.entity.Message;
import com.mycompany.messenger.browser.entity.Participant;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 *
 * @author bukja
 */
public interface IBrowser {

    public List<Participant> getParticipants();

    public List<Message> getMessages();

    public void parseJson(File file);

    public String[] filterMessages(String sender, Date fromDate, String text);
}
