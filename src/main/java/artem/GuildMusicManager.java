package artem;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import discord4j.core.object.entity.Guild;

public class GuildMusicManager {
    public final AudioPlayer player;
    private final Guild guild;
    public GuildMusicManager(AudioPlayerManager manager, Guild guild)
    {
        player = manager.createPlayer();
        this.guild = guild;
    }
}
