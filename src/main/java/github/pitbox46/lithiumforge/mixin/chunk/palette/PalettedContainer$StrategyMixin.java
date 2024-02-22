package github.pitbox46.lithiumforge.mixin.chunk.palette;

import github.pitbox46.lithiumforge.common.world.chunk.LithiumHashPalette;
import net.minecraft.core.IdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PalettedContainer;
import org.spongepowered.asm.mixin.*;

@Mixin(PalettedContainer.Strategy.class)
public abstract class PalettedContainer$StrategyMixin {
    @Mutable
    @Shadow
    @Final
    public static PalettedContainer.Strategy SECTION_STATES;

    @Unique
    private static final PalettedContainer.Configuration<?>[] BLOCKSTATE_DATA_PROVIDERS;
    @Unique
    private static final PalettedContainer.Configuration<?>[] BIOME_DATA_PROVIDERS;


    @Unique
    private static final Palette.Factory lithiumForge$HASH = LithiumHashPalette::create;
    @Mutable
    @Shadow
    @Final
    public static PalettedContainer.Strategy SECTION_BIOMES;
    @Shadow
    @Final
    static Palette.Factory GLOBAL_PALETTE_FACTORY;

    /*
     * @reason Replace the hash palette from vanilla with our own and change the threshold for usage to only 3 bits,
     * as our implementation performs better at smaller key ranges.
     * @author JellySquid, 2No2Name (avoid Configuration duplication, use hash palette for 3 bit biomes)
     */
    static {
        Palette.Factory idListFactory = GLOBAL_PALETTE_FACTORY;

        PalettedContainer.Configuration<?> arrayConfiguration4bit = new PalettedContainer.Configuration<>(PalettedContainer.Strategy.LINEAR_PALETTE_FACTORY, 4);
        PalettedContainer.Configuration<?> hashConfiguration4bit = new PalettedContainer.Configuration<>(lithiumForge$HASH, 4);
        BLOCKSTATE_DATA_PROVIDERS = new PalettedContainer.Configuration<?>[]{
                new PalettedContainer.Configuration<>(PalettedContainer.Strategy.SINGLE_VALUE_PALETTE_FACTORY, 0),
                // Bits 1-4 must all pass 4 bits as parameter, otherwise chunk sections will corrupt.
                arrayConfiguration4bit,
                arrayConfiguration4bit,
                hashConfiguration4bit,
                hashConfiguration4bit,
                new PalettedContainer.Configuration<>(lithiumForge$HASH, 5),
                new PalettedContainer.Configuration<>(lithiumForge$HASH, 6),
                new PalettedContainer.Configuration<>(lithiumForge$HASH, 7),
                new PalettedContainer.Configuration<>(lithiumForge$HASH, 8)
        };

        SECTION_STATES = new PalettedContainer.Strategy(4) {
            @Override
            public <A> PalettedContainer.Configuration<A> getConfiguration(IdMap<A> idList, int bits) {
                if (bits >= 0 && bits < BLOCKSTATE_DATA_PROVIDERS.length) {
                    //noinspection unchecked
                    return (PalettedContainer.Configuration<A>) BLOCKSTATE_DATA_PROVIDERS[bits];
                }
                return new PalettedContainer.Configuration<>(idListFactory, Mth.ceillog2(idList.size()));
            }
        };

        BIOME_DATA_PROVIDERS = new PalettedContainer.Configuration<?>[]{
                new PalettedContainer.Configuration<>(PalettedContainer.Strategy.SINGLE_VALUE_PALETTE_FACTORY, 0),
                new PalettedContainer.Configuration<>(PalettedContainer.Strategy.LINEAR_PALETTE_FACTORY, 1),
                new PalettedContainer.Configuration<>(PalettedContainer.Strategy.LINEAR_PALETTE_FACTORY, 2),
                new PalettedContainer.Configuration<>(lithiumForge$HASH, 3)
        };


        SECTION_BIOMES = new PalettedContainer.Strategy(2) {
            @Override
            public <A> PalettedContainer.Configuration<A> getConfiguration(IdMap<A> idList, int bits) {
                if (bits >= 0 && bits < BIOME_DATA_PROVIDERS.length) {
                    //noinspection unchecked
                    return (PalettedContainer.Configuration<A>) BIOME_DATA_PROVIDERS[bits];
                }
                return new PalettedContainer.Configuration<>(idListFactory, Mth.ceillog2(idList.size()));
            }
        };
    }
}
