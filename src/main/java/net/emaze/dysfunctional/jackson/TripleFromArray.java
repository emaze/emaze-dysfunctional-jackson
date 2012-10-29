package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.tuples.Triple;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.type.JavaType;

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
        final Object firstValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), firstType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Object secondValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), secondType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Object thirdValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), thirdType, null).deserialize(jp, ctxt);
        dbc.state(JsonToken.END_ARRAY == jp.nextToken(), "expected an END_ARRAY token");
        return Triple.of(firstValue, secondValue, thirdValue);
    }
}
