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

package blog.art.chess.andante.piece.orthodox;

import blog.art.chess.andante.condition.MoveFactory;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Box;
import blog.art.chess.andante.position.Direction;
import blog.art.chess.andante.position.Section;
import blog.art.chess.andante.position.Square;
import blog.art.chess.andante.position.State;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public final class Pawn extends Piece {

  public Pawn(Colour colour) {
    super(colour);
  }

  @Override
  public boolean generateMoves(Board board, Box box, State state, Square origin,
      MoveFactory moveFactory, List<Move> moves) {
    int rankOffset = switch (colour) {
      case WHITE -> 1;
      case BLACK -> -1;
    };
    for (int fileOffset : new int[]{-1, 1}) {
      Direction direction = board.getDirection(fileOffset, rankOffset);
      Square target = board.findTarget(origin, direction, 1);
      if (target != null) {
        Piece piece = board.get(target);
        if (piece != null) {
          if (piece.getColour() != colour) {
            if (board.isRebirthSquare(target, Piece.class, colour)) {
              for (Section section : box.findSections(colour)) {
                if (!moveFactory.createPromotionCapture(board, box, origin, target, section,
                    moves)) {
                  return false;
                }
              }
            } else {
              if (!moveFactory.createCapture(board, origin, target, moves)) {
                return false;
              }
            }
          }
        } else if (state.isEnPassant(target)) {
          Square stop = board.findTarget(target, board.getDirection(0, -rankOffset), 1);
          if (!moveFactory.createEnPassant(board, origin, target, stop, moves)) {
            return false;
          }
        }
      }
    }
    int fileOffset = 0;
    Direction direction = board.getDirection(fileOffset, rankOffset);
    Square target = board.findTarget(origin, direction, 1);
    if (target != null) {
      if (board.get(target) == null) {
        if (board.isRebirthSquare(target, Piece.class, colour)) {
          for (Section section : box.findSections(colour)) {
            moveFactory.newPromotion(origin, target, section, moves);
          }
        } else {
          moveFactory.newQuietMove(origin, target, moves);
          if (board.isRebirthSquare(origin, Pawn.class, colour)) {
            target = board.findTarget(origin, direction, 2);
            if (board.get(target) == null) {
              Square stop = board.findTarget(origin, direction, 1);
              moveFactory.newDoubleStep(origin, target, stop, moves);
            }
          }
        }
      }
    }
    return true;
  }

  @Override
  public String getCode(Locale locale) {
    return "";
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Pawn.class.getSimpleName() + "[", "]").add("colour=" + colour)
        .toString();
  }
}
