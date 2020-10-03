package fr.ulity.worldmanager.api;

import fr.ulity.core_v3.modules.storage.ServerConfig;
import fr.ulity.core_v3.utils.SerializeLocation;
import org.bukkit.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WorldStructureConfig {
    public static HashMap<String, WorldStructureConfig> worldsImported = new HashMap<>();
    private WorldStructureConfig classInstance;


    public final class FlagStructure {
        public Flag<Boolean> pvpAllowed = new Flag<>(classInstance, "pvp", true);

        public Flag<Boolean> placeAllowed = new Flag<>(classInstance, "block.place", true);
        public Flag<Boolean> brokeAllowed = new Flag<>(classInstance, "block.broke", true);

        public Flag<Boolean> tntAllowed = new Flag<>(classInstance, "block.tnt", true);

        public Flag<Boolean> mobSpawn = new Flag<>(classInstance, "mobs.spawn", true);
        public Flag<Boolean> mobAttack = new Flag<>(classInstance, "mobs.attack", true);
        public Flag<Boolean> mobInvincible = new Flag<>(classInstance, "mobs.invincible", false);

        public Flag<Boolean> animalSpawn = new Flag<>(classInstance, "animal.spawn", true);
        public Flag<Boolean> animalAttack = new Flag<>(classInstance, "animal.attack", true);
        public Flag<Boolean> animalInvincible = new Flag<>(classInstance, "animal.invincible", false);

        public Flag<Boolean> weatherChange = new Flag<>(classInstance, "weather", true);
        public Flag<Boolean> timeChange = new Flag<>(classInstance, "time", true);

    }

    public class DataStructure {
        public Location getSpawnLocation () {
            return SerializeLocation.getDeserializedLocation(
                    worldConf.getOrSetDefault(
                            "data.spawn",
                            SerializeLocation.getSerializedLocation(world.getSpawnLocation())
                    )
            );
        }
        public void setSpawnLocation (Location spawn) { worldConf.set("data.spawn", SerializeLocation.getSerializedLocation(spawn)); }
    }


    public DataStructure datas;
    public FlagStructure flags;
    public World world;
    public String worldname;
    public ServerConfig worldConf;

    public WorldStructureConfig (String worldname) { generate(worldname, new WorldProperties()); }
    public WorldStructureConfig (String worldname, WorldProperties properties) { generate(worldname, properties); }

    public static class WorldProperties {
        public long seed = 0;
        public World.Environment environment = World.Environment.NORMAL;
        public WorldType type = WorldType.NORMAL;
        public boolean isVoid = false;
    }


    private void generate (String worldname, WorldProperties properties) {
        classInstance = this;
        if (worldsImported.containsKey(worldname)) {
            WorldStructureConfig instance = worldsImported.get(worldname);

            this.worldConf = instance.worldConf;
            this.worldname = instance.worldname;
            this.world = instance.world;
            this.datas = instance.datas;
            this.flags = instance.flags;
        } else {
            this.worldConf = new ServerConfig(worldname, "worlds");
            this.datas = new DataStructure();
            this.flags = new FlagStructure();
            this.worldname = worldname;
            this.world = CreateOrImport.process(this, properties);

            worldsImported.put(worldname, this);

            reload();
        }




    }


    public void initFlag (Flag<?> flag) {
        flag.getValue();
        flag.getMessage();
        flag.getPermission();
    }



    public void reload () {
        // I call getter in order to define default values, and define gamerules

        initFlag(flags.pvpAllowed);
        initFlag(flags.placeAllowed);
        initFlag(flags.brokeAllowed);
        flags.tntAllowed.getValue();
        flags.animalSpawn.getValue();
        initFlag(flags.animalAttack);
        initFlag(flags.animalInvincible);
        initFlag(flags.mobAttack);
        initFlag(flags.mobInvincible);

        world.setGameRule(GameRule.DO_WEATHER_CYCLE, this.flags.weatherChange.getValue());
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, this.flags.timeChange.getValue());
        world.setGameRule(GameRule.DO_MOB_SPAWNING, this.flags.mobSpawn.getValue());

        world.setSpawnLocation(this.datas.getSpawnLocation());

        HashMap<String, List<String>> portalDefault = new HashMap<>();
        portalDefault.put("X", Arrays.asList("ANY_BLOCK", "TWO", "THREE"));
        worldConf.setDefault("portals", portalDefault);

    }


}
