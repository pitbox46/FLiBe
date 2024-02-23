package github.pitbox46.lithiumforge.mixin.entity.collisions.unpushable_cramming;

import github.pitbox46.lithiumforge.common.entity.pushable.BlockCachingEntity;
import github.pitbox46.lithiumforge.common.entity.pushable.EntityPushablePredicate;
import github.pitbox46.lithiumforge.common.entity.pushable.PushableEntityClassGroup;
import github.pitbox46.lithiumforge.common.util.collections.ReferenceMaskedList;
import github.pitbox46.lithiumforge.common.world.ClimbingMobCachingSection;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntitySection;
import net.minecraft.world.level.entity.Visibility;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Iterator;

@Mixin(EntitySection.class)
public abstract class EntitySectionMixin<T extends EntityAccess> implements ClimbingMobCachingSection {
    @Shadow
    @Final
    public ClassInstanceMultiMap<T> storage;
    @Shadow
    private Visibility chunkStatus;

    /**
     * Contains entities that are pushable under some conditions. Entities that are cached to be inside a climbable block
     * and therefore cannot be pushed (only applied to some entity types) are hidden by the mask until the cache is cleared.
     */
    @Unique
    private ReferenceMaskedList<Entity> lithiumForge$pushableEntities;

    @Override
    public AbortableIterationConsumer.Continuation lithiumForge$collectPushableEntities(Level world, Entity except, AABB box, EntityPushablePredicate<? super Entity> entityPushablePredicate, ArrayList<Entity> entities) {
        Iterator<?> entityIterator;
        if (this.lithiumForge$pushableEntities!= null) {
            entityIterator = this.lithiumForge$pushableEntities.iterator();
        } else {
            entityIterator = this.storage.iterator();
        }
        int i = 0;
        int j = 0;
        while (entityIterator.hasNext()) {
            Entity entity = (Entity) entityIterator.next();
            if (entity.getBoundingBox().intersects(box) && !entity.isSpectator() && entity != except && !(entity instanceof EnderDragon)) {
                i++;
                if (entityPushablePredicate.test(entity)) { //This predicate has side effects, might cause BlockCachingEntity to cache block and update its visibility
                    j++;
                    //skip the dragon piece check due to dragon pieces always being non pushable
                    entities.add(entity);
                }
            }
        }
        if (this.lithiumForge$pushableEntities== null && i >= 25 && i >= (j * 2)) {
            this.lithiumForge$startFilteringPushableEntities();
        }
        return AbortableIterationConsumer.Continuation.CONTINUE;
    }

    @Unique
    private void lithiumForge$startFilteringPushableEntities() {
        this.lithiumForge$pushableEntities = new ReferenceMaskedList<>();
        for (T entity : this.storage) {
            this.lithiumForge$onStartClimbingCachingEntity((Entity) entity);
        }
    }

    @Unique
    private void lithiumForge$stopFilteringPushableEntities() {
        this.lithiumForge$pushableEntities = null;
    }

    //This might be called while the world is in an inconsistent state. E.g. the entity may be in a different section than
    //it is registered to.
    @Override
    public void lithiumForge$onEntityModifiedCachedBlock(BlockCachingEntity entity, BlockState newBlockState) {
        if (this.lithiumForge$pushableEntities== null) {
            entity.lithiumSetClimbingMobCachingSectionUpdateBehavior(false);
        } else {
            this.lithiumForge$updatePushabilityOnCachedStateChange(entity, newBlockState);
        }
    }

    @Unique
    private void lithiumForge$updatePushabilityOnCachedStateChange(BlockCachingEntity entity, BlockState newBlockState) {
        boolean visible = lithiumForge$entityPushableHeuristic(newBlockState);
        //The entity might be moving into this section right now but isn't registered yet.
        // If the entity is not in the collection, do nothing.
        // When it becomes registered to this section, it will be set to the correct visibility as well.
        this.lithiumForge$pushableEntities.setVisible((Entity) entity, visible);
    }

    @Unique
    private void lithiumForge$onStartClimbingCachingEntity(Entity entity) {
        Class<? extends Entity> entityClass = entity.getClass();
        if (PushableEntityClassGroup.MAYBE_PUSHABLE.contains(entityClass)) {
            this.lithiumForge$pushableEntities.add(entity);
            boolean shouldTrackBlockChanges = PushableEntityClassGroup.CACHABLE_UNPUSHABILITY.contains(entityClass);
            if (shouldTrackBlockChanges) {
                BlockCachingEntity blockCachingEntity = (BlockCachingEntity) entity;
                this.lithiumForge$updatePushabilityOnCachedStateChange(blockCachingEntity, blockCachingEntity.lithiumForge$getCachedFeetBlockState());
                blockCachingEntity.lithiumSetClimbingMobCachingSectionUpdateBehavior(true);
            }
        }
    }


    @Inject(method = "add", at = @At("RETURN"))
    private void onEntityAdded(T entityLike, CallbackInfo ci) {
        if (this.lithiumForge$pushableEntities!= null) {
            if (!this.chunkStatus.isAccessible()) {
                this.lithiumForge$stopFilteringPushableEntities();
            } else {
                this.lithiumForge$onStartClimbingCachingEntity((Entity) entityLike);
                if (this.lithiumForge$pushableEntities.totalSize() > this.storage.size()) {
                    //Todo: Decide on proper issue handling, printing a warning (?)
                    //something is leaking somewhere, maybe due to mod compat issues!
                    this.lithiumForge$stopFilteringPushableEntities();
                }
            }
        }
    }

    @Inject(method = "remove", at = @At("RETURN"))
    private void onEntityRemoved(T entityLike, CallbackInfoReturnable<Boolean> cir) {
        if (this.lithiumForge$pushableEntities!= null) {
            if (!this.chunkStatus.isAccessible()) {
                this.lithiumForge$stopFilteringPushableEntities();
            } else {
                this.lithiumForge$pushableEntities.remove((Entity) entityLike);
            }
        }
    }

    /**
     * Whether entities with this feet BlockState should be considered to be pushable. Some entity types are not pushable
     * when they are inside climbable blocks like ladders. Returns true for edge-cases
     * like entity in a trapdoor (which maybe is climbable due to a ladder below).
     *
     * @param cachedFeetBlockState cached BlockState at entity feet
     * @return whether the entity should be treated as pushable
     */
    @Unique
    private static boolean lithiumForge$entityPushableHeuristic(BlockState cachedFeetBlockState) {
        return cachedFeetBlockState == null || !cachedFeetBlockState.is(BlockTags.CLIMBABLE);
    }
}
