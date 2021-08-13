import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
/**
 * Note:
 * Most map operations are looping through the key set and then getting the value. This
 * should have little to no performance impact; Map.get() is O(1) or constant timed.
 * It might have better performance if I looped through maps with each object as Map.entry\<\>.
 * 
 * Plague is a bloodborne infection that affects humans and other mammals. 
 * It is caused by the bacterium, Yersinia pestis. Humans usually get 
 * plague after being bitten by a rodent flea that is carrying the plague 
 * bacterium or by handling an animal infected with plague.
 * */
public class Plague extends Thread {
  /**
   * The "patient zero" of this thread
   * */
  String patient;
  /**
   * The "patient zero" of this thread
   * */
  String patient0;
  /**
   * The parent
   * */
  InfectSim parent;
  /**
   * Track for back tracking
   * */
  ArrayList<String> tracker;
  /**
   * Track for back tracking
   * */
  double lmda_;
  /**
   * rtt
   * */
  int rtt=-1;
  /**
   * Starts thread with this
   * */
  public Plague(String a, InfectSim b){
    patient=a;
    parent=b;
    tracker = new ArrayList<String>();
  }
  /**
   * Starts thread with this.
   * Sets the rtt, the thread dies if rtt gets to zero;
   * */
  public Plague(String a, InfectSim b, int c){
    patient=a;
    parent=b;
    tracker = new ArrayList<String>();
    rtt=c;
  }
  /**
   * Sets a marker on patient, 
   * skip all marked patient,
   * unmarked checks for neighbors,
   * else keep searching until parent.k_count reached, or whole population infected
   * */
  public void run(){
    while(true){
      if(rtt>=0){ // This is only used for random infection spread.
        sel_next_bfs();
        parent.cur_stat.get(patient).set(1);
        rtt--;
        log(patient);
        if(rtt==0)return;
      }
      lmda_=parent.lmda;
      if(!sel_next())return;
      mkd(patient);
      resolve_near(patient);
      log(patient);
      parent.checked_patients.incrementAndGet();
    }
  }

  /**
   * Each neighbor of a infected node will have a lmda chance of getting the infection/
   * */
  public void resolve_near(String a){
    for(Object i:parent.big_map.get(a)){
      if(ifct()&&!parent.cur_infected.containsKey(i))
        per_infect(i);
    }
  } 

  /**
   * Per infection precedure 
   * */
  public void per_infect(Object a){
    parent.cur_stat.get(a).set(1);
    parent.cur_infected.put((String)a, parent.r_n);
    lmda_--;
  }

  /**
   * Check the surrounding the patient
   * @param a the patient
   * */
  public Boolean around_infected(String a){
    for(Object i : parent.big_map.get(a)){
      if(parent.cur_stat.get(i).get()==1)
        return true;
    }
    return false;
  }
  /**
   * Breadth search spread infection
   * */
  public Boolean sel_next_bfs(){
    // patient0=patient;
    if(!mk(patient))return true;
    for(String i: (Set<String>)parent.big_map.get((String)patient0)){
      if(!mk(i)){
        patient=i;
        return true;
      }
    }
    patient0=patient;
    //Back-tracking when there are no way in-front
    if(tracker.size()<=0)return false;
    patient = tracker.get(tracker.size()-1);
    tracker.remove(tracker.size()-1);
    if(!sel_next()){
      return false;
    }
    return true;
  }


  /**
   * Gets the next patient, from the list of infected patients
   * 
   * */
  public Boolean sel_next(){
    if(!mk(patient))return true;
    for(String i:parent.cur_infected.keySet()){
      if(mk(i))continue;
      if(parent.cur_infected.get(i) < parent.r_n){ // if the lambda value is not satisfied and the node is not marked
        if(!has_infable(i))continue; // if the node doesn't have any un-infected neighbors, continue
        patient = i;
        return true;
      }
    }
    return false;
  }
  /**
   * Checks if the subject has any un-infected neighbors
   * @return true if there is, else false
   * */
  public Boolean has_infable(String a){
    for(String i: parent.cur_stat.keySet()){
      if(parent.cur_stat.get(i).get()==0)return true;
    }
    return false;
  }

  /**
   * Gets the next patient, in DFS.
   * 
   * */
  public Boolean sel_next_old(){
    if(!mk(patient))return true;
    for(String i: (Set<String>)parent.big_map.get((String)patient)){
      if(!mk(i)){
        patient=i;
        return true;
      }
    }
    //Back-tracking when there are no way in-front
    if(tracker.size()<=0)return false;
    patient = tracker.get(tracker.size()-1);
    tracker.remove(tracker.size()-1);
    if(!sel_next()){
      return false;
    }
    return true;
  }
  /**
   * Reads from the lmda of InfectSim to determine a true/false for a infection.
   * */
  public Boolean ifct(){
    double valu = parent.rdm_val.nextDouble();
    // System.out.printf("Random Infect %s\n",valu+"");
    return (lmda_)>(valu);
  }

  /**
   * Checks all the conditions, return false if not met.
   * */
  public Boolean ok_to_go(){
    if(parent.infected.get() >= parent.k_count && parent.enable_k){
      return false;
    }
    if(parent.checked_patients.get() >= parent.population_count){
      return false;
    }
    return true;
  }

  /**
   * Counts the number of direct edges it has 
   * */
  public Integer count_around(String a) {
    return parent.big_map.get(a).size();
  }
  /**
   * puts the string in the tracker
   * */
  public void log(String a){
    tracker.add(a);
  }

  /**
   * Marks the patient
   * @param a input subject
   * */
  public void mkd(String a){
    parent.cur_mk.put(a,true);

  }

  /**
   * Check for mark
   *  0:checking
   *  1:checked
   *  2:checked infected
   * */
  public boolean mk(String a){
    return (parent.cur_mk.containsKey(a));
  }
}