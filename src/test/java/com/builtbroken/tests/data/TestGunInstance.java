package com.builtbroken.tests.data;

import com.builtbroken.armory.content.items.ItemAmmo;
import com.builtbroken.armory.content.items.ItemGun;
import com.builtbroken.armory.data.ArmoryDataHandler;
import com.builtbroken.armory.data.ammo.AmmoData;
import com.builtbroken.armory.data.ammo.AmmoType;
import com.builtbroken.armory.data.clip.ClipData;
import com.builtbroken.armory.data.damage.simple.DamageSimple;
import com.builtbroken.mc.api.data.EnumProjectileTypes;
import com.builtbroken.armory.data.ranged.GunData;
import com.builtbroken.armory.data.ranged.GunInstance;
import com.builtbroken.armory.data.user.WeaponUserEntity;
import com.builtbroken.mc.api.data.weapon.IAmmoData;
import com.builtbroken.mc.api.data.weapon.ReloadType;
import com.builtbroken.mc.prefab.inventory.BasicInventory;
import com.builtbroken.mc.testing.junit.VoltzTestRunner;
import com.builtbroken.mc.testing.junit.world.FakeWorld;
import com.builtbroken.tests.AbstractArmoryTest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.junit.runner.RunWith;

/**
 * Tests functionality of the gun instance with a revolver like gun
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/21/2016.
 */
@RunWith(VoltzTestRunner.class)
public class TestGunInstance extends AbstractArmoryTest
{
    static ItemGun itemGun;
    static ItemAmmo itemAmmo;
    static GunData gunData;

    public void testInit()
    {
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        GunInstance instance = newInstance(stack, null);
        assertSame(gunData, instance.getGunData());
        assertNotNull(instance.getLoadedClip());
        assertEquals(0, instance.getLoadedClip().getAmmoCount());
        assertFalse(instance.hasMagWithAmmo());
        assertFalse(instance.hasSights());
    }

    public void testFireWeapon()
    {
        FakeWorld world = FakeWorld.newWorld("gunInstanceWeaponFireTest");
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        GunInstance instance = newInstance(stack, world);
        instance.getLoadedClip().loadAmmo((IAmmoData) ArmoryDataHandler.INSTANCE.get("ammo").get("ammo0"), 6);

        for (int i = 0; i < 6; i++)
        {
            instance.lastTimeFired = 0L;
            instance.fireWeapon(world, i);
            if (i != 5)
            {
                assertEquals("Failed to decrease ammo for shot " + i, 4 - i, instance.getLoadedClip().getAmmoCount());
                assertNotNull(instance.chamberNextRound());
            }
            else
            {
                assertEquals(0, instance.getLoadedClip().getAmmoCount());
            }
        }
    }

    public void testHasAmmo()
    {
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        GunInstance instance = newInstance(stack, null);
        assertFalse(instance.hasMagWithAmmo());
        instance.getLoadedClip().loadAmmo((IAmmoData) ArmoryDataHandler.INSTANCE.get("ammo").get("ammo0"), 1);
        assertTrue(instance.hasMagWithAmmo());
    }

    public void testReloadWeapon()
    {
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        //Over tested because I'm bored
        for (int i = 0; i < 30; i++)
        {
            GunInstance instance = newInstance(stack, null);
            assertFalse("" + i, instance.hasMagWithAmmo());

            IInventory inventory = new BasicInventory(30);
            inventory.setInventorySlotContents(i, new ItemStack(itemAmmo, 64, 0));
            assertEquals("" + i, 64, inventory.getStackInSlot(i).stackSize);
            instance.reloadWeapon(inventory, true);

            assertTrue("" + i, instance.hasMagWithAmmo());
            assertTrue("" + i, instance.getLoadedClip().getAmmoCount() == instance.getLoadedClip().getMaxAmmo());
            assertEquals("" + i, 64 - instance.getLoadedClip().getMaxAmmo(), inventory.getStackInSlot(i).stackSize);
        }

        //Over tested because I'm bored
        for (int i = 0; i < 20; i++)
        {
            GunInstance instance = newInstance(stack, null);
            assertFalse(instance.hasMagWithAmmo());

            IInventory inventory = new BasicInventory(30);
            inventory.setInventorySlotContents(i, new ItemStack(itemAmmo, 1, 0));
            inventory.setInventorySlotContents(i + 2, new ItemStack(itemAmmo, 1, 0));
            inventory.setInventorySlotContents(i + 4, new ItemStack(itemAmmo, 1, 0));
            inventory.setInventorySlotContents(i + 6, new ItemStack(itemAmmo, 1, 0));
            inventory.setInventorySlotContents(i + 8, new ItemStack(itemAmmo, 1, 0));
            instance.reloadWeapon(inventory, true);

            assertTrue("" + i, instance.hasMagWithAmmo());
            assertTrue("" + i, instance.getLoadedClip().getAmmoCount() == 5);
            assertNull("" + i, inventory.getStackInSlot(i));
            assertNull("" + i, inventory.getStackInSlot(i + 2));
            assertNull("" + i, inventory.getStackInSlot(i + 4));
            assertNull("" + i, inventory.getStackInSlot(i + 6));
            assertNull("" + i, inventory.getStackInSlot(i + 8));
        }

        //Over tested because I'm bored
        for (int i = 0; i < 20; i++)
        {
            GunInstance instance = newInstance(stack, null);
            assertFalse("" + i, instance.hasMagWithAmmo());

            IInventory inventory = new BasicInventory(30);
            inventory.setInventorySlotContents(i, new ItemStack(itemAmmo, 2, 0));
            inventory.setInventorySlotContents(i + 8, new ItemStack(itemAmmo, 3, 0));
            instance.reloadWeapon(inventory, true);

            assertTrue("" + i, instance.hasMagWithAmmo());
            assertTrue("" + i, instance.getLoadedClip().getAmmoCount() == 5);
            assertNull("" + i, inventory.getStackInSlot(i));
            assertNull("" + i, inventory.getStackInSlot(i + 8));
        }

        //TODO test clip load
    }

