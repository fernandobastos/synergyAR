package com.coredump.synergyar.android;

import com.coredump.synergyar.android.configuration.SynergyActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
/**
 * Checks correctness of the instantiation of the app.
 *
 * @author Jose Esteban Trejos Quiros
 * @version 0.0.1
 * @since 0.0.1
 */
public class AndroidLauncherTest extends ActivityInstrumentationTestCase2<SynergyActivity> {

  /**
  * No parameter constructor.
  */
  public AndroidLauncherTest() {
    super(SynergyActivity.class);
  }

  /**
  * Prepares the test.
  */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
  * Frees resources.
  */
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
  * Runs a dummie test.
  */
  @Override
  protected void runTest() throws Throwable {
    super.runTest();
  }

  /**
  * Fires the test.
  */
  @SmallTest
  public void testDoSomething() throws Throwable {
    assertEquals("", "");
  }

}
