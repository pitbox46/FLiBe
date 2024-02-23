package github.pitbox46.lithiumforge.common.world;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;

public interface ServerLevelAccessor {
    PersistentEntitySectionManager<Entity> lithiumForge$getEntitySectionManager();
}
