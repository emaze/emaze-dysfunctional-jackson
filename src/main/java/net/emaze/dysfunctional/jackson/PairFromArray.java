package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.tuples.Pair;

public class PairFromArray extends JsonDeserializer<Pair<?, ?>> {

    private final JavaType firstType;
    private final JavaType secondType;

    public PairFromArray(JavaType firstType, JavaType secondType) {
        this.firstType = firstType;
        this.secondType = secondType;
    }

    @Override
    public Pair<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        dbc.state(JsonToken.START_ARRAY == jp.getCurrentToken(), "expected a START_ARRAY token");
        jp.nextToken();
        final Object firstValue = ctxt.findContextualValueDeserializer(firstType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Object secondValue = ctxt.findContextualValueDeserializer(secondType, null).deserialize(jp, ctxt);
        dbc.state(JsonToken.END_ARRAY == jp.nextToken(), "expected an END_ARRAY token");
        return Pair.of(firstValue, secondValue);
    }
}
