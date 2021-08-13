import java.util.*;
import javax.swing.*;
import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.io.*;
import java.nio.file.*;
/**
 * The Infection simulation class 
 * Mike Yang Aug 8
 * */
public class InfectSim {

  private ConcurrentHashMap<String,Boolean> cur_recovered;//Deprecated
  private ConcurrentHashMap<String,Boolean> cur_dead;//Deprecated

  /**
   * Opens the input cvs file and reads the data into big_map
   * Resets all the things that a simulation needs for an accurate sim
   * @param usr_file_name File name of the input cvs file
   * */
  public Integer load_cvs (String usr_file_name){
    BufferedReader seed_buff= null;
    r_n=0;
    if(usr_file_name.equals("tests")){
      return 2;
    }
    if(parent!=null) {
      ttrs = Integer.parseInt(Objects.requireNonNull(parent.ttrsBox.getSelectedItem()).toString());
      thd_c = Integer.parseInt(Objects.requireNonNull(parent.threadBox.getSelectedItem()).toString());
    }
    else {
      ttrs = 9999;
      thd_c=6;
    }
    cur_infected=new ConcurrentHashMap<String, Long>();
    cur_recovered=new ConcurrentHashMap<>();
    cur_dead=new ConcurrentHashMap<>();
    sts=new VirusStats();
    r_count=0;
    d_count=0;
    if(parent != null && parent.statsWindow!=null){
      parent.statsWindow.close_stats();
      parent.openStatsButtonActionPerformed();
    }
    try{

      File seed_file = new File(usr_file_name);
      FileReader seed_rd = new FileReader(seed_file);
      seed_buff = new BufferedReader(seed_rd);
    }
    catch (Exception e){
      if(parent!= null )JOptionPane.showMessageDialog(parent, "File not found or simulation format false!");
    } 
    if(seed_buff==null){
      if(parent!= null )JOptionPane.showMessageDialog(parent, "File cannot parse, parse_cvs received null");
      return 0;
    }

    return parse_cvs(seed_buff);

  }

  /**
   * Takes a BufferedReader and reads the file line by line, while loading the data into big_map
   * 
   * @param seed_buff The input buffer
   * */
  public Integer parse_cvs(BufferedReader seed_buff){
    // Starts parsing the input seed file
    String line="";
    try {
      line = seed_buff.readLine();
    }
    catch(IOException e){
      if(parent!= null )JOptionPane.showMessageDialog(parent, "File first readLine fails, data not added.");
      }
    String[] inp;
    while (line != null){
      inp = line.split(",");
      if(!big_map.push_back(inp[0],inp[1]))
        if(parent!= null )JOptionPane.showMessageDialog(parent, "File loading fails, fix it.");
      if(!big_map.push_back(inp[1],inp[0]))
        if(parent!= null )JOptionPane.showMessageDialog(parent, "File loading fails, fix it.");
      try {
        line = seed_buff.readLine();
      }
      catch(IOException e){
        if(parent!= null )JOptionPane.showMessageDialog(parent, "File readLine fails, data not added.");
      }
    }
    population_count = big_map.size();
    get_update();
    r_n=0;
    return big_map.size();
  }

  /**
   *  Initializes the class, and big_map
   * */
  public InfectSim(Gol_gui a){
    big_map = new BigMap<String, Set<String>>();
    archiv = new ArrayList<Map<String,AtomicInteger>>();
    thd_s = new ArrayList<Thread>();
    cur_infected=new ConcurrentHashMap<String, Long>();
    cur_stat = new ConcurrentHashMap<String, AtomicInteger>();
    cur_stat.putAll(big_map.big_map_status);
    cur_mk = new ConcurrentHashMap<String, Boolean>();
    chthd(thd_c);
    parent = a;
    // get_update();
  }


