package kt.coroutines.course

import kt.coroutines.course.lessons.lesson_01.Lesson01
import kt.coroutines.course.lessons.lesson_02.Lesson02
import kt.coroutines.course.lessons.lesson_03.Lesson03

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
		"lesson2" -> Lesson02.learn()
		"lesson3" -> Lesson03.learn()
		else -> printHelp()
	}
}
