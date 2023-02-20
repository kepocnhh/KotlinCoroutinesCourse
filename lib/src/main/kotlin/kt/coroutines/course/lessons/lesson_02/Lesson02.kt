package kt.coroutines.course.lessons.lesson_02

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Lesson02 {
    fun learn() {
        println("\n\t---")
        learnJob()
        println("\n\t---")
        learnCancel()
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
                delay(1500.milliseconds)
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
//        job.cancelAndJoin()
    }
}
