task("clean").dependsOn(gradle.includedBuilds.map { it.task(":clean") })
task("build").dependsOn(gradle.includedBuilds.map { it.task(":build") })
task("test").dependsOn(gradle.includedBuilds.map { it.task(":test") })
