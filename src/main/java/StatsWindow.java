import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class StatsWindow {
  private  JFrame frame;
  private  JPanel panel;
  private  JTextArea textArea;
  private  JScrollPane scroller;
  private  JPanel inputpanel;
  private  JTextField input;
  private  JButton button;
  private JTable table;
  private DefaultTableModel tb_model= new DefaultTableModel();
  private JFileChooser fileChooser;
  private Gol_gui parent;

  public void createFrame()
  {
    EventQueue.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        frame = new JFrame("Infection Statistics");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
          e.printStackTrace();
        }
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        ArrayList<String > tmp= new ArrayList<>();
        tb_model = new DefaultTableModel();
        tb_model.addColumn("Tick");
        tb_model.addColumn("Population");
        tb_model.addColumn("Infected");
        tb_model.addColumn("Recovered");
        tb_model.addColumn("Dead");
        table = new JTable(tb_model);
        table.setFillsViewportHeight(true);
        if (parent.infect.sts != null) {
          full_update(parent.infect.sts.data);
        }
        scroller = new JScrollPane(table);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        inputpanel = new JPanel();
        inputpanel.setLayout(new FlowLayout());
        input = new JTextField(20);
        button = new JButton("Save");
        button.addActionListener(new java.awt.event.ActionListener() {
          public void actionPerformed(java.awt.event.ActionEvent evt) {
            buttonActionPerformed(evt,button);
          }
        });
        panel.add(scroller);
        inputpanel.add(input);
        inputpanel.add(button);
        panel.add(inputpanel);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        input.requestFocus();
      }
    });
  }

  /**
   * Close the window
   * */
  public void close_stats(){
    frame.setVisible(false);
    frame.dispose();
  }
  /**
   * Gets a full update of the current data available; this should only be
   * used in the startup of a stats window
   * @param a the full list of data
   * */
  public void full_update(LinkedList<ArrayList<Integer>>a){
    for(ArrayList<Integer> i : a) {
      this.update_data(i);
//      System.out.println(i.get(0));
    }
  }


  /**
   * Sets the tabel, and updates the table with the latest data.
   * @param a the input data element
   * */
  public void update_data(ArrayList<Integer>a){
//    ls = sts.data;
    tb_model.addRow(new Object[]{
      a.get(0),
      a.get(1),
      a.get(2),
      a.get(3),
      a.get(4)
    });
  }
  /**
   * Sets the parent to the Gol_gui class
   * */
  public void set_parent(Gol_gui a){
    parent =a;
  }

  /**
   * Evokes parent's export data
   * Selects the output file directory and name
   * @param evt - The event object that doesn't matter for us
   */
  private void buttonActionPerformed(java.awt.event.ActionEvent evt,JButton button) {
    parent.toggleOutbuttonActionPerformed(evt,button);
  }

}
