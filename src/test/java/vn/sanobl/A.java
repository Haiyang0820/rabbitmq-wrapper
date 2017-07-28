package vn.sanobl;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import vn.sanobl.rabbitmq.FunctionallyTest;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by cpu11118-local on 28/07/2017.
 */
public class A {
    @Test
    public void a() {
        fail();
    }

    @Category(CategoriesTest.SlowTests.class)
    @Test
    public void b() {
        FunctionallyTest tester = new FunctionallyTest();
        assertEquals("Result", 10, tester.multiply(5, 2));
    }
}