package app.core.config;


import app.core.log.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static app.core.config.ConfigLoader.load;

/**
 * Created by Ebrahim with ❤️ on 16 December 2019.
 */


public class InitStartup {
    private static final Logger log = Logger.getLogger(InitStartup.class);
    private String address = load().getLogAddress();

    public InitStartup() {
        log.info("Application is prepare for init start up");
        doInit();
    }


    public void doInit() {
        createDirectory(address);
    }


    private static void createDirectory(String address) {
        log.info(String.format("Create Directory into: %s", address));
        Path path = Paths.get(address);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error(String.format("There is error to directory creation IOException : {%s}", e.getMessage()));
                e.printStackTrace();
            }
        }
    }

}
