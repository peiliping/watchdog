package com.github.watchdog.common;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.Validate;

import java.util.Collection;


public class CMDUtil {


    public static final Option HELP = Option.builder().longOpt("help").hasArg(false).required(false).desc("help").build();


    public static boolean HELP(CommandLine commandLine, Options options) {

        if (!hasOption(commandLine, HELP)) {
            return false;
        }
        Collection<Option> collection = options.getOptions();
        System.out.println(String.format("%-18s %-7s %-10s", "Name", "HasArg", "Description"));
        for (Option op : collection) {
            String c = String.format("%-18s %-7s %-10s", op.getLongOpt(), op.hasArg(), op.getDescription());
            System.out.println(c);
        }
        return true;
    }


    public static boolean hasOption(CommandLine commandLine, Option option) {

        return commandLine.hasOption(option.getLongOpt());
    }


    public static <V> V getValue(CommandLine commandLine, Option option, Convert<V> convert, V defaultValue) {

        if (hasOption(commandLine, option)) {
            return convert.eval(commandLine.getOptionValue(option.getLongOpt()));
        }
        return defaultValue;
    }


    public static <V> V getValueForce(CommandLine commandLine, Option option, Convert<V> convert) {

        if (hasOption(commandLine, option)) {
            return convert.eval(commandLine.getOptionValue(option.getLongOpt()));
        }
        Validate.isTrue(false, option.getLongOpt() + " is null");
        return null;
    }


    public interface Convert<V> {


        V eval(String s);
    }

}
