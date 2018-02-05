package com.avenuecode.orders.resource;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OrderResourceTest.class, ProductResourceTest.class, SearchResourceTest.class })
public class AllResourceTests {

}
