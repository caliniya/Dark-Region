import org.hjson.JsonObject.readHjson

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    // 这个是构建脚本自己的需要的依赖
    dependencies {
        // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-gradle-plugin
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
        // https://mvnrepository.com/artifact/org.hjson/hjson
        classpath("org.hjson:hjson:3.1.0")
    }
}

plugins {
    java
    kotlin("jvm") version "2.0.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://raw.githubusercontent.com/Zelaux/MindustryRepo/master/repository") }
    maven { url = uri("https://www.jitpack.io") }
}

val mindustryVersion = "v153"
//val uncVersion = "2.3.1"

// 这个是项目的依赖
dependencies {
    api(kotlin("stdlib", "2.0.0"))
    // https://mvnrepository.com/artifact/com.github.Anuken.Arc/arc-core
    compileOnly("com.github.Anuken.Arc:arc-core:${mindustryVersion}")
    // https://mvnrepository.com/artifact/com.github.Anuken.Mindustry/core
    compileOnly("com.github.Anuken.Mindustry:core:${mindustryVersion}")
    // EB-wilson的依赖
    //compileOnly ("com.github.EB-wilson.UniverseCore:core:$uncVersion")
}

group = "caliniya"
// 通过解析 mod.hjson 获取 version 字段
version = try {
    // 读取并解析 mod.hjson
    val json = readHjson(layout.projectDirectory.file("mod.hjson").asFile.bufferedReader()).run {
        if (!isObject) throw GradleException("mod.hjson root element must be a JSON object (found: $type)")
        asObject()
    }

    // 获取 version 字段
    json.get("version").run {
        if (!isString) throw GradleException("'version' field must be a string value (found: $type)")
        asString()
    }
} catch (e: Exception) {
    throw GradleException("Failed to parse mod.hjson: ${e.message}", e)
}

// 很多地方用到的字段，我统一用常量存储，方便修改。
// 感谢 Meow0x7E 的帮助
val dexArchiveName = "${project.name}-v${project.version}-dex.zip"
val desktopJarName = "${project.name}-v${project.version}-desktop.jar"
val deployJarName = "${project.name}-v${project.version}.jar"
val libsDir = File(buildDir, "libs")
val dexArchivePath = File(libsDir, dexArchiveName)
val desktopJarPath = File(libsDir, desktopJarName)

// Android SDK 的路径
val sdkHome: String = System.getenv("ANDROID_HOME")
    ?: System.getenv("ANDROID_SDK_ROOT")
    ?: "${System.getenv("HOME")}/Android/Sdk"

// 设置 Java 版本
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set(desktopJarName)

    // 合并依赖进产物中
    from(configurations.runtimeClasspath.get().toList().map { if (it.isDirectory) it else zipTree(it) })

    // 这俩可以当成一个简单的示例，回头如果有啥要加入产物压缩包的东西可以从这里加
    // 包含 assets 目录中的内容
    from("assets") { include("**") }
    // 包含 icon.png 和 mod.hjson
    from("icon.png", "mod.hjson")
}

tasks.register("dexify") {
    group = "build"
    dependsOn("jar")

    doLast {
        if (!File(sdkHome).exists()) throw GradleException("No valid Android SDK found. Ensure that ANDROID_HOME is set to your Android SDK directory.")

        val d8 = File("${sdkHome}/build-tools/").listFiles()
            // 这个被注释掉的原因是因为 AIDE 的 AndroidSDK 里的脚本没有可执行权限，所以得绕一下，直接用 java 调 jar
            //?.firstOrNull { File(it, "d8").exists() || File(it, "d8.bat").exists() }
            //?.let { if (File(it, "d8").exists()) File(it, "d8") else File(it, "d8.bat") }
            ?.firstOrNull { File(it, "lib/d8.jar").exists() }
            ?.let { File(it, "lib/d8.jar") }
            ?: throw GradleException("No d8 found. Ensure that you have an Android build-tools 26.0.0+ installed.")

        val androidJar = File("${sdkHome}/platforms/").listFiles()
            ?.firstOrNull { File(it, "android.jar").exists() }
            ?.let { File(it, "android.jar") }
            ?: throw GradleException("No android.jar found. Ensure that you have an Android platform installed.")

        // 获取所有依赖路径
        val dependencies = configurations.compileClasspath.get() + configurations.runtimeClasspath.get()

        exec {
            workingDir = libsDir
            commandLine = listOf(
                // 和上面的代码相互配合，所以被注释了
                //d8.absolutePath,
                "java", "-cp", d8.absolutePath, "com.android.tools.r8.D8",
                *dependencies.flatMap { listOf("--classpath", it.absolutePath) }.toTypedArray(),
                "--classpath", androidJar.absolutePath,
                "--min-api", "14",
                "--output", dexArchiveName,
                desktopJarName
            )
            standardOutput = System.out
            errorOutput = System.err
        }
    }
}

// 合并产物压缩包为一个
tasks.register("deploy", Zip::class) {
    group = "build"
    dependsOn("dexify", "jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set(deployJarName)
    destinationDirectory.set(libsDir)

    // 要合并的压缩包
    from(zipTree(desktopJarPath), zipTree(dexArchivePath))

    // 完成后删除被合并的压缩包
    doLast { delete(desktopJarPath, dexArchivePath) }
}

// dexify 很慢，如果只在桌面版测试可以省略 dexify 步骤
tasks.register("fastDeploy") {
    group = "build"
    dependsOn("jar")

    doFirst {
        file(desktopJarPath).renameTo(File(libsDir, deployJarName))
    }
}