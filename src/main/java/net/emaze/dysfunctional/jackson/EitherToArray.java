package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import net.emaze.dysfunctional.options.Either;

public class EitherToArray extends JsonSerializer<Either> {

    @Override
    public void serialize(Either value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeStartArray();
        jgen.writeObject(value.flip().maybe());
        jgen.writeObject(value.maybe());
        jgen.writeEndArray();
    }
}
