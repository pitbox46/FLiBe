package github.pitbox46.lithiumforge.mixin.ai.pathing;

import github.pitbox46.lithiumforge.common.ai.pathing.BlockStatePathingCache;
import github.pitbox46.lithiumforge.common.world.blockview.SingleBlockBlockView;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin implements BlockStatePathingCache {
    @Shadow protected abstract BlockState asState();

    private BlockPathTypes pathNodeType = null;
    private BlockPathTypes pathNodeTypeNeighbor = null;

    @Inject(method = "initCache", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        // Reset the cached path node types, to ensure they are re-calculated.
        this.pathNodeType = null;
        this.pathNodeTypeNeighbor = null;

        BlockState state = this.asState();

        SingleBlockBlockView blockView = SingleBlockBlockView.of(state, BlockPos.ZERO);
        try {
            this.pathNodeType = Validate.notNull(WalkNodeEvaluator.getBlockPathTypeRaw(blockView, BlockPos.ZERO));
        } catch (SingleBlockBlockView.SingleBlockViewException | ClassCastException e) {
            //This is usually hit by shulker boxes, as their hitbox depends on the block entity, and the node type depends on the hitbox
            //Also catch ClassCastException in case some modded code casts BlockView to ChunkCache
            this.pathNodeType = null;
        }
        try {
            //Passing null as previous node type to the method signals to other lithium mixins that we only want the neighbor behavior of this block and not its neighbors
            this.pathNodeTypeNeighbor = (WalkNodeEvaluator.checkNeighbourBlocks(blockView, BlockPos.ZERO.mutable(), null));
            if (this.pathNodeTypeNeighbor == null) {
                this.pathNodeTypeNeighbor = BlockPathTypes.OPEN;
            }
        } catch (SingleBlockBlockView.SingleBlockViewException | ClassCastException e) {
            this.pathNodeTypeNeighbor = null;
        }
    }

    @Override
    public BlockPathTypes getPathNodeType() {
        return this.pathNodeType;
    }

    @Override
    public BlockPathTypes getNeighborPathNodeType() {
        return this.pathNodeTypeNeighbor;
    }
}
