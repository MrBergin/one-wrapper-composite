# What is this project?

I've been working on a project that's begun splitting up into independent gradle projects, composed together using
composite builds. So far it's working pretty well as each of these projects serve a different purpose and don't quite
make sense as part of the same multi-project build.

A result of these split up projects is that we now have quite a lot of duplication in terms of our gradle wrappers, each
independent build has its own wrapper configured.

A colleague of mine reached out to me asking if it's possible to drive all these projects from a single wrapper. This
would be a real "nice to have" as it's less noise to check into version control, and eliminates any chance of version
drift when we come to upgrade gradle version.

## So can it be done?

I think so! But it comes at a cost...

As of Gradle 6.8 tasks can be run from a root project, through to a composite build, with this in place what we can do
is create a minimal root project that includes all of our builds and define a gradle wrapper on there. In this project
you'll find a root, application and library set of projects, the structure is as follows:

Since `library` serves `application`, it makes sense that `application` has `library` as an included build, neither of
these have a gradle wrapper defined on them.

Now we create the `root` project, this project is simply responsible for including all the other builds, so in this case
that's `library` and `application`. `root` defined a gradle wrapper of version of 6.8.2 and we can now do the following
in the root project:

`./gradlew :application:test`
`./gradlew :library:test`

And it works without a gradle wrapper being defined in `library` or `application`!

In addition to this we can decalre things like this in our root gradle project:

```kotlin
task("clean").dependsOn(gradle.includedBuilds.map { it.task(":clean") })
task("build").dependsOn(gradle.includedBuilds.map { it.task(":build") })
task("test").dependsOn(gradle.includedBuilds.map { it.task(":test") })
```

Providing us with a nice shorthand to run all builds, or all tests like so:

`./gradlew clean` - runs all included build cleans

`./gradlew build` - runs all included build builds

`./gradlew test` - runs all included build tests

## So what's the cost?

Gradle 6.8 has only just come out recently, I believe reaping the benefits of this in Intellij IDEA is summarised in
this [youtrack issue](https://youtrack.jetbrains.com/issue/IDEA-207003) if I've understood correctly. What I've observed
in intellij gradle vs gradle on command line is running things like `gradle :application:build` doesn't work. I've also
observed defects such as running tests using the gutter icons for builds that have been included twice fails. For example
`library` is included in both `root` and `application`, and when I run a test through the gutter icon I get this error:

`Included build in C:\Development\one-wrapper-composite\library has the same root project name 'one-wrapper-composite-library' as the main build.`
