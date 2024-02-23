@MixinConfigOption(
        description = """
                Event-based system for tracking nearby entities.
                """,
        depends = {
                @MixinConfigDependency(dependencyPath = "mixin.util.entity_section_position")
        },
        enabled = false //Disabled, because mspt increase in normal worlds has been measured consistently
)
package github.pitbox46.lithiumforge.mixin.ai.nearby_entity_tracking;

import net.caffeinemc.gradle.MixinConfigDependency;
import net.caffeinemc.gradle.MixinConfigOption;