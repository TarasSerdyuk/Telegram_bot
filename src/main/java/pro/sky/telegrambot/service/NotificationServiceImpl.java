package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repositoryes.NotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationServiceImpl implements NotificationService {
    Logger logger = LoggerFactory.getLogger(Notification.class);
    private final TelegramBot telegramBot;
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(TelegramBot telegramBot, NotificationRepository notificationRepository) {
        this.telegramBot = telegramBot;
        this.notificationRepository = notificationRepository;
    }

    public void sendWelcomeMessage(Message message) {
        String welcomeText = "Добро пожаловать в мой бот " + message.chat().firstName() + "!";
        SendMessage welcomeMessage = new SendMessage(message.chat().id(), welcomeText);
        telegramBot.execute(welcomeMessage);
    }

    public void processNotificationMessage(Message message, String messageText) {
        Pattern pattern = Pattern.compile("([\\d.:\\s]{16})\\s+(.+)");
        Matcher matcher = pattern.matcher(messageText);

        if (matcher.matches()) {
            saveNotification(message, matcher);
        } else {
            sendInvalidFormatMessage(message);
        }
    }

    public void saveNotification(Message message, Matcher matcher) {
        String dateTimeString = matcher.group(1);
        String notificationText = matcher.group(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        Notification task = new Notification();
        task.setChat_id(message.chat().id());
        task.setNotification_text(notificationText);
        task.setNotification_time(dateTime);
        notificationRepository.save(task);

    }

    public void sendInvalidFormatMessage(Message message) {
        String invalidFormatText = "Некорректный формат сообщения. Используйте формат дд.мм.гггг чч:мм текст напоминания";
        SendMessage invalidFormatMessage = new SendMessage(message.chat().id(), invalidFormatText);
        telegramBot.execute(invalidFormatMessage);
    }

}