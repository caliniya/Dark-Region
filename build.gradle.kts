buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    val kotlinVersion = "2.0.0"

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
    }
}

plugins {
    java
    kotlin("jvm") version "2.0.0"
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
    api(kotlin("stdlib", "2.0.0"))
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
    // 告诉 idea 使用 java 17 开发
    sourceCompatibility = JavaVersion.VERSION_17
    // 突然想起了已经设置了编译参数 --release 8，设置这个基本没啥意义了
    //targetCompatibility = JavaVersion.VERSION_17
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
        
        val buildToolRoot = File("${sdkHome}/build-tools/")
            .listFiles()
            ?.find { File(it, "lib/d8.jar").exists() }  // 修改这里，检查d8.jar的存在
            ?: throw GradleException("No d8.jar found. Ensure that you have an Android build-tools 26.0.0+ installed.")
        
        val platformRoot = File("${sdkHome}/platforms/")
            .listFiles()
            ?.find { File(it, "android.jar").exists() }
            ?: throw GradleException("No android.jar found. Ensure that you have an Android platform installed.")
        
        // 获取所有依赖路径
        val dependencies = configurations.compileClasspath.get() + configurations.runtimeClasspath.get()
        
        // 构建classpath参数
        val classpathArgs = dependencies.flatMap { 
            listOf("--classpath", it.absolutePath) 
        } + listOf("--classpath", File(platformRoot, "android.jar").absolutePath)
        
        exec {
            workingDir = File(buildDir, "libs")
            commandLine = listOf(
    "java", "-cp", File(buildToolRoot, "lib/d8.jar").absolutePath, "com.android.tools.r8.D8"
) + classpathArgs + listOf(
    "--min-api",
    "14",
    "--output",
    "${project.name}-v${project.version}-Android.jar",
    "${project.name}-v${project.version}-Desktop.jar"
)
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