package jbro.cobblemon.simplemyroom.room;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Remembers when each room last had a player inside, so random ticks can keep going for a while after they leave. */
public final class RecentVisitWindow {
    private final Map<UUID, Long> lastVisitTicks = new HashMap<>();

    public void markVisited(UUID ownerId, long nowTick) {
        lastVisitTicks.put(ownerId, nowTick);
    }

    public boolean isRecent(UUID ownerId, long nowTick, long windowTicks) {
        Long lastVisit = lastVisitTicks.get(ownerId);
        return windowTicks > 0 && lastVisit != null && nowTick - lastVisit <= windowTicks;
    }

    public void forgetExpired(long nowTick, long windowTicks) {
        lastVisitTicks.values().removeIf(lastVisit -> nowTick - lastVisit > windowTicks);
    }

    public void forget(UUID ownerId) {
        lastVisitTicks.remove(ownerId);
    }

    public void clear() {
        lastVisitTicks.clear();
    }

    Set<UUID> rooms() {
        return Set.copyOf(lastVisitTicks.keySet());
    }
}
