package github.pitbox46.lithiumforge.common.util;

import net.minecraft.core.Holder;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraftforge.registries.ForgeRegistries;

public class POIRegistryEntries {
    //Using a separate class, so the registry lookup happens after the registry is initialized
    public static final Holder<PoiType> NETHER_PORTAL_ENTRY = ForgeRegistries.POI_TYPES.getDelegateOrThrow(PoiTypes.NETHER_PORTAL);
    public static final Holder<PoiType> HOME_ENTRY = ForgeRegistries.POI_TYPES.getDelegateOrThrow(PoiTypes.HOME);
}
