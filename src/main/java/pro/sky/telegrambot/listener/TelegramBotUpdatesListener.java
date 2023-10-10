package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repositoryes.NotificationRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final NotificationRepository notificationRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationRepository notificationRepository) {
        this.telegramBot = telegramBot;
        this.notificationRepository = notificationRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(this::processUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        try {
            logger.info("Processing update: {}", update);
            Message message = update.message() != null ? update.message() : update.editedMessage();
            if (message == null) return;

            String messageText = message.text();
            if (messageText == null) return;

            if ("/start".equals(messageText)) {
                sendWelcomeMessage(message);
            } else {
                processNotificationMessage(message, messageText);
            }
        } catch (Exception e) {
            logger.error("Error processing update: {}", update, e);
        }
    }

    private void sendWelcomeMessage(Message message) {
        String welcomeText =  "Добро пожаловать в мой бот " + message.chat().firstName() +"!";
        SendMessage welcomeMessage = new SendMessage(message.chat().id(), welcomeText);
        telegramBot.execute(welcomeMessage);
    }

    private void processNotificationMessage(Message message, String messageText) {
        Pattern pattern = Pattern.compile("([\\d.:\\s]{16})\\s+(.+)");
        Matcher matcher = pattern.matcher(messageText);

        if (matcher.matches()) {
            saveNotification(message, matcher);
        } else {
            sendInvalidFormatMessage(message);
        }
    }

    private void saveNotification(Message message, Matcher matcher) {
        String dateTimeString = matcher.group(1);
        String notificationText = matcher.group(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        Notification task = new Notification(message.chat().id(), notificationText, dateTime);
        notificationRepository.save(task);
    }

    private void sendInvalidFormatMessage(Message message) {
        String invalidFormatText = "Некорректный формат сообщения. Используйте формат дд.мм.гггг чч:мм текст напоминания";
        SendMessage invalidFormatMessage = new SendMessage(message.chat().id(), invalidFormatText);
        telegramBot.execute(invalidFormatMessage);
    }
}