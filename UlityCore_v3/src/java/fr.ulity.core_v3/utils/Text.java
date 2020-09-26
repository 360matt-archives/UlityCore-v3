package fr.ulity.core_v3.utils;

import fr.ulity.core_v3.Core;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Text {
    private final List<String> input;
    private final List<String> outputList = new ArrayList<>();
    private final StringBuilder outputStr = new StringBuilder();

    private boolean colored = false;

    private boolean encoded = false;
    private boolean newline = false;
    private int begin = 0;

    public Text(String arg) {
        this.input = Arrays.stream(new String[]{arg}).map(String::valueOf).collect(Collectors.toList());
    }

    public Text(String[] args) {
        this.input = Arrays.stream(args).map(String::valueOf).collect(Collectors.toList());
    }

    public Text(List<?> args) {
        this.input = args.stream().map(String::valueOf).collect(Collectors.toList());
    }

    public static String getColored(String message) {
        if (Core.type.equals(Core.ServerApiType.BUKKIT))
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getConverted(String str) {
        return new String(str.getBytes(), StandardCharsets.UTF_8);
    }

    public Text setColored() { this.colored = true; return this; }
    public Text setEncoded() { this.encoded = true; return this; }
    public Text setNewLine() { this.newline = true; return this; }
    public Text setBeginging(int b) { this.begin = b; return this; }

    private void generate() {
        if (outputList.size() == 0 && outputStr.length() == 0)
            for (String x : this.input) {
                if (this.begin == 0) {
                    if (this.encoded) x = getConverted(x);
                    if (this.colored) x = getColored(x);
                    if (this.newline) x = x + "\n";

                    this.outputList.add(x);
                    this.outputStr.append(x);
                    continue;
                }
                this.begin--;
            }
    }

    public List<String> outputList() { generate(); return this.outputList; }
    public String[] outputArray() { generate(); return this.outputList.toArray(new String[0]); }
    public String outputString() { generate(); return this.outputStr.toString().replaceAll("\n$", ""); }
}