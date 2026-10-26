package jbro.cobblemon.simplemyroom.room;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

/** Optional integrations contribute dimension IDs without making the room mod depend on them. */
public final class ExternalReturnHubs {
    public static final String ENTRYPOINT = "simple_myroom:other_hub_dimensions";
    private static Set<String> dimensions = Set.of();

    private ExternalReturnHubs() {
    }

    /** Called after all mod initializers, before any players can change dimensions. */
    public static void loadProviders() {
        load(FabricLoader.getInstance().getEntrypoints(ENTRYPOINT, Supplier.class).stream()
            .map(provider -> (Supplier<?>) provider).toList());
        jbro.cobblemon.simplemyroom.SimpleMyRoom.LOGGER.info("Loaded {} external return hub dimension(s).", dimensions.size());
    }

    static void load(Iterable<? extends Supplier<?>> providers) {
        Set<String> contributed = new LinkedHashSet<>();
        for (Supplier<?> provider : providers) {
            if (!(provider.get() instanceof Collection<?> values)) {
                throw new IllegalArgumentException(ENTRYPOINT + " providers must supply a collection of dimension IDs");
            }
            for (Object value : values) {
                ResourceLocation dimension = value instanceof String id ? ResourceLocation.tryParse(id) : null;
                if (dimension == null) {
                    throw new IllegalArgumentException(ENTRYPOINT + " provider supplied an invalid dimension ID: " + value);
                }
                contributed.add(dimension.toString());
            }
        }
        dimensions = Set.copyOf(contributed);
    }

    static boolean contains(ResourceLocation dimension) {
        return dimensions.contains(dimension.toString());
    }
}
