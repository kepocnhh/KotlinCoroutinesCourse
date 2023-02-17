package kt.coroutines.course.lessons.lesson_01

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue
import kotlin.system.measureNanoTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

object Lesson01 {
    fun learn() {
        println("\n\t---")
        learnGlobalScope()
        println("\n\t---")
        learnRunBlocking()
        println("\n\t---")
        learnSynchronous()
        println("\n\t---")
        learnParallel()
        println("\n\t---")
        learnAsyncAwait(CoroutineStart.DEFAULT)
        println("\n\t---")
        learnAsyncAwait(CoroutineStart.LAZY)
    }
}

private const val TIMES = 6

private fun learnGlobalScope() {
    println("try launch a new coroutine in \"GlobalScope\"...")
    println(
        "A global CoroutineScope not bound to any job. " +
                "Global scope is used to launch top-level coroutines " +
                "which are operating on the whole application lifetime and are not cancelled prematurely."
    )
    GlobalScope.launch {
        println(heavyMapOld("launched in GlobalScope"))
    }
    println("a new coroutine in \"GlobalScope\" is launched")
}

private fun learnRunBlocking() {
    println("try run a new coroutine and block the current thread...")
    runBlocking {
        println(heavyMapOld("run and block"))
    }
    println("a new coroutine is finished")
}

private fun learnSynchronous() {
    println("run synchronous...")
    val elapsed = measureNanoTime {
        runBlocking {
            repeat(TIMES) { index ->
                println(heavyMap("synchronous: $index"))
            }
        }
    }.nanoseconds
    println("synchronous is finished: $elapsed")
}

private fun learnParallel() {
    println("run parallel...")
    val elapsed = measureNanoTime {
        runBlocking {
            repeat(TIMES) { index ->
                launch {
                    println(heavyMap("parallel: $index"))
                }
            }
        }
    }.nanoseconds
    println("parallel is finished: $elapsed")
}

private fun learnAsyncAwait(start: CoroutineStart) {
    println("run deferred($start) jobs...")
    val elapsed = measureNanoTime {
        runBlocking {
            val jobs = List(TIMES) { index ->
                async(start = start) {
                    heavyMap("deferred($start): $index")
                }
            }
            jobs.forEach { println(it.await()) }
        }
    }.nanoseconds
    println("deferred($start) jobs is finished: $elapsed")
}

private suspend fun heavyMapOld(it: String): String {
    val start = System.currentTimeMillis().milliseconds
    while (true) {
        val now = System.currentTimeMillis().milliseconds
        if (now - start > 2.seconds) return "Mapped: \"$it\""
    }
}

private suspend fun heavyMap(it: String): String {
    val seconds = it.hashCode().absoluteValue % 3 + 1
    delay(seconds.seconds)
    return "Mapped: \"$it\" ($seconds seconds)"
}
