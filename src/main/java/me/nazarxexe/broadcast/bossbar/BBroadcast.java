package me.nazarxexe.broadcast.bossbar;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import lombok.Getter;
import me.nazarxexe.survival.core.tools.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * You should not use this class!!
 *
 */
public class BBroadcast extends PluginBase {

    private Config config;
    private ConfigSection broadcastSection;
    private @Getter List<BroadcastSession> sessions;
    private @Getter Plugin plugin;

    @Override
    public void onEnable() {

        this.plugin = this;

        saveDefaultConfig();

        config = new Config(
                (this.getDataFolder().getAbsolutePath() + "config.yml"),
                Config.YAML
        );
        sessions = new ArrayList<>();
        broadcastSection = config.getSection("Broadcast");

        broadcastSection.getKeys()
                .forEach((key) -> {

                    ConfigSection s = broadcastSection.getSection(key);

                    TextComponent component = new TextComponent();
                    component.add_RULE_AutoColor();

                    sessions.add(new BroadcastSession(
                            this,
                            component.setText(config.getString("text")),
                            s.getInt("interval"),
                            s.getInt("repeat")
                    ));

                    this.getLogger().info(new TextComponent()
                            .add_RULE_AutoColor()
                                    .combine(key)
                                    .combine(" &aOK.")
                            .getText());
                });
        int delay = 0;
        for (BroadcastSession b : sessions) {
            delay += (b.getRepeat() * b.getRepeat());
        }

        TaskHandler repeat = this.getServer().getScheduler().scheduleDelayedRepeatingTask(
                new Task() {
                    @Override
                    public void onRun(int i) {

                        int broadcastDelay = 0;

                        for (BroadcastSession s : getSessions()){

                            getPlugin().getServer().getScheduler().scheduleDelayedTask(new Task() {
                                @Override
                                public void onRun(int i) {

                                    s.start();

                                }
                            },
                            broadcastDelay
                            );
                            broadcastDelay += (s.getInterval()*s.getRepeat());
                        }

                    }
                },
                0,
                delay
        );
    }



}
