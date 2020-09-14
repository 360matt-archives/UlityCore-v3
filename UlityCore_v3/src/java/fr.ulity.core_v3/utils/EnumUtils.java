package fr.ulity.core_v3.utils;

import java.util.List;

public class EnumUtils {
    public static boolean contains(List list, String name) {
        for (Object str : list)
            if (str.toString().equalsIgnoreCase(name))
                return true;
        return false;
    }
}