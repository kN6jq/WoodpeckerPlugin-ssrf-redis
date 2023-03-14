package me.gv7.woodpecker.plugin.helper;

import me.gv7.woodpecker.plugin.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Payload implements IHelper {
    /**
     * @return
     */
    @Override
    public String getHelperTabCaption() {
        return "Payload";
    }

    /**
     * @return
     */
    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        List<IArg> args = new ArrayList<>();
        final IArg command = HelperPlugin.pluginHelper.createArg();
        command.setName("command");
        command.setDefaultValue("whoami");
        command.setDescription("需要转换的命令");
        command.setRequired(true);
        command.setType(0);
        args.add(command);

        final IArg type = HelperPlugin.pluginHelper.createArg();
        type.setName("type");
        type.setDefaultValue("encode/decode");
        type.setDescription("编码/解码");
        type.setRequired(true);
        type.setType(0);
        args.add(type);

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
        String type = (String) map.get("type");
        if (type.equals("encode")) {
            String payload = Utils.hexToString(command);
            iResultOutput.successPrintln(payload);
        } else if (type.equals("decode")) {
            String payload = Utils.decodeHex(command);
            iResultOutput.successPrintln(payload);
        } else {
            iResultOutput.errorPrintln("type参数错误");
        }
    }
}
