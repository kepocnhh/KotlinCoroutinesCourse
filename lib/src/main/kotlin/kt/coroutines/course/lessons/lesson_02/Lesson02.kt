package kt.coroutines.course.lessons.lesson_02

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object Lesson02 {
    fun learn() {
        println("\n\t---")
        learnJob()
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
