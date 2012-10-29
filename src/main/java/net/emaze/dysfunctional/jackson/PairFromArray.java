package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.tuples.Pair;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.JavaType;

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
        final Object firstValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), firstType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Object secondValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), secondType, null).deserialize(jp, ctxt);
        dbc.state(JsonToken.END_ARRAY == jp.nextToken(), "expected an END_ARRAY token");
        return Pair.of(firstValue, secondValue);
    }
}
