package github.pitbox46.lithiumforge.common.world;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.TransientEntitySectionManager;

public interface ClientLevelAccessor {
    TransientEntitySectionManager<Entity> lithiumForge$getEntitySectionManager();
}
