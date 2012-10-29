package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.options.Maybe;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

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
