/**
 * @author sik
 *
 */
package com.sik.ivb;

import org.junit.Before;
import org.junit.Test;
/**
 * Right-click and do: Run As > JUnit 
 *
 */
public class Run4AdhocUpdate {
	
	@Before
	public void init() {
		System.setProperty("myproperty", "foo");
	}
	
	@Test
	public void test2() {
		Application m4u = new Application();
		m4u.update(true);
	}

}
