import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.*;
import javax.swing.*;
import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.io.*;

/**
 * Sets and reads configuration file.
 * If configuration file doesn't exist,the frame should
 * generate a configuration file on exit.
 * Default configuration file name is "config.conf"
 * */
public class ConfigParse {
  private Gol_gui parent;
  public String config_file_path ;
  /**
   * Sets the parent, and gets the user directory; also
   * sets the config file name and path
   * @param a Gol_gui class parent
   * */
  public ConfigParse(Gol_gui a){
    parent=a;
    config_file_path = parent.curDir+File.separator+"config.conf";
  }
  public void start_up(){
    File fpath=new File(config_file_path);
    if(!fpath.isFile()){
      System.out.printf("No configuration file exists in %s\nSkipping...\n",config_file_path);
    }
    else {
      System.out.printf("Parsing existing configuration file exists in %s\n",config_file_path);

    }
//    if(!File.createTempFile(config_file_path).exists())return;
    FileReader fr;
    try {
      FileInputStream frstream = new FileInputStream(config_file_path);
      BufferedReader br = new BufferedReader(new InputStreamReader(frstream));

      String strLine;
      String str1, str2;
      //Read File Line By Line
      while ((strLine = br.readLine()) != null)   {
        // Print the content on the console
//        System.out.println (strLine);
        String[] tmp = strLine.split("@");
        str1=tmp[0];
        str2=tmp[1];
        switch (str1) {
          case "sim_thread_count" -> {
            parent.infect.thd_c = val_regex_int(str2);
            parent.threadBox.insertItemAt(str2,0);
            parent.threadBox.setSelectedIndex(0);
          }
          case "graph_antialias" -> parent.graphPane.antialias_view = val_regex_bool(str2);
          case "graph_uninfected_lines" -> parent.graphPane.enable_uninf = val_regex_bool(str2);
          case "graph_show_dot" -> parent.graphPane.dot = val_regex_bool(str2);
          case "graph_outgoing_lines" -> parent.graphPane.outin = val_regex_bool(str2);
          case "sim_total_ticks" -> {
            parent.ttrsBox.insertItemAt(str2,0);
            parent.ttrsBox.setSelectedIndex(0);
            parent.infect.ttrs = val_regex_int(str2);
          }
          case "sim_fps" -> {
            parent.fps = val_regex_int(str2);
            parent.speedBox.insertItemAt(str2,0) ;
            parent.speedBox.setSelectedIndex(0);
          }
          case "sim_lambda" -> {
            parent.infect.lmda = val_regex_double(str2);
            parent.lmdaBox.insertItemAt(str2,0) ;
            parent.lmdaBox.setSelectedIndex(0);
          }
          case "sim_k_prob" -> {
            parent.infect.k_prob = val_regex_double(str2);
            parent.k_probBox.insertItemAt(str2,0) ;
            parent.k_probBox.setSelectedIndex(0);
          }
          case "sim_k_count" -> {
            parent.infect.k_count = val_regex_int(str2);
            parent.k_countBox.insertItemAt(str2,0) ;
            parent.k_countBox.setSelectedIndex(0);
          }
        }
      }

      //Close the input stream
      frstream.close();
    }
    catch(Exception e){
      JOptionPane.showMessageDialog(parent, "No correct configuration files");
      System.out.println("import failure: "+e.getMessage());
    }
    return;
  }

  public void write_config(){
    File fpath=new File(config_file_path);
    if(!fpath.isFile()){
      System.out.printf("Overwriting configuration file %s\n",config_file_path);
    }
    else {
      System.out.printf("Writing configuration file in %s\n",config_file_path);

    }
    FileWriter fw;
    try {
      fw = new FileWriter(config_file_path);
      fw.write("sim_thread_count"+"@"+parent.infect.thd_c+"\n");
      fw.write("sim_total_ticks"+"@"+parent.infect.ttrs+"\n");
      fw.write("sim_k_count"+"@"+parent.infect.k_count+"\n");
      fw.write("sim_k_prob"+"@"+parent.infect.k_prob+"\n");
      fw.write("sim_lambda"+"@"+parent.infect.lmda+"\n");
      fw.write("sim_fps"+"@"+parent.speedBox.getSelectedItem().toString()+"\n");
      fw.write("graph_uninfected_lines"+"@"+val_regex_bool(parent.graphPane.enable_uninf)+"\n");
      fw.write("graph_antialias"+"@"+val_regex_bool(parent.graphPane.antialias_view)+"\n");
      fw.write("graph_show_dot"+"@"+val_regex_bool(parent.graphPane.dot)+"\n");
      fw.write("graph_outgoing_lines"+"@"+val_regex_bool(parent.graphPane.outin)+"\n");

      fw.close();
    }
    catch(Exception e){
      JOptionPane.showMessageDialog(parent, "Unable to export, file path or file name faults");
      System.out.println("Export failure");
    }
  }

  /**
   * Regular expression parser.
   * Parses boolean string "true" or "false"
   * @return true if "true": else false
   * */
  private Boolean val_regex_bool(String a){
    return a.strip().equals("true");
  }
  /**
   * Regular expression parser.
   * Parses integer strings
   * @return integer value of the string
   * */
  private Integer val_regex_int(String a){
    return Integer.parseInt(a.strip());
  }
  /**
   * Regular expression parser.
   * Parses double strings
   * @return double value of the string
   * */
  private Double val_regex_double(String a){
    return Double.parseDouble(a.strip());
  }
  /**
   * Overloads val_regex_bool, instead the other way around.
   * Regular expression writer.
   * Translates boolean to regular expression
   * @return integer value of the string
   * */
  private String val_regex_bool(Boolean a){
    return a?"true":"false";
  }
}
