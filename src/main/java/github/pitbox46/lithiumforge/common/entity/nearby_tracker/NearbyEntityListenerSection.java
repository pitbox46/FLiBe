package github.pitbox46.lithiumforge.common.entity.nearby_tracker;

import net.minecraft.world.entity.SectionedEntityCache;

public interface NearbyEntityListenerSection {
    void addListener(NearbyEntityListener listener);

    void removeListener(SectionedEntityCache<?> sectionedEntityCache, NearbyEntityListener listener);
}
