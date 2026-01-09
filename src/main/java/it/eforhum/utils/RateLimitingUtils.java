package it.eforhum.utils;

import java.util.List;
import java.util.logging.Logger;


public class RateLimitingUtils {

    private static final Logger logger = Logger.getLogger(RateLimitingUtils.class.getName());
    private static final List<String> whitelistIps = 
        List.of(System.getenv("whitelist_ips") != null ? System.getenv("whitelist_ips").split(",") : new String[0]);

    private RateLimitingUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isWhitelisted(String ip){
        return whitelistIps.contains(ip) || whitelistIps.isEmpty();
    }
}
