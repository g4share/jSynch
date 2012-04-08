package com.g4share.jSynch;

import com.g4share.jSynch.guice.Factory.PointStoreHelperFactory;
import com.g4share.jSynch.guice.GuiceAbstractBinderHelper;
import com.g4share.jSynch.guice.GuiceBinderFactoryHelper;
import com.g4share.jSynch.log.LogLevel;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.share.ConfigInfo;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.SynchManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * User: gm
 * Date: 3/8/12
 * Time: 12:20 AM
 */
public class Runner {
    public static void main(String[] args){

        CmdOptions options = CmdOptions.parse(args);
        String error = options.getParseError();


        String currentPath = getJarLocation();
        GuiceAbstractBinderHelper binderFactory = new GuiceBinderFactoryHelper(options.getEnvironment()).getBinderHelper(currentPath);
        Logger defaultLogger = binderFactory.getDefaultLogger();

        //check command line options
        if (error != null) {
            defaultLogger.logEvent(LogLevel.CRITICAL, error );
        }

        //override LogLevel
        if (options.getLogLevel() != LogLevel.NONE) defaultLogger.setLevel(options.getLogLevel());

        if (options.showHint()) {
            defaultLogger.logEvent(LogLevel.CRITICAL, CmdOptions.getHint());
        }
        if (error != null || options.showHint()) {
            System.exit(1);
        }


        final Logger fileLogger = binderFactory.getLogger();
        //override LogLevel
        if (options.getLogLevel() != LogLevel.NONE) fileLogger.setLevel(options.getLogLevel());

        final SynchManager synchManager = binderFactory.getSynchManager();

        final ConfigInfo cInfo = binderFactory.readConfigInfo();
        if (cInfo == null){
            fileLogger.logEvent(LogLevel.TRACE, "Could not read configuration. Exit.");
            System.exit(1);
        }

        final PointStoreHelperFactory pointStoreHelperFactory = binderFactory.getPointStoreHelperFactory();

        Worker worker = new Worker(pointStoreHelperFactory, synchManager, fileLogger);
        worker.Synch(cInfo);
    }


    private static String getJarLocation() {
        final ProtectionDomain domain;
        final CodeSource source;
        final URL url;
        final URI uri;

        domain = Runner.class.getProtectionDomain();
        source = domain.getCodeSource();
        url  = source.getLocation();

        try {
            uri = url.toURI();
        } catch (URISyntaxException ex) {
            return null;
        }

        String jarPath = uri.getPath();
        //return "/E:/gitStore/jSynch/config";

        return jarPath.substring(0, jarPath.lastIndexOf(Constants.JAVA_PATH_DELIMITER));
    }
}
