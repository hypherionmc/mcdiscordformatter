# MCDiscordFormatter
**Forked from [THIS REPO](https://github.com/QuiltServerTools/MCDiscordReserializer) and ported to be compatible with Modern MC versions**

---

A library for transcoding between Minecraft and Discord.

Minecraft text is represented by Mojangs internal components

Discord text is represented using Java Strings (not relying on any specific Discord library)
and is translated using a fork of [Discord's SimpleAST](https://github.com/discordapp/SimpleAST),
[here](https://github.com/Vankka/SimpleAST).

## Dependency information

#### Maven
```xml
<repository>
    <id>hypherion-maven-releases</id>
    <name>HypherionSA's Maven</name>
    <url>https://maven.firstdarkdev.xyz/releases</url>
</repository>

<dependency>
    <groupId>me.hypherionmc.sdlink</groupId>
    <artifactId>mcdiscordformatter-1.20.3</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle
```groovy
repositories {
    maven {
        url "https://maven.firstdarkdev.xyz/releases"
    }
}

dependencies {
    implementation("me.hypherionmc.sdlink:mcdiscordformatter-1.20.3:2.0.0")
}
```

## Basic usage
```java
// For Minecraft -> Discord translating
String output = DiscordSerializer.INSTANCE.serialize(TextComponent.of("Bold").decoration(TextDecoration.BOLD, true));

// For Discord -> Minecraft translating
Component output = MinecraftSerializer.INSTANCE.serialize("**Bold**");
```
