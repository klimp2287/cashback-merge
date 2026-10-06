package academy.cashbackmerge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;

public class GameConfigReader {

    private final ObjectMapper objectMapper;
    private final ConfigValidator validator;

    public GameConfigReader() {
        this.objectMapper = new ObjectMapper();
        this.validator = new ConfigValidator();
    }

    public GameConfig read(Path path) {
        try {
            GameConfig config = objectMapper.readValue(path.toFile(), GameConfig.class);
            validator.validate(config);
            return config;
        } catch (IOException e) {
            throw new IllegalArgumentException("Не удалось прочитать конфиг", e);
        }
    }
}
