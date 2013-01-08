package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import net.emaze.dysfunctional.options.Box;

public class BoxToArray extends JsonSerializer<Box> {

    @Override
    public void serialize(Box value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        if (value.hasContent()) {
            jgen.writeObject(value.getContent());
        }
        jgen.writeEndArray();
    }
}
