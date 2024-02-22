package github.pitbox46.lithiumforge.common.entity.movement_tracker;

public interface MovementTrackerCache {
    void remove(SectionedEntityMovementTracker<?, ?> tracker);

    <S extends SectionedEntityMovementTracker<?, ?>> S deduplicate(S tracker);
}
