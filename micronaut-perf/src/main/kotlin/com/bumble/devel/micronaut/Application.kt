package com.bumble.devel.micronaut

import io.micronaut.runtime.Micronaut.*

fun main(args: Array<String>) {
	build()
			.args(*args)
			.packages("com.bumble.devel.micronaut")
			.start()
}


