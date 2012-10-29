package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.contracts.dbc;
import net.emaze.dysfunctional.options.Box;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.type.JavaType;

/**
 *
 * @author rferranti
 */
public class DysfunctionalModule extends SimpleModule {

    public DysfunctionalModule() {
        super("dysfunctional-module", new Version(1, 0, 0, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addDeserializers(new Deserializers.Base() {
            @Override
            public JsonDeserializer<?> findBeanDeserializer(final JavaType type, final DeserializationConfig config, final DeserializerProvider provider, BeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
                if (Maybe.class.isAssignableFrom(type.getRawClass())) {
                    return new MaybeFromArray(type.containedType(0));
                }
                if (Box.class.isAssignableFrom(type.getRawClass())) {
                    return new BoxFromArray(type.containedType(0));
                }
                if (Pair.class.isAssignableFrom(type.getRawClass())) {
                    return new PairFromArray(type.containedType(0), type.containedType(1));
                }
                if (Triple.class.isAssignableFrom(type.getRawClass())) {
                    return new TripleFromArray(type.containedType(0), type.containedType(1), type.containedType(2));
                }
                return null;
            }
        });
    }

    public static class BoxFromArray extends JsonDeserializer<Box<?>> {

        private final JavaType nestedType;

        public BoxFromArray(JavaType nestedType) {
            this.nestedType = nestedType;
        }

        @Override
        public Box<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            boolean hasValue = false;
            Object value = null;
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                value = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), nestedType, null).deserialize(jp, ctxt);
                hasValue = true;
            }
            return hasValue ? Box.of(value) : Box.empty();
        }
    }

    public static class MaybeFromArray extends JsonDeserializer<Maybe<?>> {

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

    public static class PairFromArray extends JsonDeserializer<Pair<?, ?>> {

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

    public static class TripleFromArray extends JsonDeserializer<Triple<?, ?, ?>> {

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
            jp.nextToken(); // [
            final Object firstValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), firstType, null).deserialize(jp, ctxt);
            jp.nextToken(); // ,
            final Object secondValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), secondType, null).deserialize(jp, ctxt);
            jp.nextToken(); // ,
            final Object thirdValue = ctxt.getDeserializerProvider().findValueDeserializer(ctxt.getConfig(), thirdType, null).deserialize(jp, ctxt);
            dbc.state(JsonToken.END_ARRAY == jp.nextToken(), "expected an END_ARRAY token");
            return Triple.of(firstValue, secondValue, thirdValue);
        }
    }
}
