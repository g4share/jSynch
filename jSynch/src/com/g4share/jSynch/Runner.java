package com.g4share.jSynch;

import com.g4share.jSynch.guice.*;
import com.g4share.jSynch.log.FileLoggerProperties;
import com.g4share.jSynch.log.Logger;
import com.g4share.jSynch.log.LoggerProperties;
import com.g4share.jSynch.share.Constants;
import com.g4share.jSynch.share.SynchManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.FactoryModuleBuilder;

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
        String currentPath = getJarLocation();

        Injector injector = Guice.createInjector(new BindModule());
        Logger defaultLogger = injector.getInstance(Key.get(Logger.class, DefaultLogger.class));
        defaultLogger.logEvent(currentPath);

        if (currentPath == null){
            defaultLogger.logFatal("Could not get current path.");
            System.exit(1);
        }

        String configPath = "aa";//FSHelper.combine(currentPath, "config.xml");

        FileLoggerPropertiesFactory fileLoggerPropertiesFactory = injector.getInstance(FileLoggerPropertiesFactory.class);
        FileLoggerFactory fileLoggerFactory = injector.getInstance(FileLoggerFactory.class);
        Logger fileLogger = fileLoggerFactory.create(fileLoggerPropertiesFactory.create(configPath));

        FSSynchManagerFactory fsSynchManagerFactory = injector.getInstance(FSSynchManagerFactory.class);
        SynchManager synchManager = fsSynchManagerFactory.create(fileLogger);

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
        return jarPath.substring(0, jarPath.lastIndexOf(Constants.JAVA_PATH_DELIMITER));
    }

}
