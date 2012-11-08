/**
* Paints an analog clock synchronized with the system clock.
* @author Paolo Boschini
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Clock extends JFrame {

  private static final long serialVersionUID = 1L;

  public Clock() {
    ClockPanel container = new ClockPanel();
    add(container, BorderLayout.CENTER);
    setBackground(new Color(24, 116, 205));
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);
    // setSize(500, 500);
    // setUndecorated(true);
    pack();
    setVisible(true);
  }

  public static void main(String[] args) {
    new Clock();
  }
}

class ClockPanel extends JPanel implements Runnable {

  private static final long serialVersionUID = 1L;
  Thread t = new Thread(this);
  private int thick = 1;
  int xsec, ysec, xmin, ymin, xhour, yhour;

  public ClockPanel() {
    setMinimumSize(new Dimension(500, 500));
    setMaximumSize(new Dimension(500, 500));
    setPreferredSize(new Dimension(500, 500));
    setLayout(null);
    t.start();
  }

  public void run() {
    while(true) {
      try {
        xsec = minToLocation(Calendar.getInstance().get(Calendar.SECOND), 200).x;
        ysec = minToLocation(Calendar.getInstance().get(Calendar.SECOND), 200).y;
        xmin = minToLocation(Calendar.getInstance().get(Calendar.MINUTE), 180).x;
        ymin = minToLocation(Calendar.getInstance().get(Calendar.MINUTE), 180).y;
        xhour = minToLocation(Calendar.getInstance().get(Calendar.HOUR)*5+getRelativeHour(Calendar.getInstance().get(Calendar.MINUTE)), 150).x;
        yhour = minToLocation(Calendar.getInstance().get(Calendar.HOUR)*5+getRelativeHour(Calendar.getInstance().get(Calendar.MINUTE)), 150).y;
        repaint();
        Thread.sleep(500);
      } catch(InterruptedException ie){
        ie.printStackTrace();
      }
    }
  }

  public int getRelativeHour(int min) {
    return min / 12;
  }

  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    g2.clearRect(0, 0, getWidth(), getHeight());

    g2.setColor(new Color(200,200,200));
    for (int i = 0; i < 60; i++) {
      g2.setColor((i <= Calendar.getInstance().get(Calendar.SECOND)) ? Color.white : new Color(160,160,160));
      if(i % 5 == 0)
        g2.fillOval(minToLocation(i, 210).x - 4 , minToLocation(i, 210).y - 4, 8, 8);
      else
        g2.fillOval(minToLocation(i, 210).x - 2 , minToLocation(i, 210).y - 2, 4, 4);
    }

    g2.setColor(Color.white);
    g2.drawString(Calendar.getInstance().get(Calendar.HOUR)+" : ", 430, 485);
    g2.drawString(Calendar.getInstance().get(Calendar.MINUTE)+" : ", 450, 485);
    g2.drawString(Calendar.getInstance().get(Calendar.SECOND)+"", 480, 485);

    for (int z = 0; z < thick; z++) {
      g2.setColor(Color.white);
      g2.drawOval(25 + z, 25 + z, 450 - (z*2), 450 - (z*2));
    }

    g2.drawLine(250, 250, xsec, ysec);
    g2.drawLine(250, 250, xmin, ymin);
    g2.drawLine(250, 250, xhour, yhour);
  }

  public Point minToLocation(int min, int r) {
    double t = 2 * Math.PI * (min-15) / 60;
    int x = (int) Math.round(250 + r * Math.cos(t));
    int y = (int) Math.round(250 + r * Math.sin(t));
    // System.out.println(x + ", " + y);
    return new Point(x, y);
  }
}