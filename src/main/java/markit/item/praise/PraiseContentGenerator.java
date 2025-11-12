package markit.item.praise;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PraiseContentGenerator {

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages", Locale.KOREA);

    public static String generateContentFromTopic(List<String> topicNames) {

        String defaultPraise = BUNDLE.getString(PraiseMessage.DEFAULT.getKey());
        String prefix = BUNDLE.getString(PraiseMessage.PREFIX.getKey());
        String andSeparator = BUNDLE.getString(PraiseMessage.SEPARATOR.getKey());

        if (topicNames == null || topicNames.isEmpty()) {
            return defaultPraise;
        }

        if (topicNames.size() == 1) {
            return prefix + topicNames.get(0) + " . ";
        }

        String result = String.join(", ", topicNames.subList(0, topicNames.size() - 1))
                + andSeparator + topicNames.get(topicNames.size() - 1);
        return prefix + result + " . ";
    }
}