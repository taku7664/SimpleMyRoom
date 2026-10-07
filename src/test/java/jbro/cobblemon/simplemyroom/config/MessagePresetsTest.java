package jbro.cobblemon.simplemyroom.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class MessagePresetsTest {
    @Test
    void presetsCoverEveryMessageAndPreserveFormattingArguments() throws Exception {
        JsonObject defaults = new Gson().toJsonTree(new SimpleMyRoomMessages()).getAsJsonObject();
        for (String language : new String[] { "en_us", "ko_kr" }) {
            JsonObject preset = JsonParser.parseString(Files.readString(
                Path.of("examples", "simple-myroom-messages." + language + ".json"))).getAsJsonObject();
            assertEquals(defaults.keySet(), preset.keySet(), language);
            for (String key : defaults.keySet()) {
                String value = preset.get(key).getAsString();
                assertFalse(value.isBlank(), language + ": " + key);
                assertEquals(arguments(defaults.get(key).getAsString()), arguments(value), language + ": " + key);
            }
            assertTrue(preset.has("helpEnter"));
            String help = SimpleMyRoomMessages.format(preset.get("helpEnter").getAsString(), Map.of("root", "myroom"));
            assertTrue(help.startsWith("/myroom enter "));
            assertFalse(help.contains("{root}"));
        }
    }

    private static Set<String> arguments(String text) {
        Set<String> result = new HashSet<>();
        var matcher = Pattern.compile("\\{[a-zA-Z]+\\}").matcher(text);
        while (matcher.find()) result.add(matcher.group());
        return result;
    }
}
