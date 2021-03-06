package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.emaze.dysfunctional.options.Maybe;

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
            value = jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : ctxt.findContextualValueDeserializer(nestedType, null).deserialize(jp, ctxt);
            hasValue = true;
        }
        return hasValue ? Maybe.just(value) : Maybe.nothing();
    }
}
