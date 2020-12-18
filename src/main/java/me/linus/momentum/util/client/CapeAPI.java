package me.linus.momentum.util.client;

import me.linus.momentum.mixin.MixinInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class CapeAPI implements MixinInterface {
    List<UUID> uuids = new ArrayList<>();

    public CapeAPI() {
        try {
            URL cache = new URL("https://raw.githubusercontent.com/linustouchtips/momentum-resources/master/capes.json");
            BufferedReader in = new BufferedReader(new InputStreamReader(cache.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                uuids.add(UUID.fromString(inputLine));
        } catch(Exception e) {

        }
    }

    public boolean hasCape(UUID id) {
        return uuids.contains(id);
    }
}
