rootProject.name = "mind-share"

val modulesApiDir = File(rootProject.projectDir, "api")
val modulesDomainDir = File(rootProject.projectDir, "domain")
val modulesCoreDir = File(rootProject.projectDir, "core")

modulesApiDir.listFiles()?.forEach { file ->
    if (file.isDirectory) {
        include("api:${file.name}")
    }
}

modulesDomainDir.listFiles()?.forEach { file ->
    if (file.isDirectory) {
        include("domain:${file.name}")
    }
}

modulesCoreDir.listFiles()?.forEach { file ->
    if (file.isDirectory) {
        include("core:${file.name}")
    }
}

