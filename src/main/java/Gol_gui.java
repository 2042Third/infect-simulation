import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.Box;
import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.io.*;
import javax.swing.filechooser.FileFilter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.net.URL;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Gol_gui extends JFrame
{

  // The board of the game of life 
  int[][] board;

  // Contains the separator of the clients' specific OS
  private static String sp = File.separator;

  // Contains the absolute working directory
  public static String curDir = System.getProperty("user.dir")+sp+"..";

  // Contains the input file path
  private static String seed_dir_sub = "src"+sp+"main"+sp+"resources";

  // Contains the input file path
  private static String src_dir_sub = "src"+sp+"main"+sp+"java";

  // Contains the input file path
  private static String src_dir = curDir+sp+src_dir_sub;

  // Contains the input file path
  private static String seed_dir = curDir+sp+seed_dir_sub;

  public InfectSim infect;

  // Icon Image 
  // Image icon_img = new ImageIcon(src_dir+sp+"icon.png").getImage();
  // BufferedImage icon_img;
  ImageIcon icon_img;

  // Graphing object 
  public DisplayGraphics graphPane;

  // Initializes the swing file chooser dialog
  JFileChooser fileChooser=new JFileChooser(new File(seed_dir));
  JFileChooser dirChooser=new JFileChooser(new File(src_dir_sub));

  // Screen default size
  private int screenHeight = 1080;
  private int screenWidth = 1920;


  /**
  * Initializes 
  */
  private void initComponents() 
  {
    //-----------------------------MENU---------------------------------
    menuBar = new JMenuBar();
    fileMenu = new JMenu("File");
    helpMenu = new JMenu("Help");
    openMenuItem = new JMenuItem();
    quitMenuItem = new JMenuItem("Quit");
    saveAsMenuItem = new JMenuItem("Output Directory");
    aboutMenuItem = new JMenuItem("About");
    outputFilePatternMenuItem = new JMenuItem();
    quickStartMenuItem = new JMenuItem("Quick Start");
    SpinnerModel sm = new SpinnerNumberModel(10, 0, 496000, 1); //default value,lower bound,upper bound,increment by
    iterationSpinner = new JSpinner(sm);
    try{
      icon_img = new Gol_gui().getImage("icon.png");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    firsPane = new JPanel();
    scndPane = new DisplayGraphics();
    // scndPane = new JPanel();
    graphPane = (DisplayGraphics) scndPane;
    firsPane.setLayout(new BoxLayout(firsPane, BoxLayout.Y_AXIS));

    iteratLabel = new JLabel("\'th iteration");
    setIterationLabel = new JLabel("Set Total Iteration");
    statLabel = new JLabel("0");
    Font font = new Font("Courier", Font.BOLD,24);
    statLabel.setFont(font);
    population_count_lb = new JLabel("0");
    population_count_lb.setFont(font);
    population_count_lb_text = new JLabel("total population");

    // population_count_lb_text.setFont(font);

    dirChooser.setDialogTitle("Output Directory");
    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    dirChooser.setCurrentDirectory(new File(".."));

    ChangeListener spinnerListener = new SpinnerChanged();

    openMenuItem.setText("Open");
    openMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openMenuItem);
    toggleOutMenu = new JMenu("Toggle Output");
    enableOutMenuItem = new JMenuItem("Enable Output to Disk");
    disableOutMenuItem = new JMenuItem("Disable Output to Disk");

    outputFilePatternMenuItem.setText("Outout File Pattern");
    outputFilePatternMenuItem.addActionListener(new ActionListener() {
         @Override
     public void actionPerformed(ActionEvent evt) {
      // if(gol_obj==null){
      //   JOptionPane.showMessageDialog(firsPane, "Choose a seed first!");
      //   return;
      // }
        String fopta = (String)JOptionPane.showInputDialog(
           firsPane,
           "Enter output file prefix\n(example: \"out\",\"sim\")", 
           "Outfile Pattern",            
           JOptionPane.PLAIN_MESSAGE,
           null,            
           null, 
           ""
        );

        String foptb = (String)JOptionPane.showInputDialog(
           firsPane,
           "Enter output file suffix\n(example: \".txt\",\".out\")", 
           "Outfile Pattern",            
           JOptionPane.PLAIN_MESSAGE,
           null,            
           null, 
           ""
        );
        // gol_obj.of_patn(fopta,foptb);
      }
      });

    quickStartMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        quickStartMenuItemActionPerformed(evt);
      }
    });

    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aboutMenuItemActionPerformed(evt);
      }
    });

    quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        quitMenuItemActionPerformed(evt);
      }
    });

    enableOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        enableOutMenuItemActionPerformed(evt);
      }
    });

    disableOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        disableOutMenuItemActionPerformed(evt);
      }
    });

    saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAsMenuItemActionPerformed(evt);
      }
    });

    displayMenu = new JMenu("Display");

    viewColorMenuItem=new JMenuItem("Line Color 2");
    cellColorMenuItem=new JMenuItem("Line Color 1");
    antialiasMenuItem=new JMenuItem("Antialiasing Toggle");
    viewColorMenuItem.addActionListener(new ImmediateListener1());
    cellColorMenuItem.addActionListener(new ImmediateListener());
    antialiasMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        antialiasMenuItemActionPerformed(evt);
      }
    });
    simulationMenu = new JMenu("Simulation");
    simulationMenuRandomInfect = new JMenuItem("Random Infect");
    simulationMenuRandomInfect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        simulationMenuRandomInfectActionPerformed(evt);
      }
    });
    simulationMenuGreaterThanInfect = new JMenuItem("Greater Than Infect");
    simulationMenuGreaterThanInfect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        simulationMenuGreaterThanInfectActionPerformed(evt);
      }
    });
    simulationMenuSpreadInfect = new JMenuItem("Spread Infect");
    simulationMenuSpreadInfect.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        simulationMenuSpreadInfectActionPerformed(evt);
      }
    });
    uninfMenuItem=new JMenuItem("Toggle Un-infected lines");
    uninfMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        uninfMenuItemActionPerformed(evt);
      }
    });
    outinMenuItem=new JMenuItem("Toggle outgoing lines");
    outinMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        outinMenuItemActionPerformed(evt);
      }
    });
    dotMenuItem=new JMenuItem("Toggle dot");
    dotMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        dotMenuItemActionPerformed(evt);
      }
    });
    displayMenu.add(cellColorMenuItem);
    displayMenu.add(viewColorMenuItem);
    displayMenu.addSeparator();
    displayMenu.add(antialiasMenuItem);
    displayMenu.addSeparator();
    displayMenu.add(uninfMenuItem);
    displayMenu.add(outinMenuItem);
    displayMenu.add(dotMenuItem);

    fileMenu.add(saveAsMenuItem);
    toggleOutMenu.add(enableOutMenuItem);
    toggleOutMenu.add(disableOutMenuItem);
    fileMenu.add(toggleOutMenu);
    fileMenu.add(outputFilePatternMenuItem);
    fileMenu.addSeparator();
    fileMenu.add(quitMenuItem);
    helpMenu.add(quickStartMenuItem);
    helpMenu.addSeparator();
    helpMenu.add(aboutMenuItem);
    simulationMenu.add(simulationMenuRandomInfect);
    simulationMenu.add(simulationMenuGreaterThanInfect);
    simulationMenu.add(simulationMenuSpreadInfect);
    menuBar.add(fileMenu);
    menuBar.add(displayMenu);
    menuBar.add(simulationMenu);
    menuBar.add(helpMenu);

    setJMenuBar(menuBar);

