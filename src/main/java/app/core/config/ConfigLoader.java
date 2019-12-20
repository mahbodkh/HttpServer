package app.core.config;

import app.core.util.PortHandler;

/**
 * Created by Ebrahim with ❤️ on 17 December 2019.
 */


public class ConfigLoader {
    private static ConfigLoader config = new ConfigLoader();
    private String packageName;


    public static ConfigLoader load() {
        if (config == null) {
            synchronized (config) {
                config = new ConfigLoader();
            }
        }
        return config;
    }


    private ConfigLoader() {
        loadConfig();
    }

    private long start = 0;
    private long finish = 0;
    private int port = 0;
    private String host = "";
    private String appName = "";
    private int timeout = 0;
    private int poolSize = 0;
    private String logPolicy = "";
    private String logAddress = "";

    private void loadConfig() {
        start = System.currentTimeMillis();
        port = PortHandler.getValidPortParam(ConfigReaderFile.getString("sever.port"));
        host = ConfigReaderFile.getString("sever.host");
        appName = ConfigReaderFile.getString("sever.name");
        timeout = Integer.parseInt(ConfigReaderFile.getString("timeout"));
        poolSize = Integer.parseInt(ConfigReaderFile.getString("thread.pool.size"));
        logPolicy = ConfigReaderFile.getString("log.write.disk");
        logAddress = ConfigReaderFile.getString("log.write.disk.address");
        packageName = ConfigReaderFile.getString("app.package.name");
    }

    public long getStart() {
        return start;
    }

    public long getFinish() {
        return finish;
    }

    public int getPort() {
        return port;
    }

    public String getAppName() {
        return appName;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public String getLogPolicy() {
        return logPolicy;
    }

    public String getLogAddress() {
        return logAddress;
    }

    public String getHost() {
        return host;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}