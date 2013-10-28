package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.options.Either;
import net.emaze.dysfunctional.options.Maybe;
import org.junit.Assert;
import org.junit.Test;

public class EitherFromArrayTest {

    @Test
    public void canDeserializeRightEither() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithEither got = mapper.readValue("{'inner':[[],[42]]}".replace("'", "\""), BeanWithEither.class);
        Either<Boolean, Integer> expected = Either.right(42);
        Assert.assertEquals(expected, got.getInner());
    }

    @Test
    public void canDeserializeLeftEither() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithEither got = mapper.readValue("{'inner':[[true],[]]}".replace("'", "\""), BeanWithEither.class);
        Either<Boolean, Integer> expected = Either.left(true);
        Assert.assertEquals(expected, got.getInner());
    }

    @Test
    public void canDeserializeTwoEither() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithTwoEither got = mapper.readValue("{'former':[[true],[]], 'latter':[[42],[]]}".replace("'", "\""), BeanWithTwoEither.class);
        Either<Boolean, Integer> expectedFormer = Either.left(true);
        Either<Integer, Boolean> expectedLatter = Either.left(42);
        Assert.assertEquals(expectedFormer, got.getFormer());
        Assert.assertEquals(expectedLatter, got.getLatter());
    }

    @Test
    public void canDeserializeNested() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        final BeanWithNestedEither got = mapper.readValue("{'inner':[[],[[[],['test']]]]}".replace("'", "\""), BeanWithNestedEither.class);
        final Either<Boolean, String> inner = Either.right("test");
        Either<Boolean, Either<Boolean, String>> expected = Either.right(inner);
        Assert.assertEquals(expected, got.getInner());
    }

    @Test
    public void canDeserializeReifiedEither() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        Either<Integer, Integer> got = mapper.readValue("[[],[42]]", ReifiedEither.class);
        final Either<Integer, Integer> expected = Either.right(42);
        Assert.assertEquals(expected, got);
    }

    public static class ReifiedEither extends Either<Integer, Integer> {

        public ReifiedEither(Maybe<Integer> left, Maybe<Integer> right) {
            super(left, right);
        }
    }

    public static class BeanWithEither {

        private Either<Boolean, Integer> inner;

        public Either<Boolean, Integer> getInner() {
            return inner;
        }

        public void setInner(Either<Boolean, Integer> inner) {
            this.inner = inner;
        }
    }

    public static class BeanWithTwoEither {

        private Either<Boolean, Integer> former;
        private Either<Integer, Boolean> latter;

        public Either<Boolean, Integer> getFormer() {
            return former;
        }

        public void setFormer(Either<Boolean, Integer> former) {
            this.former = former;
        }

        public Either<Integer, Boolean> getLatter() {
            return latter;
        }

        public void setLatter(Either<Integer, Boolean> latter) {
            this.latter = latter;
        }
    }

    public static class BeanWithNestedEither {

        private Either<Boolean, Either<Boolean, String>> inner;

        public Either<Boolean, Either<Boolean, String>> getInner() {
            return inner;
        }

        public void setInner(Either<Boolean, Either<Boolean, String>> inner) {
            this.inner = inner;
        }
    }
}