//-----------------------------LHS PANEL---------------------------------
    // iterationSpinner.setBounds(10,4294999,0,1);
    iterationSpinner.addChangeListener(spinnerListener);
    rCountLabel=new JLabel("0");
    dCountLabel=new JLabel("0");
    firsPane.add(Box.createRigidArea(new Dimension(200, 10)));
    statLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(statLabel);
    iteratLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(iteratLabel);
    infected_lb=new JLabel("0");
    infected_lb.setAlignmentX(Component.CENTER_ALIGNMENT);
    infected_lb_text=new JLabel("Infected");
    infected_lb_text.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(infected_lb);
    firsPane.add(infected_lb_text);
    add_button("Start",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        startButtonActionPerformed(evt);
      }
    }, firsPane);
    add_button("Stop",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stopButtonActionPerformed(evt);
      }
    }, firsPane);

    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    dirLabel1=new JLabel("Seed File:");
    dirLabel2=new JLabel("(No seed file selected)");
    dirLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
    population_count_lb.setAlignmentX(Component.CENTER_ALIGNMENT);
    population_count_lb_text.setAlignmentX(Component.CENTER_ALIGNMENT);
    dirLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(dirLabel1);
    firsPane.add(dirLabel2);
    add_button("Reload",new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        reloadButtonActionPerformed(evt);
      }
    }, firsPane);
    openStatsButton=new JButton("Open Statistics");
    openStatsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(new JLabel("-"));
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(openStatsButton);
    openStatsButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openStatsButtonActionPerformed(evt,openStatsButton);
      }
    });
    toggleOutButton = new JButton("Export Data");
    toggleOutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    toggleOutButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        toggleOutbuttonActionPerformed(evt,toggleOutButton);
      }
    });
    firsPane.add(toggleOutButton);
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(new JLabel("-"));
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));



    firsPane.add(population_count_lb);
    firsPane.add(population_count_lb_text);

    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    speedBoxLabel = new JLabel("Change fps:");
    speedBoxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

    firsPane.add(speedBoxLabel);
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    speedBox = new JComboBox();
    speedBox.setMaximumSize(new Dimension(100,30)); 
    speedBox.setEditable(true);
    speedBox.addItem(1);
    speedBox.addItem(2);
    speedBox.addItem(5);
    speedBox.addItem(10);
    speedBox.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               speedBoxctionPerformed(evt);
            }
    });
    firsPane.add(speedBox);
    threadBoxLabel=new JLabel("Thread count:");
    threadBoxLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    firsPane.add(threadBoxLabel);
    threadBox = new JComboBox();
    threadBox.setMaximumSize(new Dimension(100,30)); 
    threadBox.setEditable(true);
    threadBox.addItem(4);
    threadBox.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent evt)
            {
               threadBoxctionPerformed(evt);
            }
    });
    firsPane.add(threadBox);
    ttrs_lb=new JLabel("Total ticks");
    ttrs_lb.setAlignmentX(Component.CENTER_ALIGNMENT);
    ttrsBox=new JComboBox();
    ttrsBox.setEditable(true);
    ttrsBox.setMaximumSize(new Dimension(100,30));
    ttrsBox.addItem(99999);
    ttrsBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        ttrsBoxActionPerformed(evt);
      }
    });
    firsPane.add(ttrs_lb);
    firsPane.add(ttrsBox);
    // History portion
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));
    firsPane.add(new JLabel("-"));
    firsPane.add(Box.createRigidArea(new Dimension(10, 10)));

    lmdaLabel = new JLabel("Lambda value:");
    lmdaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    lmdaBox=new JComboBox();
    lmdaBox.setMaximumSize(new Dimension(100,30));
    lmdaBox.setEditable(true);
