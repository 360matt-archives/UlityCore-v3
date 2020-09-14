package fr.ulity.core_v3.modules.language;

import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.bukkit.BukkitAPI;
import fr.ulity.core_v3.bungeecord.BungeeAPI;
import fr.ulity.core_v3.modules.storage.Config;
import fr.ulity.core_v3.utils.ListingResources;
import fr.ulity.core_v3.utils.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lang {
    public static HashMap<String, Config> langDatas = new HashMap<>();

    public static void importFrom(Class clazz, String packagePath) {
        packagePath = packagePath.replaceAll("\\.", "/");

        try {
            for (String x : ListingResources.getResourceListing(clazz, packagePath)) {
                Matcher m = Pattern.compile("(.*).yml").matcher(x);
                if (m.find()) {
                    URL urlLooped = clazz.getClassLoader().getResource(packagePath + x);
                    if (urlLooped != null) {
                        InputStream in = urlLooped.openStream();

                        Config lang = new Config(m.group(1), "languages/" + Core.type.toString().toLowerCase());
                        lang.addDefaultsFromInputStream(in);
                        langDatas.put(m.group(1), lang);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static String getDefaulLang() {
        return Core.config.getString("global.lang");
    }

    public static void reload() {
        Class<?> clazz = Core.type.equals(Core.ServerApiType.BUKKIT) ? BukkitAPI.plugin.getClass() : BungeeAPI.plugin.getClass();

        langDatas.clear();

        importFrom(clazz, clazz.getPackage().getName() + "/languages/");
        importFrom(Core.class, Core.class.getPackage().getName() + "/common/languages/");
        importFrom(Core.class, Core.class.getPackage().getName() + "/" + Core.type.toString().toLowerCase() + "/languages/");
    }


    public static Config getPreferedLang(Object arg) {
        String langIso = "";

        if (arg instanceof String)
            langIso = arg.toString();
        else if (Core.type.equals(Core.ServerApiType.BUKKIT) && arg instanceof Player)
            langIso = ((Player) arg).getLocale().toLowerCase().split("_")[0];
        else if (Core.type.equals(Core.ServerApiType.BUNGEE))
            langIso = ((ProxiedPlayer) arg).getLocale().getLanguage();

        if (langDatas.containsKey(langIso))
            return langDatas.get(langIso);

        File file = new File(Core.basePath.getPath() + "/languages/" + Core.type.toString().toLowerCase() + File.separator + langIso + ".yml");
        if (file.exists()) {
            Config lang = new Config(file);
            langDatas.put(langIso, lang);
            return lang;
        }
        return langDatas.get(getDefaulLang());
    }


    private static String convertText(String str) {
        return Text.getColored(
                Text.getConverted(str));
    }

    public static String get(String exp) {
        return convertText(langDatas.get(Core.config.getString("global.lang")).getString(exp));
    }

    public static String get(Player arg, String exp) {
        return convertText(getPreferedLang(arg).getString(exp));
    }

    public static String get(CommandSender arg, String exp) {
        return convertText(getPreferedLang(arg).getString(exp));
    }

    public static String get(ProxiedPlayer arg, String exp) {
        return convertText(getPreferedLang(arg).getString(exp));
    }

    public static int getInt(String exp) {
        return getInt(getDefaulLang(), exp);
    }

    public static int getInt(Object lang, String exp) {
        return getPreferedLang(lang).getInt(exp);
    }

    public static List getList(String exp) {
        return getList(getDefaulLang(), exp);
    }

    public static List getList(Object lang, String exp) {
        return (new Text(getPreferedLang(lang).getList(exp)))
                .setEncoded()
                .setColored()
                .outputList();
    }

    public static String[] getArray(String exp) {
        return (new Text(getList(exp))).outputArray();
    }

    public static String[] getArray(Object arg, String exp) {
        return (new Text(getList(arg, exp))).outputArray();
    }

    public static boolean getBoolean(String exp) {
        return getBoolean(getDefaulLang(), exp);
    }

    public static boolean getBoolean(Object arg, String exp) {
        return getPreferedLang(arg).getBoolean(exp);
    }

    public static PreparedLang prepare(String exp) {
        return new PreparedLang(exp);
    }
}