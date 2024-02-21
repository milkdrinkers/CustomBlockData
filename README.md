<h1 align="center">CustomBlockData</h1>
<p align="center">
    <img src="https://img.shields.io/github/license/milkdrinkers/CustomBlockData?color=blue&style=flat-square" alt="license"/>
    <a href="https://maven.athyrium.eu/#/releases/com/github/milkdrinkers/customblockdata"><img alt="Maven metadata URL" src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.athyrium.eu%2Freleases%2Fcom%2Fgithub%2Fmilkdrinkers%2Fcustomblockdata%2Fmaven-metadata.xml&style=flat-square&label=Maven Version&color=44cc11"></a>
    <a href="https://maven.athyrium.eu/javadoc/releases/com/github/milkdrinkers/customblockdata/latest"><img src="https://img.shields.io/badge/Javadoc-8A2BE2?style=flat-square" alt="javadoc"/></a>
    <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/milkdrinkers/customblockdata?style=flat-square&label=Last%20Commit">
</p>

---

## Description

> [!NOTE]  
> This is a fork of the original repository found [here](https://github.com/mfnalex/CustomBlockData). All credit goes to the original creator and contributors.

CustomBlockData is a library for the Bukkit API that allows you to store ANY information inside blocks.
It does so by providing the `CustomBlockData` class which implements the `PersistentDataContainer` interface.

It does not need any files or databases by saving the information inside the chunk's PersistentDataContainer, and the information is persistent even after server restarts.

CustomBlockData is compatible with all Bukkit versions from 1.16.5 onwards, including all forks. Older versions are not supported because *Chunk* only implements *PersistentDataHolder* since 1.16.3.

---

## Useful Links

* **JavaDoc** - [Link](https://maven.athyrium.eu/javadoc/releases/com/github/milkdrinkers/customblockdata/latest)

---

## Advantages

- It does not need any files or databases
- When the chunk where the block is inside gets deleted, there will be no leftover information
- You can store anything that can be stored inside a normal `PersistantDataContainer` (which means, basically, **anything**)
- It can automatically keep track of block changes and automatically delete block data when a block gets broken, move data when a block gets moved, etc
  - You can make specific blocks protected from this, or listen to the cancellable `CustomBlockDataEvent`s 
  - (This is disabled by default for backwards compatibility - just call `CustomBlockData#registerListener(Plugin)` to enable it)

---

## Usage

To get a block's PersistentDataContainer, all you have to do is create an instance of `CustomBlockData` providing the block and
the instance of your main class:

```java
PersistentDataContainer customBlockData = new CustomBlockData(block, plugin);
```

If you want CustomBlockData to automatically handle moving/removing block data for changed blocks (e.g. move data when a block gets moved with a piston, or to remove data when a player breaks a block, etc) you must call CustomBlockData.registerListener(Plugin) once in your onEnable().

For more information about how to use it, just look at the [API docs for the PersistentDataContainer](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/persistence/PersistentDataContainer.html) or look at [this tutorial](https://www.spigotmc.org/threads/a-guide-to-1-14-persistentdataholder-api.371200/).

---

## Build Tool Setup

---

### Gradle Dependency Setup

**Repository**
```kotlin
repositories { 
    maven("https://maven.athyrium.eu/releases/")
}
```

**Dependencies**
```kotlin
dependencies {
    implementation("com.github.milkdrinkers:customblockdata:1.0.0")
}
```

**Shading & Relocating**
> [!IMPORTANT]  
> You must shade (and you should relocate) the `customblockdata` package. You will need the Shadow plugin found [here](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow). Add the following to your shadowJar section!
```kotlin
shadowJar {
    relocate("com.github.milkdrinkers.customblockdata", "your.package.customblockdata")
}
```

Optionally, make the build task depend on shadowJar:
```kotlin
build { 
    dependsOn(shadowJar)
}
```

---

### Maven Dependency Setup

**Repository**
```xml
<repository>
    <id>milkdrinkers-releases</id>
    <name>Milkdrinkers Maven Repository</name>
    <url>https://maven.athyrium.eu/releases</url>
</repository>
```

**Dependencies**
```xml
<dependency>
    <groupId>com.github.milkdrinkers</groupId>
    <artifactId>customblockdata</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

**Shading & Relocating**
> [!IMPORTANT]  
> You must shade (and you should relocate) the `customblockdata` package.
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.5.0</version>
      <configuration>
        <relocations>
          <relocation>
            <pattern>com.github.milkdrinkers.customblockdata</pattern>
            <shadedPattern>YOUR.PACKAGE.NAME.customblockdata</shadedPattern>
          </relocation>
        </relocations>
      </configuration>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>shade</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```