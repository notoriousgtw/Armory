package com.builtbroken.armory.data.projectiles;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/2016.
 */
public enum EnumProjectileTypes
{
    ARROW,
    BOLT,
    ROCKET,
    GRENADE,
    BULLET,
    LASER;

    public static EnumProjectileTypes get(int type)
    {
        if (type >= 0 && type < values().length)
        {
            return values()[type];
        }
        return BULLET;
    }
}