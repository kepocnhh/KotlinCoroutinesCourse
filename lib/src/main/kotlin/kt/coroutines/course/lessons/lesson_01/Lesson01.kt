package kt.coroutines.course.lessons.lesson_01

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Lesson01 {
    fun learn() {
        println("\n\t---")
        learnGlobalScope()
    }
}

private fun learnGlobalScope() {
    println("try launch a new coroutine in \"GlobalScope\"...")
    println(
        "A global CoroutineScope not bound to any job. " +
                "Global scope is used to launch top-level coroutines " +
                "which are operating on the whole application lifetime and are not cancelled prematurely."
    )
    GlobalScope.launch {
        println(heavyMapOld("foo bar"))
    }
    println("a new coroutine in \"GlobalScope\" is launched")
}

private suspend fun heavyMapOld(it: String): String {
    val start = System.currentTimeMillis().milliseconds
    while (true) {
        val now = System.currentTimeMillis().milliseconds
        if (start - now > 2.seconds) return "Mapped: \"$it\""
    }
}
