package com.builtbroken.armory.data.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/28/2017.
 */
public class DamageSimple extends DamageData
{
    public final float damage;
    public final String damageSource;

    public DamageSimple(String type, float damage)
    {
        this.damageSource = type;
        this.damage = damage;
    }

    @Override
    public boolean onImpact(Entity attacker, Entity entity, double hitX, double hitY, double hitZ, float velocity, float scale)
    {
        //TODO create damage source with shooter, gun data, and damage type
        //TODO calculate armor
        //TODO apply force
        entity.attackEntityFrom(DamageSource.generic, damage * scale); //TODO replace with exact type

        return true;
    }

    @Override
    public float getBaseDamage()
    {
        return damage;
    }
}
