package pro.sky.telegrambot.service;


import com.pengrad.telegrambot.model.Message;
import pro.sky.telegrambot.model.Notification;


import java.util.List;
import java.util.regex.Matcher;



public interface NotificationService {

    public void sendWelcomeMessage(Message message);

    public void processNotificationMessage(Message message, String messageText);

    public void saveNotification(Message message, Matcher matcher);

    public void sendInvalidFormatMessage(Message message);

}