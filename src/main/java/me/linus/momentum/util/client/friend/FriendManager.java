package me.linus.momentum.util.client.friend;

import me.linus.momentum.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 11/29/2020
 */

public class FriendManager {

    public static List<Friend> friends;

    public FriendManager() {
        friends = new ArrayList<>();
    }

    public static List<Friend> getFriends() {
        return friends;
    }

    public static boolean isFriend(String name) {
        boolean b = false;
        for (Friend f : getFriends()) {
            if (f.getName().equalsIgnoreCase(name))
                b = true;
        }

        return b;
    }

    public static Friend getFriendByName(String name) {
        Friend fr = null;
        for (Friend f : getFriends()) {
            if (f.getName().equalsIgnoreCase(name))
                fr = f;
        }

        return fr;
    }

    public static boolean isFriendModuleEnabled() {
        return ModuleManager.getModuleByName("Friends").isEnabled();
    }

    public static void addFriend(String name) {
        friends.add(new Friend(name));
    }

    public static void removeFriend(String name) {
        friends.remove(getFriendByName(name));
    }
}
