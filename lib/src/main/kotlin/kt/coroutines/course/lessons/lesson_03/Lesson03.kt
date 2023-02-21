package kt.coroutines.course.lessons.lesson_03

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Lesson03 {
    fun learn() {
        println("\n\t---")
        learnSequential()
        println("\n\t---")
        learnConcurrent()
        println("\n\t---")
        learnConcurrentLazy()
        println("\n\t---")
        learnDeferred()
        println("\n\t---")
        learnStructuredConcurrency()
        println("\n\t---")
        learnStructuredConcurrencyError()
    }
}

private fun learnSequential() {
    runBlocking {
        println("measure sequential...")
        val time = measureTimeMillis {
            val one = delayAndGet(2.seconds, 13)
            val two = delayAndGet(1.seconds, 29)
            println("The answer is ${one + two}")
        }.milliseconds
        println("Completed in $time")
    }
}

private fun learnConcurrent() {
    runBlocking {
        println("measure concurrent...")
        val time = measureTimeMillis {
            val one = async {
                println("start async 1")
                delayAndGet(2.seconds, 13)
            }
            val two = async {
                println("start async 2")
                delayAndGet(1.seconds, 29)
            }
            println("The answer is ${one.await() + two.await()}")
        }.milliseconds
        println("Completed in $time")
    }
}

private fun learnConcurrentLazy() {
    runBlocking {
        println("measure concurrent lazy...")
        val time = measureTimeMillis {
            val one = async(start = CoroutineStart.LAZY) {
                println("start async 1")
                delayAndGet(2.seconds, 13)
            }
            val two = async(start = CoroutineStart.LAZY) {
                println("start async 2")
                delayAndGet(1.seconds, 29)
            }
            println("The answer is ${one.await() + two.await()}")
        }.milliseconds
        println("Completed in $time")
    }
}

private fun learnDeferred() {
    println("measure deferred...")
    val time = measureTimeMillis {
        val one = delayAndGetAsync(2.seconds, 13)
        val two = delayAndGetAsync(1.seconds, 29)
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }.milliseconds
    println("Completed in $time")
}

private fun learnStructuredConcurrency() {
    runBlocking {
        println("measure structured concurrency...")
        val time = measureTimeMillis {
            println("The answer is ${concurrentSum()}")
        }.milliseconds
        println("Completed in $time")
    }
}

private fun learnStructuredConcurrencyError() {
    runBlocking {
        println("measure structured concurrency error...")
        val time = measureTimeMillis {
            try {
                println("The answer is ${concurrentSumError()}")
            } catch (e: Throwable) {
                println("sum error: $e")
            }
        }.milliseconds
        println("Completed in $time")
    }
}

suspend fun concurrentSum(): Int {
    return coroutineScope {
        val one = async {
            println("start async 1")
            delayAndGet(2.seconds, 13)
        }
        val two = async {
            println("start async 2")
            delayAndGet(1.seconds, 29)
        }
        println("start awaits")
        one.await() + two.await()
    }
}

suspend fun concurrentSumError(): Int {
    return coroutineScope {
        val one = async {
            println("\tstart async 1")
            try {
                delayAndGet(42.seconds, 13)
            } catch (e: CancellationException) {
                println("\t\tcanceled async 1")
                -1 // todo ?
            } finally {
                println("\t\tfinish async 1")
            }
        }
        val two = async<Int> {
            println("\tstart async 2")
            delay(1.seconds)
            error("error async 2")
        }
        println("start awaits")
        one.await() + two.await()
    }
}

private suspend fun <T : Any> delayAndGet(time: Duration, value: T): T {
    delay(time)
    return value
}

private fun <T : Any> delayAndGetAsync(time: Duration, value: T): Deferred<T> {
    return GlobalScope.async {
        println("start async: $value")
        delayAndGet(time, value = value)
    }
}
