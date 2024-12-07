import org.hsbc.cache.CacheTemplate;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CacheTemplateTest {

    @Test
    void testPutAndGet() {
        CacheTemplate cacheTemplate = new CacheTemplate();
        cacheTemplate.put("key1", "value1", 5, TimeUnit.SECONDS);
        assertEquals("value1", cacheTemplate.get("key1"));
    }

    @Test
    void testExpiration() throws InterruptedException {
        CacheTemplate cacheTemplate = new CacheTemplate();

        cacheTemplate.put("key2", "value2", 1, TimeUnit.SECONDS);

        assertEquals("value2", cacheTemplate.get("key2"));

        Thread.sleep(1500);

        assertNull(cacheTemplate.get("key2"));
    }

    @Test
    void testEviction() {
        CacheTemplate cacheTemplate = new CacheTemplate();

        cacheTemplate.put("key1", "value1", 5, TimeUnit.SECONDS);
        cacheTemplate.put("key2", "value2", 5, TimeUnit.SECONDS);
        cacheTemplate.put("key3", "value3", 5, TimeUnit.SECONDS);

        assertEquals("value1", cacheTemplate.get("key1"));
        assertEquals("value2", cacheTemplate.get("key2"));
        assertEquals("value3", cacheTemplate.get("key3"));

        cacheTemplate.remove("key2");

        assertNull(cacheTemplate.get("key2"));

        assertEquals("value1", cacheTemplate.get("key1"));
        assertEquals("value3", cacheTemplate.get("key3"));
    }

    @Test
    void testCacheSizeLimit() {
        CacheTemplate cacheTemplate = new CacheTemplate();

        cacheTemplate.put("key1", "value1", 5, TimeUnit.SECONDS);
        cacheTemplate.put("key2", "value2", 5, TimeUnit.SECONDS);
        cacheTemplate.put("key3", "value3", 5, TimeUnit.SECONDS);

        assertEquals("value1", cacheTemplate.get("key1"));
        assertEquals("value2", cacheTemplate.get("key2"));
        assertEquals("value3", cacheTemplate.get("key3"));
    }
}