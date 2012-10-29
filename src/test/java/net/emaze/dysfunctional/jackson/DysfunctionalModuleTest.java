package net.emaze.dysfunctional.jackson;

import java.io.IOException;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.BoxFromArrayTest;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.MaybeFromArrayTest;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.MaybeFromArrayTest.BeanWithMaybe;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.MaybeToArrayTest;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.PairFromArrayTest;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.PairFromArrayTest.BeanWithPair;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.PairToArrayTest;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.TripleFromArrayTest;
import net.emaze.dysfunctional.jackson.DysfunctionalModuleTest.ZenTest;
import net.emaze.dysfunctional.options.Box;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author rferranti
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    MaybeFromArrayTest.class,
    MaybeToArrayTest.class,
    BoxFromArrayTest.class,
    PairFromArrayTest.class,
    PairToArrayTest.class,
    TripleFromArrayTest.class,
    ZenTest.class
})
public class DysfunctionalModuleTest {

    public static class MaybeToArrayTest {

        @Test
        public void canSerialize() throws IOException {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            final BeanWithMaybe bwm = new BeanWithMaybe();
            bwm.setInner(Maybe.just(42));
            final String got = mapper.writeValueAsString(bwm);
            Assert.assertTrue(got.contains("42"));
        }
    }

    public static class PairToArrayTest {

        @Test
        public void canSerialize() throws IOException {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            final BeanWithPair bwp = new BeanWithPair();
            bwp.setInner(Pair.of("42", 43));
            final String got = mapper.writeValueAsString(bwp);
            Assert.assertTrue(got.contains("\"42\"") && got.contains("43"));
        }
    }

    public static class MaybeFromArrayTest {

        @Test
        public void worksForMe() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            //Mario bean = new Mario();
            //bean.setLuigi(Maybe.just(42));
            BeanWithMaybe mario = mapper.readValue("{'inner': [42]}".replace('\'', '"'), BeanWithMaybe.class);
            Assert.assertEquals(Maybe.just(42), mario.getInner());
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

    public static class BoxFromArrayTest {

        @Test
        public void emptyBox() throws IOException {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            final BeanWithBox bean = mapper.readValue("{'inner': []}".replace('\'', '"'), BeanWithBox.class);
            Assert.assertEquals(Box.empty(), bean.getInner());
        }

        @Test
        public void filledBox() throws IOException {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            final BeanWithBox bean = mapper.readValue("{'inner': [42]}".replace('\'', '"'), BeanWithBox.class);
            Assert.assertEquals(Box.of(42), bean.getInner());
        }

        public static class BeanWithBox {

            public Box<Integer> getInner() {
                return inner;
            }

            public void setInner(Box<Integer> inner) {
                this.inner = inner;
            }
            private Box<Integer> inner;
        }
    }

    public static class PairFromArrayTest {

        @Test
        public void canDeserialize() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            BeanWithPair bean = mapper.readValue("{'inner':['42',42]}".replace('\'', '"'), BeanWithPair.class);
            Assert.assertEquals(Pair.of("42", 42), bean.getInner());
        }

        public static class BeanWithPair {

            private Pair<String, Integer> inner;

            public Pair<String, Integer> getInner() {
                return inner;
            }

            public void setInner(Pair<String, Integer> inner) {
                this.inner = inner;
            }
        }
    }

    public static class TripleFromArrayTest {

        @Test
        public void canDeserialize() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            BeanWithTriple bean = mapper.readValue("{'inner':['42',42, true]}".replace('\'', '"'), BeanWithTriple.class);
            Assert.assertEquals(Triple.of("42", 42, true), bean.getInner());
        }

        public static class BeanWithTriple {

            public Triple<String, Integer, Boolean> getInner() {
                return inner;
            }

            public void setInner(Triple<String, Integer, Boolean> inner) {
                this.inner = inner;
            }
            private Triple<String, Integer, Boolean> inner;
        }
    }

    public static class ZenTest {

        @Test
        public void canDeserializeKebab() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new DysfunctionalModule());
            ZenBean bean = mapper.readValue("{'kebab':[[['42',42, true]],[['42'],[42],[true]]]}".replace('\'', '"'), ZenBean.class);
            Assert.assertEquals(Pair.of(Maybe.just(Triple.of("42", 42, true)), Triple.of(Maybe.just("42"), Maybe.just(42), Maybe.just(true))), bean.getKebab());
        }

        public static class ZenBean { //I'll take one with everything

            private Pair<Maybe<Triple<String, Integer, Boolean>>, Triple<Maybe<String>, Maybe<Integer>, Maybe<Boolean>>> kebab;

            public Pair<Maybe<Triple<String, Integer, Boolean>>, Triple<Maybe<String>, Maybe<Integer>, Maybe<Boolean>>> getKebab() {
                return kebab;
            }

            public void setKebab(Pair<Maybe<Triple<String, Integer, Boolean>>, Triple<Maybe<String>, Maybe<Integer>, Maybe<Boolean>>> kebab) {
                this.kebab = kebab;
            }
        }
    }
}
