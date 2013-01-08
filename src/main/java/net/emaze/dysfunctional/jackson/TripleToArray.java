package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import net.emaze.dysfunctional.tuples.Triple;

public class TripleToArray extends JsonSerializer<Triple> {

    @Override
    public void serialize(Triple value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeObject(value.first());
        jgen.writeObject(value.second());
        jgen.writeObject(value.third());
        jgen.writeEndArray();
    }
}
