package dev.sabri.securityjwt.scopes.notifications;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    public List<Notification> findByReceiver(String receiver);
    public List<Notification> deleteByReceiver(String receiver);
}
