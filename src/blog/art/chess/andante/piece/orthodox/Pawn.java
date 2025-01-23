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

import blog.art.chess.andante.move.Capture;
import blog.art.chess.andante.move.DoubleStep;
import blog.art.chess.andante.move.EnPassant;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.Promotion;
import blog.art.chess.andante.move.PromotionCapture;
import blog.art.chess.andante.move.QuietMove;
import blog.art.chess.andante.move.fairy.CirceCapture;
import blog.art.chess.andante.move.fairy.CirceCaptureCastling;
import blog.art.chess.andante.move.fairy.CirceEnPassant;
import blog.art.chess.andante.move.fairy.CirceEnPassantCastling;
import blog.art.chess.andante.move.fairy.CircePromotionCapture;
import blog.art.chess.andante.move.fairy.CircePromotionCaptureCastling;
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

public class Pawn extends Piece {

  public Pawn(Colour colour) {
    super(colour);
  }

  @Override
  public boolean generateMoves(Board board, Box box, State state, boolean circe, Square origin,
      List<Move> moves) {
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
            if (piece.isRoyal()) {
              return false;
            }
            if (moves != null) {
              if (board.isRebirthSquare(target, Piece.class, colour)) {
                if (circe) {
                  Square rebirth = board.findRebirthSquare(piece.getClass(), piece.getColour(),
                      target);
                  if (board.get(rebirth) == null || rebirth.equals(origin)) {
                    if (piece.isCastling()) {
                      for (Section section : box.findSections(colour)) {
                        moves.add(
                            new CircePromotionCaptureCastling(origin, target, section, rebirth));
                      }
                    } else {
                      for (Section section : box.findSections(colour)) {
                        moves.add(new CircePromotionCapture(origin, target, section, rebirth));
                      }
                    }
                    continue;
                  }
                }
                for (Section section : box.findSections(colour)) {
                  moves.add(new PromotionCapture(origin, target, section));
                }
              } else {
                if (circe) {
                  Square rebirth = board.findRebirthSquare(piece.getClass(), piece.getColour(),
                      target);
                  if (board.get(rebirth) == null || rebirth.equals(origin)) {
                    if (piece.isCastling()) {
                      moves.add(new CirceCaptureCastling(origin, target, rebirth));
                    } else {
                      moves.add(new CirceCapture(origin, target, rebirth));
                    }
                    continue;
                  }
                }
                moves.add(new Capture(origin, target));
              }
            }
          }
        } else if (state.isEnPassant(target)) {
          Square stop = board.findTarget(target, board.getDirection(0, -rankOffset), 1);
          piece = board.get(stop);
          if (piece.isRoyal()) {
            return false;
          }
          if (moves != null) {
            if (circe) {
              Square rebirth = board.findRebirthSquare(piece.getClass(), piece.getColour(), stop);
              if (board.get(rebirth) == null || rebirth.equals(origin)) {
                if (piece.isCastling()) {
                  moves.add(new CirceEnPassantCastling(origin, target, stop, rebirth));
                } else {
                  moves.add(new CirceEnPassant(origin, target, stop, rebirth));
                }
                continue;
              }
            }
            moves.add(new EnPassant(origin, target, stop));
          }
        }
      }
    }
    if (moves != null) {
      int fileOffset = 0;
      Direction direction = board.getDirection(fileOffset, rankOffset);
      Square target = board.findTarget(origin, direction, 1);
      if (target != null) {
        if (board.get(target) == null) {
          if (board.isRebirthSquare(target, Piece.class, colour)) {
            for (Section section : box.findSections(colour)) {
              moves.add(new Promotion(origin, target, section));
            }
          } else {
            moves.add(new QuietMove(origin, target));
            if (board.isRebirthSquare(origin, Pawn.class, colour)) {
              target = board.findTarget(origin, direction, 2);
              if (board.get(target) == null) {
                Square stop = board.findTarget(origin, direction, 1);
                moves.add(new DoubleStep(origin, target, stop));
              }
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
