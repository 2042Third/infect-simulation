//package test.java;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

//import java.util.*;

public class TestRunning{
  public static void main(String[] args) {
    Result result = JUnitCore.runClasses(PlagueTest.class);
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
    if (result.wasSuccessful()) {
      System.out.println("All tests passed");
    } else {
      System.out.println(result.getFailureCount() + " test failed");
    }

//    System.out.println(result.wasSuccessful());
  }

}