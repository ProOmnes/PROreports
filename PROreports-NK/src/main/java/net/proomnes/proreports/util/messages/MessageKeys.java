package net.proomnes.proreports.util.messages;

import lombok.Getter;

@Getter
public enum MessageKeys {

    /*
      SYSTEM MESSAGES
     */
    SYSTEM_PREFIX("system.prefix", "§8» §cReports §8| §7", false)



    ;

    private final String key;
    private final String defaultMessage;
    private final boolean prefix;

    MessageKeys(final String key, final String defaultMessage, final boolean prefix) {
        this.key = key;
        this.defaultMessage = defaultMessage;
        this.prefix = prefix;
    }

}
