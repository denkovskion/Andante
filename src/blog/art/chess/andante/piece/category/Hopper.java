/*
 * MIT License
 *
 * Copyright (c) 2024-2025 Ivan Denkovski
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

import blog.art.chess.andante.condition.MoveFactory;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Direction;
import blog.art.chess.andante.position.Square;
import java.util.List;

public interface Hopper {

  Colour getColour();

  List<Direction> getHops(Board board);

  default boolean generateMoves(Board board, Square origin, MoveFactory moveFactory,
      List<Move> moves) {
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
                  moveFactory.createCapture(board, origin, target, moves);
                }
              } else {
                moveFactory.newQuietMove(origin, target, moves);
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
