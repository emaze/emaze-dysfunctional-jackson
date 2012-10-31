package net.emaze.dysfunctional.jackson.exp;

import java.io.IOException;
import net.emaze.dysfunctional.order.ComparableComparator;
import net.emaze.dysfunctional.order.IntegerSequencingPolicy;
import net.emaze.dysfunctional.ranges.DenseRange;
import net.emaze.dysfunctional.ranges.Endpoints;
import net.emaze.dysfunctional.ranges.Range;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class RangeTest {

    @Test
    public void canDeserialize() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new RangeModule());
        final RangeBean bean = mapper.readValue("{'inner':{'s':'net.emaze.dysfunctional.order.IntegerSequencingPolicy' ,'c':'net.emaze.dysfunctional.order.ComparableComparator', 'd':[{'l':1,'u':2,'e':'IncludeBoth'}]}}".replace('\'', '"'), RangeBean.class);
        final DenseRange<Integer> range = new DenseRange<Integer>(new IntegerSequencingPolicy(), new ComparableComparator<Integer>(), Endpoints.IncludeBoth, 1, 2);
        Assert.assertEquals(range, bean.getInner());
    }

    public static class RangeBean {

        private Range<Integer> inner;

        public Range<Integer> getInner() {
            return inner;
        }

        public void setInner(Range<Integer> inner) {
            this.inner = inner;
        }
    }
}