package github.pitbox46.lithiumforge.common.entity;

import net.minecraft.entity.ai.pathing.EntityNavigation;

public interface NavigatingEntity {
    boolean isRegisteredToWorld();

    void setRegisteredToWorld(EntityNavigation navigation);

    EntityNavigation getRegisteredNavigation();

    void updateNavigationRegistration();

}
