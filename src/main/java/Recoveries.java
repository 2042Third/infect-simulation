import jdk.management.jfr.RecordingInfo;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
/**
 * To have better performance, recovery or death checking and setting
 * is running on a different thread.
 * */
public class Recoveries implements Runnable {
  /**
   * infected sim field
   * */
  InfectSim parent;

  /**
   * Initializes the recovery checking
   * */
  public Recoveries (InfectSim a){
    parent = a;
  }

  /**
   * Checks all the infected subjects, cur_infected;
   * if the subjects rounds-since-infection >= k_count:
   * the subject will have k_prob chance of recovery,
   * else death
   * */
  public void run(){
    for(String i: parent.cur_infected.keySet()){
      if((parent.cur_stat.get(i).get()==1)&&
      (-parent.cur_infected.get(i)+parent.r_n)>= parent.k_count){
        if(rd_(parent.k_prob))
          per_recovery(i);
        else
          per_death(i);
      }
    }
  }
  /**
   * Random resolve
   * @return true if a > then random value, else false
   * */
  public Boolean rd_(double a){
    return parent.rdm_val.nextDouble() <= a;
  }

  /**
   * Per recovery precedure
   * */
  public void per_recovery(Object a){
    parent.cur_stat.get(a).set(2);
    parent.r_count++;
  }
  /**
   * Per death precedure
   * */
  public void per_death(Object a){
    parent.cur_stat.get(a).set(-1);
    parent.d_count++;
  }
}
