package fpt.gymmanagement.service;

public interface CacheService {
    void save(String key, String value, long timeLife);

    void invalidCache(String key);

    String getByKey(String key);
}
