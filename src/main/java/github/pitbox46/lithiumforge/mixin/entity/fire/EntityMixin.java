package github.pitbox46.lithiumforge.mixin.entity.fire;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Moves the fire/lava check computation in {@link Entity#move(MoverType, Vec3)} to {@link Entity#setSecondsOnFire(int)}
 * <br>
 * We only have to calculate when the entity is set on fire.
 * <br>
 * Note: This assumes that the fire and lava calls {@link Entity#setSecondsOnFire(int)}.
 * This looks like a safe assumption and nothing breaks if the assumption is violated
 */
@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Level level();

    @Shadow public abstract AABB getBoundingBox();

    @Unique
    private static final Stream<?> lithiumforge$EMPTY_STREAM = Stream.empty();

    @Unique
    private boolean lithiumforge$inLavaOrFireLastTick = false;

    @Inject(at = @At(value = "HEAD"), method = "setSecondsOnFire")
    private void onSetFire(int pSeconds, CallbackInfo ci) {
        if (!lithiumforge$inLavaOrFireLastTick) {
            // Moved from Entity#move. No need to
            lithiumforge$inLavaOrFireLastTick = this.level()
                    .getBlockStatesIfLoaded(this.getBoundingBox().deflate(1.0E-6D))
                    .noneMatch((p_20127_) -> p_20127_.is(BlockTags.FIRE) || p_20127_.is(Blocks.LAVA));
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getBlockStatesIfLoaded(Lnet/minecraft/world/phys/AABB;)Ljava/util/stream/Stream;"), method = "move")
    private Stream<?> passDummyValue(Level instance, AABB aabb) {
        return lithiumforge$EMPTY_STREAM;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;noneMatch(Ljava/util/function/Predicate;)Z"), method = "move")
    private boolean noneMatchProxy(Stream<?> instance, Predicate<?> predicate) {
        boolean temp = lithiumforge$inLavaOrFireLastTick;
        lithiumforge$inLavaOrFireLastTick = false;
        return temp;
    }
}
