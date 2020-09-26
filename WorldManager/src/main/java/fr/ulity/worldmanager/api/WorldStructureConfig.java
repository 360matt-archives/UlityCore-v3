package fr.ulity.worldmanager.api;

import fr.ulity.core_v3.modules.storage.ServerConfig;
import org.bukkit.World;

public class WorldStructureConfig {
    public static final class FlagStructure {
        BlockStructure block = new BlockStructure();
        PlayerStructure player = new PlayerStructure();

        public static final class BlockStructure {
            boolean place;
            boolean broke;
        }

        public static final class PlayerStructure {
            boolean pvp;
        }



    }
    public static final class PermissionStructure {
        BlockStructure block = new BlockStructure();
        PlayerStructure player = new PlayerStructure();

        public static final class BlockStructure {
            String place;
            String broke;
        }

        public static final class PlayerStructure {
            String pvp;
        }



    }

    public final class DataStructure {
        public GlobalData global = new GlobalData();

        public final class GlobalData {

        }


    }




    private final FlagStructure flags;
    private final PermissionStructure perms;
    private final DataStructure datas;
    private final World world;
    private final ServerConfig worldConf;

    public WorldStructureConfig (World world) {
        this.world = world;
        this.worldConf = new ServerConfig(world.getName(), "worlds");
        this.flags = new FlagStructure();
        this.datas = new DataStructure();
        this.perms = new PermissionStructure();

        reload();
        save();
    }




    public void save () {
        worldConf.set("flags.player.pvp",  flags.player.pvp);
        worldConf.set("flags.block.place", flags.block.place);
        worldConf.set("flags.block.broke", flags.block.broke);

        worldConf.set("perms.player.pvp",  perms.player.pvp);
        worldConf.set("perms.block.place", perms.block.place);
        worldConf.set("perms.block.broke", perms.block.broke);


    }

    public void reload () {
        flags.player.pvp  = worldConf.getOrSetDefault("flags.player.pvp",  true);
        flags.block.place = worldConf.getOrSetDefault("flags.block.place", true);
        flags.block.broke = worldConf.getOrSetDefault("flags.block.broke", true);

        perms.player.pvp  = worldConf.getOrSetDefault("perms.player.pvp",  "WorldManager." + world.getName() + ".pvp");
        perms.block.place = worldConf.getOrSetDefault("perms.block.place", "WorldManager." + world.getName() + ".place");
        perms.block.broke = worldConf.getOrSetDefault("perms.block.broke", "WorldManager." + world.getName() + ".broke");



    }


}
