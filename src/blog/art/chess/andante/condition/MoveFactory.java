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
import blog.art.chess.andante.move.DoubleStep;
import blog.art.chess.andante.move.EnPassant;
import blog.art.chess.andante.move.LongCastling;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.Promotion;
import blog.art.chess.andante.move.PromotionCapture;
import blog.art.chess.andante.move.QuietMove;
import blog.art.chess.andante.move.ShortCastling;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Section;
import blog.art.chess.andante.position.Square;
import java.util.List;

public interface MoveFactory {

  default void newQuietMove(Square origin, Square target, List<Move> moves) {
    if (moves != null) {
      moves.add(new QuietMove(origin, target));
    }
  }

  default void createCapture(Board board, Square origin, Square target, List<Move> moves) {
    if (moves != null) {
      moves.add(new Capture(origin, target));
    }
  }

  default void newLongCastling(Square origin, Square target, Square origin2, Square target2,
      List<Move> moves) {
    if (moves != null) {
      moves.add(new LongCastling(origin, target, origin2, target2));
    }
  }

  default void newShortCastling(Square origin, Square target, Square origin2, Square target2,
      List<Move> moves) {
    if (moves != null) {
      moves.add(new ShortCastling(origin, target, origin2, target2));
    }
  }

  default void newDoubleStep(Square origin, Square target, Square stop, List<Move> moves) {
    if (moves != null) {
      moves.add(new DoubleStep(origin, target, stop));
    }
  }

  default void createEnPassant(Board board, Square origin, Square target, Square stop,
      List<Move> moves) {
    if (moves != null) {
      moves.add(new EnPassant(origin, target, stop));
    }
  }

  default void newPromotion(Square origin, Square target, Section section, List<Move> moves) {
    if (moves != null) {
      moves.add(new Promotion(origin, target, section));
    }
  }

  default void createPromotionCapture(Board board, Square origin, Square target, Section section,
      List<Move> moves) {
    if (moves != null) {
      moves.add(new PromotionCapture(origin, target, section));
    }
  }
}
