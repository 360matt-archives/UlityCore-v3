package fr.ulity.core_v3.modules.commandHandlers.bukkit;

import java.util.ArrayList;
import java.util.Arrays;

public class TabCommandBukkit {
    private final int indice;
    private final String text;
    private final String permission;
    private final ArrayList<String> textAvant;

    public TabCommandBukkit(int indice, String text, String permission, String... textAvant) {
        this.indice = indice;
        this.text = text;
        this.permission = permission;
        if (textAvant == null || textAvant.length < 1)
            this.textAvant = null;
        else {
            this.textAvant = Arrays.<String>stream(textAvant).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }
    }

    public String getText() {
        return this.text;
    }

    public int getIndice() {
        return this.indice;
    }

    public String getPermission() {
        return this.permission;
    }

    public ArrayList<String> getTextAvant() {
        return this.textAvant;
    }

}
