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

package blog.art.chess.andante.piece.orthodox;

import blog.art.chess.andante.move.LongCastling;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.ShortCastling;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.piece.category.Leaper;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Box;
import blog.art.chess.andante.position.Direction;
import blog.art.chess.andante.position.Square;
import blog.art.chess.andante.position.State;
import java.util.List;
import java.util.StringJoiner;

public class King extends Piece implements Leaper {

  public King(Colour colour) {
    super(colour);
  }

  @Override
  public List<Direction> getLeaps(Board board) {
    return board.getDirections(0, 1, 1, 1);
  }

  @Override
  public boolean generateMoves(Board board, Box box, State state, Square origin, List<Move> moves) {
    boolean result = generateMoves(board, origin, moves);
    if (moves != null && result) {
      if (board.isRebirthSquare(origin, King.class, colour) && !state.isNoCastling(origin)) {
        for (int fileOffset : new int[]{-1, 1}) {
          Direction direction = board.getDirection(fileOffset, 0);
          int distance = 1;
          while (true) {
            Square origin2 = board.findTarget(origin, direction, distance);
            if (origin2 != null) {
              Piece piece2 = board.get(origin2);
              if (piece2 != null) {
                if (piece2 instanceof Rook && piece2.getColour() == colour && board.isRebirthSquare(
                    origin2, Rook.class, colour) && !state.isNoCastling(origin2)) {
                  Square target = board.findTarget(origin, direction, 2);
                  Square target2 = board.findTarget(origin, direction, 1);
                  moves.add(fileOffset > 0 ? new ShortCastling(origin, target, origin2, target2)
                      : new LongCastling(origin, target, origin2, target2));
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
      }
    }
    return result;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", King.class.getSimpleName() + "[", "]").add("colour=" + colour)
        .toString();
  }
}
