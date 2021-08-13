import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.Random.*;
import java.util.*;

/**
 * Replacement for map
 * Handles the data for the infection simulation, ensuring thread saftey at every level.
 * Mainly for error handling, trying not to throw.
 * Mike Yang Aug 8
 * */
public class BigMap<K,V extends Set<K> > {

  /**
   * Puts the value into the inner set if it isn't there; initializes the key if it isn't already.
   * @param a the input key string
   * @param b the input value string
   * */
  public boolean push_back(K a, K b){
    try{
      big_map.putIfAbsent(a,this.init_new_set());
    }
    catch(Exception e){
      System.out.printf("Big map fails, push_back -> putIfAbsent()\n");
      return false;
    }
    try{
      big_map.get(a).add(b);
      push_back(a,new AtomicInteger(default_status));

    }
    catch(Exception e){
      System.out.printf("Big map fails, push_back -> big_map.get(a).add(b)\n");
      return false;
    }
    return true;
  }

  /**
   * Puts the value into the inner set if it isn't there; initializes the key if it isn't already.
   * 
   * @param a the input key string
   * @param b the input value string
   * */
  public boolean push_back(K a, AtomicInteger b){
    try{
      big_map_status.put(a,b);

    }
    catch(Exception e){
      System.out.printf("Big map status fails, push_back -> putIfAbsent()\n");
      return false;
    }

    return true;
  }

  /**
   *  Initializes a new concurrent set for big_map
   * @return concurrent set
   * */
  public Set<K> init_new_set(){
    return Collections.newSetFromMap(new ConcurrentHashMap<K,Boolean>());
  }
  /**
   *  Initializes a new concurrent set for big_map
   * @return concurrent set
   * */
  public Integer size(){
    return big_map.size();
  }
  /**
   * Reduce the existing big_map into an array of integer
   * @return an array of integer 
   * */
  public ArrayList<Integer> reduce_to_int (){
    ArrayList<Integer> lst = new ArrayList<Integer>();
    for(K str : big_map.keySet()){
      lst.add(str.hashCode());
    }
    return lst;
  }
  /**
   * Randomly return a key
   * */
  public String rd_get (Map<String,?> input){
    Integer a = (int)((input.size()) *(rdm.nextDouble()))-1;
    Integer b = 0;
    for(String i: input.keySet()){
      if(b>=a)return i;
      b++;
    }
    return null;
  }
  /**
   * Get the key list
   * @return an array of integer 
   * */
  public ArrayList<String> get_all_str (Map<String,?> a){
    ArrayList<String> lst = new ArrayList<String>();
    Set<String> key_l = a.keySet();
    for(String str : key_l){
      lst.add(str);
    }
    return lst;
  }

  /**
   * Initializes a map
   * */
  public BigMap(){
    big_map = new ConcurrentHashMap<K,Set<K>>();
    big_map_status=new ConcurrentHashMap<K,AtomicInteger>();
  }
  /**
   * Return the big_map
   * */
  public ConcurrentMap<K,Set<K>> gt(){
    return big_map;
  } 
  /**
   * Return the big_map status
   * */
  public ConcurrentMap<K,AtomicInteger> gt_stat(){
    return big_map_status;
  } 

  /**
   * Sets the status map
   * 
   * */
  public void  set_map_stat(ConcurrentMap<K,AtomicInteger> a){
    big_map_status.putAll(a);
  }

  /**
   * Gets the value of the big_map
   * @param a key
   * */
  public Set<K> get(K a){
    return big_map.get(a);
  }
  /**
   * Gets the status of the big map
   * */
  public AtomicInteger get_stat(K a){
    return big_map_status.get(a);
  }
  /**
   * Random variable
   * */
  Random rdm = new Random();

  /**
   * The default status
   * */
  Integer default_status = 0;

  /**
   *  Map that contains the whole thing
   * */
  public ConcurrentMap<K,Set<K>> big_map;

  /**
   * Status map, this should be abstracted away from the user; 
   * they should only notice an inconspicuous increase in memory usage 
   * */
  public ConcurrentMap<K, AtomicInteger> big_map_status;
};