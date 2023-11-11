package artem;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import discord4j.core.object.entity.User;


public abstract class MessageListener {

    public Mono<Void> processSearchCommand(Message eventMessage) {
        return Mono.just(eventMessage)
                .doOnNext(message -> {
                    System.out.println("Получено сообщение: " + message.getContent());
                    System.out.println("Автор: " + message.getAuthor().map(User::getUsername).orElse("Неизвестно"));
                })
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith("!s"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> {
                    // Обработка команды !s
                    String query = eventMessage.getContent().substring(3); // Удаляем "!s " из текста команды
                    String searchResult = YouTubeIntegration.searchVideo(query);
                    return channel.createMessage("Результат поиска: " + searchResult);
                })
                .doOnNext(response -> System.out.println("Ответ: " + response))
                .then();
    }

    public Mono<Void> processCommand(Message eventMessage) {
        String content = eventMessage.getContent();
        if (content.startsWith("!todo")) {
            return processTodoCommand(eventMessage);
        } else if (content.startsWith("!s")) {
            return processSearchCommand(eventMessage);
        }
        // Добавьте обработку других команд, если необходимо
        return Mono.empty();
    }

    private Mono<Void> processTodoCommand(Message eventMessage) {
        // Обработка команды !todo
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
                        " - поиграть в игру\n"))
                .doOnNext(response -> System.out.println("Ответ: " + response))
                .then();
    }

}