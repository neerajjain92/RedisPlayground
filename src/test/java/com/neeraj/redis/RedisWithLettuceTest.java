package com.neeraj.redis;

import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.TransactionResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * @author neeraj on 2019-03-17
 * Copyright (c) 2019, RedisPlayground.
 * All rights reserved.
 */
@Slf4j
public class RedisWithLettuceTest {
    RedisWithLettuce redisWithLettuce;

    @Before
    public void init() {
        redisWithLettuce = new RedisWithLettuce();
        redisWithLettuce.connectToRedis();
    }

    @After
    public void cleanup() {
        redisWithLettuce.closeAndShutdownRedisConnection();
    }

    @Test
    public void testIfConnectionToRedisIsSuccessful() {
        assertTrue("Connection to Redis is successful", redisWithLettuce.statefulRedisConnection.isOpen());
    }

    @Test
    public void whenCloseAndShutdownRedisConnectionIsCalled_ItDisconnectsFromRedis() {
        assertTrue("Connection to Redis is successful", redisWithLettuce.statefulRedisConnection.isOpen());
        redisWithLettuce.closeAndShutdownRedisConnection();
        assertFalse("Connection to Redis is closed", redisWithLettuce.statefulRedisConnection.isOpen());
    }

    @Test
    public void testOperationsInRedis() {
        redisWithLettuce.writeDateToRedis("application.name", "RedisPlayground");
        assertEquals("RedisPlayground", redisWithLettuce.getDataFromRedis("application.name"));
    }

    @Test
    public void testWriteAndGetDataFromRedisHashSet() {
        redisWithLettuce.writeDataToRedisHashSet("employee", "firstName", "Neeraj");
        redisWithLettuce.writeDataToRedisHashSet("employee", "lastName", "Jain");
        redisWithLettuce.writeDataToRedisHashSet("employee", "employeeId", "56421");
        redisWithLettuce.writeDataToRedisHashSet("employee", "employer", "Apple");

        Map<String, String> values = redisWithLettuce.getValuesFromHashSet("employee");

        assertEquals(4, values.size());
    }

    @Test
    public void getDataFromRedisUsingAsyncCommands() throws ExecutionException, InterruptedException {
        redisWithLettuce.writeDateToRedis("testing:async", "Using StateFulRedis Connection's async command");
        RedisFuture<String> future = redisWithLettuce.getDataFromRedisUsingAsyncCommands("testing:async");

        String value = future.get();
        assertNotNull(value);
        assertEquals("Using StateFulRedis Connection's async command", value);
    }

    @Test
    public void testListDataStructureWithRedis() {
        redisWithLettuce.writeDataToRedisList("tasks", "Review PR", "Increase Code-Coverage", "Integrate Sonar");

        assertEquals("Integrate Sonar", redisWithLettuce.getDataFromListInAscOrder("tasks"));
        assertEquals("Review PR", redisWithLettuce.getDataFromListInDescOrder("tasks"));

        redisWithLettuce.deleteKey("tasks");
    }

    @Test
    public void testSetDataStructureWithRedis() {
        redisWithLettuce.writeDataToRedisSetDataStructure("animals", "cat", "dog", "dog");
        assertEquals(2, redisWithLettuce.getDataFromSet("animals").size());
    }

    @Test
    public void testTransactionsInRedis() throws ExecutionException, InterruptedException {
        TransactionResult transactionResult = redisWithLettuce.transactionsInRedis();
        assertNotNull(transactionResult);
        assertEquals(3, transactionResult.size());
    }

}
