package me.gv7.woodpecker.plugin;

import me.gv7.woodpecker.plugin.helper.Cron;
import me.gv7.woodpecker.plugin.helper.Dict;
import me.gv7.woodpecker.plugin.helper.Gopher;
import me.gv7.woodpecker.plugin.helper.Payload;

import java.util.ArrayList;

public class HelperPlugin implements IHelperPlugin{
    public static IHelperPluginCallbacks callbacks;
    public static IPluginHelper pluginHelper;
    @Override
    public void HelperPluginMain(IHelperPluginCallbacks iHelperPluginCallbacks) {
        this.callbacks = iHelperPluginCallbacks;
        this.pluginHelper = iHelperPluginCallbacks.getPluginHelper();
        iHelperPluginCallbacks.setHelperPluginName("ssrf-redis");
        iHelperPluginCallbacks.setHelperPluginAutor("Xm17");
        iHelperPluginCallbacks.setHelperPluginVersion("0.0.1");
        iHelperPluginCallbacks.setHelperPluginDescription("HelperPlugin");
        iHelperPluginCallbacks.registerHelper(new ArrayList<IHelper>(){{
            add(new Payload());
            add(new Dict());
            add(new Cron());
            add(new Gopher());
        }});

    }
}
