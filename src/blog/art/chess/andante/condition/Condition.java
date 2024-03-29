/*
 * MIT License
 *
 * Copyright (c) 2024 Ivan Denkovski
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package blog.art.chess.andante.condition;

import blog.art.chess.andante.move.Action;
import blog.art.chess.andante.move.AndernachAction;
import blog.art.chess.andante.move.Capture;
import blog.art.chess.andante.move.CirceAction;
import blog.art.chess.andante.move.EnPassant;
import blog.art.chess.andante.move.PromotionCapture;
import blog.art.chess.andante.move.QuietMove;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.piece.orthodox.King;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Square;
import java.util.List;

public enum Condition {
  ANDERNACH {
    @Override
    public void generateAction(Board board, Capture move) {
      generateAction(board, move.getOrigin(), move.getTarget(), move.getActions());
    }

    @Override
    public void generateAction(Board board, PromotionCapture move) {
      generateAction(board, move.getOrigin(), move.getTarget(), move.getActions());
    }

    @Override
    public void generateAction(Board board, EnPassant move) {
      generateAction(board, move.getOrigin(), move.getTarget(), move.getActions());
    }

    private void generateAction(Board board, Square origin, Square target, List<Action> actions) {
      Piece piece = board.get(origin);
      if (!(piece instanceof King)) {
        Colour originColour = piece.getColour();
        Colour targetColour = originColour.getOpposite();
        actions.add(new AndernachAction(target, originColour, targetColour));
      }
    }
  }, ANTI_ANDERNACH {
    @Override
    public void generateAction(Board board, QuietMove move) {
      generateAction(board, move.getOrigin(), move.getTarget(), move.getActions());
    }

    private void generateAction(Board board, Square origin, Square target, List<Action> actions) {
      Piece piece = board.get(origin);
      if (!(piece instanceof King)) {
        Colour originColour = piece.getColour();
        Colour targetColour = originColour.getOpposite();
        actions.add(new AndernachAction(target, originColour, targetColour));
      }
    }
  }, CIRCE {
    @Override
    public void generateAction(Board board, Capture move) {
      generateAction(board, move.getOrigin(), move.getTarget(), move.getActions());
    }

    @Override
    public void generateAction(Board board, PromotionCapture move) {
      generateAction(board, move.getOrigin(), move.getTarget(), move.getActions());
    }

    @Override
    public void generateAction(Board board, EnPassant move) {
      generateAction(board, move.getOrigin(), move.getStop(), move.getActions());
    }

    private void generateAction(Board board, Square origin, Square capture, List<Action> actions) {
      Piece piece = board.get(capture);
      if (!(piece instanceof King)) {
        Square rebirth = board.findRebirthSquare(capture, piece.getClass(), piece.getColour());
        if (board.get(rebirth) == null || rebirth.equals(origin)) {
          actions.add(new CirceAction(capture, rebirth));
        }
      }
    }
  };

  public void generateAction(Board board, QuietMove move) {
  }

  public void generateAction(Board board, Capture move) {
  }

  public void generateAction(Board board, PromotionCapture move) {
  }

  public void generateAction(Board board, EnPassant move) {
  }
}
