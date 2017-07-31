package vn.sanobl;

import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import vn.sanobl.rabbitmq.FunctionallyTest;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Created by cpu11118-local on 28/07/2017.
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(CategoriesTest.FastTests.class)
@Suite.SuiteClasses({ A.class, B.class })
//@RunWith(Categories.class)
//@Categories.IncludeCategory(SlowTests.class)
//@Categories.ExcludeCategory(FastTests.class)
//@Suite.SuiteClasses({ A.class, B.class })
public class CategoriesTest {
    public interface FastTests { /* category marker */
    }

    public interface SlowTests { /* category marker */
    }



}




