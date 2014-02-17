
import javax.swing.*;
import java.util.*;

public class AlphaBetaChess {

  // lower case = black
  // upper case = white
  static String chessBoard[][] = {
    {"r", "n", "b", "q", "k", "b", "n", "r"},
    {"p", "p", "p", "p", "p", "p", "p", "p"},
    {" ", " ", " ", " ", " ", " ", " ", " "},
    {" ", " ", " ", " ", " ", " ", " ", " "},
    {" ", " ", " ", " ", " ", " ", " ", " "},
    {" ", " ", " ", " ", " ", " ", " ", " "},
    {"P", "P", "P", "P", "P", "P", "P", "P"},
    {"R", "N", "B", "Q", "K", "B", "N", "R"}
  };

  static int kingPositionC, kingPositionL;
  static int humanAsWhite = -1; //1 == human as white, 0 == human as black
  static int globalDepth = 4;

  public static void main(String[] args) {
    while (!"K".equals(chessBoard[kingPositionC / 8][kingPositionC % 8])) {
      kingPositionC++;
    }
    while (!"k".equals(chessBoard[kingPositionL / 8][kingPositionL % 8])) {
      kingPositionL++;
    }
    JFrame f = new JFrame("Justin's Chess Engine");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UserInterface ui = new UserInterface();
    f.add(ui);
    f.setSize(UserInterface.squareSize * 8, UserInterface.squareSize * 9);
    f.setLocationRelativeTo(null);
    f.setVisible(true);

    Object[] option = {"Computer", "Human"};
    humanAsWhite = JOptionPane.showOptionDialog(null,
            "Who should play as white?", "Justin's Chess Options",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);

    if (humanAsWhite == 0) {
      makeMove(alphaBeta(globalDepth, 10000000, -10000000, "", 0));
      flipBoard();
      ui.repaint();
    }
  }

  // Returns move & score of the optimal route
  // retruns in the form of 1234b########## (move,piece,score)
  public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
    String list = possibleMoves();

    if (depth == 0 || list.length() == 0) { // max depth
      return move + (rating() * (player * 2 - 1));
    }

    //sort later
    player = 1 - player; // either 1 or 0

    for (int i = 0; i < list.length(); i += 5) {
      makeMove(list.substring(i, i + 5));
      flipBoard();
      String returnString = alphaBeta(depth - 1, beta, alpha, list.substring(i, i + 5), player);
      int value = Integer.valueOf(returnString.substring(5));
      flipBoard();
      undoMove(list.substring(i, i + 5));
      if (player == 0) {
        if (value <= beta) {
          beta = value;
          if (depth == globalDepth) {
            move = returnString.substring(0, 5);
          }
        }
      } else {
        if (value > alpha) {
          alpha = value;
          if (depth == globalDepth) {
            move = returnString.substring(0, 5);
          }
        }
      }
      if (alpha >= beta) {
        if (player == 0) {
          return move + beta;
        } else {
          return move + alpha;
        }
      }
    }

