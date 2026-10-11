package jbro.cobblemon.simplemyroom.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RecentVisitWindowTest {
    private static final UUID OWNER = new UUID(0, 1);
    private static final UUID OTHER = new UUID(0, 2);

    @Test
    void roomStaysRecentUntilTheWindowEnds() {
        RecentVisitWindow window = new RecentVisitWindow();
        window.markVisited(OWNER, 100);

        assertTrue(window.isRecent(OWNER, 100, 12000));
        assertTrue(window.isRecent(OWNER, 12100, 12000));
        assertFalse(window.isRecent(OWNER, 12101, 12000));
        assertFalse(window.isRecent(OTHER, 100, 12000));
    }

    @Test
    void zeroWindowDisablesRecentRooms() {
        RecentVisitWindow window = new RecentVisitWindow();
        window.markVisited(OWNER, 100);

        assertFalse(window.isRecent(OWNER, 100, 0));
    }

    @Test
    void laterVisitRestartsTheWindowAndExpiredRoomsAreForgotten() {
        RecentVisitWindow window = new RecentVisitWindow();
        window.markVisited(OWNER, 0);
        window.markVisited(OTHER, 0);
        window.markVisited(OWNER, 10000);

        assertTrue(window.isRecent(OWNER, 20000, 12000));
        window.forgetExpired(20000, 12000);
        assertEquals(Set.of(OWNER), window.rooms());
    }
}
