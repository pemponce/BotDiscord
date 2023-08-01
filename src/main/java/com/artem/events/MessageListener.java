package com.artem.events;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import discord4j.core.object.entity.User;


public abstract class MessageListener {

    public Mono<Void> processCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .doOnNext(message -> {
                    System.out.println("Получено сообщение: " + message.getContent());
                    System.out.println("Автор: " + message.getAuthor().map(User::getUsername).orElse("Неизвестно"));
                })
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!todo"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Планы на сегодня:\n" +
                        " - написать бота\n" +
                        " - пообедать\n" +
                        " - поиграть в игру\n" +
                        "@mononoawaree "))
                .doOnNext(response -> System.out.println("Ответ: " + response))
                .then();
    }
}