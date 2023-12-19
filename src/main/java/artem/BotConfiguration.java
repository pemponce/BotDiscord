package artem;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class BotConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BotConfiguration.class);
    private final YouTubeIntegration youTubeIntegration;
    @Value("${token}")
    private String token;

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        GatewayDiscordClient client = null;

        try {
            client = DiscordClientBuilder.create(token)
                    .build()
                    .login()
                    .block();

            log.info("Subscribing...");

            for (EventListener<T> listener : eventListeners) {
                assert client != null;

                client.on(listener.getEventType())
                        .flatMap(listener::execute)
                        .onErrorResume(listener::handleError)
                        .subscribe();

                log.info("Subscribed: " + listener.getClass().getSimpleName());
            }
        } catch (Exception exception) {
            log.error("Be sure to use a valid bot token!", exception);
        }

        return client;
    }

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        return playerManager;
    }

    @Bean(name = "!p")
    public Command todoCommand() {
        return (Optional<User> author, MessageChannel channel, String[] arguments) -> channel.createMessage("executed !todo command");
    }

    @Bean(name = "!s")
    public Command searchCommand() {
        return (Optional<User> author, MessageChannel channel, String[] arguments) -> {
            if (arguments.length == 0) {
                return channel.createMessage("Command usage: !s <text>");
            }

            String query = String.join(" ", arguments);
            String videoUrl = youTubeIntegration.searchVideo(query);


            return channel.createMessage("Video URL: " + videoUrl);
        };
    }

    @Bean(name = "!q")
    public Command joinVoiceCommand() {
        return (Optional<User> author, MessageChannel channel, String[] arguments) -> {



            return channel.createMessage("asfwr");
        };
    }
}