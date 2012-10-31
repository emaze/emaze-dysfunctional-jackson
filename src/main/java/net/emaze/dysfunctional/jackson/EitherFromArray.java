package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public class EitherFromArray extends JsonDeserializer<Either<?, ?>> {

    private final JavaType maybeLeftType;
    private final JavaType maybeRightType;

    public EitherFromArray(JavaType leftType, JavaType rightType) {
        final TypeFactory typeFactory = TypeFactory.defaultInstance();
        this.maybeLeftType = typeFactory.constructParametricType(Maybe.class, leftType);
        this.maybeRightType = typeFactory.constructParametricType(Maybe.class, rightType);
    }

    @Override
    public Either<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        dbc.state(JsonToken.START_ARRAY == jp.getCurrentToken(), "expected a START_ARRAY token");
        jp.nextToken();
        final Maybe<Object> maybeLeft = (Maybe<Object>) ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), maybeLeftType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Maybe<Object> maybeRight = (Maybe<Object>) ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), maybeRightType, null).deserialize(jp, ctxt);
        dbc.state(JsonToken.END_ARRAY == jp.nextToken(), "expected an END_ARRAY token");
        return new Either<Object, Object>(maybeLeft, maybeRight);
    }
}
