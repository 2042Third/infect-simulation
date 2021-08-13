import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.awt.Font;
import java.awt.Graphics;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.net.URL;
import java.util.Random.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
   * Drawing class
   * 
   * */
public class DisplayGraphics extends JPanel implements Runnable{


    // Contains the separator of the clients' specific OS
    private static String sp = File.separator;

    // Contains the absolute working directory
    private static String curDir = System.getProperty("user.dir")+sp+"..";

    // Contains the input file path
    private static String seed_dir_sub = "src"+sp+"main"+sp+"resources";

    // Contains the input file path
    private static String src_dir_sub = "src"+sp+"main"+sp+"java";

    // Contains the input file path
    private static String src_dir = curDir+sp+src_dir_sub;

    // Contains the input file path
    private static String seed_dir = curDir+sp+seed_dir_sub;
    /**
     * integer thread safe
     * */
    public AtomicInteger it;
     /**
    * width
    * */
    protected int w = 40; 
    /**
    * height
    * */
    protected int h = 40; 
    /**
    * static width
    * */
    protected static int dw = 50; 
    /**
    * static height
    * */
    protected static int dh = 50; 
    /**
     * Dot size
     * */
    protected int dotsz=3;
    /**
    * board absolute x size
    * */
    protected int X;  
    /**
    * board absolute y size
    * */
    protected int Y; 

    /**
    * default graphics display size for x
    * */
    protected int resx=1280; 
    /**
    * default graphics display size for y
    * */
    protected int resy=720; 

    /**
    * changing width of the screen
    * */
    protected int width = this.getWidth(); 
    /**
    * changing height of the screen
    * */
    protected int height = this.getHeight(); 

    /**
    * whether history view is open
    * */
    protected boolean hist_view = true; 
    /**
    * whether antialiasing is on
    * */
    protected boolean antialias_view = true; 
    /**
    * stores history boards, resets after changing the iteration size
    * */
    protected int[][] hist_board ; 
    /**
    * tells where the history view is pointing to 
    * */
    protected int hist_it = 0; 

    /**
    * string for history view
    * */
    protected String hist_string1="History View"; 
    /**
    * string for history view
    * */
    protected ArrayList<Integer> hash_lst; 
    /**
    * string for history view
    * */
    protected ArrayList<String> str_lst; 
    /**
    * string for history view
    * */
    protected Map<String, p> coord_lst; 
    /**
    * string for history view
    * */
    protected Boolean dis_hash_lst = false; 
    /**
    * string for history view
    * */
    protected Boolean dis_str_lst = false; 
    /**
    * string for current view
    * */
    protected String hist_string2="Current View"; 
    /**
    * string for formating in graphics display
    * */
    protected AttributedString hist1; 
    /**
    * string for formating in graphics display
    * */
    protected AttributedString hist2; 
    /**
    * font of one of the display letters
    * */
    protected Font font1 = new Font("Serif", Font.PLAIN, 24); 
    /**
     * Enable the un-infected lines
     * */
    public Boolean enable_uninf=true;

    /**
    * color palette one
    * */
    protected Color color1 = new Color(150, 148, 0);
    /**
    * color pallete two
    * */
    protected Color color2 = new Color(0, 146, 0); 
    /**
    * coolor pallete three
    * */
    protected Color color3 = new Color(255,0,0); 
    /**
    * coolor pallete three
    * */
    protected Color color4 = new Color(0, 0, 0);
    /**
    * coolor pallete three
    * */
    protected Color color5 = new Color(21, 61, 249); 
    /**\
     * the parent
     * */
    public Gol_gui parent ; 
    /**
     * out or in
     * */
    public Boolean outin=true;
    /**
     * out or in
     * */
    public Boolean dot=true;

    /**
    * board copy of the display engine
    * */
    private int[][] board; 

    // Image im1 = new ImageIcon(src_dir+sp+"submitty_logo-min.jpg").getImage();
    URL  imgURL = getClass().getResource("rpi_logo.jpg"); // RPI logo for start-up sequence
    Image im2 = new ImageIcon(imgURL).getImage(); // RPI logo for start-up sequence
    /**
     * Sets the parent 
     * */
    public void set_parent(Gol_gui a){
      parent=a;
    }
    /**
     * Truncates/increases the size of a to fit the display of the simulation 
     * @param a - input size 
     * @return - a size that fit the current display 
     * 
     * */
    private int fit(int a){
      return (a+w+h/2)/(2);
    }



    /**
     * Sets the board to be painted in the next repaint()/paintComponent()
     * @param b - the board to be painted;
     * */
    public void set_board(int[][] b){
      board = atomic_copy(b);
    }

    /**
     * Sets the board to be painted in the next repaint()/paintComponent()
     * @param b - the board to be painted;
     * */
    public void set_hist_board(int[][] b){
      hist_board = atomic_copy(b);
    }    

    /**
     * Take the list of hash codes, and display the presentation.
     * @param b a list of hash codes
     * 
     * */
    public void display_hash_lst(ArrayList<Integer> b){
      hash_lst = b;
      dis_hash_lst=true;

      this.repaint();
    }
    /**
     * Take the list of hash codes, and display the presentation.
     * @param b a list of hash codes
     * 
     * */
    public void display_str_lst(ArrayList<String> b){
      str_lst = b;
      dis_str_lst=true;
      gen_coord_lst(b);
      this.repaint();
    }

    /**
     * Sets the list for display, and generate random 
     * 
     * @param b list for display
     * */
    public void gen_coord_lst (ArrayList<String> b){
      coord_lst = new HashMap<String,p>();
      Random rd = new Random();
      for (String i: b){
        coord_lst.put(i,new p((int)(rd.nextDouble()*resx)
                              ,(int)(rd.nextDouble()*resy)));
      }

    }

