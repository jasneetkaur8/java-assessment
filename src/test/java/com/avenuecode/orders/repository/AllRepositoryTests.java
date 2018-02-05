package com.avenuecode.orders.repository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OrderRepositoryTest.class, ProductRepositoryTest.class })
public class AllRepositoryTests {

}
