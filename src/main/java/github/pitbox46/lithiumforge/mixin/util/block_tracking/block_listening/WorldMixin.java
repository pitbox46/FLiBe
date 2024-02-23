package github.pitbox46.lithiumforge.mixin.util.block_tracking.block_listening;

import github.pitbox46.lithiumforge.common.entity.block_tracking.SectionedBlockChangeTracker;
import github.pitbox46.lithiumforge.common.util.deduplication.LithiumInterner;
import github.pitbox46.lithiumforge.common.util.deduplication.LithiumInternerWrapper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class WorldMixin implements LithiumInternerWrapper<SectionedBlockChangeTracker> {
    private final LithiumInterner<SectionedBlockChangeTracker> blockChangeTrackers = new LithiumInterner<>();

    @Override
    public SectionedBlockChangeTracker getCanonical(SectionedBlockChangeTracker value) {
        return this.blockChangeTrackers.getCanonical(value);
    }

    @Override
    public void deleteCanonical(SectionedBlockChangeTracker value) {
        this.blockChangeTrackers.deleteCanonical(value);
    }
}
