package fr.ulity.worldmanager.api;

public class Flag<T> {
    private final WorldStructureConfig worldStruct;
    private final T defaultValue;

    private String path;
    private String permission;

    public Flag (WorldStructureConfig worldConf, String id, T defaultValue) {
        this.worldStruct = worldConf;
        this.defaultValue = defaultValue;

        path = "flags." + id;
        permission = "WorldManager." + worldConf.worldname + "." + id;
    }

    public T getValue () { return worldStruct.worldConf.getOrSetDefault( path + ".value", defaultValue); }
    public void setValue (T stat) { worldStruct.worldConf.set(path + ".value", stat); }
    public String getPermission () { return worldStruct.worldConf.getOrSetDefault(path + ".permission", permission); }
    public void setPermission (String stat) { worldStruct.worldConf.set(path + ".permission", stat); }
    public String getMessage () { return worldStruct.worldConf.getOrSetDefault(path + ".message", "none"); }
    public void setMessage (String stat) { worldStruct.worldConf.set(path + ".message", stat); }
}
