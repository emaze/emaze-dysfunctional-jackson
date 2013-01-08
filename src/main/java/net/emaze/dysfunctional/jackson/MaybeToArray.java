package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import net.emaze.dysfunctional.options.Maybe;

public class MaybeToArray extends JsonSerializer<Maybe> {

    @Override
    public void serialize(Maybe value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        if (value.hasValue()) {
            jgen.writeObject(value.value());
        }
        jgen.writeEndArray();
    }
}
