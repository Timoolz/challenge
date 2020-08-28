package ng.chaka.challenge;

import ng.chaka.challenge.service.BaseService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BinaryGapTest {

    @Autowired
    BaseService baseService;

    int number = -1;
    int result = -1;


    @Test
    public void testBinaryGapAlgorithm() {

        //number 9 has binary representation 1001 and contains a binary gap of length 2
        number = 9;
        result = baseService.maxBinaryGap(number);
        Assert.assertEquals(result, 2);


        //number 529 has binary representation 1000010001 and contains two binary gaps: one of length 4 and one of length 3
        number = 529;
        result = baseService.maxBinaryGap(number);
        Assert.assertEquals(result, 4);


        //The number 20 has binary representation 10100 and contains one binary gap of length 1
        number = 20;
        result = baseService.maxBinaryGap(number);
        Assert.assertEquals(result, 1);


        //The number 15 has binary representation 1111 and has no binary gaps
        number = 15;
        result = baseService.maxBinaryGap(number);
        Assert.assertEquals(result, 0);


        //The number 32 has binary representation 100000 and has no binary gaps.
        number = 32;
        result = baseService.maxBinaryGap(number);
        Assert.assertEquals(result, 0);

        //number 1041 has binary representation 10000010001 and contains two binary gaps: one of length 5 and one of length 3
        number = 1041;
        result = baseService.maxBinaryGap(number);
        Assert.assertEquals(result, 5);


    }
}
