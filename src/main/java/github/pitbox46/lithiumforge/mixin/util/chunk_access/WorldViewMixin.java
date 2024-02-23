package github.pitbox46.lithiumforge.mixin.util.chunk_access;

import github.pitbox46.lithiumforge.common.world.ChunkView;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelReader.class)
public interface WorldViewMixin extends ChunkView {

    @Shadow
    @Nullable ChunkAccess getChunk(int var1, int var2, ChunkStatus var3, boolean var4);

    @Override
    default @Nullable ChunkAccess getLoadedChunk(int chunkX, int chunkZ) {
        return this.getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
    }
}
