package com.neeraj.redis;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.TransactionResult;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;
import com.lambdaworks.redis.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    RedisCommands<String, String> syncCommands;
    RedisAsyncCommands<String, String> asyncCommands;

    public void connectToRedis() {
        redisClient = RedisClient.create(RedisURI.create("redis://localhost:6379"));
        statefulRedisConnection = redisClient.connect();
        syncCommands = statefulRedisConnection.sync();
        asyncCommands = statefulRedisConnection.async();
        log.info("Connected to Redis using SSL");
    }

    public void closeAndShutdownRedisConnection() {
        statefulRedisConnection.close();
        redisClient.shutdown();
    }

    public void writeDateToRedis(String key, String value) {
        syncCommands.set(key, value);
    }

    public String getDataFromRedis(String key) {
        return syncCommands.get(key);
    }

    public void writeDataToRedisHashSet(String key, String field, String value) {
        syncCommands.hset(key, field, value);
    }

    public Map<String, String> getValuesFromHashSet(String key) {
        return syncCommands.hgetall(key);
    }

    public RedisFuture<String> getDataFromRedisUsingAsyncCommands(String key) {
        return asyncCommands.get(key);
    }

    // Redis Data Structures
    // List
    public void writeDataToRedisList(String key, String... values) {
        asyncCommands.lpush(key, values);
    }

    public void deleteKey(String key) {
        syncCommands.del(key);
    }

    public String getDataFromListInAscOrder(String key) {
        return syncCommands.lpop(key);
    }

    public String getDataFromListInDescOrder(String key) {
        return syncCommands.rpop(key);
    }

    // Sets
    public void writeDataToRedisSetDataStructure(String key, String... values) {
        asyncCommands.sadd(key, values);
    }

    public Set<String> getDataFromSet(String key) {
        return syncCommands.smembers(key);
    }

    // Transactions in Redis
    public TransactionResult transactionsInRedis() throws ExecutionException, InterruptedException {
        asyncCommands.multi();

        RedisFuture<String> result1 = asyncCommands.set("key1", "value1");
        RedisFuture<String> result2 = asyncCommands.set("key2", "value2");
        RedisFuture<String> result3 = asyncCommands.set("key3", "value3");

        RedisFuture<TransactionResult> execResult = asyncCommands.exec();

        // Here transaction is completed
        return execResult.get();
    }

}
