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

import blog.art.chess.andante.move.AndernachMove;
import blog.art.chess.andante.move.Capture;
import blog.art.chess.andante.move.CirceMove;
import blog.art.chess.andante.move.EnPassant;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.PromotionCapture;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Box;
import blog.art.chess.andante.position.Section;
import blog.art.chess.andante.position.Square;
import java.util.List;

public interface CirceAndernach extends MoveFactory {

  @Override
  default boolean createCapture(Board board, Square origin, Square target, List<Move> moves) {
    Piece other = board.get(target);
    if (other.isRoyal()) {
      return false;
    }
    if (moves != null) {
      Square rebirth = board.findRebirthSquare(other.getClass(), other.getColour(), target);
      Piece piece = board.get(origin);
      if (board.get(rebirth) == null || rebirth.equals(origin)) {
        boolean castling = other.isCastling();
        if (!piece.isRoyal()) {
          boolean castling2 = piece.isCastling() && board.findRebirthSquare(piece.getClass(),
              piece.getColour().getOpposite(), target).equals(target);
          moves.add(new AndernachMove(new CirceMove(new Capture(origin, target), rebirth, castling),
              castling2));
        } else {
          moves.add(new CirceMove(new Capture(origin, target), rebirth, castling));
        }
      } else {
        if (!piece.isRoyal()) {
          boolean castling = piece.isCastling() && board.findRebirthSquare(piece.getClass(),
              piece.getColour().getOpposite(), target).equals(target);
          moves.add(new AndernachMove(new Capture(origin, target), castling));
        } else {
          moves.add(new Capture(origin, target));
        }
      }
    }
    return true;
  }

  @Override
  default boolean createEnPassant(Board board, Square origin, Square target, Square stop,
      List<Move> moves) {
    Piece other = board.get(stop);
    if (other.isRoyal()) {
      return false;
    }
    if (moves != null) {
      Square rebirth = board.findRebirthSquare(other.getClass(), other.getColour(), stop);
      Piece piece = board.get(origin);
      if ((board.get(rebirth) == null || rebirth.equals(origin) || rebirth.equals(stop))
          && !rebirth.equals(target)) {
        boolean castling = other.isCastling();
        if (!piece.isRoyal()) {
          boolean castling2 = piece.isCastling() && board.findRebirthSquare(piece.getClass(),
              piece.getColour().getOpposite(), target).equals(target);
          moves.add(new AndernachMove(
              new CirceMove(new EnPassant(origin, target, stop), rebirth, castling), castling2));
        } else {
          moves.add(new CirceMove(new EnPassant(origin, target, stop), rebirth, castling));
        }
      } else {
        if (!piece.isRoyal()) {
          boolean castling = piece.isCastling() && board.findRebirthSquare(piece.getClass(),
              piece.getColour().getOpposite(), target).equals(target);
          moves.add(new AndernachMove(new EnPassant(origin, target, stop), castling));
        } else {
          moves.add(new EnPassant(origin, target, stop));
        }
      }
    }
    return true;
  }

  @Override
  default boolean createPromotionCapture(Board board, Box box, Square origin, Square target,
      Section section, List<Move> moves) {
    Piece other = board.get(target);
    if (other.isRoyal()) {
      return false;
    }
    if (moves != null) {
      Square rebirth = board.findRebirthSquare(other.getClass(), other.getColour(), target);
      Piece piece = box.peek(section);
      if (board.get(rebirth) == null || rebirth.equals(origin)) {
        boolean castling = other.isCastling();
        if (!piece.isRoyal()) {
          boolean castling2 = piece.isCastling() && board.findRebirthSquare(piece.getClass(),
              piece.getColour().getOpposite(), target).equals(target);
          moves.add((new AndernachMove(
              new CirceMove(new PromotionCapture(origin, target, section), rebirth, castling),
              castling2)));
        } else {
          moves.add(
              new CirceMove(new PromotionCapture(origin, target, section), rebirth, castling));
        }
      } else {
        if (!piece.isRoyal()) {
          boolean castling = piece.isCastling() && board.findRebirthSquare(piece.getClass(),
              piece.getColour().getOpposite(), target).equals(target);
          moves.add(new AndernachMove(new PromotionCapture(origin, target, section), castling));
        } else {
          moves.add(new PromotionCapture(origin, target, section));
        }
      }
    }
    return true;
  }
}
