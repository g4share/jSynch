package com.g4share.jSynch;

import com.g4share.jSynch.log.LogLevel;

/**
 * User: gm
 * Date: 3/12/12
 */
public final class CmdOptions {
    private String parseError;
    
    private LogLevel logLevel = LogLevel.getDefaultLevel();
    private boolean h;

    private CmdOptions() {}

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public boolean showHint() {
        return h;
    }

    public static CmdOptions parse(String[] args){
        CmdOptions options = new CmdOptions();

        for (int i=0; i < args.length; i++){
            switch (args[i]){
                case "-h" :
                    if (args.length > 1){
                        options.parseError = "\"-h\" should be the only parameter.\n";
                    }
                    options.h = true;
                    return options;
                case "-level" :
                    if (args.length < ++i + 1){
                        options.parseError = "Please specify LogLevel value.\n";
                        options.h = true;
                        return options;
                    }
                    options.logLevel = LogLevel.fromString(args[i]);
                    if  (options.logLevel == LogLevel.NONE) {
                        options.parseError = "Incorrect value for LogLevel. Please specify a valid one.\n";
                        options.h = true;
                        return options;
                    }
                    break;
                default:
                    options.parseError = "The \"" + args[i] + "\" key is not recognized.\n";
                    options.h = true;
                    return options;

            }
        }

        return options;
    }

    public String getParseError() {
        return parseError;
    }

    public static String getHint(){
        return "Use java -jar jSynch.jar [-h ][-level TRACE|INFO|ERROR|FATAL|NONE]\n" +
               "    -h                                        : show this help;\n" +
               "    -level TRACE|INFO|ERROR|FATAL|NONE        : set the LogLevel to the specified one;\n" +
               "This LogLevel is overridden by the config value (if any).\n" +
               "Config.xml file should be located in the same folder as the jSynch.jar file.\n" +
                "   Thank you for choosing us.";
    }
}
