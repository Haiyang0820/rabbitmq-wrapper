package vn.sanobl;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import vn.sanobl.rabbitmq.FunctionallyTest;

import static org.junit.Assert.assertEquals;

/**
 * Created by cpu11118-local on 28/07/2017.
 */
@Category({ CategoriesTest.SlowTests.class, CategoriesTest.FastTests.class })
public class B {
    @Test
    public void c() {
        FunctionallyTest tester = new FunctionallyTest();
        assertEquals("Result", 200, tester.multiply(100, 2));
    }
}