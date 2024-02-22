package github.pitbox46.lithiumforge.common.entity.nearby_tracker;

import org.jetbrains.annotations.Nullable;

public interface NearbyEntityListenerProvider {
    @Nullable
    NearbyEntityListenerMulti getListener();

    void addListener(NearbyEntityTracker listener);
}