  /**
   *  Run a tick, cleans the previous run, copied to the big_map
   * */
  public Integer get_update(){
    if(!startup_procedure()) return 0;
    for(Thread i: thd_s){
      // System.out.println("Starting a thread...");
      i.start();
    }
    for(Thread i: thd_s){ // Wait for all the dispatched threads to join.
      try{                // We need this to keep the program from thread bombing our computer
        i.join();
      }
      catch(Exception e){
        System.out.println("Thread joining failure, skipping; no need to elevate.");
      }
    }
    end_procedure();
    return 0;
  }
  /**
   * Do all the initialization needed for every tick
   * @return true, if the start-up is successful; false otherwise.
   * */
  public Boolean startup_procedure(){
    if(r_n>ttrs){
      if(parent!= null )JOptionPane.showMessageDialog(parent, "Simulation stopped. \nTick count reached "+ttrs);
      if(parent!= null )parent.action=false;
      return false;
    }
    chthd(thd_c);// might need to change the frequency of calling this
    thd_s.add(new Thread( new Recoveries(this)));// Add recovery checking
    cur_stat.putAll(big_map.big_map_status);
//    clr_mk(cur_mk);
    cur_mk.clear();
    return true;
  }

  /**
   * Do all the clean-up and data collection at the of every tick
   * */
  public void end_procedure(){
    if(enable_history)put_archiv();
    big_map.set_map_stat(cur_stat);
    sts.add(new Stats(
      (int) r_n,
      big_map.size(),
      cur_infected.size(),
      r_count,
      d_count
      ));
    r_n++;//round number ++

    if(parent!= null ) { // don't need to change GUI elemnts in headless mode
      if(parent.statsWindow!=null)parent.statsWindow.update_data(sts.latest());
      parent.statLabel.setText(r_n + "");
      parent.infected_lb.setText(cur_infected.size() + "");
      System.out.printf("###########################\n%s infected, %s'th round\n", cur_infected.size(), r_n);
    }
  }
  /**
   * Run without GUI interactions
   * */
  public InfectSim(){
    big_map = new BigMap<String, Set<String>>();
    archiv = new ArrayList<Map<String,AtomicInteger>>();
    thd_s = new ArrayList<Thread>();
    cur_infected=new ConcurrentHashMap<String, Long>();
    cur_stat = new ConcurrentHashMap<String, AtomicInteger>();
    cur_stat.putAll(big_map.big_map_status);
    cur_mk = new ConcurrentHashMap<String, Boolean>();
    chthd(thd_c);
  }

  /**
   * Testing procedure
   * Acquires the needed telemetry and passes a file.
   * @return true if the test succeeds
   * */
  public Boolean testing_procedure(){
    Integer a =load_cvs("../src/test/resources/GermanBoysSchoolClass.edges.csv");
    if(a!=0)
      return true;
    return false;
  }
  /**
   *
   * */
  /**
   * Sets all the marks to default value
   * This happens every round
   * */
  public void clr_mk(Map<String, AtomicBoolean> a){
    for(String i:a.keySet()){
      a.get(i).set(false);
    }
  }
  /**
   * Copy the current status into to archiv
   * */
  public void put_archiv (){
    archiv.add(new ConcurrentHashMap<String, AtomicInteger>(cur_stat));
  }


  /**
   * Thread changer
   * Should reduce the times that one need to call this.
   * If not all thread join, this function will return
   * @param a thread count
   * */
  public void chthd (int a){
    String seed_a = "seed";
    if(thd_joined()){
      thd_s = new ArrayList<Thread>();
      if(a>cur_infected.size())
        a=cur_infected.size();
      for(int i =0 ; i< a;i++){
        thd_s.add(new Thread(new Plague(big_map.rd_get(cur_infected),this)));
      }
    }
  }

  /**
   * peek threads joinable
   * */
  public boolean thd_joined(){
    for(Thread i: thd_s){
      if(i==null)return true;
      if(i.isAlive())return false;
    }
    return true;
  }

  /**
   * The population of the simulation
   * */
  public Integer size(){
    return big_map.size();
  }

