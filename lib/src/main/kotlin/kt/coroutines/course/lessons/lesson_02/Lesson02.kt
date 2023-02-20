package kt.coroutines.course.lessons.lesson_02

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
