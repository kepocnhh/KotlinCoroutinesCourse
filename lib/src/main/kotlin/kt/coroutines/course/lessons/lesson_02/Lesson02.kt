package kt.coroutines.course.lessons.lesson_02

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

object Lesson02 {
    fun learn() {
        println("\n\t---")
        learnJob()
        println("\n\t---")
        learnCancel()
        println("\n\t---")
        learnCancellation()
        println("\n\t---")
        learnFinally()
        println("\n\t---")
        learnNonCancellable()
        println("\n\t---")
        learnTimeout()
        println("\n\t---")
        learnTimeoutOrNull()
    }
}

private fun learnJob() {
    runBlocking {
        val job: Job = launch {
            println("launch job")
        }
        println("join job: $job")
        job.join()
        println("launched job: $job")
        println(
            """
                [new] -> [active] -> [completing] -> [completed]
                                |    /
                                |   /
                                |  /
                                | /
                                |/
                                [cancelling] -> [cancelled]
            """.trimIndent()
        )
    }
}

private fun learnCancel() {
    runBlocking {
        println("launch job...")
        val job = launch {
            println("job launched")
            val size = 1_000
            repeat(1_000) { index ->
                println("job: $index/$size")
                delay(1.5.seconds)
            }
        }
        println("start delay")
        delay(4.seconds)
        println("finish delay")
        println("cancel job: $job")
        job.cancel()
        println("join job: $job")
        job.join()
        println("cancelled job: $job")
    }
}

private fun learnCancellation() {
    runBlocking {
        println("launch job...")
        val context: CoroutineContext = Dispatchers.Default
        val job = launch(context) {
            println("job launched")
            println("no suspend functions")
            println("context: $context")
            var index = 0
            while (isActive) {
                println("job: ${index++}")
                val now = System.nanoTime().nanoseconds
                while (System.nanoTime().nanoseconds - now < 1.5.seconds) {
                    // ...
                }
            }
            println("job is not active")
        }
        println("start delay")
        delay(4.seconds)
        println("finish delay")
        println("cancel and join job: $job")
        job.cancelAndJoin()
        println("cancelled and joined job: $job")
    }
}

private fun learnFinally() {
    runBlocking {
        println("launch job...")
        val job = launch {
            println("job launched")
            var index = 0
            try {
                while (true) {
                    println("job: ${index++}")
                    delay(1.5.seconds)
                }
            } catch (e: CancellationException) {
                println("job cancelled: $e")
            } catch (e: Throwable) {
                println("unexpected job error: $e")
            } finally {
                println("job finish")
            }
        }
        println("start delay")
        delay(4.seconds)
        println("finish delay")
        println("cancel and join job: $job")
        job.cancelAndJoin()
        println("cancelled and joined job: $job")
    }
}

private fun learnNonCancellable() {
    runBlocking {
        println("launch job...")
        val job = launch {
            println("job launched")
            var index = 0
            try {
                while (true) {
                    println("job: ${index++}")
                    delay(1.5.seconds)
                }
            } finally {
//                delay(2.seconds) // return immediately
                withContext(NonCancellable) {
                    println("start delay finally")
                    delay(2.seconds)
                    println("finish delay finally")
                }
            }
        }
        println("start delay")
        delay(4.seconds)
        println("finish delay")
        println("cancel and join job: $job")
        job.cancelAndJoin()
        println("cancelled and joined job: $job")
    }
}

private fun learnTimeout() {
    runBlocking {
        val time = 4.seconds
        println("with timeout($time)...")
        try {
            withTimeout(time) {
                println("start with timeout($time)")
                var index = 0
                while (true) {
                    println("times: ${index++}")
                    delay(1.5.seconds)
                }
            }
        } catch (e: TimeoutCancellationException) {
            println("timeout: $e")
        } catch (e: Throwable) {
            println("unexpected with timeout($time) error: $e")
        }
        println("with timeout($time) finish")
    }
}

private fun learnTimeoutOrNull() {
    runBlocking {
        (3 to 5).also { (seconds, times) ->
            val time = seconds.seconds
            println("with timeout($time)...")
            val result = withTimeoutOrNull(time) {
                println("start with timeout($time)")
                repeat(times) { index ->
                    delay(1.seconds)
                }
                "result:$seconds/$times"
            }
            println("result with timeout($time): $result")
        }
        (5 to 3).also { (seconds, times) ->
            val time = seconds.seconds
            println("with timeout($time)...")
            val result = withTimeoutOrNull(time) {
                println("start with timeout($time)")
                repeat(times) { index ->
                    delay(1.seconds)
                }
                "result:$seconds/$times"
            }
            println("result with timeout($time): $result")
        }
    }
}
