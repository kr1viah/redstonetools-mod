plugins {
	id("java-library")
	id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/") {
		name = "Paper"
	}
	maven("https://maven.enginehub.org/repo/") {
		name = "WorldEdit Maven"
	}
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:${project.property("minecraft_version")}.build.+")
	implementation("com.sk89q.worldedit:worldedit-bukkit:${project.property("worldedit_version")}")
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
	runServer {
		minecraftVersion("26.1.2")
		jvmArgs("-Xms4G", "-Xmx4G", "-Dcom.mojang.eula.agree=true")
	}

	processResources {
		val props = mapOf("version" to version)
		filesMatching("plugin.yml") {
			expand(props)
		}
	}
}
