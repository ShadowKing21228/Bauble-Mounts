package net.shadowking21.baublemounts.forge.config;

import net.shadowking21.shadowconfig.annotation.ConfigComment;

import java.util.ArrayList;
import java.util.List;

public class BMConfigForge {

    @ConfigComment("This is cooldown value of mount spawn in SECONDS." +
            " Default: 5")
    public int cooldownValue = 5;

    @ConfigComment("If true, Mount Bauble will be destroyed when the mount is released," +
            " otherwise the Mount Bauble will remain in your hand without the mount inside it." +
            " Default: true")
    public boolean destructionUponRelease = true;

    @ConfigComment("If true, Broken Mount Bauble will appear in the slot when the mount dies," +
            " otherwise, it will not appear and the mount cannot be resurrected." +
            " Default: true")
    public boolean brokenBaubleAppearance = true;

    @ConfigComment("If true, custom tooltip is shows on Bauble Mounts items " +
            "Default: true")
    public boolean isCustomTooltipRender = true;

    @ConfigComment("A blacklist of entities (It is assumed that mounts) that cannot be placed in Mount Bauble")
    public List<String> mountList = new ArrayList<>(List.of("modname:entityname"));
}
