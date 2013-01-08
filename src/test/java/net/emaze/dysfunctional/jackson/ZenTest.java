package net.emaze.dysfunctional.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import net.emaze.dysfunctional.options.Maybe;
import net.emaze.dysfunctional.tuples.Pair;
import net.emaze.dysfunctional.tuples.Triple;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rferranti
 */
public class ZenTest {

    @Test
    public void canDeserializeKebab() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new DysfunctionalModule());
        ZenBean bean = mapper.readValue("{'kebab':[[['42',42, true]],[['42'],[42],[true]]]}".replace('\'', '"'), ZenBean.class);
        Assert.assertEquals(Pair.of(Maybe.just(Triple.of("42", 42, true)), Triple.of(Maybe.just("42"), Maybe.just(42), Maybe.just(true))), bean.getKebab());
    }

    public static class ZenBean {

        //I'll take one with everything
        private Pair<Maybe<Triple<String, Integer, Boolean>>, Triple<Maybe<String>, Maybe<Integer>, Maybe<Boolean>>> kebab;

        public Pair<Maybe<Triple<String, Integer, Boolean>>, Triple<Maybe<String>, Maybe<Integer>, Maybe<Boolean>>> getKebab() {
            return kebab;
        }

        public void setKebab(Pair<Maybe<Triple<String, Integer, Boolean>>, Triple<Maybe<String>, Maybe<Integer>, Maybe<Boolean>>> kebab) {
            this.kebab = kebab;
        }
    }

}
