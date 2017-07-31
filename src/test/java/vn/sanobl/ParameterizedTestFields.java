package vn.sanobl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import vn.sanobl.rabbitmq.FunctionallyTest;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by cpu11118-local on 28/07/2017.
 */
@RunWith(Parameterized.class)
public class ParameterizedTestFields {

    @Parameterized.Parameter(0)
    public int m1;
    @Parameterized.Parameter(1)
    public int m2;
    @Parameterized.Parameter(2)
    public int result;


    // creates the test data
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { 1 , 2, 2 }, { 5, 3, 15 }, { 121, 4, 48422 }, { 121, 4, 4843 } };
        return Arrays.asList(data);
    }


    @Test
    public void testMultiplyException() {
        FunctionallyTest tester = new FunctionallyTest();

        assertEquals("Result========>", result, tester.multiply(m1, m2));
        fail("FAIL ;;;;");
        fail();
    }

}
