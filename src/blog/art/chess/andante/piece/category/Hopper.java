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

package blog.art.chess.andante.piece.category;

import blog.art.chess.andante.move.Capture;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.QuietMove;
import blog.art.chess.andante.move.fairy.CirceCapture;
import blog.art.chess.andante.move.fairy.CirceCaptureCastling;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Direction;
import blog.art.chess.andante.position.Square;
import java.util.List;

public interface Hopper {

  Colour getColour();

  List<Direction> getHops(Board board);

  default boolean generateMoves(Board board, boolean circe, Square origin, List<Move> moves) {
    for (Direction direction : getHops(board)) {
      int distance = 1;
      while (true) {
        Square target = board.findTarget(origin, direction, distance);
        if (target != null) {
          if (board.get(target) != null) {
            target = board.findTarget(origin, direction, distance + 1);
            if (target != null) {
              Piece piece = board.get(target);
              if (piece != null) {
                if (piece.getColour() != getColour()) {
                  if (piece.isRoyal()) {
                    return false;
                  }
                  if (moves != null) {
                    if (circe) {
                      Square rebirth = board.findRebirthSquare(piece.getClass(), piece.getColour(),
                          target);
                      if (board.get(rebirth) == null || rebirth.equals(origin)) {
                        if (piece.isCastling()) {
                          moves.add(new CirceCaptureCastling(origin, target, rebirth));
                        } else {
                          moves.add(new CirceCapture(origin, target, rebirth));
                        }
                        break;
                      }
                    }
                    moves.add(new Capture(origin, target));
                  }
                }
              } else {
                if (moves != null) {
                  moves.add(new QuietMove(origin, target));
                }
              }
            }
            break;
          } else {
            distance++;
          }
        } else {
          break;
        }
      }
    }
    return true;
  }
}
