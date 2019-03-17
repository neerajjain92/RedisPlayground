package com.neeraj.redis;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author neeraj on 2019-03-17
 * Copyright (c) 2019, RedisPlayground.
 * All rights reserved.
 */
public class RedisWithLettuceTest {
    RedisWithLettuce redisWithLettuce;

    @Before
    public void init() {
        redisWithLettuce = new RedisWithLettuce();
    }

    @Test
    public void testIfConnectionToRedisIsSuccessful() {
        redisWithLettuce.connectToRedis();
        assertTrue("Connection to Redis is successful", redisWithLettuce.statefulRedisConnection.isOpen());
    }

    @Test
    public void whenCloseAndShutdownRedisConnectionIsCalled_ItDisconnectsFromRedis() {
        redisWithLettuce.connectToRedis();
        assertTrue("Connection to Redis is successful", redisWithLettuce.statefulRedisConnection.isOpen());

        redisWithLettuce.closeAndShutdownRedisConnection();
        assertFalse("Connection to Redis is closed", redisWithLettuce.statefulRedisConnection.isOpen());
    }

    @Test
    public void testOperationsInRedis() {
        redisWithLettuce.connectToRedis();
        redisWithLettuce.writeDateToRedis("application.name", "RedisPlayground");
        assertEquals(redisWithLettuce.getDataFromRedis("application.name"), "RedisPlayground");
    }
}
