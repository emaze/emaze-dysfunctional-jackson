package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.tuples.Pair;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class PairToArray extends JsonSerializer<Pair> {

    @Override
    public void serialize(Pair value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeObject(value.first());
        jgen.writeObject(value.second());
        jgen.writeEndArray();
    }
}
