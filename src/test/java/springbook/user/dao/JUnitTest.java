package springbook.user.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;


public class JUnitTest {
    static Set<JUnitTest> testObjects = new HashSet<>();
    
    @Test public void test1(){
        assertThat(testObjects, not(hasItem(this)));
        this.testObjects.add(this);
    }
    
    @Test public void test2(){
        assertThat(testObjects, not(hasItem(this)));
        this.testObjects.add(this);
    }
    
    @Test public void test3(){
        assertThat(testObjects, not(hasItem(this)));
        this.testObjects.add(this);
    }
}
