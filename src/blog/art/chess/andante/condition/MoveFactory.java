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

public interface MoveFactory {

  default Move newQuietMove(Square origin, Square target) {
    return new QuietMove(origin, target);
  }

  default Move createCapture(Board board, Square origin, Square target) {
    return new Capture(origin, target);
  }

  default Move newLongCastling(Square origin, Square target, Square origin2, Square target2) {
    return new LongCastling(origin, target, origin2, target2);
  }

  default Move newShortCastling(Square origin, Square target, Square origin2, Square target2) {
    return new ShortCastling(origin, target, origin2, target2);
  }

  default Move newDoubleStep(Square origin, Square target, Square stop) {
    return new DoubleStep(origin, target, stop);
  }

  default Move createEnPassant(Board board, Square origin, Square target, Square stop) {
    return new EnPassant(origin, target, stop);
  }

  default Move newPromotion(Square origin, Square target, Section section) {
    return new Promotion(origin, target, section);
  }

  default Move createPromotionCapture(Board board, Square origin, Square target, Section section) {
    return new PromotionCapture(origin, target, section);
  }
}
