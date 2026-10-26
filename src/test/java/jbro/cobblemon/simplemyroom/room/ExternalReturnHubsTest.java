package jbro.cobblemon.simplemyroom.room;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.function.Supplier;
import jbro.cobblemon.simplemyroom.config.SimpleMyRoomConfig;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ExternalReturnHubsTest {
    private static final ResourceLocation OUTSIDE = ResourceLocation.parse("minecraft:overworld");
    private static final ResourceLocation HUB = ResourceLocation.parse("integration:hub");
    private static final ResourceLocation TEMPORARY = ResourceLocation.parse("integration:temporary");

    @AfterEach
    void clearProviders() {
        ExternalReturnHubs.load(List.of());
    }

    @Test
    void temporaryTripKeepsTheOriginalPointUntilThePlayerReturnsOutside() {
        ExternalReturnHubs.load(List.<Supplier<?>>of(() -> List.of(HUB.toString(), TEMPORARY.toString())));
        assertTrue(ReturnDimensionPolicy.shouldSaveOnEntry(OUTSIDE, false, List.of()));
        assertFalse(ReturnDimensionPolicy.isStale(HUB, List.of()));
        assertFalse(ReturnDimensionPolicy.isStale(TEMPORARY, List.of()));
        assertFalse(ReturnDimensionPolicy.shouldSaveOnEntry(TEMPORARY, true, List.of()));
        assertFalse(ReturnDimensionPolicy.isStale(RoomDimensions.ID, List.of()));
        assertTrue(ReturnDimensionPolicy.isStale(OUTSIDE, List.of()));
        assertTrue(ReturnDimensionPolicy.shouldSaveOnEntry(OUTSIDE, true, List.of()));
    }

    @Test
    void configurationChangesDoNotRemoveIntegrationHubs() {
        ExternalReturnHubs.load(List.<Supplier<?>>of(() -> List.of(TEMPORARY.toString())));
        assertFalse(ReturnDimensionPolicy.isStale(HUB, List.of(HUB.toString())));
        assertTrue(ReturnDimensionPolicy.isStale(HUB, List.of()));
        assertFalse(ReturnDimensionPolicy.isStale(TEMPORARY, List.of()));
        assertTrue(new SimpleMyRoomConfig().returnBehavior.otherHubDimensions.isEmpty());
    }

    @Test
    void noIntegrationLeavesStandaloneBehaviorUnchanged() {
        ExternalReturnHubs.load(List.of());
        assertTrue(ReturnDimensionPolicy.isStale(TEMPORARY, List.of()));
    }

    @Test
    void invalidProviderDoesNotReplaceThePreviousRules() {
        ExternalReturnHubs.load(List.<Supplier<?>>of(() -> List.of(TEMPORARY.toString())));
        assertThrows(IllegalArgumentException.class,
            () -> ExternalReturnHubs.load(List.<Supplier<?>>of(() -> List.of("Invalid dimension"))));
        assertThrows(IllegalArgumentException.class,
            () -> ExternalReturnHubs.load(List.<Supplier<?>>of(() -> "not a collection")));
        assertThrows(IllegalArgumentException.class,
            () -> ExternalReturnHubs.load(List.<Supplier<?>>of(() -> List.of(123))));
        assertFalse(ReturnDimensionPolicy.isStale(TEMPORARY, List.of()));
    }
}
