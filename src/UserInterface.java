import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserInterface extends JPanel 
  implements MouseListener, MouseMotionListener {
  
  static int x = 20, y = 20;

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.setBackground(Color.yellow);
    
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    
    g.setColor(Color.BLUE);
    g.fillRect(x - 20, y - 20, 40, 40);
    g.setColor(new Color(190,81,215));
    g.fillRect(40, 20, 80, 50);
    
    Image chessPiecesImage = new ImageIcon("ChessPieces.png").getImage();
    g.drawImage(chessPiecesImage, x, 0, x + 100, 100, x, 0, x + 100, 100, this);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    x = e.getX();
    y = e.getY();
    repaint();
  }
  @Override
  public void mousePressed(MouseEvent e) {
  }
  @Override
  public void mouseReleased(MouseEvent e) {
  }
  @Override
  public void mouseEntered(MouseEvent e) {
  }
  @Override
  public void mouseExited(MouseEvent e) {
  }
  @Override
  public void mouseDragged(MouseEvent e) {
  }
  @Override
  public void mouseMoved(MouseEvent e) {
  }
}