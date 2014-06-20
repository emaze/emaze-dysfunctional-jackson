package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.tuples.Triple;

public class TripleFromArray extends JsonDeserializer<Triple<?, ?, ?>> {

    private final JavaType firstType;
    private final JavaType secondType;
    private final JavaType thirdType;

    public TripleFromArray(JavaType firstType, JavaType secondType, JavaType thirdType) {
        this.firstType = firstType;
        this.secondType = secondType;
        this.thirdType = thirdType;
    }

    @Override
    public Triple<?, ?, ?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        dbc.state(JsonToken.START_ARRAY == jp.getCurrentToken(), "expected a START_ARRAY token");
        jp.nextToken();
        final Object firstValue = jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : ctxt.findContextualValueDeserializer(firstType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Object secondValue = jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : ctxt.findContextualValueDeserializer(secondType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Object thirdValue = jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : ctxt.findContextualValueDeserializer(thirdType, null).deserialize(jp, ctxt);
        dbc.state(JsonToken.END_ARRAY == jp.nextToken(), "expected an END_ARRAY token");
        return Triple.of(firstValue, secondValue, thirdValue);
    }
}
