package github.pitbox46.lithiumforge.common.world.chunk;


import net.minecraft.world.level.chunk.Palette;

public interface CompactingPackedIntegerArray {
    /**
     * Copies the data out of this array into a new non-packed array. The returned array contains a copy of this array
     * re-mapped using {@param destPalette}.
     */
    <T> void compact(Palette<T> srcPalette, Palette<T> dstPalette, short[] out);
}
