package github.pitbox46.lithiumforge.mixin.chunk.entity_class_groups;

import github.pitbox46.lithiumforge.common.world.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements ServerLevelAccessor {
    @Shadow @Final public PersistentEntitySectionManager<Entity> entityManager;

    @Override
    public PersistentEntitySectionManager<Entity> lithiumForge$getEntitySectionManager() {
        return entityManager;
    }
}
