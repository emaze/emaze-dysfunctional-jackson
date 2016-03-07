package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;

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
        final Maybe<Object> maybeLeft = (Maybe<Object>) ctxt.findContextualValueDeserializer(maybeLeftType, null).deserialize(jp, ctxt);
        jp.nextToken();
        final Maybe<Object> maybeRight = (Maybe<Object>) ctxt.findContextualValueDeserializer(maybeRightType, null).deserialize(jp, ctxt);
        dbc.state(JsonToken.END_ARRAY == jp.nextToken(), "expected an END_ARRAY token");
        return new Either<>(maybeLeft.optional(), maybeRight.optional());
    }
}
