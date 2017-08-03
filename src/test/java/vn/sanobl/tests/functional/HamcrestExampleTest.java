package vn.sanobl.tests.functional;

import org.junit.Assert;
import org.junit.Test;
import vn.sanobl.rabbitmq.FunctionallyTest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Every.everyItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by cpu11118-local on 01/08/2017.
 */
public class HamcrestExampleTest {

    @Test
    public void hamcrestListTest(){
        List<Integer> list = Arrays.asList(3, 6, 10, 20);
        assertThat(list, hasSize(4));

        // ensure the order is correct
        assertThat(list, contains(3, 6, 10, 20));

//        assertThat(list, containsInAnyOrder(2, 4, 5));

        assertThat(list, everyItem(greaterThan(1)));


        String str1 = "aaaaaaaa";
        assertThat(str1, is(instanceOf(String.class)));
    }

    @Test
    public void testMoreThanOneReturnValue()  {
        Iterator<String> i= mock(Iterator.class);
        when(i.next()).thenReturn("Mockito").thenReturn("rocks");
        String result= i.next()+" "+i.next();
        //assert
        assertEquals("Mockito rocks", result);
    }

    @Test
    public void test1()  {
        //  create mock
        FunctionallyTest test = mock(FunctionallyTest.class);

        // define return value for method getUniqueId()
        when(test.multiply(1, 1)).thenReturn(43);

        // use mock in test....
        assertEquals(test.multiply(1, 1), 43);
    }
}
