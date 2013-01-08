package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.emaze.dysfunctional.options.Box;

public class BoxFromArray extends JsonDeserializer<Box<?>> {

    private final JavaType nestedType;

    public BoxFromArray(JavaType nestedType) {
        this.nestedType = nestedType;
    }

    @Override
    public Box<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        boolean hasValue = false;
        Object value = null;
        while (jp.nextToken() != JsonToken.END_ARRAY) {
            value = ctxt.findContextualValueDeserializer(nestedType, null).deserialize(jp, ctxt);
            hasValue = true;
        }
        return hasValue ? Box.of(value) : Box.empty();
    }
}
