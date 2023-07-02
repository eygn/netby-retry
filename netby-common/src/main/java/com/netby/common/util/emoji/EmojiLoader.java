package com.netby.common.util.emoji;


import com.netby.common.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Loads the emojis from a JSON database.
 *
 * @author: byg
 */
public class EmojiLoader {

    /**
     * No need for a constructor, all the methods are static.
     */
    private EmojiLoader() {
    }

    /**
     * Loads a JSONArray of emojis from an InputStream, parses it and returns the
     * associated list of {@link Emoji}s
     *
     * @param stream the stream of the JSONArray
     * @return the list of {@link Emoji}s
     * @throws IOException if an error occurs while reading the stream or parsing
     *                     the JSONArray
     */
    @SuppressWarnings("rawtypes")
    public static List<Emoji> loadEmojis(InputStream stream) throws IOException {
        List<Map> list = JsonUtil.readAsList(inputStreamToString(stream), Map.class);
        List<Emoji> emojis = new ArrayList<>(list.size());
        for (Map map : list) {
            Emoji emoji = buildEmojiFromJson(map);
            if (emoji != null) {
                emojis.add(emoji);
            }
        }
        return emojis;
    }

    private static String inputStreamToString(
        InputStream stream
    ) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(stream, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String read;
        while ((read = br.readLine()) != null) {
            sb.append(read);
        }
        br.close();
        return sb.toString();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static Emoji buildEmojiFromJson(
        Map map
    ) {
        if (!map.containsKey("emoji")) {
            return null;
        }

        byte[] bytes = map.get("emoji").toString().getBytes(StandardCharsets.UTF_8);
        String description = null;
        if (map.containsKey("description")) {
            description = map.get("description").toString();
        }
        boolean supportsFitzpatrick = false;
        if (map.containsKey("supports_fitzpatrick")) {
            supportsFitzpatrick = "true".equals(map.get("supports_fitzpatrick"));
        }
        List<String> aliases, tags;

        if (map.containsKey("aliases")) {
            aliases = (List<String>) map.get("aliases");
        } else {
            aliases = Collections.emptyList();
        }
        if (map.containsKey("tags")) {
            tags = (List<String>) map.get("tags");
        } else {
            tags = Collections.emptyList();
        }
        return new Emoji(description, supportsFitzpatrick, aliases, tags, bytes);
    }
}
