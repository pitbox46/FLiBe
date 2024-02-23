@MixinConfigOption(
        description = "Uses faster block access for block collisions and delayed entity access with grouped boat/shulker for entity collisions when available",
        depends = {
                @MixinConfigDependency(dependencyPath = "mixin.util.block_tracking"),
                @MixinConfigDependency(dependencyPath = "mixin.util.chunk_access")
        }
)
package github.pitbox46.lithiumforge.mixin.entity.collisions.intersection;

import net.caffeinemc.gradle.MixinConfigDependency;
import net.caffeinemc.gradle.MixinConfigOption;