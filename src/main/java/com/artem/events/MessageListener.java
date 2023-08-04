package com.artem.events;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import reactor.core.publisher.Mono;

import discord4j.core.object.entity.User;


public abstract class MessageListener {

    public Mono<Void> processCommand(Message eventMessage) {

        return Mono.just(eventMessage).doOnNext(message -> {
            System.out.println("Получено сообщение: " + message.getContent());
            System.out.println("Автор: " + message.getAuthor().map(User::getUsername).orElse("Неизвестно"));
        }).filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false)).flatMap(message -> {
            if (message.getContent().equalsIgnoreCase("!todo")) {
                return message.getChannel().flatMap(channel -> channel.createMessage("Планы на сегодня:\n" +
                        " - написать бота\n" +
                        " - подрочить\n" +
                        " - поспать\n" +
                        "@mononoawaree")).doOnNext(response -> System.out.println("Ответ: " + response)).then();
            } else if (message.getContent().equalsIgnoreCase("!help")) {
                return message.getChannel().flatMap(channel -> channel.
                                createMessage("чтобы запустить ботика напишите !p название песни"))
                        .doOnNext(response -> System.out.println("Ответ: " + response)).then();
            } else if (message.getContent().startsWith("!p ")) {
                String songName = message.getContent().substring(3);

                return (message.getChannel().flatMap(channel -> channel.createMessage("!p " + songName)))
                        .doOnNext(response -> System.out.println("Ответ: " + response))
                        .then();
            } else {
                return Mono.empty(); // Если ни одна из команд не совпала, возвращаем пустой Mono
            }
        });
    }
}