    if (player == 0) {
      return move + beta;
    } else {
      return move + alpha;
    }
  }

  public static void flipBoard() {
    String temp;
    for (int i = 0; i < 32; i++) {
      int r = i / 8, c = i % 8;
      if (Character.isUpperCase(chessBoard[r][c].charAt(0))) {
        temp = chessBoard[r][c].toLowerCase();
      } else {
        temp = chessBoard[r][c].toUpperCase();
      }
      if (Character.isUpperCase(chessBoard[7 - r][7 - c].charAt(0))) {
        chessBoard[r][c] = chessBoard[7 - r][7 - c].toLowerCase();
      } else {
        chessBoard[r][c] = chessBoard[7 - r][7 - c].toUpperCase();
      }
      chessBoard[7 - r][7 - c] = temp;
    }

    int kingTemp = kingPositionC;
    kingPositionC = 63 - kingPositionL;
    kingPositionL = 63 - kingTemp;
  }

  public static void makeMove(String move) {
    if (move.charAt(4) != 'P') {
      //x1,y1,x2,y2, captured-piece
      chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))];
      chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = " ";
      if ("K".equals(chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
        kingPositionC = 8 * Character.getNumericValue(move.charAt(2)) + Character.getNumericValue(move.charAt(3));
      }
    } else {
      // if pawn promotion
      //column1, column2, captured-piece, new-piece, P
      chessBoard[1][Character.getNumericValue(move.charAt(0))] = " ";
      chessBoard[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(3));

    }
  }

  public static void undoMove(String move) {
    if (move.charAt(4) != 'P') {
      //x1,y1,x2,y2, captured-piece
      chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))] = chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))];
      chessBoard[Character.getNumericValue(move.charAt(2))][Character.getNumericValue(move.charAt(3))] = String.valueOf(move.charAt(4));
      if ("K".equals(chessBoard[Character.getNumericValue(move.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
        kingPositionC = 8 * Character.getNumericValue(move.charAt(0)) + Character.getNumericValue(move.charAt(1));
      }
    } else {
      // if pawn promotion
      //column1, column2, captured-piece, new-piece, P
      chessBoard[1][Character.getNumericValue(move.charAt(0))] = "P";
      chessBoard[0][Character.getNumericValue(move.charAt(1))] = String.valueOf(move.charAt(2));
    }
  }

  public static String possibleMoves() {
    String list = "";
    for (int i = 0; i < 64; i++) {
      switch (chessBoard[i / 8][i % 8]) {
        case "P":
          list += possibleP(i);
          break;
        case "R":
          list += possibleR(i);
          break;
        case "N":
          list += possibleN(i);
          break;
        case "B":
          list += possibleB(i);
          break;
        case "Q":
          list += possibleQ(i);
          break;
        case "K":
          list += possibleK(i);
          break;
      }
    }
    return list; //x1, y1, x2, y2, captured piece
  }

  public static String possibleP(int i) {
    String list = "", oldPiece;
    int r = i / 8, c = i % 8;
    for (int j = -1; j <= 1; j += 2) {
      try { //capture
        if (Character.isLowerCase(chessBoard[r - 1][c + j].charAt(0)) && i >= 16) {
          oldPiece = chessBoard[r - 1][c + j];
          chessBoard[r][c] = " ";
          chessBoard[r - 1][c + j] = "P";
          if (kingSafe()) {
            list = list + r + c + (r - 1) + (c + j) + oldPiece;
          }
          chessBoard[r][c] = "P";
          chessBoard[r - 1][c + j] = oldPiece;
        }
      } catch (Exception e) {
      }
      try { //promotion and capture
        if (Character.isLowerCase(chessBoard[r - 1][c + j].charAt(0)) && i < 16) {
          String[] temp = {"Q", "R", "B", "N"};
          for (int k = 0; k < 4; k++) {
            oldPiece = chessBoard[r - 1][c + j];
            chessBoard[r][c] = " ";
            chessBoard[r - 1][c + j] = temp[k];
            if (kingSafe()) {
              //column 1, column 2, captured-piece, new-piece,P
              list = list + c + (c + j) + oldPiece + temp[k] + "P";
            }
            chessBoard[r][c] = "P";
            chessBoard[r - 1][c + j] = oldPiece;
          }
        }
      } catch (Exception e) {
      }
    }
    try { //move one up
      if (" ".equals(chessBoard[r - 1][c]) && i >= 16) {
        oldPiece = chessBoard[r - 1][c];
        chessBoard[r][c] = " ";
        chessBoard[r - 1][c] = "P";
        if (kingSafe()) {
          list = list + r + c + (r - 1) + (c) + oldPiece;
        }
        chessBoard[r][c] = "P";
        chessBoard[r - 1][c] = oldPiece;
      }
    } catch (Exception e) {
    }

    try { // promotion with no capture
      if (" ".equals(chessBoard[r - 1][c]) && i < 16) {
        String[] temp = {"Q", "R", "B", "N"};
        for (int k = 0; k < 4; k++) {
          oldPiece = chessBoard[r - 1][c];
          chessBoard[r][c] = " ";
          chessBoard[r - 1][c] = temp[k];
          if (kingSafe()) {
            //column 1, column 2, captured-piece, new-piece,P
            list = list + c + (c) + oldPiece + temp[k] + "P";
          }
          chessBoard[r][c] = "P";
          chessBoard[r - 1][c] = oldPiece;
        }
      }
    } catch (Exception e) {
    }

    try { //move two up
      if (" ".equals(chessBoard[r - 1][c]) && " ".equals(chessBoard[r - 2][c]) && i >= 48) {
        oldPiece = chessBoard[r - 2][c];
        chessBoard[r][c] = " ";
        chessBoard[r - 2][c] = "P";
        if (kingSafe()) {
          list = list + r + c + (r - 2) + (c) + oldPiece;
        }
        chessBoard[r][c] = "P";
        chessBoard[r - 2][c] = oldPiece;
      }
    } catch (Exception e) {
    }

    return list;
  }

  public static String possibleR(int i) {
    String list = "", oldPiece;
    int r = i / 8, c = i % 8;
    int temp = 1;
    for (int j = -1; j <= 1; j += 2) {
      try {
        while (" ".equals(chessBoard[r][c + temp * j])) {
          oldPiece = chessBoard[r][c + temp * j];
          chessBoard[r][c] = " ";
          chessBoard[r][c + temp * j] = "R";
          if (kingSafe()) {
            list = list + r + c + r + (c + temp * j) + oldPiece;
          }
          chessBoard[r][c] = "R";
          chessBoard[r][c + temp * j] = oldPiece;
          temp++;
        }
        if (Character.isLowerCase(chessBoard[r][c + temp * j].charAt(0))) {
          oldPiece = chessBoard[r][c + temp * j];
          chessBoard[r][c] = " ";
          chessBoard[r][c + temp * j] = "R";
          if (kingSafe()) {
            list = list + r + c + r + (c + temp * j) + oldPiece;
          }
          chessBoard[r][c] = "R";
          chessBoard[r][c + temp * j] = oldPiece;
        }
      } catch (Exception e) {
      }

      temp = 1;
      try {
        while (" ".equals(chessBoard[r + temp * j][c])) {
          oldPiece = chessBoard[r + temp * j][c];
          chessBoard[r][c] = " ";
          chessBoard[r + temp * j][c] = "R";
          if (kingSafe()) {
            list = list + r + c + (r + temp * j) + (c) + oldPiece;
          }
          chessBoard[r][c] = "R";
          chessBoard[r + temp * j][c] = oldPiece;
          temp++;
        }
        if (Character.isLowerCase(chessBoard[r + temp * j][c].charAt(0))) {
          oldPiece = chessBoard[r + temp * j][c];
          chessBoard[r][c] = " ";
          chessBoard[r + temp * j][c] = "R";
          if (kingSafe()) {
            list = list + r + c + (r + temp * j) + (c) + oldPiece;
          }
          chessBoard[r][c] = "R";
          chessBoard[r + temp * j][c] = oldPiece;
        }
      } catch (Exception e) {
      }
      temp = 1;
    }
    return list;
  }

  public static String possibleN(int i) {
    String list = "", oldPiece;
    int r = i / 8, c = i % 8;
    for (int j = -1; j <= 1; j += 2) {
      for (int k = -1; k <= 1; k += 2) {
        try {
          if (Character.isLowerCase(chessBoard[r + j][c + k * 2].charAt(0)) || " ".equals(chessBoard[r + j][c + k * 2])) {
            oldPiece = chessBoard[r + j][c + k * 2];
            chessBoard[r][c] = " ";
            if (kingSafe()) {
              list = list + r + c + (r + j) + (c + k * 2) + oldPiece;
            }
            chessBoard[r][c] = "N";
            chessBoard[r + j][c + k * 2] = oldPiece;
          }
        } catch (Exception e) {
        }
        try {
          if (Character.isLowerCase(chessBoard[r + j * 2][c + k].charAt(0)) || " ".equals(chessBoard[r + j * 2][c + k])) {
            oldPiece = chessBoard[r + j * 2][c + k];
            chessBoard[r][c] = " ";
            if (kingSafe()) {
              list = list + r + c + (r + j * 2) + (c + k) + oldPiece;
            }
            chessBoard[r][c] = "N";
            chessBoard[r + j * 2][c + k] = oldPiece;
          }
        } catch (Exception e) {
        }
      }
    }
    return list;
  }

  public static String possibleB(int i) {
    String list = "", oldPiece;
    int r = i / 8, c = i % 8;
    int temp = 1;
    for (int j = -1; j <= 1; j += 2) {
      for (int k = -1; k <= 1; k += 2) {
        try {
          while (" ".equals(chessBoard[r + temp * j][c + temp * k])) {
            oldPiece = chessBoard[r + temp * j][c + temp * k];
            chessBoard[r][c] = " ";
            chessBoard[r + temp * j][c + temp * k] = "B";
            if (kingSafe()) {
              list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
            }
            chessBoard[r][c] = "B";
            chessBoard[r + temp * j][c + temp * k] = oldPiece;
            temp++;
          }
          if (Character.isLowerCase(chessBoard[r + temp * j][c + temp * k].charAt(0))) {
            oldPiece = chessBoard[r + temp * j][c + temp * k];
            chessBoard[r][c] = " ";
            chessBoard[r + temp * j][c + temp * k] = "B";
            if (kingSafe()) {
              list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
            }
            chessBoard[r][c] = "B";
            chessBoard[r + temp * j][c + temp * k] = oldPiece;
          }
        } catch (Exception e) {
        }
        temp = 1;
      }
    }
    return list;
  }

  public static String possibleQ(int i) {
    String list = "", oldPiece;
    int r = i / 8, c = i % 8;
    int temp = 1;
    for (int j = -1; j <= 1; j++) {
      for (int k = -1; k <= 1; k++) {
        if (j != 0 || k != 0) {
          try {
            while (" ".equals(chessBoard[r + temp * j][c + temp * k])) {
              oldPiece = chessBoard[r + temp * j][c + temp * k];
              chessBoard[r][c] = " ";
              chessBoard[r + temp * j][c + temp * k] = "Q";
              if (kingSafe()) {
                list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
              }
              chessBoard[r][c] = "Q";
              chessBoard[r + temp * j][c + temp * k] = oldPiece;
              temp++;
            }
            if (Character.isLowerCase(chessBoard[r + temp * j][c + temp * k].charAt(0))) {
              oldPiece = chessBoard[r + temp * j][c + temp * k];
              chessBoard[r][c] = " ";
              chessBoard[r + temp * j][c + temp * k] = "Q";
              if (kingSafe()) {
                list = list + r + c + (r + temp * j) + (c + temp * k) + oldPiece;
              }
              chessBoard[r][c] = "Q";
              chessBoard[r + temp * j][c + temp * k] = oldPiece;
            }
          } catch (Exception e) {
          }
          temp = 1;
        }
      }
    }
    return list;
  }

  public static String possibleK(int i) {
    String list = "", oldPiece;
    int r = i / 8, c = i % 8;
    for (int j = 0; j < 9; j++) {
      if (j != 4) {
        try {
          if (Character.isLowerCase(chessBoard[r - 1 + j / 3][c - 1 + j % 3].charAt(0)) || " ".equals(chessBoard[r - 1 + j / 3][c - 1 + j % 3])) {
            oldPiece = chessBoard[r - 1 + j / 3][c - 1 + j % 3];
            chessBoard[r][c] = " ";
            chessBoard[r - 1 + j / 3][c - 1 + j % 3] = "K";
            int kingTemp = kingPositionC;
            kingPositionC = i + (j / 3) * 8 + j % 3 - 9;
            if (kingSafe()) {
              list = list + r + c + (r - 1 + j / 3) + (c - 1 + j % 3) + oldPiece;
            }
            chessBoard[r][c] = "K";
            chessBoard[r - 1 + j / 3][c - 1 + j % 3] = oldPiece;
            kingPositionC = kingTemp;
          }
        } catch (Exception e) {
        }
      }
    }
    //need to add casting later
    return list;
  }

  public static int rating() {
    return (int) (Math.random() * ((1000 - 0) + 1));
  }

  public static boolean kingSafe() {
    int temp = 1;
    // check bishop and queen diagonal movement
    for (int i = -1; i <= 1; i += 2) {
      for (int j = -1; j <= 1; j += 2) {
        try {
          while (" ".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j])) {
            temp++;
          }
          if ("b".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j])
                  || "q".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8 + temp * j])) {
            return false;
          }
        } catch (Exception e) {
        }
        temp = 1;
      }
    }
    // check rook/queen horizonal/vertical movement
    for (int i = -1; i <= 1; i += 2) {
      try {
        while (" ".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i])) {
          temp++;
        }
        if ("r".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i])
                || "q".equals(chessBoard[kingPositionC / 8][kingPositionC % 8 + temp * i])) {
          return false;
        }
      } catch (Exception e) {
      }
      temp = 1;
      try {
        while (" ".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8])) {
          temp++;
        }
        if ("r".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8])
                || "q".equals(chessBoard[kingPositionC / 8 + temp * i][kingPositionC % 8])) {
          return false;
        }
      } catch (Exception e) {
      }
      temp = 1;
    }
    //knight
    for (int i = -1; i <= 1; i += 2) {
      for (int j = -1; j <= 1; j += 2) {
        try {
          if ("n".equals(chessBoard[kingPositionC / 8 + i][kingPositionC % 8 + j * 2])) {
            return false;
          }
        } catch (Exception e) {
        }
        try {
          if ("n".equals(chessBoard[kingPositionC / 8 + i * 2][kingPositionC % 8 + j])) {
            return false;
          }
        } catch (Exception e) {
        }
      }
    }
    //pawn
    if (kingPositionC >= 16) {
      try {
        if ("p".equals(chessBoard[kingPositionC / 80 - 1][kingPositionC % 8 - 1])) {
          return false;
        }
      } catch (Exception e) { }
      try {
        if ("p".equals(chessBoard[kingPositionC / 80 - 1][kingPositionC % 8 + 1])) {
          return false;
        }
      } catch (Exception e) { }
      //king
      for (int i = -1; i <= 1; i++) {
        for (int j = -1; j <= 1; j++) {
          if (i != 0 || j != 0) {
            try {
              if ("k".equals(chessBoard[kingPositionC / 8 + i][kingPositionC % 8 + j])) {
                return false;
              }
            } catch (Exception e) {
            }
          }
        }
      }
    }
    return true;
  }
}
