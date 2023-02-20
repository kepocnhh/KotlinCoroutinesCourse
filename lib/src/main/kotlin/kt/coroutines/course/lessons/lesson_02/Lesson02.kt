package kt.coroutines.course.lessons.lesson_02

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
            } catch (e: Throwable) {
                when (e) {
                    is CancellationException -> {
                        println("job cancelled: $e")
                    }
                    else -> {
                        println("unexpected job error: $e")
                    }
                }
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
