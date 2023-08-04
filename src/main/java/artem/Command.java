package artem;

import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

import java.util.Optional;

@FunctionalInterface
public interface Command {
    Mono<?> execute(Optional<User> author, MessageChannel channel, String[] arguments);
}
