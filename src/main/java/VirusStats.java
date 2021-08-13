import java.sql.Array;
import java.util.*;
/**
 * Keeps track of the data collected.
 * Total number of population
 * Infection force
 * Number of infections
 * Number of new infections
 *
 * */
public class VirusStats {
    /**
     * Initializes things
     * */
    public VirusStats() {
      data = new LinkedList<>();
    }
    /**
     * Stores and calculates the data from InfectSim
     * @param sts Statistics of a round
     * */
    public void add(Stats sts){
      /**
       * Each data point:
       * ary[0]: tick number
       * ary[1]: total population
       * ary[2]: infected population
       * ary[3]: recovered population
       * ary[4]: dead population
       * */
      ArrayList<Integer> tmp_ary=new ArrayList<>();
      tmp_ary.add(sts.rn);
      tmp_ary.add(sts.total_p);
      tmp_ary.add(sts.infected_num);
      tmp_ary.add(sts.recovered_num);
      tmp_ary.add(sts.dead_num);
      data.add(tmp_ary);

      //System.out.println(data.get(data.size()-1));
    }

    /**
     * Returns the latest data from data
     * @return an array of data that represents the last tick
     * */
    public ArrayList<Integer> latest(){
      return data.get(data.size()-1);
    }
    public LinkedList<ArrayList<Integer>> data;
}
/**
 * Every tick, InfectSim class should generate data for this class and give it to Virus Stats
 * */
class Stats{
    /**
     * Takes the data given and store it
     * @param n1 round number
     * @param n2 total population
     * @param n3 infected population
     * @param n4 recovered population
     * @param n5 dead population
     * */
    public Stats(Integer n1,Integer n2, Integer n3, Integer n4,Integer n5){
       rn = n1;
       total_p=n2;
       infected_num=n3;
       recovered_num=n4;
       dead_num=n5;
    }
    public Integer rn;//Round number
    public Integer total_p;//total population
    public Integer infected_num;//total infected number
    public Integer recovered_num;//total infected number
    public Integer dead_num;//total infected number

}