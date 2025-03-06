/*
 * MIT License
 *
 * Copyright (c) 2025 Ivan Denkovski
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

import blog.art.chess.andante.move.Capture;
import blog.art.chess.andante.move.EnPassant;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.PromotionCapture;
import blog.art.chess.andante.move.fairy.CirceCapture;
import blog.art.chess.andante.move.fairy.CirceCaptureCastling;
import blog.art.chess.andante.move.fairy.CirceEnPassant;
import blog.art.chess.andante.move.fairy.CirceEnPassantCastling;
import blog.art.chess.andante.move.fairy.CircePromotionCapture;
import blog.art.chess.andante.move.fairy.CircePromotionCaptureCastling;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Section;
import blog.art.chess.andante.position.Square;
import java.util.List;
import java.util.StringJoiner;

public class CirceMoveFactory implements MoveFactory {

  @Override
  public void createCapture(Board board, Square origin, Square target, List<Move> moves) {
    if (moves != null) {
      Piece piece = board.get(target);
      Square rebirth = board.findRebirthSquare(piece.getClass(), piece.getColour(), target);
      if (board.get(rebirth) == null || rebirth.equals(origin)) {
        if (piece.isCastling()) {
          moves.add(new CirceCaptureCastling(origin, target, rebirth));
        } else {
          moves.add(new CirceCapture(origin, target, rebirth));
        }
      } else {
        moves.add(new Capture(origin, target));
      }
    }
  }

  @Override
  public void createEnPassant(Board board, Square origin, Square target, Square stop,
      List<Move> moves) {
    if (moves != null) {
      Piece piece = board.get(stop);
      Square rebirth = board.findRebirthSquare(piece.getClass(), piece.getColour(), stop);
      if (board.get(rebirth) == null || rebirth.equals(origin)) {
        if (piece.isCastling()) {
          moves.add(new CirceEnPassantCastling(origin, target, stop, rebirth));
        } else {
          moves.add(new CirceEnPassant(origin, target, stop, rebirth));
        }
      } else {
        moves.add(new EnPassant(origin, target, stop));
      }
    }
  }

  @Override
  public void createPromotionCapture(Board board, Square origin, Square target, Section section,
      List<Move> moves) {
    if (moves != null) {
      Piece piece = board.get(target);
      Square rebirth = board.findRebirthSquare(piece.getClass(), piece.getColour(), target);
      if (board.get(rebirth) == null || rebirth.equals(origin)) {
        if (piece.isCastling()) {
          moves.add(new CircePromotionCaptureCastling(origin, target, section, rebirth));
        } else {
          moves.add(new CircePromotionCapture(origin, target, section, rebirth));
        }
      } else {
        moves.add(new PromotionCapture(origin, target, section));
      }
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", CirceMoveFactory.class.getSimpleName() + "[", "]").toString();
  }
}
