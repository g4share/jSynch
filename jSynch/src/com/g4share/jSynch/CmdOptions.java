package com.g4share.jSynch;

import com.g4share.jSynch.guice.BinderEnvironment;
import com.g4share.jSynch.log.LogLevel;

/**
 * User: gm
 * Date: 3/12/12
 */
public final class CmdOptions {
    private String parseError;
    
    private LogLevel logLevel = LogLevel.NONE;
    private BinderEnvironment environment = BinderEnvironment.getDefault();

    private boolean h;
    private boolean status;
    private String homeFolder;

    private CmdOptions() {}

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public boolean showHint() {
        return h;
    }

    public boolean showStatus() {
        return status;
    }

    public BinderEnvironment getEnvironment() {
        return environment;
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
                case "-stat" :
                    options.status = true;
                    break;
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
                case "-env" :
                    if (args.length < ++i + 1){
                        options.parseError = "Please specify environment value.\n";
                        options.h = true;
                        return options;
                    }
                    options.environment = BinderEnvironment.fromString(args[i]);
                    if  (options.environment == null) {
                        options.environment = BinderEnvironment.getDefault();
                        options.parseError = "Incorrect value for environment. Please specify a valid one.\n";
                        options.h = true;
                        return options;
                    }
                    break;
                case "-home" :
                    if (args.length < ++i + 1){
                        options.parseError = "Please specify home folder.\n";
                        options.h = true;
                        return options;
                    }
                    options.homeFolder = args[i];
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

    public String getHomeFolder() {
        return homeFolder;
    }

    public static String getHint(){
        return "Use java -jar jSynch.jar [-h ]\n" +
               "                         [-stat]\n" +
               "                         [-home {home folder}]\n" +
               "                         [-level TRACE|INFO|ERROR|FATAL|CRITICAL|NONE]\n" +
               "                         [-env PRODUCTION|DEVELOPMENT\n" +
               "    -h                                        : show this help;\n" +
               "    -stat                                     : get status info;\n" +
               "    -home                                     : home folder (location where config file is located);\n" +
               "    -env                                      : set the environment;\n" +
               "    -level TRACE|INFO|ERROR|FATAL|NONE        : set the LogLevel to the specified one;\n" +
               "This LogLevel overrides the config value (if any).\n" +
               "Config.xml file should be located in the same folder as the jSynch.jar file.\n" +
                "   Thank you for choosing us.";
    }
}
