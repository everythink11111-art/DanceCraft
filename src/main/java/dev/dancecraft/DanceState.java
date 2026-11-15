package dev.dancecraft;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class DanceState {
    private static final Map<UUID, Long> DANCERS = new ConcurrentHashMap<>();
    private static long clientTick;

    private DanceState() {}

    public static boolean toggle(UUID playerId) {
        if (DANCERS.remove(playerId) != null) return false;
        DANCERS.put(playerId, clientTick);
        return true;
    }

    public static boolean isDancing(UUID playerId) {
        return DANCERS.containsKey(playerId);
    }

    public static float danceTime(UUID playerId) {
        Long started = DANCERS.get(playerId);
        return started == null ? 0.0f : (clientTick - started) / 20.0f;
    }

    public static void tick() {
        clientTick++;
    }
}
