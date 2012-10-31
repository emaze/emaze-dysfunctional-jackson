package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.options.Either;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class EitherToArray extends JsonSerializer<Either> {

    @Override
    public void serialize(Either value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeObject(value.flip().maybe());
        jgen.writeObject(value.maybe());
        jgen.writeEndArray();
    }
}
