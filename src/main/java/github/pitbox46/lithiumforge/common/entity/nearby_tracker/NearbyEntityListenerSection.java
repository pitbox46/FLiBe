package github.pitbox46.lithiumforge.common.entity.nearby_tracker;

import net.minecraft.world.level.entity.EntitySectionStorage;

public interface NearbyEntityListenerSection {
    void addListener(NearbyEntityListener listener);

    void removeListener(EntitySectionStorage<?> sectionedEntityCache, NearbyEntityListener listener);
}
