# MCDiscordFormatter
**Forked from [THIS REPO](https://github.com/QuiltServerTools/MCDiscordReserializer) and ported to be compatible with Modern MC versions**

---

A library for transcoding between Minecraft and Discord.

## Dependency information

#### Maven

```xml
<repository>
    <id>hypherion-maven-releases</id>
    <name>HypherionSA's Maven</name>
    <url>https://maven.firstdark.dev/releases</url>
</repository>

<dependency>
    <groupId>com.hypherionmc.modutils</groupId>
    <artifactId>mcdiscordformatter</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle
```groovy
repositories {
    maven {
        url "https://maven.firstdark.dev/releases"
    }
}

dependencies {
    implementation("com.hypherionmc.modutils:mcdiscordformatter:3.0.0")
}
```

## Basic usage

```java
// For Minecraft -> Discord translating
String output = DiscordSerializer.INSTANCE.serialize(Component.text("Bold").decoration(TextDecoration.BOLD, true));

// For Discord -> Minecraft translating
Component output = MinecraftSerializer.INSTANCE.serialize("**Bold**");
```
