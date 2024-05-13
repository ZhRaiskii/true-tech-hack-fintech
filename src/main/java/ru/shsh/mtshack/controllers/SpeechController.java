package ru.shsh.mtshack.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shsh.mtshack.services.UserService;

@Controller
@RequiredArgsConstructor
public class SpeechController {

    private final UserService userService;
    private static final String GREET_MESSAGE = "Привет! Как я могу помочь вам?";
    private static final String REQUEST_PAYMENT_TYPE_MESSAGE = "Выберите тип платежа (жкх, штрафы, налоги):";
    private static final String INVALID_PAYMENT_TYPE_MESSAGE = "Извините, указанный тип платежа не существует.";
    private static final String REQUEST_PAYMENT_AMOUNT_MESSAGE = "Введите сумму платежа:";
    private static final String INSUFFICIENT_FUNDS_MESSAGE = "На вашем счете недостаточно средств для выполнения платежа.";
    private static final String PAYMENT_SUCCESS_MESSAGE = "Платеж выполнен успешно!";
    private static final String PAYMENT_ERROR_MESSAGE = "Ошибка при выполнении платежа.";
    private static final String REQUEST_RECIPIENT_PHONE_MESSAGE = "Введите номер телефона получателя:";
    private static final String REQUEST_TRANSFER_AMOUNT_MESSAGE = "Введите сумму перевода:";
    private static final String INVALID_PHONE_NUMBER_MESSAGE = "Неправильный номер телефона. Пожалуйста, введите номер еще раз.";

    private boolean isWaitingForPaymentType = false;
    private boolean isWaitingForPaymentAmount = false;
    private boolean isWaitingForRecipientPhoneNumber = false;
    private boolean isWaitingForTransferAmount = false;
    private String senderPhoneNumber;
    private String recipientPhoneNumber;

    @PostMapping("/processMessage")
    public ResponseEntity<String> receiveTextFromClient(@RequestParam("text") String text) {
        if (text.contains("привет мтс")) {
            return ResponseEntity.ok(GREET_MESSAGE);
        } else if (text.contains("платеж")) {
            isWaitingForPaymentType = true;
            return ResponseEntity.ok(REQUEST_PAYMENT_TYPE_MESSAGE);
        } else if (isWaitingForPaymentType) {
            isWaitingForPaymentType = false;
            if (text.contains("жкх") || text.contains("штрафы") || text.contains("налоги")){
                isWaitingForPaymentAmount = true;
                return ResponseEntity.ok(REQUEST_PAYMENT_AMOUNT_MESSAGE);
            } else {
                return ResponseEntity.ok(INVALID_PAYMENT_TYPE_MESSAGE);
            }
        } else if (isWaitingForPaymentAmount) {
            isWaitingForPaymentAmount = false;
            double amount;
            try {
                amount = Double.parseDouble(text);
            } catch (NumberFormatException e) {
                return ResponseEntity.ok("Неверный формат суммы. Пожалуйста, введите числовое значение.");
            }
            senderPhoneNumber = userService.getUserId();
            double balance = userService.getAccountBalanceByPhoneNumber(senderPhoneNumber);
            if (balance >= amount) {
                senderPhoneNumber = userService.getUserId();
                boolean paymentResult = userService.payment(senderPhoneNumber, amount);
                if (paymentResult) {
                    return ResponseEntity.ok(PAYMENT_SUCCESS_MESSAGE);
                } else {
                    return ResponseEntity.ok(PAYMENT_ERROR_MESSAGE);
                }
            } else {
                return ResponseEntity.ok(INSUFFICIENT_FUNDS_MESSAGE);
            }
        } else if (text.contains("перевести")) {
            isWaitingForRecipientPhoneNumber = true;
            return ResponseEntity.ok(REQUEST_RECIPIENT_PHONE_MESSAGE);
        } else if (isWaitingForRecipientPhoneNumber) {
            isWaitingForRecipientPhoneNumber = false;
            recipientPhoneNumber = text;
            // Проверка валидности номера телефона
            if (!isValidPhoneNumber(recipientPhoneNumber)) {
                return ResponseEntity.ok(INVALID_PHONE_NUMBER_MESSAGE);
            }
            isWaitingForTransferAmount = true;
            return ResponseEntity.ok(REQUEST_TRANSFER_AMOUNT_MESSAGE);
        } else if (isWaitingForTransferAmount) {
            isWaitingForTransferAmount = false;
            double amount;
            try {
                amount = Double.parseDouble(text);
            } catch (NumberFormatException e) {
                return ResponseEntity.ok("Неверный формат суммы. Пожалуйста, введите числовое значение.");
            }
            senderPhoneNumber = userService.getUserId();
            boolean transferResult = userService.transferToUser(senderPhoneNumber, recipientPhoneNumber, amount);
            if (transferResult) {
                return ResponseEntity.ok("Перевод выполнен успешно!");
            } else {
                return ResponseEntity.ok("Ошибка при выполнении перевода.");
            }
        } else if (text.contains("баланс")) {
            String phoneNumber = userService.getUserId();
            double balance = userService.getAccountBalanceByPhoneNumber(phoneNumber);
            return ResponseEntity.ok("Ваш текущий баланс: " + balance);
        } else {
            return ResponseEntity.ok("Извините, я вас не понял. Пожалуйста, повторите запрос.");
        }
    }


    private boolean isValidPhoneNumber(String phoneNumber) {

        return true; // Пример: всегда возвращает true для простоты демонстрации
    }
}
