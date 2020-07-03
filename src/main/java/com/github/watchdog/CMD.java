package com.github.watchdog;


import com.github.watchdog.common.CMDUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;


public class CMD extends CMDUtil {


    static final Options OPTIONS = new Options();

    public static final Option LOGLEVEL = Option.builder().longOpt("logLevel").hasArg(true).required(false).desc("root logger level (info)").build();

    static final Option SSL = Option.builder().longOpt("ssl").hasArg(false).required(false).desc("使用ssl (false)").build();

    public static final Option HOST = Option.builder().longOpt("host").hasArg(true).required(false).desc("ip or address (*)").build();

    public static final Option PORT = Option.builder().longOpt("port").hasArg(true).required(false).desc("port (443)").build();

    public static final Option PATH = Option.builder().longOpt("path").hasArg(true).required(false).desc("path (/ws)").build();

    static final Option HEARTBEAT = Option.builder().longOpt("heartBeat").hasArg(true).required(false).desc("发送心跳包的字符串,时间戳用%s占位 (*)").build();

    static final Option HEARTBEATINTERVAL = Option.builder().longOpt("heartBeatInterval").hasArg(false).required(false).desc("心跳间隔时间 (10s)").build();

    static final Option SUBSCRIBE = Option.builder().longOpt("subscribe").hasArg(true).required(false).desc("订阅请求的字符串 (*)").build();

    static final Option SUBSCRIBEINTERVAL = Option.builder().longOpt("subscribeInterval").hasArg(false).required(false).desc("订阅请求间隔时间 (5ms)").build();

    static final Option MARKET = Option.builder().longOpt("market").hasArg(true).required(false).desc("交易所名称 (*)").build();

    static final Option MARKETCONFIG = Option.builder().longOpt("marketConfig").hasArg(true).required(false).desc("Market配置").build();

    static final Option BARKIDS = Option.builder().longOpt("barkIds").hasArg(true).required(false).desc("通知用户的BarkID,逗号分割 (*)").build();

    static {
        OPTIONS.addOption(HELP).addOption(LOGLEVEL)
                .addOption(SSL).addOption(HOST).addOption(PORT).addOption(PATH)
                .addOption(HEARTBEAT).addOption(HEARTBEATINTERVAL)
                .addOption(SUBSCRIBE).addOption(SUBSCRIBEINTERVAL)
                .addOption(MARKET).addOption(MARKETCONFIG)
                .addOption(BARKIDS);
    }

    public static boolean HELP(CommandLine commandLine) {

        return HELP(commandLine, OPTIONS);
    }
}