    public void testUnloadWeapon()
    {
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        GunInstance instance = newInstance(stack, null);
        instance.getLoadedClip().loadAmmo((IAmmoData) ArmoryDataHandler.INSTANCE.get("ammo").get("ammo" + 1), 6);
        assertTrue(instance.hasMagWithAmmo());

        IInventory inventory = new BasicInventory(30);
        instance.unloadWeapon(inventory);
        assertFalse(instance.hasMagWithAmmo());
        assertNotNull(inventory.getStackInSlot(0));
        assertSame(itemAmmo, inventory.getStackInSlot(0).getItem());
        assertSame(1, inventory.getStackInSlot(0).getItemDamage());
        assertSame(6, inventory.getStackInSlot(0).stackSize);

        //TODO test clip unload
    }

    public void testIsManuallyFeedClip()
    {
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        GunInstance instance = newInstance(stack, null);
        assertTrue(instance.isManuallyFeedClip());
    }

    public void testSave()
    {
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        GunInstance instance = newInstance(stack, null);
        instance.getLoadedClip().loadAmmo((IAmmoData) ArmoryDataHandler.INSTANCE.get("ammo").get("ammo" + 2), 6);
        instance.chamberNextRound();
        assertTrue(instance.hasMagWithAmmo());

        NBTTagCompound nbt = new NBTTagCompound();
        instance.save(nbt);

        assertEquals(2, nbt.func_150296_c().size());
        assertTrue(nbt.hasKey(GunInstance.NBT_ROUND));
        assertTrue(nbt.hasKey(GunInstance.NBT_CLIP));

        //Any time this code is changed test for legacy loading of old data
    }

    public void testLoad()
    {
        ItemStack stack = new ItemStack(itemGun, 1, 0);
        GunInstance instance = newInstance(stack, null);

        instance.getLoadedClip().loadAmmo((IAmmoData) ArmoryDataHandler.INSTANCE.get("ammo").get("ammo" + 2), 6);
        instance.chamberNextRound();
        assertTrue(instance.hasMagWithAmmo());

        NBTTagCompound nbt = new NBTTagCompound();
        instance.save(nbt);

        instance = newInstance(stack, null);
        instance.load(nbt);
        assertNotNull(instance.getChamberedRound());
        assertNotNull(instance.getLoadedClip());
        assertSame(5, instance.getLoadedClip().getAmmoCount());

        //Any time this code is changed test for legacy loading of old data
    }

    private GunInstance newInstance(ItemStack stack, World world)
    {
        return new GunInstance(stack, new WeaponUserEntity(new EntityZombie(world)), gunData);
    }

    @Override
    public void setUpForEntireClass()
    {
        super.setUpForEntireClass();
        itemGun = new ItemGun();
        itemAmmo = new ItemAmmo();

        AmmoType ammoType = new AmmoType(null, "9mm", "9mm", EnumProjectileTypes.BULLET);
        ArmoryDataHandler.INSTANCE.get("ammoType").add(ammoType);

        gunData = new GunData(null, "gun", "handgun", "revolver", ammoType, ReloadType.HAND_FEED, new ClipData(null, "revolverClip", "revolverClip", ReloadType.HAND_FEED, ammoType, 6));
        ArmoryDataHandler.INSTANCE.get("gun").metaToEntry.put(0, gunData);
        gunData.set(itemAmmo, 0);
        ArmoryDataHandler.INSTANCE.get("gun").add(gunData);

        for (int i = 0; i < 5; i++)
        {
            AmmoData data = new AmmoData(null, "ammo" + i, "ammo" + i, ammoType);
            data.damageData.add(new DamageSimple(null, "impact", 5 + i));
            data.set(itemAmmo, i);
            ArmoryDataHandler.INSTANCE.get("ammo").add(data);
            ArmoryDataHandler.INSTANCE.get("ammo").metaToEntry.put(i, data);
        }
    }

    @Override
    public void tearDownForEntireClass()
    {
        super.tearDownForEntireClass();
    }
}
