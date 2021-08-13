import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;


public class PlagueTest {
  InfectSim unit1=new InfectSim();
  VirusStats unit2=new VirusStats();
  InfectSim unit3=new InfectSim();
  Stats unit2_supports=new Stats(100,99,0,0,0);
  ArrayList<Integer> expected = new ArrayList<>();

  @Test
  public void test_loads(){
    Assertions.assertTrue(unit1.testing_procedure(),"InfectSimTest suit failed!");
  }
  @Test
  public void test_stat_loading(){
    expected.add(100);
    expected.add(99);
    expected.add(0);
    expected.add(0);
    expected.add(0);
    unit2.add(unit2_supports);

    Assertions.assertIterableEquals(expected,unit2.latest(),"InfectSimTest suit failed!");
  }
  @Test
  public void test_stat_retrival(){
    expected.add(100);
    expected.add(99);
    expected.add(0);
    expected.add(0);
    expected.add(0);
    unit2.add(unit2_supports);
    unit2.add(unit2_supports);
    unit2.add(unit2_supports);

    Assertions.assertIterableEquals(expected,unit2.latest(),"InfectSimTest suit failed!");
  }

  @Test
  public void patient_recovery(){
    Integer b = 20;//rounds
    unit3.testing_procedure();
    unit3.rd_infect();
    for(int i=0; i<b;i++){// Run b rounds, to build-up recoveries
      unit3.get_update();
    }
    Assertions.assertEquals(0,unit3.get_update());//
    Integer a = unit3.sts.latest().get(3);//Get the last recovery telemitry
    Assertions.assertNotEquals(0,a,"No recovery in "+b+" rounds, actual "+a);
  }

  @Test
  public void patient_death(){
    Integer b = 20;//rounds
    unit3.testing_procedure();
    unit3.rd_infect();
    for(int i=0; i<b;i++){// Run b rounds, to build-up recoveries
      unit3.get_update();
    }
    Assertions.assertEquals(0,unit3.get_update());//
    Integer a = unit3.sts.latest().get(4);//Get the last recovery telemitry
    Assertions.assertNotEquals(0,a,"No death in "+b+" rounds, actual "+a);
  }
  @Test
  public void patient_infection(){
    Integer b = 20;//rounds
    unit3.testing_procedure();
    unit3.rd_infect();
    for(int i=0; i<b;i++){// Run b rounds, to build-up recoveries
      unit3.get_update();
    }
    Assertions.assertEquals(0,unit3.get_update());//
    Integer a = unit3.sts.latest().get(2);//Get the last recovery telemitry
    Assertions.assertNotEquals(0,a,"No infection in "+b+" rounds, actual "+a);
  }
}