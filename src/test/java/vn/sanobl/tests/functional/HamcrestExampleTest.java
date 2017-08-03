package vn.sanobl.tests.functional;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Every.everyItem;
import static org.junit.Assert.assertThat;

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
}
