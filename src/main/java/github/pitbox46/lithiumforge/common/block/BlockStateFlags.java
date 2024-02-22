package github.pitbox46.lithiumforge.common.block;

import github.pitbox46.lithiumforge.common.ai.pathing.BlockStatePathingCache;
import github.pitbox46.lithiumforge.common.ai.pathing.PathNodeCache;
import github.pitbox46.lithiumforge.common.entity.FluidCachingEntity;
import it.unimi.dsi.fastutil.objects.Reference2BooleanArrayMap;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;

public class BlockStateFlags {
    public static final boolean ENABLED = BlockCountingSection.class.isAssignableFrom(LevelChunkSection.class);

    public static final int NUM_LISTENING_FLAGS;
    public static final ListeningBlockStatePredicate[] LISTENING_FLAGS;
    public static final int LISTENING_MASK_OR;

    //Listening Flag
    public static final ListeningBlockStatePredicate ANY;

    public static final int NUM_TRACKED_FLAGS;
    public static final TrackedBlockStatePredicate[] TRACKED_FLAGS;

    //Counting flags
    public static final TrackedBlockStatePredicate OVERSIZED_SHAPE;
    public static final TrackedBlockStatePredicate PATH_NOT_OPEN;
    public static final TrackedBlockStatePredicate WATER;
    public static final TrackedBlockStatePredicate LAVA;

    public static final TrackedBlockStatePredicate[] FLAGS;

    //Non counting flags
    public static final TrackedBlockStatePredicate ENTITY_TOUCHABLE;

    static {
        Reference2BooleanArrayMap<ListeningBlockStatePredicate> listeningFlags = new Reference2BooleanArrayMap<>();

        ANY = new ListeningBlockStatePredicate(listeningFlags.size()) {
            @Override
            public boolean test(BlockState operand) {
                return true;
            }
        };
        //false -> we listen to changes of all blocks that pass the predicate test.
        //true -> we only listen to changes of the predicate test result
        listeningFlags.put(ANY, false);

        NUM_LISTENING_FLAGS = listeningFlags.size();
        int listenMaskOR = 0;
        int iteration = 0;
        for (var entry : listeningFlags.reference2BooleanEntrySet()) {
            boolean listenOnlyXOR = entry.getBooleanValue();
            listenMaskOR |= listenOnlyXOR ? 0 : 1 << iteration;
        }
        LISTENING_MASK_OR = listenMaskOR;
        LISTENING_FLAGS = listeningFlags.keySet().toArray(new ListeningBlockStatePredicate[NUM_LISTENING_FLAGS]);


        ArrayList<TrackedBlockStatePredicate> countingFlags = new ArrayList<>(listeningFlags.keySet());

        OVERSIZED_SHAPE = new TrackedBlockStatePredicate(countingFlags.size()) {
            @Override
            public boolean test(BlockState operand) {
                return operand.hasLargeCollisionShape();
            }
        };
        countingFlags.add(OVERSIZED_SHAPE);

        if (FluidCachingEntity.class.isAssignableFrom(Entity.class)) {
            WATER = new TrackedBlockStatePredicate(countingFlags.size()) {
                @Override
                public boolean test(BlockState operand) {
                    return operand.getFluidState().is(FluidTags.WATER);
                }
            };
            countingFlags.add(WATER);

            LAVA = new TrackedBlockStatePredicate(countingFlags.size()) {
                @Override
                public boolean test(BlockState operand) {
                    return operand.getFluidState().is(FluidTags.LAVA);
                }
            };
            countingFlags.add(LAVA);
        } else {
            WATER = null;
            LAVA = null;
        }

        if (BlockStatePathingCache.class.isAssignableFrom(AbstractBlock.AbstractBlockState.class)) {
            PATH_NOT_OPEN = new TrackedBlockStatePredicate(countingFlags.size()) {
                @Override
                public boolean test(BlockState operand) {
                    return PathNodeCache.getNeighborPathNodeType(operand) != BlockPathTypes.OPEN;
                }
            };
            countingFlags.add(PATH_NOT_OPEN);
        } else {
            PATH_NOT_OPEN = null;
        }

        NUM_TRACKED_FLAGS = countingFlags.size();
        TRACKED_FLAGS = countingFlags.toArray(new TrackedBlockStatePredicate[NUM_TRACKED_FLAGS]);

        ArrayList<TrackedBlockStatePredicate> flags = new ArrayList<>(countingFlags);

        ENTITY_TOUCHABLE = new TrackedBlockStatePredicate(countingFlags.size()) {
            //How to find the remapped methods:
            //1) Run in the debugger: System.out.println(FabricLoader.getInstance().getMappingResolver().getNamespaceData("intermediary").methodNames)
            //2) Ctrl+F for the method name, in this case "onEntityCollision". Make sure to find the correct one.
            private final String remapped_onEntityCollision = FabricLoader.getInstance().getMappingResolver().mapMethodName("intermediary", "net.minecraft.class_4970", "method_9548", "(Lnet/minecraft/class_2680;Lnet/minecraft/class_1937;Lnet/minecraft/class_2338;Lnet/minecraft/class_1297;)V");
            @Override
            public boolean test(BlockState operand) {
                return ReflectionUtil.hasMethodOverride(operand.getBlock().getClass(), AbstractBlock.class, true, this.remapped_onEntityCollision, BlockState.class, World.class, BlockPos.class, Entity.class);
            }
        };
        flags.add(ENTITY_TOUCHABLE);

        FLAGS = flags.toArray(new TrackedBlockStatePredicate[0]);
    }
}
