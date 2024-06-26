import java.time.Instant

plugins {
    `java-library`
    `maven-publish`

    eclipse
    idea
}

group = "com.github.milkdrinkers"
version = "1.0.0"
description = "Bukkit API Library to add PersistentDataContainers to blocks"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withJavadocJar()
    withSourcesJar()
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

    testImplementation(platform("org.junit:junit-bom:5.11.0-M2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

//    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.74.0")

    testImplementation("org.slf4j:slf4j-simple:2.0.13")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
        options.compilerArgs.addAll(arrayListOf("-Xlint:all", "-Xlint:-processing", "-Xdiags:verbose"))
    }

    javadoc {
        isFailOnError = false
        val options = options as StandardJavadocDocletOptions
        options.encoding = Charsets.UTF_8.name()
        options.overview = "src/main/javadoc/overview.html"
        options.isDocFilesSubDirs = true
        options.tags("apiNote:a:API Note:", "implNote:a:Implementation Note:", "implSpec:a:Implementation Requirements:")
        options.use()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

// Apply custom version arg
val versionArg = if (hasProperty("customVersion"))
    (properties["customVersion"] as String).uppercase() // Uppercase version string
else
    "${rootProject.version}-SNAPSHOT-${Instant.now().epochSecond}" // Append snapshot to version

// Strip prefixed "v" from version tag
rootProject.version = if (versionArg.first().equals('v', true))
    versionArg.substring(1)
else
    versionArg.uppercase()

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${rootProject.group}"
            artifactId = "customblockdata"
            version = "${rootProject.version}"

            pom {
                name.set("CustomBlockData")
                description.set(rootProject.description.orEmpty())
                url.set("https://github.com/milkdrinkers/CustomBlockData")
                licenses {
                    license {
                        name.set("GNU General Public License version 3")
                        url.set("https://opensource.org/license/gpl-3-0/")
                    }
                }
                developers {
                    developer {
                        id.set("darksaid98")
                        name.set("darksaid98")
                        organization.set("Milkdrinkers")
                        organizationUrl.set("https://github.com/milkdrinkers")
                    }
                    developer {
                        id.set("mfnalex")
                        name.set("Alexander Majka")
                        email.set("mfnalex@jeff-media.com")
                        organization.set("JEFF Media GbR")
                        organizationUrl.set("jeff-media.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/milkdrinkers/CustomBlockData.git")
                    developerConnection.set("scm:git:ssh://github.com:milkdrinkers/CustomBlockData.git")
                    url.set("https://github.com/milkdrinkers/CustomBlockData")
                }
            }

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "releases"
            url = uri("https://maven.athyrium.eu/releases")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
        maven {
            name = "snapshots"
            url = uri("https://maven.athyrium.eu/snapshots")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}