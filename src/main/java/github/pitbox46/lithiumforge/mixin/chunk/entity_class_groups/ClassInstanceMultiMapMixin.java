package github.pitbox46.lithiumforge.mixin.chunk.entity_class_groups;

import github.pitbox46.lithiumforge.common.entity.EntityClassGroup;
import github.pitbox46.lithiumforge.common.world.chunk.ClassGroupFilterableList;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.minecraft.util.ClassInstanceMultiMap;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Patches {@link ClassInstanceMultiMap} to allow grouping entities by arbitrary groups of classes instead of one class only.
 */
@Mixin(ClassInstanceMultiMap.class)
public abstract class ClassInstanceMultiMapMixin<T> implements ClassGroupFilterableList<T> {

    @Shadow
    @Final
    private List<T> allInstances;

    @Unique
    private final Reference2ReferenceArrayMap<EntityClassGroup, ReferenceLinkedOpenHashSet<T>> lithiumForge$entitiesByGroup =
            new Reference2ReferenceArrayMap<>();

    /**
     * Update our collections
     */
    @ModifyVariable(method = "add(Ljava/lang/Object;)Z", at = @At("HEAD"), argsOnly = true)
    public T add(T entity) {
        for (Map.Entry<EntityClassGroup, ReferenceLinkedOpenHashSet<T>> entityGroupAndSet : this.lithiumForge$entitiesByGroup.entrySet()) {
            EntityClassGroup entityGroup = entityGroupAndSet.getKey();
            if (entityGroup.contains(((Entity) entity).getClass())) {
                entityGroupAndSet.getValue().add((entity));
            }
        }
        return entity;
    }

    /**
     * Update our collections
     */
    @ModifyVariable(method = "remove(Ljava/lang/Object;)Z", at = @At("HEAD"), argsOnly = true)
    public Object remove(Object o) {
        for (ReferenceLinkedOpenHashSet<T> entitySet : this.lithiumForge$entitiesByGroup.values()) {
            //noinspection SuspiciousMethodCalls
            entitySet.remove(o);
        }
        return o;
    }

    /**
     * Get entities of a class group
     */
    @Unique
    public Collection<T> lithiumForge$getAllOfGroupType(EntityClassGroup type) {
        Collection<T> collection = this.lithiumForge$entitiesByGroup.get(type);

        if (collection == null) {
            collection = this.lithiumForge$createAllOfGroupType(type);
        }

        return collection;
    }

    /**
     * Start grouping by a new class group
     */
    @Unique
    private Collection<T> lithiumForge$createAllOfGroupType(EntityClassGroup type) {
        ReferenceLinkedOpenHashSet<T> allOfType = new ReferenceLinkedOpenHashSet<>();

        for (T entity : this.allInstances) {
            if (type.contains(entity.getClass())) {
                allOfType.add(entity);
            }
        }
        this.lithiumForge$entitiesByGroup.put(type, allOfType);

        return allOfType;
    }
}