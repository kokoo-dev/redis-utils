package com.kokoo.redisutils.lock.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.integration.support.locks.ExpirableLockRegistry
import org.springframework.stereotype.Service


@Service
class LockService(
    private val lockRegistry: ExpirableLockRegistry,
    private val intRedisTemplate: RedisTemplate<String, Int>,
    private val transactionRedisTemplate: RedisTemplate<String, Int>,

    private val redisTemplate: StringRedisTemplate,
    private val decrementRedisScript: RedisScript<Boolean>
) {

    private val log = KotlinLogging.logger {}

    fun lockBySpringIntegration(lockKey: String, counter: Int) {
        val lock = lockRegistry.obtain(lockKey)
        try {
            val unlocked = lock.tryLock()

            if (!unlocked) {
                lock.lockInterruptibly()
            }

            val number = intRedisTemplate.opsForValue().get(lockKey) ?: 0
            intRedisTemplate.opsForValue().increment(lockKey)

            val increasedNumber = intRedisTemplate.opsForValue().get(lockKey)

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

    fun lockByMultiExec(lockKey: String, counter: Int) {
        // cluster 에서 사용 불가
        transactionRedisTemplate.execute {
            try {
                it.watch(lockKey.toByteArray())
                it.multi()

                val number = it.stringCommands().get(lockKey.toByteArray())?.toString(Charsets.UTF_8) ?: ""

                it.stringCommands().incr(lockKey.toByteArray())

                // exec() 전 원하는 get 불가
                val increasedNumber = it.stringCommands().get(lockKey.toByteArray())?.toString(Charsets.UTF_8) ?: ""
            } catch (e: Exception) {
                log.error { e }
                it.discard()
            }

            it.exec()
        }
    }

    fun lockByLuaScript(lockKey: String) {
        val result = redisTemplate.execute(decrementRedisScript, listOf(lockKey))
        log.info { result }
    }
}