package com.builtbroken.armory.data.user;

import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2017.
 */
public class WeaponUserEntity<E extends Entity> implements IWeaponUser
{
    public final E entity;

    public WeaponUserEntity(E entity)
    {
        this.entity = entity;
    }

    @Override
    public Pos getEntityPosition()
    {
        return new Pos(x(), y() + (entity.height / 2f), z());
    }

    @Override
    public Pos getEntityAim()
    {
        //Used to calculate x and z position
        double f3 = MathHelper.cos((float) (-yaw() * 0.017453292F - Math.PI));
        double f4 = MathHelper.sin((float) (-yaw() * 0.017453292F - Math.PI));
        double f5 = -MathHelper.cos((float) (-pitch() * 0.017453292F));

        //used to calculate aim y
        double aimY = MathHelper.sin((float) (-pitch() * 0.017453292F));

        double aimX = f4 * f5;
        double aimZ = f3 * f5;
        return new Pos(aimX, aimY, aimZ);
    }

    @Override
    public Pos getProjectileSpawnOffset()
    {
        //Find our hand position so to position starting point near barrel of the gun
        final float rotationHand = MathHelper.wrapAngleTo180_float((float) (yaw() + 90));
        final double r = Math.toRadians(rotationHand);
        final Pos hand = new Pos(
                (Math.cos(r) - Math.sin(r)) * 0.5,
                -0.5,
                (Math.sin(r) + Math.cos(r)) * 0.5
        );
        return hand;
    }

    @Override
    public IInventory getInventory()
    {
        return entity instanceof IInventory ? (IInventory) entity : null; //TODO maybe use fake inventory to reduce chance of NPE
    }

    @Override
    public boolean isAmmoSlot(int slot)
    {
        return true;
    }

    @Override
    public double yaw()
    {
        return entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw);
    }

    @Override
    public double pitch()
    {
        return entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch);
    }

    @Override
    public double roll()
    {
        return 0;
    }

    @Override
    public World world()
    {
        return entity.worldObj;
    }

    @Override
    public double x()
    {
        return entity.posX;
    }

    @Override
    public double y()
    {
        return entity.posY;
    }

    @Override
    public double z()
    {
        return entity.posZ;
    }

    @Override
    public Entity getShooter()
    {
        return entity;
    }
}
