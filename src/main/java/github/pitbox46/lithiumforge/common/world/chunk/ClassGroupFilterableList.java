package github.pitbox46.lithiumforge.common.world.chunk;


import github.pitbox46.lithiumforge.common.entity.EntityClassGroup;

import java.util.Collection;

public interface ClassGroupFilterableList<T> {
    Collection<T> getAllOfGroupType(EntityClassGroup type);

}