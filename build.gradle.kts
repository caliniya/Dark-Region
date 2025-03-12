plugins {
    java
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://ghproxy.net/raw.githubusercontent.com/Zelaux/MindustryRepo/master/repository") }
    maven { url = uri("https://www.jitpack.io") }
}

val mindustryVersion = "v146"
val jabelVersion = "93fde537c7"

dependencies {
    compileOnly("com.github.Anuken.Arc:arc-core:${mindustryVersion}")
    compileOnly("com.github.Anuken.Mindustry:core:${mindustryVersion}")
    implementation("com.github.liplum:MultiCrafterLib:v1.8")
    //这个有点问题，但暂时用不到它，就不修了
    //annotationProcessor("com.github.Anuken:jabel:$jabelVersion")
}

group = "meow0x7e"
version = layout.projectDirectory.file("mod.hjson").asFile.reader().buffered().lines()
    .filter {
        (!Regex("^\\s*//.*$").matches(it) && Regex("^\\s*\"version\": ?\".*\",?$").matches(it))
    }.map {
        it.replace(Regex("^\\s*\"version\": ?\""), "")
            .replace(Regex("\",?$"), "")
    }.toList().firstOrNull() ?: "undefined"


val sdkHome: String = System.getenv("ANDROID_HOME")
    ?: System.getenv("ANDROID_SDK_ROOT")
    ?: "${System.getenv("HOME")}/Android/Sdk"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

allprojects {
    tasks.withType(JavaCompile::class.java).apply {
        configureEach {
            options.compilerArgs.addAll(listOf("--release", "8"))
        }
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set("${project.name}-v${project.version}-Desktop.jar")

    from(configurations.runtimeClasspath.get().toList()
        .map { if (it.isDirectory) it else zipTree(it) })
    from("assets/") { include("**") }
    from("icon.png", "mod.hjson")
}

tasks.register("jarAndroid") {
    dependsOn("jar")
    doLast {
        if (!File(sdkHome).exists()) throw GradleException("No valid Android SDK found. Ensure that ANDROID_HOME is set to your Android SDK directory.")

        val platformRoot =
            File("${sdkHome}/platforms/").listFiles()?.find { File(it!!, "android.jar").exists() }
                ?: throw GradleException("No android.jar found. Ensure that you have an Android platform installed.")

        val buildToolRoot =
            File("${sdkHome}/build-tools/").listFiles()?.find { File(it, "d8").exists() }
                ?: throw GradleException("No d8 found. Ensure that you have an Android build-tool installed.")

        //dex and desugar files - this requires d8 in your PATH
        project.exec {
            commandLine(ArrayList<String>().apply {
                add(File(buildToolRoot, "d8").absolutePath)
                //collect dependencies needed for desugaring
                addAll(listOf(
                    *configurations.compileClasspath.get().toList().toTypedArray(),
                    *configurations.runtimeClasspath.get().toList().toTypedArray(),
                    File(platformRoot, "android.jar")
                ).flatMap {
                    listOf("--classpath", it.absolutePath)
                })
                add("--min-api")
                add("14")
                add("--output")
                add("${project.name}-v${project.version}-Android.jar")
                add("${project.name}-v${project.version}-Desktop.jar")
            })
            workingDir = File("${buildDir}/libs/")
        }
    }
}

tasks.register("deploy", Jar::class) {
    dependsOn(tasks.getByName("jarAndroid"), "jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set("${project.name}-v${project.version}.jar")

    from(
        zipTree("${buildDir}/libs/${project.name}-v${project.version}-Desktop.jar"),
        zipTree("${buildDir}/libs/${project.name}-v${project.version}-Android.jar")
    )

    doLast {
        delete("${buildDir}/libs/${project.name}-v${project.version}-Desktop.jar")
        delete("${buildDir}/libs/${project.name}-v${project.version}-Android.jar")
    }
}
