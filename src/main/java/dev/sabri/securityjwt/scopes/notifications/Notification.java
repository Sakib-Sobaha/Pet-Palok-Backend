package dev.sabri.securityjwt.scopes.notifications;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Setter
@Getter
@Data
@Document(collection = "_notifications")

public class Notification {
    @Id
    private String id;
    private String receiver;
    private NotificationType type;
    private String text;
    private String mainContextId;
    private Date timestamp;
    private Boolean unread;

    @Override
    public String toString()
    {
        return "Notification : " + id + "\nreceiver: " + receiver + " : " + type + "\n text:" + text + " : context: - " + mainContextId;

    }
}


