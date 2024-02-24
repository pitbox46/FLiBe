package github.pitbox46.lithiumforge.mixin.ai.nearby_entity_tracking;

import github.pitbox46.lithiumforge.common.entity.nearby_tracker.NearbyEntityListenerMulti;
import github.pitbox46.lithiumforge.common.entity.nearby_tracker.NearbyEntityListenerProvider;
import github.pitbox46.lithiumforge.common.util.tuples.Range6Int;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PersistentEntitySectionManager.Callback.class)
public class ServerEntityManagerListenerMixin<T extends EntityAccess> {
    @Shadow
    @Final
    private T entity;

    @Shadow
    private long currentSectionKey;

    @Shadow
    @Final
    PersistentEntitySectionManager<T> this$0;

    @Inject(
            method = "onMove",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/entity/EntitySection;add(Lnet/minecraft/world/level/entity/EntityAccess;)V",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onUpdateEntityPosition(CallbackInfo ci, BlockPos blockPos, long newPos, Visibility entityTrackingStatus, EntitySection<T> entityTrackingSection) {
        NearbyEntityListenerMulti listener = ((NearbyEntityListenerProvider) this.entity).getListener();
        if (listener != null)
        {
            Range6Int chunkRange = listener.getChunkRange();
            listener.updateChunkRegistrations(
                    this$0.sectionStorage,
                    SectionPos.of(this.currentSectionKey), chunkRange,
                    SectionPos.of(newPos), chunkRange
            );
        }
    }

    @Inject(
            method = "onRemove",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onRemoveEntity(Entity.RemovalReason reason, CallbackInfo ci) {
        NearbyEntityListenerMulti listener = ((NearbyEntityListenerProvider) this.entity).getListener();
        if (listener != null) {
            listener.removeFromAllChunksInRange(
                    this.this$0.sectionStorage,
                    SectionPos.of(this.currentSectionKey)
            );
        }
    }
}
