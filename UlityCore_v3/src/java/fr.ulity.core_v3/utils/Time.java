package fr.ulity.core_v3.utils;

import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.core_v3.modules.storage.Config;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {
    public int seconds;
    public int milliseconds;
    public String text = "";

    private Config playerLang = Lang.getPreferedLang(Lang.getDefaulLang());

    public Time(String text) {
        secondFromText(text);
        this.milliseconds = this.seconds * 1000;
        textFromSeconds(this.seconds);
    }

    public Time(String text, Object lang) {
        this.playerLang = Lang.getPreferedLang(lang);

        secondFromText(text);
        this.milliseconds = this.seconds * 1000;
        textFromSeconds(this.seconds);
    }

    public Time(int seconds) {
        this.seconds = seconds;
        this.milliseconds = this.seconds * 1000;
        textFromSeconds(this.seconds);
    }

    public Time(int seconds, Object player) {
        this.playerLang = Lang.getPreferedLang(player);

        this.seconds = seconds;
        this.milliseconds = this.seconds * 1000;
        textFromSeconds(this.seconds);
    }

    private void secondFromText(String text) {
        Pattern pattern = Pattern.compile("([0-9]+)([A-z])", 8);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int number = Integer.parseInt(matcher.group(1));
            String multiplier = matcher.group(2);

            this.seconds += (multiplier.equals("s") ? number : 0) +
                    (multiplier.equals("m") ? (number * 60) : 0) +
                    (multiplier.equals("h") ? (number * 60 * 60) : 0) +
                    (multiplier.equals("d") || multiplier.equals("j") ? (number * 60 * 60 * 24) : 0) +
                    (multiplier.equals("w") ? (number * 60 * 60 * 24 * 7) : 0) +
                    (multiplier.equals("o") ? (number * 60 * 60 * 24 * 31) : 0) +
                    (multiplier.equals("y") ? (number * 60 * 60 * 24 * 365) : 0);
        }
    }

    private String exp(String exp) {
        return (exp.equals("separator") ? "" : " ") + this.playerLang.getString("period." + exp);
    }

    private void textFromSeconds(int seconds) {
        HashMap<String, Integer> degre = new HashMap<>();
        degre.put("year", (int) Math.floor(seconds / 31536000));
        seconds -= degre.get("year") * 31536000;
        degre.put("month", (int) Math.floor(seconds / 2592000));
        seconds -= degre.get("month") * 2592000;
        degre.put("day", (int) Math.floor(seconds / 86400));
        seconds -= degre.get("day") * 86400;
        degre.put("hour", (int) Math.floor(seconds / 3600));
        seconds -= degre.get("hour") * 3600;
        degre.put("minute", (int) Math.floor(seconds / 60));
        seconds -= degre.get("minute") * 60;
        degre.put("second", seconds);

        AtomicBoolean comma = new AtomicBoolean(false);
        degre.forEach((key, value) -> {
            if (value != 0) {
                this.text += (comma.get() ? ", " : "") + value + ((value > 1) ? exp(key + "s") : exp(key));
                comma.set(true);
            }
        });
    }
}