import Lesson_6.Homework6;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class Homework6Test {

    private static Homework6 hwSix = null;

    private boolean resBool;
    private int[] x1;
    private int[] x2;

    public Homework6Test(int[] x1, int[] x2, boolean resBool) {
        this.x1 = x1;//input array
        this.x2 = x2;//output array
        this.resBool = resBool;//1 and 4 only in the array status
    }

    @Parameters
    public static Collection myTestData() {
        return Arrays.asList(new Object[][]{
                        {new int[]{1, 2, 3, 4, 5, 6}, new int[]{5, 6}, false},
                        {new int[]{1, 1, 1, 1}, new int[]{}, false},
                        {new int[]{4, 1, 1, 4}, new int[]{}, true},
                        {new int[]{4, 5, 5, 5}, new int[]{5, 5, 5}, false},
                        {new int[]{1,1,1,4,4,1,4,4}, new int[]{5, 5, 5}, true},
                        {new int[]{1,1,1,1,1,1}, new int[]{5, 5, 5}, false},
                        {new int[]{4,4,4,4}, new int[]{5, 5, 5}, false},
                        {new int[]{1,4,4,1,1,4,3}, new int[]{5, 5, 5}, false}
                }
        );
    }

    @Test
    public void boolTest() {
        Assert.assertEquals(hwSix.arrayOnlyFourAndOne(x1), resBool);
    }

    @Test
    public void arrTest() {
        Assert.assertArrayEquals(hwSix.arrayCutAfterLastFour(x1), x2);
    }

    @Before
    public void init() {
        System.out.println("init");
        hwSix = new Homework6();
    }

    @After
    public void tearDown() throws Exception {
        hwSix = null;
    }
}
