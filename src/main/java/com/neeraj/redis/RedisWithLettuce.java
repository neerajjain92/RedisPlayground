package com.neeraj.redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

/**
 * Class to experiment with Redis Client {@link com.lambdaworks.redis.RedisClient}
 *
 * @author neeraj on 2019-03-17
 * Copyright (c) 2019, RedisPlayground.
 * All rights reserved.
 */
@Slf4j
public class RedisWithLettuce {

    RedisClient redisClient;
    StatefulRedisConnection<String, String> statefulRedisConnection;
    RedisCommands<String, String> commands;

    public void connectToRedis() {
        redisClient = RedisClient.create(RedisURI.create("redis://localhost:6379"));
        statefulRedisConnection = redisClient.connect();
        commands = statefulRedisConnection.sync();
        log.info("Connected to Redis using SSL");
    }

    public void closeAndShutdownRedisConnection() {
        statefulRedisConnection.close();
        redisClient.shutdown();
    }

    public void writeDateToRedis(String key, String value) {
        commands.set(key, value);
    }

    public String getDataFromRedis(String key) {
        return commands.get(key);
    }
}
