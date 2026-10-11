package jbro.cobblemon.simplemyroom.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import jbro.cobblemon.simplemyroom.room.ActiveRoomService;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin {
    @Shadow
    @Final
    ServerLevel level;

    // Vanilla 1.21.1 only random-ticks chunks with a player nearby; recently visited active rooms keep growing crops.
    @WrapOperation(
        method = "tickChunks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ChunkMap;anyPlayerCloseEnoughForSpawning(Lnet/minecraft/world/level/ChunkPos;)Z"
        )
    )
    private boolean simpleMyRoom$tickRecentlyVisitedActiveRooms(ChunkMap chunkMap, ChunkPos chunk, Operation<Boolean> original) {
        return original.call(chunkMap, chunk) || ActiveRoomService.ticksWithoutPlayers(level, chunk);
    }
}
