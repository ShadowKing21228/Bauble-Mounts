package net.shadowking21.baublemounts;

import java.util.ArrayList;
import java.util.List;

public class BMConfig {

    public int cooldownValue = 5;

    public boolean destructionUponRelease = true;

    public boolean brokenBaubleAppearance = true;

    public boolean isCustomTooltipRender = true;

    public List<String> mountList = new ArrayList<>(List.of("modname:entityname"));

    public static BMConfig config;
}
