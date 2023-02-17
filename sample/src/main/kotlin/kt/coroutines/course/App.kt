package kt.coroutines.course

import kt.coroutines.course.lessons.lesson_01.Lesson01

private fun printHelp() {
	println(
		"""
			Usage: sample {task_name}
			
			Tasks:
			 'lesson{number}': run lesson by {number} where it is in [1..?]
		""".trimIndent()
	)
}

fun main(args: Array<String>) {
	if (args.size != 1) {
		printHelp()
		return
	}
	when (args.single()) {
		"lesson1" -> Lesson01.learn()
		else -> printHelp()
	}
}
