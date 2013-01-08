package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.options.Maybe;
import org.junit.Assert;
import org.junit.Test;

public class MaybeFromArrayTest {

    @Test
    public void worksForMe() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        BeanWithMaybe bean = mapper.readValue("{'inner': [42]}".replace('\'', '"'), BeanWithMaybe.class);
        Assert.assertEquals(Maybe.just(42), bean.getInner());
    }

    @Test
    public void isReentrant() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        Outer outer = mapper.readValue("{ 'mario': [{'inner': [42]}]}".replace('\'', '"'), Outer.class);
        Assert.assertEquals(Maybe.just(42), outer.getMario().value().getInner());
    }

    @Test
    public void isReentrantWithTwoMaybes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        TwoMaybes outer = mapper.readValue("{ 'first': [42], 'second':[43]}".replace('\'', '"'), TwoMaybes.class);
        Assert.assertEquals(Maybe.just(42), outer.getFirst());
        Assert.assertEquals(Maybe.just(43), outer.getSecond());
    }

    @Test
    public void isReentrantWithTwoMaybesWithDifferentTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        TwoDifferentMaybes outer = mapper.readValue("{ 'first': [42], 'second':['43']}".replace('\'', '"'), TwoDifferentMaybes.class);
        Assert.assertEquals(Maybe.just(42), outer.getFirst());
        Assert.assertEquals(Maybe.just("43"), outer.getSecond());
    }

    @Test
    public void maybeOfMaybeJust() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        MaybeOfMaybe maybeOfMaybe = mapper.readValue("{'value':[[42]]}".replace('\'', '"'), MaybeOfMaybe.class);
        Assert.assertEquals(Maybe.just(Maybe.just(42)), maybeOfMaybe.getValue());
    }

    public static class MaybeOfMaybe {

        public Maybe<Maybe<Integer>> value;

        public Maybe<Maybe<Integer>> getValue() {
            return value;
        }

        public void setValue(Maybe<Maybe<Integer>> value) {
            this.value = value;
        }
    }

    public static class TwoDifferentMaybes {

        private Maybe<Integer> first;
        private Maybe<String> second;

        public Maybe<Integer> getFirst() {
            return first;
        }

        public void setFirst(Maybe<Integer> first) {
            this.first = first;
        }

        public Maybe<String> getSecond() {
            return second;
        }

        public void setSecond(Maybe<String> second) {
            this.second = second;
        }
    }

    public static class TwoMaybes {

        private Maybe<Integer> first;
        private Maybe<Integer> second;

        public Maybe<Integer> getFirst() {
            return first;
        }

        public void setFirst(Maybe<Integer> first) {
            this.first = first;
        }

        public Maybe<Integer> getSecond() {
            return second;
        }

        public void setSecond(Maybe<Integer> second) {
            this.second = second;
        }
    }

    public static class Outer {

        private Maybe<BeanWithMaybe> mario;

        public Maybe<BeanWithMaybe> getMario() {
            return mario;
        }

        public void setMario(Maybe<BeanWithMaybe> mario) {
            this.mario = mario;
        }
    }

    public static class BeanWithMaybe {

        private Maybe<Integer> inner;

        public Maybe<Integer> getInner() {
            return inner;
        }

        public void setInner(Maybe<Integer> inner) {
            this.inner = inner;
        }
    }
    
}
