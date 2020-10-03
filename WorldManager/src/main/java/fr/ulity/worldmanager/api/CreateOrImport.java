package fr.ulity.worldmanager.api;

import fr.ulity.worldmanager.bukkit.WorldManagerBukkit;
import fr.ulity.worldmanager.bukkit.mechanicEvent.KeepSpawnInMemoryEvent;
import org.bukkit.*;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class CreateOrImport {


    public static World process (WorldStructureConfig struc, WorldStructureConfig.WorldProperties properties) {
        return (WorldUtils.exist(struc.worldname) && struc.worldConf.contains("gen"))
                ? importWorld(struc)
                : create(struc, properties);
    }


    public static World create (WorldStructureConfig struc, WorldStructureConfig.WorldProperties properties) {
        World world = Bukkit.getWorld(struc.worldname);
        if (world == null) {
            WorldCreator creator = new WorldCreator(struc.worldname);
            creator.environment(properties.environment);
            creator.type(properties.type);
            if (properties.seed != 0)
                creator.seed(properties.seed);

            if (properties.type.equals(WorldType.FLAT))
                creator.generateStructures(false);

            if (properties.isVoid) {
                properties.type = WorldType.FLAT;
                creator.type(WorldType.FLAT);
                creator.generator(new ChunkGenerator() {
                    @Override
                    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
                        ChunkData chunkData = createChunkData(world);
                        if (x == 0 && z == 0) {
                            chunkData.setBlock(0, 90, 0, Material.STONE);
                            world.setSpawnLocation(0, 90, 0);
                        }
                        return chunkData;
                    }
                });
            }

            Listener listener = new KeepSpawnInMemoryEvent();

            WorldManagerBukkit.plugin.registerEvent(listener);
            world = creator.createWorld();
            WorldManagerBukkit.plugin.unregisterEvent(listener);

            struc.worldConf.set("gen.seed", properties.seed);
            struc.worldConf.set("gen.env", properties.environment);
            struc.worldConf.set("gen.type", properties.type);
            struc.worldConf.set("gen.void", properties.isVoid);

        } else {
            struc.worldConf.set("gen.seed", world.getSeed());
            struc.worldConf.set("gen.env", world.getEnvironment());
            struc.worldConf.set("gen.type", world.getWorldType().getName().replaceAll("DEFAULT", "NORMAL"));
            struc.worldConf.set("gen.void", false);

        }


        return world;
    }

    public static World importWorld (WorldStructureConfig struc) {
        WorldStructureConfig.WorldProperties properties = new WorldStructureConfig.WorldProperties();
        if (struc.worldConf.contains("gen")) {
            properties.environment = World.Environment.valueOf(struc.worldConf.getString("gen.env").toUpperCase());
            properties.type = WorldType.getByName(struc.worldConf.getString("gen.type").toUpperCase());
            properties.seed = struc.worldConf.getLong("gen.seed");
            properties.isVoid = struc.worldConf.getBoolean("gen.void");
        }
        return create(struc, properties);
    }

}