  /**
   * Randomly infect a subject
   * */
  public void rd_infect(){
    String subj = big_map.rd_get(big_map.big_map_status);
    per_infect(subj);
    if(parent!=null)parent.infected_lb.setText(cur_infected.size()+"");

  }
  /**
   * Exports the current data from VirusStats class to the file
   * at path_name;
   * The file will be a cvs formatted file
   * @param path_name the absolute file name
   * */
  public void export_sts(String path_name){
    if(sts==null) return;
    FileWriter fw;
    try {
      fw = new FileWriter(path_name);
      for (ArrayList<Integer> a:sts.data) {
        String ln= String.format("%s, %s, %s, %s, %s\n",
          a.get(0),
          a.get(1),
          a.get(2),
          a.get(3),
          a.get(4)
        );
        fw.write(ln);
        System.out.println("Export "+ln);
      }
      fw.close();
    }
    catch(Exception e){
      JOptionPane.showMessageDialog(parent, "Unable to export, file path or file name faults");
      System.out.println("Export failure");
    }

  }

  /**
   * Infect a subject if the subject has more than s_count of neighbors
   * */
  public void gt_infect(){
    for(Object i : big_map.big_map.keySet()){
      if(big_map.get((String) i).size()>s_count)
        per_infect(i);
    }
  }

    /**
   * Per infection precedure 
   * */
  public void per_infect(Object a){
    cur_stat.get(a).set(1);
    cur_infected.put((String)a, r_n);
  }

  /**
   * Infect a subject if the subject has more than s_count of neighbors
   * */
  public void spread_infect(){
    String subj = big_map.rd_get(big_map.big_map);
    per_infect(subj);    
    new Thread(new Plague(subj,this,k_count));
  }

  /**
   * History rounds
   * */  
  public ArrayList<Map<String,AtomicInteger>>archiv;

  /**
   * Labmda infection force
   * Interface
   * */
  public double lmda =0.7; 

  /**
   * Labmda infection force
   * Interface
   * */
  public int k_count =7;
  /**
   * Labmda infection force
   * Interface
   * */
  public double k_prob =0.6;
  /**
   * Labmda infection force
   * Interface
   * */
  public int s_count =10; 
  /**
   * Labmda infection force
   * Interface
   * */
  public Boolean enable_k =false; 
  /**
   * The thread should stop when this gets to k_count
   * Interface
   * */
  public AtomicInteger infected = new AtomicInteger(0); 
  /**
   * This is the most up-to-date status
   * */
  public ConcurrentHashMap<String, AtomicInteger>cur_stat; 
  /**
   * The marking tracker for each round, 
   * A Plague can only count the neighbors if its unmarked.
   * */
  public ConcurrentHashMap<String, Boolean>cur_mk;
  /**
   * The marking tracker for each round, 
   * A Plague can only count the neighbors if its unmarked.
   * */
  public ConcurrentHashMap<String, Long>cur_infected ; 
  /**
   * Thread count
   * Interface
   * */
  public int thd_c = 6; 
  /**
   * Thread count
   * Interface
   * */
  public long population_count; 
  /**
   * The total checked patient count, the threads should stop when this 
   * gets to population_count
   * */
  public AtomicInteger checked_patients = new AtomicInteger(0); 
  /**
   * Threads
   * Interface
   * */
  protected ArrayList<Thread> thd_s; 
  /**
   * True when there is map loaded
   * */
  public Boolean loaded=false;
  /**
   * Parent valiable
   * */
  public Gol_gui parent;
  /**
   * (Deprecated) History is stored in a separate structure for
   * better memory management.
   * *kept for compatibility
   * */
  public Boolean enable_history = false;
  /**
   * random value object for true randomness on every .next()
   * */
  public Random rdm_val =new Random();
  /**
   * Round number
   * */
  public long r_n =0;
  /**
   *  Map that contains the whole thing
   * */
  public BigMap<String, Set<String>> big_map;
  /**
   * Data collection class field
   * */
  public VirusStats sts;
  /**
   * Rounds to try
   * Total rounds*
   * */
  public Integer ttrs=9999999;
  /**
   * Death count
   * */
  public Integer d_count =0;
  /**
   * recovery count
   * */
  public Integer r_count=0;
}
