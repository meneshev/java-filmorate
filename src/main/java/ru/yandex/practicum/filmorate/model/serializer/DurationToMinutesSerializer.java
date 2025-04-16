package ru.yandex.practicum.filmorate.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.Duration;

public class DurationToMinutesSerializer extends JsonSerializer<Duration> {
    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (duration == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeNumber(duration.toMinutes());
        }
    }
}

