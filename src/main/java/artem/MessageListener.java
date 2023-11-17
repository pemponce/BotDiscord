package artem;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.context.ConfigurableApplicationContext;
import reactor.core.publisher.Mono;

public abstract class MessageListener {

    public Mono<Void> processCommand(Message eventMessage) {

        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith("!"))
                .flatMap(message -> {

                    ConfigurableApplicationContext context = BotApl.getContext();

                    MessageChannel channel = message.getChannel().block();
                    assert channel != null;

                    String[] commandComponents = message.getContent().split(" ");
                    String commandLabel = commandComponents[0];

                    if (!context.containsBean(commandLabel))
                        return channel.createMessage("Command not found!");

                    String[] arguments = new String[commandComponents.length - 1];
                    System.arraycopy(commandComponents, 1, arguments, 0, arguments.length);

                    Command commandBean = context.getBean(commandLabel, Command.class);
                    return commandBean.execute(message.getAuthor(), channel, arguments);
                })
                .then();
    }
}