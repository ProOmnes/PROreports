package net.proomnes.proreports.util.messages;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;

import java.util.HashMap;
import java.util.Map;

public class MessageLoader {

    private final Map<String, String> cachedMessages = new HashMap<>();

    public MessageLoader(final PluginBase pluginBase) {
        pluginBase.saveResource("/lang/" + pluginBase.getConfig().getString("settings.lang") + ".yml");
        pluginBase.saveResource("/lang/en-us.yml");
        pluginBase.saveResource("/lang/de-de.yml");

        final Config config = new Config(pluginBase.getDataFolder() + "/lang/" + pluginBase.getConfig().getString("settings.lang") + ".yml");
        config.getAll().forEach((key, value) -> {
            if (value instanceof String) {
                this.cachedMessages.put(key, (String) value);
            }
        });
    }

    public String get(final MessageKeys messageKeys, final Object... toReplace) {
        String message = this.cachedMessages.getOrDefault(messageKeys.getKey(), messageKeys.getDefaultMessage());

        if (messageKeys.isPrefix()) {
            message = this.cachedMessages.get("system.prefix") + message;
        }

        int i = 0;
        for (final Object replacement : toReplace) {
            message = message.replace("{" + i + "}", String.valueOf(replacement));
            i++;
        }

        message = message.replace("&", "§");

        return message;
    }

}
