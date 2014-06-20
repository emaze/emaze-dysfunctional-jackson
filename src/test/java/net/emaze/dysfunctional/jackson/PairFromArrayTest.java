package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.tuples.Pair;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class PairFromArrayTest {

    @Test
    public void canDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        BeanWithPair bean = mapper.readValue("{'inner':['42',42]}".replace('\'', '"'), BeanWithPair.class);
        Assert.assertEquals(Pair.of("42", 42), bean.getInner());
    }

    @Test
    public void canDeserializeNullOnLeft() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        BeanWithPair bean = mapper.readValue("{'inner':[null,42]}".replace('\'', '"'), BeanWithPair.class);
        Assert.assertEquals(Pair.of(null, 42), bean.getInner());
    }

    @Test
    public void canDeserializeNullOnRight() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        BeanWithPair bean = mapper.readValue("{'inner':['42',null]}".replace('\'', '"'), BeanWithPair.class);
        Assert.assertEquals(Pair.of("42", null), bean.getInner());
    }

    @Test
    public void canDeserializeNullOnBoth() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        BeanWithPair bean = mapper.readValue("{'inner':[null,null]}".replace('\'', '"'), BeanWithPair.class);
        Assert.assertEquals(Pair.of(null, null), bean.getInner());
    }

    @Test
    public void canDeserializeReifiedPair() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        Pair<Integer, Integer> got = mapper.readValue("[42,42]", ReifiedPair.class);
        Assert.assertEquals(Pair.of(42, 42), got);
    }

    public static class ReifiedPair extends Pair<Integer, Integer> {

        public ReifiedPair(Integer f, Integer l) {
            super(f, l);
        }
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
