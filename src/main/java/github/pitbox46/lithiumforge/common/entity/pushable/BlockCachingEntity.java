package github.pitbox46.lithiumforge.common.entity.pushable;


import net.minecraft.world.level.block.state.BlockState;

public interface BlockCachingEntity {

    default void lithiumOnBlockCacheDeleted() {

    }

    default void lithiumOnBlockCacheSet(BlockState newState) {

    }

    default void lithiumSetClimbingMobCachingSectionUpdateBehavior(boolean listening) {
        throw new UnsupportedOperationException();
    }

    BlockState lithiumForge$getCachedFeetBlockState();
}