//    lmdaBox.addItem(4);
    lmdaBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        lmdaBoxctionPerformed(evt);
      }
    });
    firsPane.add(lmdaLabel);
    firsPane.add(lmdaBox);
    k_countLabel = new JLabel("Days to recovery value:");
    k_countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    k_countBox=new JComboBox();
    k_countBox.setMaximumSize(new Dimension(100,30));
    k_countBox.setEditable(true);
//    k_countBox.addItem(4);
    k_countBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        k_countBoxActionPerformed(evt);
      }
    });
    firsPane.add(k_countLabel);
    firsPane.add(k_countBox);
    k_probLabel = new JLabel("Recovery Probability value:");
    k_probLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    k_probBox=new JComboBox();
    k_probBox.setMaximumSize(new Dimension(100,30));
    k_probBox.setEditable(true);
//    k_probBox.addItem(4);
    k_probBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        k_probBoxActionPerformed(evt);
      }
    });
    firsPane.add(k_probLabel);
    firsPane.add(k_probBox);


//-----------------------------MAIN PANEL---------------------------------
    mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,firsPane, scndPane);
    mainPane.setDividerLocation( 150 );
    mainPane.setForeground( new Color( 0,0,0 ) );
    getContentPane().add(mainPane);

//-----------------------------INITS---------------------------------
    infect = new InfectSim(this);
    graphPane.set_parent(this);
    lmdaBox.addItem(infect.lmda+"");
    k_countBox.addItem(infect.k_count+"");
    k_probBox.addItem(infect.k_prob+"");
  //-----------------------------CONFIG---------------------------------
  conf_p = new ConfigParse(this);
  conf_p.start_up();
  System.out.println("trying to config");
  addWindowListener(new WindowAdapter()
  {
    @Override
    public void windowClosing(WindowEvent e)
    {
      conf_p.write_config();
      e.getWindow().dispose();
    }
  });

  } // END of initComponents()
  /**
   * Opens a table window that shows the current available data for this run
   * @param evt not important
   * @param toggleOutButton button pointer, not used
   * */
  private void openStatsButtonActionPerformed(ActionEvent evt, JButton toggleOutButton) {
    if(statsWindow!=null){
      //Clean up
    }
    else {
      statsWindow=new StatsWindow();
      statsWindow.set_parent(this);
    }
    statsWindow.createFrame();
//    if(infect.sts!=null)
//      statsWindow.full_update(infect.sts.data);
  }
  /**
   * Overloads ^ that one.
   * Opens a table window that shows the current available data for this run
   * */
  public void openStatsButtonActionPerformed() {
    if(statsWindow!=null){
      //clean up, if need be
    }
    else {
      statsWindow=new StatsWindow();
      statsWindow.set_parent(this);
    }
    statsWindow.createFrame();

  }
  /**
   *  Changes the total tick that the simulation runs
   * @param evt Event thing that we don't care
   * */
  private void ttrsBoxActionPerformed(ActionEvent evt) {
    if(infect==null){
      return;
    }
    infect.ttrs =Integer.parseInt(Objects.requireNonNull(ttrsBox.getSelectedItem()).toString());
//    infect.thd_c=thd;

  }

  /**
   * Adds a button to the panel, pnl. 
   * It would be Green if label is "Start"
   * It would be Red if label is "Stop"
   * It would be Yellow if label is "Reload"
   * @param lable - (misspelled) should be "label", but it doesn't matter
   * @param listener - the listener that needs to be called when the event triggers 
   * @param pnl - the panel that the button should be attached to
   * 
   * */
  public void add_button(String lable, ActionListener listener, JPanel pnl){
    JButton button = new JButton(lable);
    if (lable=="Start") button.setBackground(Color.GREEN);
    else if (lable=="Stop") button.setBackground(Color.RED);
    else if (lable=="Reload") button.setBackground(Color.YELLOW);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.addActionListener(listener);
    pnl.add(button);

  }

  /**
   * (Not used in final product)
   * Adds a button to the Menu, pnl. 
   * It would be Green if label is "Start"
   * It would be Red if label is "Stop"
   * It would be Yellow if label is "Reload"
   * @param lable - (misspelled) should be "label", but it doesn't matter
   * @param listener - the listener that needs to be called when the event triggers 
   * @param pnl - the Menu that the button should be attached to
   * 
   * */
  public void add_button_menu(String lable, ActionListener listener, JMenu pnl){
    JButton button = new JButton(lable);
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.addActionListener(listener);
    pnl.add(button);

  }

  /**
  * Starts the gui application
  */
  public void Gol_gui()
  {
    
    
    setTitle("Infection Simulation");
    System.out.printf("in frame: %s;%s",curDir,src_dir);

    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screenSize = kit.getScreenSize();
    screenHeight = screenSize.height;
    screenWidth = screenSize.width;

    setSize(screenWidth / 2, screenWidth / 3);
    setLocationByPlatform(true);
    String currentDir = System.getProperty("user.dir");
    
    initComponents();
    setIconImage(icon_img.getImage()); 
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
    } catch (Exception e) {
      System.out.println("No good window start! theme ");
    }
  }

  
  /**
  * Open file chooser.
  * @param evt - The event object that doesn't matter for us
  * 
  */
  public void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) 
  {

    if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
        return;
    }
    if (action ) {
      JOptionPane.showMessageDialog(this, "Simulation is running,");
        return;
    }

