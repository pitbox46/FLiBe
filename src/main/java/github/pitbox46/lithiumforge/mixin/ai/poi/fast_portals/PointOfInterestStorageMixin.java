package github.pitbox46.lithiumforge.mixin.ai.poi.fast_portals;

import com.mojang.datafixers.DataFixer;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiSection;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.storage.SectionStorage;
import org.spongepowered.asm.mixin.*;

import java.nio.file.Path;
import java.util.Optional;

@Mixin(PoiManager.class)
public abstract class PointOfInterestStorageMixin extends SectionStorage<PoiSection> {

    @Shadow
    @Final
    private LongSet loadedChunks;

    @Unique
    private final LongSet preloadedCenterChunks = new LongOpenHashSet();
    @Unique
    private int preloadRadius = 0;

    public PointOfInterestStorageMixin(
            Path path, DataFixer dataFixer, boolean dsync,
            RegistryAccess registryManager, LevelHeightAccessor world
    ) {
        super(
                path, PoiSection::codec, PoiSection::new,
                dataFixer, DataFixTypes.POI_CHUNK, dsync, registryManager, world
        );
    }

    /**
     * @author Crec0, 2No2Name
     * @reason Streams in this method cause unnecessary lag. Simply rewriting this to not use streams, we gain
     * considerable performance. Noticeable when large amount of entities are traveling through nether portals.
     * Furthermore, caching whether all surrounding chunks are loaded is more efficient than caching the state
     * of single chunks only.
     */
    @Overwrite
    public void ensureLoadedAndValid(LevelReader worldView, BlockPos pos, int radius) {
        if (this.preloadRadius != radius) {
            //Usually there is only one preload radius per PointOfInterestStorage. Just in case another mod adjusts it dynamically, we avoid
            //assuming its value.
            this.preloadedCenterChunks.clear();
            this.preloadRadius = radius;
        }
        long chunkPos = ChunkPos.asLong(pos);
        if (this.preloadedCenterChunks.contains(chunkPos)) {
            return;
        }
        int chunkX = SectionPos.blockToSectionCoord(pos.getX());
        int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());

        int chunkRadius = Math.floorDiv(radius, 16);
        int maxHeight = this.levelHeightAccessor.getMaxSection() - 1;
        int minHeight = this.levelHeightAccessor.getMinSection();

        for (int x = chunkX - chunkRadius, xMax = chunkX + chunkRadius; x <= xMax; x++) {
            for (int z = chunkZ - chunkRadius, zMax = chunkZ + chunkRadius; z <= zMax; z++) {
                lithium$preloadChunkIfAnySubChunkContainsPOI(worldView, x, z, minHeight, maxHeight);
            }
        }
        this.preloadedCenterChunks.add(chunkPos);
    }

    @Unique
    private void lithium$preloadChunkIfAnySubChunkContainsPOI(LevelReader worldView, int x, int z, int minSubChunk, int maxSubChunk) {
        ChunkPos chunkPos = new ChunkPos(x, z);
        long longChunkPos = chunkPos.toLong();

        if (this.loadedChunks.contains(longChunkPos)) return;

        for (int y = minSubChunk; y <= maxSubChunk; y++) {
            Optional<PoiSection> section = this.get(SectionPos.asLong(x, y, z));
            if (section != null && section.isPresent()) {
                boolean result = section.get().isValid();
                if (result) {
                    if (this.loadedChunks.add(longChunkPos)) {
                        worldView.getChunk(x, z, ChunkStatus.EMPTY);
                    }
                    break;
                }
            }
        }
    }
}
