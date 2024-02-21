import java.time.Instant

plugins {
    `java-library`

    id("com.github.johnrengelman.shadow") version "8.1.1"

    eclipse
    idea
}

group = "com.jeff-media"
version = "2.2.2"
description = "Bukkit API Library to add PersistentDataContainers to blocks"
val mainPackage = "${project.group}.${rootProject.name}"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
    withJavadocJar()
}


repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://mvn-repo.arim.space/lesser-gpl3/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.jetbrains:annotations:24.1.0")

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

//    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.74.0")

    testImplementation("org.slf4j:slf4j-simple:2.0.12")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(17)
        options.compilerArgs.addAll(arrayListOf("-Xlint:all", "-Xlint:-processing", "-Xdiags:verbose"))
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("")

        // Shadow classes
        fun reloc(originPkg: String, targetPkg: String) = relocate(originPkg, "${mainPackage}.lib.${targetPkg}")

//        reloc("space.arim.morepaperlib", "morepaperlib")
//        reloc("com.github.milkdrinkers.Crate", "crate")
//        reloc("com.github.milkdrinkers.colorparser", "colorparser")
//        reloc("dev.jorel.commandapi", "commandapi")
//        reloc("dev.triumphteam.gui", "gui")
//        reloc("com.zaxxer.hikari", "hikaricp")

        minimize()
    }

    /*runServer {
        // Configure the Minecraft version for our task.
        minecraftVersion("1.20.2")

        // IntelliJ IDEA debugger setup: https://docs.papermc.io/paper/dev/debugging#using-a-remote-debugger
        jvmArgs("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-DPaper.IgnoreJavaVersion=true", "-Dcom.mojang.eula.agree=true", "-DIReallyKnowWhatIAmDoingISwear")
        systemProperty("terminal.jline", false)
        systemProperty("terminal.ansi", true)

        // Automatically install dependencies
        downloadPlugins {
//            modrinth("carbon", "2.1.0-beta.21")
//            github("jpenilla", "MiniMOTD", "v2.0.13", "minimotd-bukkit-2.0.13.jar")
//            hangar("squaremap", "1.2.0")
//            url("https://download.luckperms.net/1515/bukkit/loader/LuckPerms-Bukkit-5.4.102.jar")
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
        }
    }*/
}

// Apply custom version arg
val versionArg = if (hasProperty("customVersion"))
    (properties["customVersion"] as String).uppercase() // Uppercase version string
else
    "${project.version}-SNAPSHOT-${Instant.now().epochSecond}" // Append snapshot to version

// Strip prefixed "v" from version tag
project.version = if (versionArg.first().equals('v', true))
    versionArg.substring(1)
else
    versionArg.uppercase()