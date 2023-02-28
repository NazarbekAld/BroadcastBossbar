package me.nazarxexe.broadcast.bossbar;

import cn.nukkit.utils.BossBarColor;
import lombok.Getter;
import me.nazarxexe.survival.core.bossbar.DynamicBossbar;
import me.nazarxexe.survival.core.tools.text.TextComponent;

public class BroadcastSession {

    private TextComponent textComponent;
    private @Getter int interval;
    private @Getter int repeat;
    private BBroadcast plugin;

    private DynamicBossbar current;

    public BroadcastSession(BBroadcast plugin, TextComponent textComponent, int interval, int repeat) {
        this.textComponent = textComponent;
        this.interval = interval;
        this.repeat = repeat;
        this.plugin = plugin;
    }

    public void start() {
        current =  new DynamicBossbar(
                plugin,
                textComponent,
                BossBarColor.PURPLE
        );
        plugin.getServer().getOnlinePlayers().forEach(((uuid, player) -> {
            current.show(player);
        }));
        current.decreasingTask(interval, (repeat/100f));
    }

    public void stop() {
        current.getBossbarDummy().forEach(((player, dummyBossBar) -> {
            current.hide(player);
        }));
    }




}