//    toggleOutButton.setText("File Output: off");
    infect = new InfectSim(this);
    current_file_sel = fileChooser.getSelectedFile().getAbsoluteFile().getPath();
    Integer out_i = infect.load_cvs(current_file_sel);
    graphPane.gen_coord_lst(infect.big_map.get_all_str(infect.big_map.gt()));
    graphPane.dis_str_lst=true;
    population_count_lb.setText(out_i+"");
    graphPane.repaint();
    String[] dir_ = current_file_sel.split(sp);
    int ia=dir_.length;
    dirLabel2.setText("..."+sp+dir_[ia-2]+sp+dir_[ia-1]);
    infect.loaded = true;
  }



  /**
  * Reloads the simulation
  * @param evt - The event object that doesn't matter for us
  */
  private void reloadButtonActionPerformed(ActionEvent evt){
    if (current_file_sel == null ){
      JOptionPane.showMessageDialog(this, "Select file to reload");
      openMenuItemActionPerformed(evt);

    }
    else {
      
      Integer out_i = infect.load_cvs(current_file_sel);
    graphPane.gen_coord_lst(infect.big_map.get_all_str(infect.big_map.gt()));
      statLabel.setText(out_i+"");
      graphPane.repaint();
      dirLabel2.setText(current_file_sel);
    }

  }
  

  /**
  * Changes the fps of the simulation 
  * @param evt - The event object that doesn't matter for us
  */
  private void speedBoxctionPerformed(java.awt.event.ActionEvent evt) {
    int spd =Integer.parseInt(speedBox.getSelectedItem().toString());
    // System.out.println(spd);
    fps = (1000/spd) ;
    // System.out.println(fps);
  }

  /**
   * Changes the thread count
   * @param evt - The event object that doesn't matter for us
   */
  private void threadBoxctionPerformed(java.awt.event.ActionEvent evt) {
    int thd =Integer.parseInt(threadBox.getSelectedItem().toString());
    infect.thd_c=thd;
  }
  /**
   * Changes the thread count
   * @param evt - The event object that doesn't matter for us
   */
  private void lmdaBoxctionPerformed(java.awt.event.ActionEvent evt) {
    Double doublenum =Double.parseDouble(lmdaBox.getSelectedItem().toString());
    infect.lmda=doublenum;
  }
  /**
   * Changes the thread count
   * @param evt - The event object that doesn't matter for us
   */
  private void k_probBoxActionPerformed(java.awt.event.ActionEvent evt) {
    Double doublenum =Double.parseDouble(k_probBox.getSelectedItem().toString());
    infect.k_prob=doublenum;
  }
  /**
   * Changes the thread count
   * @param evt - The event object that doesn't matter for us
   */
  private void k_countBoxActionPerformed(java.awt.event.ActionEvent evt) {
    int thd =Integer.parseInt(k_countBox.getSelectedItem().toString());
    infect.k_count=thd;
  }

  /**
  * Go to the prevous board from the current history board
  * @param evt - The event object that doesn't matter for us
  * 
  */
  private void histPrevButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if(infect==null)return;
    hist_prev();
  } 

 
  
  /**
  * Go to the next history board view
  * @param evt - The event object that doesn't matter for us
  */
  private void histNextButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if(infect==null)return;
    hist_next();
  } 
  
  /**
  * Enables outputs to file, also updates the toggleOutButton if successful
  * @param evt - The event object that doesn't matter for us
  * 
  */
  private void enableOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    JOptionPane.showMessageDialog(this, "Yet to implement");
    

  } 
  
  /**
  * Enables outputs to file, also updates the toggleOutButton if successful
  * @param evt - The event object that doesn't matter for us
  * 
  */
  private void disableOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    JOptionPane.showMessageDialog(this, "Yet to implement");

  } 

  /**
  * Toggles the history view on/off
  * @param evt - The event object that doesn't matter for us
  */
  private void histToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {
    JOptionPane.showMessageDialog(this, "Yet to implement, might want to status show here");
    infect.enable_history = (infect.enable_history)? false:true;
  } 

  /**
  * Selects the output file directory and name
  * @param evt - The event object that doesn't matter for us
  */
  public void toggleOutbuttonActionPerformed(java.awt.event.ActionEvent evt,JButton b) {
    if(current_file_sel==null){
      JOptionPane.showMessageDialog(this, "Choose a seed first");
      return;
    }
    else if(infect==null){
      JOptionPane.showMessageDialog(this, "No Simulation is running, don't have any data");
      return;
    }
    fileChooser.setSelectedFile(new File("sim_out.csv"));
    if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
      return;
    }
    if (action ) {
      JOptionPane.showMessageDialog(this, "Simulation is running,");
      return;
    }
    String file_sel = fileChooser.getSelectedFile().getAbsoluteFile().getPath();
    infect.export_sts(file_sel);
  } 
  

  /**
  * Toggles Antialiasing for the display on/off
  * @param evt - The event object that doesn't matter for us
  */
  private void antialiasMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    graphPane.toggle_antialias();
    graphPane.repaint();
  } 
  /**
  * Shows a quick start guide (more information please read the manual)
  * @param evt - The event object that doesn't matter for us
  */
  private void quickStartMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    JOptionPane.showMessageDialog(this, "Open a seed file from the menu. \nThen hit start.");
  }
  /**
  * Decreases the size of the board
  * @param evt - The event object that doesn't matter for us
  */
  private void decSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (seed_dir == null ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    graphPane.dec_size();
  }
  /**
  * Increases the size of the board
  * @param evt - The event object that doesn't matter for us
  */
  private void incSizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (seed_dir == null ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    graphPane.inc_size();
  }

  /**
  * Randomly infect a subject
  * @param evt - The event object that doesn't matter for us
  */
  private void simulationMenuRandomInfectActionPerformed(java.awt.event.ActionEvent evt) {
    if (!infect.loaded ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    infect.rd_infect();
  }

  /**
  * Infect everu subject that has greater than s_count neighbors
  * @param evt - The event object that doesn't matter for us
  */
  private void simulationMenuGreaterThanInfectActionPerformed(java.awt.event.ActionEvent evt) {
    if (!infect.loaded ){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    infect.gt_infect();
  }
  /**
  * Randomly select a subject and spread to k_count nodes
  * @param evt - The event object that doesn't matter for us
  */
  private void simulationMenuSpreadInfectActionPerformed(java.awt.event.ActionEvent evt) {
    if (!infect.loaded){
      JOptionPane.showMessageDialog(this, "Choose a seed first!");
    }
    infect.spread_infect();
  }

  /**
  * Start button that starts 
  * @param evt - The event object that doesn't matter for us
  */
  private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
    if (action) return;
    action = true;
    if (infect==null) 
    {
      JOptionPane.showMessageDialog(this, "Please load a seed file first!");
      return;
    }
    new Thread(graphPane).start();

  }

  /**
  * Stop button action
  * @param evt - The event object that doesn't matter for us
  */
  private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {
    action = false;
  }

  /**
  * Open the about page
  * @param evt - The event object that doesn't matter for us
  */
  private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    if (dialog == null) 
    dialog = new AboutDialog(this);
    dialog.setVisible(true);
  }

  /**
  * Open the output directory file dialog
  * @param evt - The event object that doesn't matter for us
  */
  private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    if(infect==null)return;
    JOptionPane.showMessageDialog(this, "Yet to implement");

    graphPane.repaint();


  }

  /**
  * Quits the program 
  * @param evt - The event object that doesn't matter for us
  */
  private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    System.exit(0);
  }

  /**
  * The iteration changer that sets the iteration count of the game of life API.
  * *IMPORTANT: this will reset the history view log
   */
  private void iterationSpinnerStateChanged(ChangeEvent e){
    
  }
  

  /**
   * behavior of the spinner
   * */
  private class SpinnerChanged implements ChangeListener
    {
      public void stateChanged(ChangeEvent e){
      iterationSpinnerStateChanged(e);
    }};

  /**
   * Delegates the game of life API and the graphics engine to display the previous history board 
   * 
   * */
  public void hist_prev(){
    
  }

  /**
   * Delegates the game of life API and the graphics engine to display the next history board 
   * 
   * */
  public void hist_next(){

    
  }

  /**
   * Toggle the uninfected lines
   * */
  public void uninfMenuItemActionPerformed(ActionEvent evt){
    graphPane.enable_uninf= !graphPane.enable_uninf;
    graphPane.repaint();
  }
  /**
   * Toggle to show the out-going or the in-coming lines
   * */
  public void outinMenuItemActionPerformed(ActionEvent evt){
    graphPane.outin= !graphPane.outin;
    graphPane.repaint();
  }
  /**
   * Toggle the out-going or the in-coming lines
   * */
  public void dotMenuItemActionPerformed(ActionEvent evt){
    graphPane.dot= !graphPane.dot;
    graphPane.repaint();
  }

   /**
    * Credit: The example given on lecture 8
    * This listener pops up a modeless color chooser. The panel color is changed immediately when
    * the user picks a new color.
    */
   private class ImmediateListener implements ActionListener
   {
      public ImmediateListener()
      {
         chooser = new JColorChooser();
         chooser.getSelectionModel().addChangeListener(new ChangeListener()
            {
               public void stateChanged(ChangeEvent event)
               {
                  graphPane.set_color3(chooser.getColor());
                  graphPane.repaint();
               }
            });

         dialog = new JDialog((Frame) null, false /* not modal */);
         dialog.add(chooser);
         dialog.pack();
      }

      public void actionPerformed(ActionEvent event)
      {
         chooser.setColor(getBackground());
         dialog.setVisible(true);
      }

      private JDialog dialog;
      private JColorChooser chooser;
   }

   /**
    * Credit: The example given on lecture 8
    * This listener pops up a modeless color chooser. The panel color is changed immediately when
    * the user picks a new color.
    */
   private class ImmediateListener1 implements ActionListener
   {
      public ImmediateListener1()
      {
         chooser = new JColorChooser();
         chooser.getSelectionModel().addChangeListener(new ChangeListener()
            {
               public void stateChanged(ChangeEvent event)
               {
                  graphPane.set_color2(chooser.getColor());
                  graphPane.repaint();
               }
            });

         dialog = new JDialog((Frame) null, false /* not modal */);
         dialog.add(chooser);
         dialog.pack();
      }

      public void actionPerformed(ActionEvent event)
      {
         chooser.setColor(getBackground());
         dialog.setVisible(true);
      }

      private JDialog dialog;
      private JColorChooser chooser;
   }

   /**
    * Credit: https://coderanch.com/t/569491/java/images-jar-file
    * Takes a relative path to get a ImageIcon
    * @param path - file path of the system to a image
    * 
    * */
  private  ImageIcon getImage(String path){
 
  // System.out.println(path);
  URL  imgURL = getClass().getResource(path);
  System.out.println(imgURL);
      if (imgURL != null) {
          return new ImageIcon(imgURL);
      } 
      else {
          System.err.println("Couldn't find file: " + path+"\n");
          return null;
      }
  }

  /**
   * The current history board index 
   * */
  private int hist_ind; 
  /**
   * The fps of the orunning simulation 
   * */
  public Integer fps=1000;
  /**
   * Whether the simulation is running
   * */
  public boolean action=false; 

  /**
   * Iteration label
   * */
  public JLabel iteratLabel; 
  /**
   * Set iteration label
   * */
  public JLabel setIterationLabel; 
  /**
   * The current iteration of the simulation 
   * */
  public JLabel statLabel; 
  /**
   * The fps label (not used)
   * */
  public JLabel speedLabel; 

  /**
   * The thread label
   * */
  public JLabel threadBoxLabel; 
  /**
   * the fps changer combox box 
   * */
  private JLabel speedBoxLabel; 
  /**
   * The first directory label
   * */
  public JLabel dirLabel1;  
  /**
   * The first directory label
   * */
  public JLabel infected_lb;  
  /**
   * The first directory label
   * */
  public JLabel infected_lb_text;  
  /**
   * the second directory label
   * */
  public JLabel dirLabel2; 
  /**
   * the History label
   * */
  public JLabel histLabel; 
  /**
   * The history iteration label (may not be used)
   * */
  public JLabel histLabelIt;
  /**
   * Changes lambda
   * */
  public JLabel lmdaLabel;
  /**
   * Changes lambda
   * */
  public JComboBox lmdaBox;
  /**
   * Days to recovery/death
   * */
  public JLabel k_countLabel;
  /**
   * Changes days to recovery/death
   * */
  public JComboBox k_countBox;
  /**
   * Possibility of recovery
   * */
  public JLabel k_probLabel;
  /**
   * Changes the probability of recovery
   * */
  public JComboBox k_probBox;

  /**
   * death count label
   * */
  public JLabel dCountLabel;
  /**
   * recovery count label
   * */
  public JLabel rCountLabel;
  /**
   * the Size label
   * */
  public JLabel sizeLabel; 
  /**
   * The menu bar
   * */
  private JMenuBar menuBar; 
  /**
   * the file menu
   * */
  private JMenu fileMenu; 
  /**
   * the help menu
   * */
  private JMenu helpMenu; 
  /**
   * the toggle file output sub-menu
   * */
  private JMenu toggleOutMenu; 
  /**
   * The open menu item that opens the file chooser
   * */
  private JMenuItem openMenuItem; 
  /**
   * the display menu
   * */
  private JMenu displayMenu; 
  /**
   * the view color changer menu item 
   * */
  private JMenuItem viewColorMenuItem; 
  /**
   * the antialiasing toggle menu item 
   * */
  private JMenuItem antialiasMenuItem; 
  /**
   * the antialiasing toggle menu item 
   * */
  private JMenuItem uninfMenuItem; 
  /**
   * Toggle between outgoing or incoming lines
   * */
  private JMenuItem outinMenuItem;
  /**
   * Toggle between outgoing or incoming lines
   * */
  private JMenuItem dotMenuItem;
  /**
   * the cell color changer menu item 
   * */
  private JMenuItem cellColorMenuItem; 
  /**
   * the quit menu item
   * */
  private JMenuItem quitMenuItem; 
  /**
   * the saveAs menu item  (name changed to "Output Directory")
   * */
  private JMenuItem saveAsMenuItem;    
  /**
   * the enable output menu item
   * */
  private JMenuItem enableOutMenuItem;  
  /**
   * the disable output menu item 
   * */
  private JMenuItem disableOutMenuItem;   
  /**
   * the about menu item 
   * */
  private JMenuItem aboutMenuItem; 
  /**
   * the output files pattern changer menu item that opens dialogs to change the output file pattern
   * */
  private JMenuItem outputFilePatternMenuItem; 
  /**
   * the quick start menu item 
   * */
  private JMenuItem quickStartMenuItem;
  public  ConfigParse conf_p;
  /**
   * the iteration changer spinner 
   * */
  private JSpinner iterationSpinner; 
  /**
   * the left hand side panel (quick access menu)
   * */
  private JPanel firsPane; 
  /**
   * the right hand side panel (display)
   * */
  private JPanel scndPane; 
  /**
   * The start button
   * */
  private JButton startButton; 
  /**
   * the toggle output to disk button 
   * */
  private JButton toggleOutButton; 
  /**
   * the stop button
   * */
  private JButton stopButton; 
  /**
   * the history toggle for toggling the history view
   * */
  private JButton histToggleButton; 
  /**
   * the "<" button goes to the previous button
   * */
  private JButton histPrevButton; 
  /**
   * the ">" button that goes the the next history board 
   * */
  private JButton histNextButton; 
  /**
   * the "+" button that increases teh size of the display
   * */
  private JButton incSizeButton; 
  /**
   * the "-" button that decrease the size of the display
   * */
  private JButton decSizeButton; 
  /**
   * The simulation menu
   * */
  private JMenu simulationMenu; 
  /**
   * The simulation menu
   * */
  private JMenuItem simulationMenuRandomInfect; 
  /**
   * The simulation menu
   * */
  private JMenuItem simulationMenuGreaterThanInfect; 
  /**
   * The simulation menu
   * */
  private JMenuItem simulationMenuSpreadInfect;
  /**
   * the fps changer combo box 
   * */
  public JComboBox speedBox;
  /**
   * the fps changer combo box
   * */
  public JButton openStatsButton;
  /**
   * the fps changer combo box
   * */
  public StatsWindow statsWindow;
  /**
   * the fps changer combo box 
   * */
  public JComboBox threadBox;
  /**
   * Total tick count label
   * */
  public JLabel ttrs_lb;
  /**
   * the total ticks changer combo box
   * */
  public JComboBox ttrsBox;
  /**
   * the history view changer combo box (redected)
   * */
  private JComboBox histBox; 
  /**
   * the main panel
   * */
  private JSplitPane mainPane; 
  /**
   * the history buttons' container box
   * */
  private Box histBoxSizer;  
  /**
   * the color changer #1
   * */
  private ImmediateListener colorChooser1; 
  /**
   * the color  chanager #2
   * */
  private ImmediateListener1 colorChooser2; 
  /**
   * the about dialog 
   * */
  private AboutDialog dialog; 
  /**
   * Population label
   * */
  private JLabel population_count_lb; 
  /**
   * Population label
   * */
  private JLabel population_count_lb_text; 
  /**
   * File directory 
   * */
  private String current_file_sel; 

}

/**
 * Credit: Used the example template from lecture 8
 * 
 * A dialog that displays about.
 */
class AboutDialog extends JDialog
{
   public AboutDialog(JFrame owner)
   {
      super(owner, "About DialogTest", true);

      // add HTML label to center

      add(
            new JLabel(
                  "<html><h1><i>Infection</i></h1><hr>By Mike Yang</html>"),
            BorderLayout.CENTER);

      
      JButton ok = new JButton("Ok");
      ok.addActionListener(new ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               setVisible(false);
            }
         });

      // add Ok button to southern border

      JPanel panel = new JPanel();
      panel.add(ok);
      add(panel, BorderLayout.SOUTH);

      Toolkit kit = Toolkit.getDefaultToolkit();
      Dimension screenSize = kit.getScreenSize();
      screenHeight = screenSize.height;
      screenWidth = screenSize.width;

      setSize(screenWidth / 6, screenHeight / 6);
      setLocationByPlatform(true);
   }

  private int screenHeight = 500;
  private int screenWidth = 400;
}




