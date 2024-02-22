package github.pitbox46.lithiumforge.common.util.deduplication;

public interface LithiumInternerWrapper<T> {

    T getCanonical(T value);

    void deleteCanonical(T value);
}
