package com.builtbroken.armory.content.prefab;

import com.builtbroken.armory.Armory;
import com.builtbroken.armory.data.ArmoryDataHandler;
import com.builtbroken.armory.data.ArmoryEntry;
import com.builtbroken.mc.api.IWorldPosition;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.IPacketReceiver;
import com.builtbroken.mc.core.network.packet.PacketPlayerItem;
import com.builtbroken.mc.core.network.packet.PacketType;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.List;
import java.util.Map;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/20/2016.
 */
public class ItemMetaArmoryEntry<E extends ArmoryEntry> extends Item implements IPacketReceiver, IPostInit
{
    public final String typeName;

    public ItemMetaArmoryEntry(String name, String typeName)
    {
        ArmoryDataHandler.INSTANCE.get(typeName).add(this);
        this.typeName = typeName;
        this.setUnlocalizedName(Armory.PREFIX + name);
        this.setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        if (Engine.runningAsDev)
        {
            list.add("" + getData(stack));
        }
    }

    public E getData(ItemStack stack)
    {
        return (E) ArmoryDataHandler.INSTANCE.get(typeName).metaToEntry.get(stack.getItemDamage());
    }

    @Override
    public void onPostInit()
    {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (event.world.provider.dimensionId == 0)
        {
            ArmoryDataHandler.INSTANCE.get(typeName).init(this);
        }
    }

    @SubscribeEvent
    public void onConnect(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP)
        {
            sendSyncPacket((EntityPlayerMP) event.player);
        }
    }


    @Override
    public void read(ByteBuf buf, EntityPlayer player, PacketType packet)
    {
        ArmoryDataHandler.INSTANCE.get(typeName).readBytes(buf);
    }

    protected void sendSyncPacket(EntityPlayerMP player)
    {
        PacketPlayerItem packet = new PacketPlayerItem(Item.getIdFromItem(this) * -1);
        ArmoryDataHandler.INSTANCE.get(typeName).writeBytes(packet.data());
        Engine.instance.packetHandler.sendToPlayer(packet, player);
    }

    @Override
    public boolean shouldReadPacket(EntityPlayer player, IWorldPosition receiveLocation, PacketType packet)
    {
        return player.worldObj.isRemote;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List items)
    {
        Map<Integer, E> map = ArmoryDataHandler.INSTANCE.get(typeName).metaToEntry;
        for (Map.Entry<Integer, E> entry : map.entrySet())
        {
            getSubItems(item, entry.getKey(), entry.getValue(), tab, items);
        }
    }

    @SideOnly(Side.CLIENT)
    protected void getSubItems(Item item, int meta, E armoryEntry, CreativeTabs tab, List items)
    {
        items.add(new ItemStack(item, 1, meta));
    }
}