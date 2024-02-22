package com.kokoo.redisutils.lock

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.integration.support.locks.ExpirableLockRegistry
import org.springframework.stereotype.Service

@Service
class LockService(
    private val lockRegistry: ExpirableLockRegistry,
    private val intRedisTemplate: RedisTemplate<String, Int>
) {

    companion object {
        private const val REDIS_KEY = "key"
    }

    private val log = KotlinLogging.logger {}

    fun lock(lockKey: String, counter: Int) {
        val lock = lockRegistry.obtain(lockKey)
        try {
            val unlocked = lock.tryLock()

            if (!unlocked) {
                lock.lockInterruptibly()
            }

            val number = intRedisTemplate.opsForValue().get(REDIS_KEY) ?: 0
            intRedisTemplate.opsForValue().increment(REDIS_KEY)

            val random = (10..300).random()
            Thread.sleep(random.toLong())

            val increasedNumber = intRedisTemplate.opsForValue().get(REDIS_KEY)

            if (number.plus(1) != increasedNumber) {
                // lock failed..
            }
        } catch (e: Exception) {
            // exception
            log.error { e }
        } finally {
            lock.unlock()
        }
    }
}