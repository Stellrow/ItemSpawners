package ro.Stellrow.ItemSpawners.nms;

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

public class NBTModule_1_14_R1 extends NBTModule{
    public void setSpawner(Block b, String type){
        CraftWorld world = (CraftWorld)b.getWorld();
        BlockPosition pos = new BlockPosition(b.getLocation().getBlockX(),b.getLocation().getBlockY(),b.getLocation().getBlockZ());
        TileEntityMobSpawner tile = (TileEntityMobSpawner) world.getHandle().getTileEntity(pos);
        MobSpawnerData data = new MobSpawnerData();
        NBTTagCompound a = data.getEntity();
        NBTTagCompound armors = new NBTTagCompound();
        armors.setString("id", "minecraft:armor_stand");
        armors.setInt("NoBasePlate", 1);
        NBTTagList armor = new NBTTagList();

        NBTTagCompound boots = new NBTTagCompound();
        boots.setString("id", "minecraft:air");
        boots.setInt("Count", 1);
        armor.add(boots);

        NBTTagCompound legs = new NBTTagCompound();
        legs.setString("id", "minecraft:air");
        legs.setInt("Count", 1);
        armor.add(legs);

        NBTTagCompound chest = new NBTTagCompound();
        chest.setString("id", "minecraft:air");
        chest.setInt("Count", 1);
        armor.add(chest);

        NBTTagCompound head = new NBTTagCompound();
        head.setString("id", "minecraft:"+type);
        head.setInt("Count", 1);
        armor.add(head);

        armors.set("ArmorItems", armor);
        armors.setInt("Invisible", 1);
        a.a(armors);

        tile.getSpawner().setSpawnData(data);

    }
}
