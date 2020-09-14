package fr.ulity.core_v3.modules.storage;

import de.leonhard.storage.Json;

import java.io.IOException;
import java.nio.file.Files;

public class Temp extends Json {
    public Temp() throws IOException {
        super(Files.createTempFile("ucore", ".tmp.json").toFile());
    }
}