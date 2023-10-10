package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class Notification {
    @Id
    private Long chat_id;
    private String notification_text;
    private LocalDateTime notification_time;

    public Notification() {
    }

    public Notification(Long chat_id, String notification_text, LocalDateTime notification_time) {
        this.chat_id = chat_id;
        this.notification_text = notification_text;
        this.notification_time = notification_time;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public LocalDateTime getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(LocalDateTime notification_time) {
        this.notification_time = notification_time;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "chat_id=" + chat_id +
                ", notification_text='" + notification_text + '\'' +
                ", notification_time=" + notification_time +
                '}';
    }
}