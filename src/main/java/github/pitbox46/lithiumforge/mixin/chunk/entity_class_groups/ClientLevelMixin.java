package github.pitbox46.lithiumforge.mixin.chunk.entity_class_groups;

import github.pitbox46.lithiumforge.common.world.ClientLevelAccessor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientLevel.class)
public class ClientLevelMixin implements ClientLevelAccessor {
    @Shadow @Final public TransientEntitySectionManager<Entity> entityStorage;

    @Override
    public TransientEntitySectionManager<Entity> lithiumForge$getEntitySectionManager() {
        return entityStorage;
    }
}
