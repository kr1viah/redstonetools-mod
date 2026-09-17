plugins {
	id("java-library")
	id("xyz.jpenilla.run-paper") version "3.0.2"
	id("io.papermc.paperweight.userdev") version "2.0.0-beta.21"
}

version = "${project.property("mod_version")}+${stonecutter.current.project}"
group = project.property("maven_group")!!

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/") {
		name = "Paper"
	}
	maven("https://maven.enginehub.org/repo/") {
		name = "WorldEdit Maven"
	}
}

base.archivesName = project.property("archives_base_name") as String

dependencies {
	paperweight.paperDevBundle("${project.property("minecraft_version")}.build.+")
//	compileOnly("io.papermc.paper:paper-api:${project.property("minecraft_version")}.build.+")
	compileOnly("com.sk89q.worldedit:worldedit-bukkit:${project.property("worldedit_version")}")
}

tasks.register<Copy>("collectFile") {
	group = "build"

	from(tasks.jar.map { it.archiveFile })
	into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod_version")}"))
}

tasks.register<DefaultTask>("buildAndCollect") {
	group = "build"
	dependsOn(tasks.named("build"), tasks.named("collectFile"))
}

java {
	toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
	runServer {
		minecraftVersion(project.property("minecraft_version") as String)
		jvmArgs("-Xms4G", "-Xmx4G", "-Dcom.mojang.eula.agree=true")
	}

	processResources {
		val props = mapOf("version" to version)
		filesMatching("plugin.yml") {
			expand(props)
		}
	}
}
