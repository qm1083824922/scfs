package com.scfs.web.base;



import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml"})
public class BaseJUnitTest extends AbstractJUnit4SpringContextTests {

    public final static Logger LOGGER = LoggerFactory.getLogger(BaseJUnitTest.class);
}
