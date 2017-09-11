package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.repositories.NotificationRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService extends LifelineService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> fetchLatestNotifications() {
        return notificationRepository.findFirst10ByOrderByEventDateDesc();
    }

    public void logLotteryDraw(String name) {
        String description = (!name.isEmpty()) ? "Lottery Draw Made - " + name : "Lottery Draw Made";
        Notification lotteryDrawNotification = new Notification(DateTime.now().toDate(), "Draw", description);
        saveNotification(lotteryDrawNotification);
    }

    public void logNewMemberAdded() {
        Notification lotteryDrawNotification = new Notification(DateTime.now().toDate(), "NewMember", "New Member Added");
        saveNotification(lotteryDrawNotification);
    }

    public void logMembershipsClosed(int numberClosed) {
        String description = (numberClosed == 1) ? numberClosed + " Membership Closed" : numberClosed + " Memberships Closed";
        Notification membershipClosedNotification = new Notification(DateTime.now().toDate(), "NewMember", description);
        saveNotification(membershipClosedNotification);
    }

    public void logPayment(int numberOfPayments) {
        String message = (numberOfPayments == 1) ? "Payment Entered Manually" : numberOfPayments + " Payments Imported";
        Notification paymentNotification = new Notification(DateTime.now().toDate(), "Payment", message);
        saveNotification(paymentNotification);
    }
}
