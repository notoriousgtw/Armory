package com.builtbroken.armory;

import com.builtbroken.armory.content.entity.projectile.EntityAmmoProjectile;
import com.builtbroken.armory.content.items.ItemAmmo;
import com.builtbroken.armory.content.items.ItemClip;
import com.builtbroken.armory.content.items.ItemGun;
import com.builtbroken.armory.content.prefab.ItemMetaArmoryEntry;
import com.builtbroken.armory.content.sentry.entity.EntitySentry;
import com.builtbroken.armory.content.sentry.tile.ItemSentry;
import com.builtbroken.armory.data.ArmoryDataHandler;
import com.builtbroken.armory.data.ammo.AmmoData;
import com.builtbroken.armory.data.clip.ClipData;
import com.builtbroken.armory.data.ranged.GunData;
import com.builtbroken.armory.data.sentry.SentryData;
import com.builtbroken.armory.json.processors.*;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.json.JsonContentLoader;
import com.builtbroken.mc.lib.mod.AbstractMod;
import com.builtbroken.mc.lib.mod.ModCreativeTab;
import com.builtbroken.mc.mods.nei.NEIProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.block.Block;

/**
 * Created by robert on 11/18/2014.
 */
@Mod(modid = Armory.DOMAIN, name = "Armory", version = Armory.VERSION, dependencies = Armory.DEPENDENCIES)
public final class Armory extends AbstractMod
{
    /** Name of the channel and mod ID. */
    public static final String DOMAIN = "armory";
    public static final String PREFIX = DOMAIN + ":";

    /** The version of WatchYourStep. */
    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;
    public static final String DEPENDENCIES = "required-after:voltzengine;";

    @Mod.Instance(DOMAIN)
    public static Armory INSTANCE;

    @SidedProxy(clientSide = "com.builtbroken.armory.client.ClientProxy", serverSide = "com.builtbroken.armory.server.ServerProxy")
    public static CommonProxy proxy;

    public static ModCreativeTab CREATIVE_TAB;

    public static Block blockSentry;

    public static ItemMetaArmoryEntry<GunData> itemGun;
    public static ItemMetaArmoryEntry<ClipData> itemClip;
    public static ItemMetaArmoryEntry<AmmoData> itemAmmo;
    public static ItemMetaArmoryEntry<SentryData> itemSentry;

    //Configs
    /** Overrides the delay between attacks on entities */
    public static boolean overrideDamageDelay = true;

    public Armory()
    {
        super(DOMAIN, "Armory");
        CREATIVE_TAB = new ModCreativeTab("armory");
        getManager().setTab(CREATIVE_TAB);
        modIssueTracker = "https://github.com/BuiltBrokenModding/Armory/issues";
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        Engine.requestOres();
        Engine.requestResources();
        Engine.requestSheetMetalContent();
        Engine.requestMultiBlock();
        Engine.requestSimpleTools();
        Engine.requestCircuits();
        Engine.requestCraftingParts();

        ArmoryDataHandler.INSTANCE.add(new ArmoryDataHandler.ArmoryData(References.BBM_CONFIG_FOLDER, "gun"));
        ArmoryDataHandler.INSTANCE.add(new ArmoryDataHandler.ArmoryData(References.BBM_CONFIG_FOLDER, "ammo"));
        ArmoryDataHandler.INSTANCE.add(new ArmoryDataHandler.ArmoryData(References.BBM_CONFIG_FOLDER, "ammoType"));
        ArmoryDataHandler.INSTANCE.add(new ArmoryDataHandler.ArmoryData(References.BBM_CONFIG_FOLDER, "clip"));
        ArmoryDataHandler.INSTANCE.add(new ArmoryDataHandler.ArmoryData(References.BBM_CONFIG_FOLDER, "sentry"));

        JsonContentLoader.INSTANCE.add(new AmmoTypeJsonProcessor());
        JsonContentLoader.INSTANCE.add(new AmmoJsonProcessor());
        JsonContentLoader.INSTANCE.add(new ClipJsonProcessor());
        JsonContentLoader.INSTANCE.add(new GunJsonProcessor());
        JsonContentLoader.INSTANCE.add(new SentryJsonProcessor());

        //TODO gen more guns if registered guns is greater than 32000 (Which should never happen)
        itemGun = getManager().newItem("armoryGun", new ItemGun());
        itemClip = getManager().newItem("armoryClip", new ItemClip());
        itemAmmo = getManager().newItem("armoryAmmo", new ItemAmmo());
        itemSentry = getManager().newItem("armorySentry", new ItemSentry());

        blockSentry.setCreativeTab(null);
        NEIProxy.hideItem(blockSentry);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
        EntityRegistry.registerModEntity(EntityAmmoProjectile.class, "ArmoryProjectile", 0, this, 500, 1, true);
        EntityRegistry.registerModEntity(EntitySentry.class, "ArmorySentry", 1, this, 500, 1, true);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }

    @Override
    public CommonProxy getProxy()
    {
        return proxy;
    }
}
