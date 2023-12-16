package io.proj3ct.FunBot.service;

import io.proj3ct.FunBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    static final String HELP_TEXT = "This bot was invented for fun";

    public TelegramBot(BotConfig config){
        this.config = config;
        List<BotCommand> listjfCommands = new ArrayList<>();
        listjfCommands.add(new BotCommand("/start","get a welcome message"));
        listjfCommands.add(new BotCommand("/mydata","get your data stored"));
        listjfCommands.add(new BotCommand("/deletdada","delete my data"));
        listjfCommands.add(new BotCommand("/help","info how to use this bot"));
        listjfCommands.add(new BotCommand("/settings","set your preferences"));
        try {
            this.execute(new SetMyCommands(listjfCommands,new BotCommandScopeDefault(),null));
        }
catch (TelegramApiException e){
            log.error("Error settings bot commans list:" + e.getMessage());
}
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText){
                case "/start":
                    try {
                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "/help":

                    sendMessage(chatId,HELP_TEXT);
                    break;
                case "/register":

                    register(chatId);
                    break;
                default:
                    sendMessage(chatId, "Command was not recognized");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("YES_BUTTON")){
                String text = "You pressed YES buttom";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int)messageId);
                try {
                    execute(message);
                }
                catch (TelegramApiException e){
                    log.error("Error occured:" + e.getMessage());
                }

            }
            else if (callbackData.equals("NO_BUTTON")) {
                String text = "You pressed NO buttom";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int)messageId);
                try {
                    execute(message);
                }
                catch (TelegramApiException e){
                    log.error("Error occured:" + e.getMessage());
                }

            }
        }
    }

    private void register(long chatId) {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Do you now");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var yesButton = new InlineKeyboardButton();

        yesButton.setText("Yes");
        yesButton.setCallbackData("YES_BUTTON");

        var noButton = new InlineKeyboardButton();

        noButton.setText("No");
        noButton.setCallbackData("NO_BUTTON");

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);

        message.setReplyMarkup(markupInLine);
        try {
            execute(message);
        }
        catch (TelegramApiException e){
            log.error("Error occured:" + e.getMessage());
        }


    }

    private void prepearAndSendMessage(long chatId, String helpText) {
    }

    private void startCommandReceived(long chatId, String name) throws TelegramApiException{


        String answer = " Hi, " + name +", nice to meet you!";
        log.info("Replied to user" + name);


        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        row.add("weather");
        row.add("joke");

        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("register");
        row.add("check_my_data");
        row.add("delete my data");

        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        try {
          execute(message);
        }
        catch (TelegramApiException e){
log.error("Error occured:" + e.getMessage());
        }
    }

}


// Я дальше уже не знаю что делать,я уже все обшарил в интернете.
//
// Я очень хочу пройти у вас стажировку,я готов обучаться всему,спасибо за внимание

