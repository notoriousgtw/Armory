package com.builtbroken.armory.registry.processor;

import com.builtbroken.armory.data.ArmoryEntry;
import com.builtbroken.mc.prefab.json.processors.JsonProcessor;
import com.google.gson.JsonElement;

/**
 * Handles processing a single type of item
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/15/2016.
 */
public class ArmoryEntryJsonProcessor<E extends ArmoryEntry> extends JsonProcessor<E>
{
    /** Key used to load data from a json file */
    public final String jsonKey;

    public ArmoryEntryJsonProcessor(String jsonKey)
    {
        this.jsonKey = jsonKey;
    }

    @Override
    public boolean canProcess(JsonElement element)
    {
        return element.isJsonObject() && element.getAsJsonObject().has(jsonKey);
    }

    public E process(JsonElement element)
    {
        return null;
    }
}
