package net.emaze.dysfunctional.jackson.ranges;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.Ranges;
import net.emaze.dysfunctional.jackson.DysfunctionalModule;
import net.emaze.dysfunctional.order.ComparableComparator;
import net.emaze.dysfunctional.order.NextIntegerSequencingPolicy;
import net.emaze.dysfunctional.order.NextLongSequencingPolicy;
import net.emaze.dysfunctional.ranges.Range;
import org.junit.Assert;
import org.junit.Test;

public class RangeTest {

    private static final Ranges<Integer> RANGES = new Ranges<Integer>(new ComparableComparator<Integer>(), new NextIntegerSequencingPolicy(), 0);
    private static final Ranges<Long> LONG_RANGES = new Ranges<Long>(new ComparableComparator<Long>(), new NextLongSequencingPolicy(), 0L);

    @Test
    public void canSerializeAndDeserialize() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        mapper.registerModule(new RangeModule<Integer>(RANGES, Integer.class));

        final Range<Integer> sourceRange = RANGES.closed(1, 3);
        final RangeBean source = RangeBean.of(sourceRange);
        final RangeBean got = mapper.readValue(mapper.writeValueAsString(source), RangeBean.class);
        Assert.assertEquals(sourceRange, got.getInner());
    }

    @Test
    public void canDeserialize() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        mapper.registerModule(new RangeModule<Integer>(RANGES, Integer.class));
        final RangeBean bean = mapper.readValue("{'inner':[{'b':1,'e':[3]}]}".replace('\'', '"'), RangeBean.class);
        Assert.assertEquals(RANGES.closed(1, 2), bean.getInner());
    }

    @Test(expected = JsonMappingException.class)
    public void cannotDeserializeWhenTypeParamterIsDifferent() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        mapper.registerModule(new RangeModule<Long>(LONG_RANGES, Long.class));
        mapper.readValue("{'inner':[{'b':1,'e':[2]}]}".replace('\'', '"'), RangeBean.class);
    }

    @Test
    public void canRegisterMultipleRangeModules() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        mapper.registerModule(new RangeModule<Long>(LONG_RANGES, Long.class));
        mapper.registerModule(new RangeModule<Integer>(RANGES, Integer.class));
        final RangeBean bean = mapper.readValue("{'inner':[{'b':1,'e':[2]}]}".replace('\'', '"'), RangeBean.class);
        Assert.assertTrue(bean.getInner().begin() instanceof Integer);
    }

    @Test
    public void canSerializeRange() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        mapper.registerModule(new RangeModule<Integer>(RANGES, Integer.class));
        final String wrote = mapper.writeValueAsString(RangeBean.of(RANGES.closed(1, 10)));
        Assert.assertEquals("{'inner':[{'b':1,'e':[11]}]}".replace('\'', '"'), wrote);
    }

    @Test
    public void canSerializeSparseRange() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        mapper.registerModule(new RangeModule<Integer>(RANGES, Integer.class));
        final String wrote = mapper.writeValueAsString(RangeBean.of(RANGES.union(RANGES.closed(1, 2), RANGES.closed(9, 10))));
        Assert.assertEquals("{'inner':[{'b':1,'e':[3]},{'b':9,'e':[11]}]}".replace('\'', '"'), wrote);
    }

    public static class RangeBean {

        private Range<Integer> inner;

        public Range<Integer> getInner() {
            return inner;
        }

        public void setInner(Range<Integer> inner) {
            this.inner = inner;
        }

        public static RangeBean of(Range<Integer> range) {
            final RangeBean rb = new RangeBean();
            rb.setInner(range);
            return rb;
        }
    }
}
