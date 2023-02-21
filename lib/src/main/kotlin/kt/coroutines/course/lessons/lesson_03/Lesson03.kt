package kt.coroutines.course.lessons.lesson_03

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
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

private suspend fun <T : Any> delayAndGet(time: Duration, value: T): T {
    delay(time)
    return value
}