    /**
     * Hash to x
     *  
     * Takes a hash code and return a x-axis fitted value
     * @param b a hash code
     * */
    public Integer htx(Integer b){
      return b%1563731%resx;
    }


    /**
     * Hash to y
     * Takes a hash code and return a y-axis fitted value
     * @param b a hash code
     * */
    public Integer hty(Integer b){
      return b%1563731%resy;
    }



    /**
     * Toggles Antialiasing for the display engine, on/off
     * 
     * 
     * */
    public void toggle_antialias(){

      antialias_view=antialias_view?false:true;
    }

    /**
     * Sets the board to be painted in the next repaint()/paintComponent()
     * @param intx - the board to be painted;
     * */
    public void set_board_size( int intx, int inty){
      resx=intx*(dw+15);
      resy=inty*(dh+15);
      X=intx;
      Y=inty;
      w=resx/2/X;
      h=resy/2/Y;
    }

    /**
     * Increases the size of the current display  
     * 
     * */
    public void inc_size(){
      if (X==0) return;
      resx+=20;
      resy+=20;
      w=resx/2/X;
      h=resx/2/X;
      repaint();
    }

    /**
     * Decrease the size of the current display
     * 
     * */
    public void dec_size(){
      if (X==0) return;
      resx-=20;
      resy-=20;
      w=resx/2/X;
      h=resx/2/X;
      repaint();
    }


    /**
     * 
     * Sets the color of the color pallete #2; if you are looking to change the color pallete #1 (green), it cannot be changed.
     * 
     * */
    public void set_color2(Color c){
      color2 = c;
    }

    /**
     * 
     * Sets the color of the color pallete #3; if you are looking to change the color pallete #1 (green), it cannot be changed.
     * 
     * */
    public void set_color3(Color c){
      color3 = c;
    }

    /**
     * Draw a dot on the display area at position (x,y) 
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * 
     * */
    private void draw_dot(int x, int y, Graphics g){
      g.fillOval(fit(w)*x,fit(h)*y, w, h);
    }

    /**
     * Draw a dot on the display area at position (x,y) from offset ox and oy
     * @param x - x axis position 
     * @param y - y axis position
     * @param g - Graphics instance, also extends Graphics2D 
     * @param ox - offset of the x axis position
     * @param oy - offset of the y axis position
     * 
     * */
    private void draw_dot(int x, int y, Graphics g, int ox,int oy){
      g.fillOval(fit(w)*x+ox,fit(h)*y+oy, w, h);
    }

    /**
     * Draws the lines of the the edges
     * */
    public void draw_connections(Graphics2D g){
      g.setColor(color2);
      for(String i: coord_lst.keySet()){
        p a=coord_lst.get(i);
        if(dot)g.fillRect(a.x,a.y,dotsz,dotsz); // toggle dot
        // Set<String> nlst = ;
        for (Object f: parent.infect.big_map.get(i)){
          p b = coord_lst.get(f);
          // it = ;
          if(parent.infect.big_map.get_stat((String) f).get()==1){
            if(outin ){
              if(parent.infect.big_map.get_stat(i).get()!=1){
                continue;
              }
            }
            g.setColor(color3);
          }

          else if(parent.infect.big_map.get_stat((String) f).get()==2){
            if(outin ){
              if(parent.infect.big_map.get_stat(i).get()!=2){
                continue;
              }
            }
            g.setColor(color2);
          }
          else if(parent.infect.big_map.get_stat((String) f).get()==-1){
            if(outin ){
              if(parent.infect.big_map.get_stat(i).get()!=-1){
                continue;
              }
            }
            g.setColor(color4);
          }
          else {
            g.setColor(color1);
            if(!enable_uninf)continue;
          }

          g.drawLine(a.x,a.y, b.x,b.y);
        }
      }
    } 

    public void run(){
      if(coord_lst==null){
        gen_coord_lst(parent.infect.big_map.get_all_str(parent.infect.big_map.gt()));

      }
      while(parent.action){
        // System.out.println("Interfacing thread...");

        parent.infect.get_update();
        this.repaint();
        try{
          Thread.sleep((int)parent.fps);    // This is fixed for keeping the game logic consistent at all fps. 
            }
        catch(InterruptedException e){
          System.out.println("Tick sleep interapted.");
        }
      }
    }

    @Override
    /**
     * Calculates and displays rasterized (pixiel by pixiel) image of every frame in the simulation
     * @param g - graphics instance for display
     * */
    public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;
      g2.setColor(color1);
      if(dis_hash_lst){
        for(Integer i: hash_lst){
          g2.fillRect(htx(i),hty(i),5,5);
        }
      }
      if(dis_str_lst){
        draw_connections(g2);
      }
      g2.setColor(new Color(0,0,0));
      // g2.fillOval(2,2,100,100);
    }

  /**
     * Sets the board, and displays the board.
     * @param bd - the board to be painted;
     * */
    public void display_board( int[][] bd){
      set_board(bd);
      repaint();
    }

  /**
  *  Creates a copy of a board
  *  @param board - the target board to be copied 
  *  @return a copy of the board
  * 
  */
  protected int[][] atomic_copy(int[][] board){
    int[][] copy_board = new int[X][Y];
    for (int i=0;i<X;i++){
      for(int z=0;z<Y;z++){
        copy_board[i][z] = (board[i][z]);
      }
    }
    return copy_board;
  }
}

class p {
  Integer x;
  Integer y;
  public p(Integer ix, Integer iy){
    x=ix;
    y=iy;
  }
}
