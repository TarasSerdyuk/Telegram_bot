package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repositoryes.NotificationRepository;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationScheduler {
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final NotificationRepository notificationRepository;

    public NotificationScheduler(TelegramBot telegramBot, NotificationRepository notificationRepository) {
        this.telegramBot = telegramBot;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void checker() {
        logger.info("checker start");
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Notification> notifications = notificationRepository.findByNotificationTime(now);
        notifications.forEach(this::sendNotify);
    }

    private void sendNotify(Notification notification) {
        logger.info("sendNotify start");
        SendMessage message = new SendMessage(notification.getChat_id(), notification.getNotification_text());
        telegramBot.execute(message);
    }
}