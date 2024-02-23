package github.pitbox46.lithiumforge.common.block;

import github.pitbox46.lithiumforge.common.entity.block_tracking.SectionedBlockChangeTracker;

public interface BlockListeningSection {

    void addToCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker);
    void removeFromCallback(ListeningBlockStatePredicate blockGroup, SectionedBlockChangeTracker tracker);
}
