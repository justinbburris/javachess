import javax.swing.*;

public class AlphaBetaChess {
  // lower case = black
  // upper case = white
  static String chessBoard[][] = {
    {"r","n","b","q","k","b","n","r"},
    {"p","p","p","p","p","p","p","p"},
    {" "," "," "," "," "," ","P"," "},
    {" "," "," "," "," "," "," "," "},
    {" "," "," "," "," "," "," "," "},
    {" "," "," "," "," "," "," "," "},
    {"P","P","P","P","P","P","P","P"},
    {"R","N","B","Q","K","B","N","R"}
  };
  
  static int kingPositionW, kingPositionB;
  
  public static void main(String[] args) {
    /*JFrame f=new JFrame("Justin's Chess Engine");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UserInterface ui = new UserInterface();
    f.add(ui);
    f.setSize(500, 500);
    f.setVisible(true);*/
    
    System.out.println(possibleMoves());
  }
  
  public static String possibleMoves() {
    String list = "";
    for(int i = 0; i < 64; i++) {
      switch(chessBoard[i / 8][i % 8]) {
        case "P": 
          list+= possibleP(i);
          break;
        case "R": 
          list+= possibleR(i);
          break;
        case "N": 
          list+= possibleN(i);
          break;
        case "B": 
          list+= possibleB(i);
          break;
        case "Q": 
          list+= possibleQ(i);
          break;
        case "K": 
          list+= possibleK(i);
          break;
      }
    }
    return list; //x1, y1, x2, y2, captured piece
  }
  
  public static String possibleP(int i) {
    String list = "";
    
    return list;
  }
  public static String possibleR(int i) {
    String list = "";
    
    return list;
  }
  public static String possibleN(int i) {
    String list = "";
    
    return list;
  }
  public static String possibleB(int i) {
    String list = "";
    
    return list;
  }
  public static String possibleQ(int i) {
    String list = "";
    
    return list;
  }  
  public static String possibleK(int pos) {
    String list = "", oldPiece;
    int rank = pos / 8, file = pos % 8;
    
    for(int i = 0; i < 9; i++) {
      if(i != 4) {
        try {
          String attackedPiece = chessBoard[rank - 1 + (i / 3)][file - 1 + (i % 3)];

          // Ensure king can move to location
          if(Character.isLowerCase(attackedPiece.charAt(0)) || " ".equals(attackedPiece)) {
            chessBoard[rank][file] = " ";
            chessBoard[rank - 1 + (i / 3)][file - 1 + (i % 3)] = "K";
            int oldKing = kingPositionW;
            kingPositionW = i + (i/3) * 8 + i%3 - 9;
            if(kingSafe()) {
              list = list + rank + file + (rank - 1 + (i / 3)) + (file - 1 + (i % 3)) + attackedPiece;
            }
            // Restore pieces to their original positions
            chessBoard[rank][file] = "K";
            chessBoard[rank - 1 + (i / 3)][file - 1 + (i % 3)] = attackedPiece;
            kingPositionW = oldKing;
          }
        } catch(Exception e) {
          // Ignore
        }
      }
    }
    //TODO: Add in castling
    return list;
  }
  
  public static boolean kingSafe() {
    return true;
  }
}