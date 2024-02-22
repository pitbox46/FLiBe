package github.pitbox46.lithiumforge.common.block;

public interface BlockCountingSection {
    boolean mayContainAny(TrackedBlockStatePredicate trackedBlockStatePredicate);
}
