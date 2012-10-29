package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.options.Box;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

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
