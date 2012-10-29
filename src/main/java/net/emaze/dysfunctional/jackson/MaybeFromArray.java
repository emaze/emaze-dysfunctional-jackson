package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.options.Maybe;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.JavaType;

public class MaybeFromArray extends JsonDeserializer<Maybe<?>> {

    private final JavaType nestedType;

    public MaybeFromArray(JavaType nestedType) {
        this.nestedType = nestedType;
    }

    @Override
    public Maybe<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        boolean hasValue = false;
        Object value = null;
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            value = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), nestedType, null).deserialize(jp, ctxt);
            hasValue = true;
        }
        return hasValue ? Maybe.just(value) : Maybe.nothing();
    }
}
