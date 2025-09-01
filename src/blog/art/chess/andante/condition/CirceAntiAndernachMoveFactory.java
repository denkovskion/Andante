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

import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.position.Board;
import blog.art.chess.andante.position.Box;
import blog.art.chess.andante.position.Section;
import blog.art.chess.andante.position.Square;
import java.util.List;
import java.util.StringJoiner;

public class CirceAntiAndernachMoveFactory implements Circe, AntiAndernach {

  @Override
  public void newQuietMove(Board board, Square origin, Square target, List<Move> moves) {
    AntiAndernach.super.newQuietMove(board, origin, target, moves);
  }

  @Override
  public boolean createCapture(Board board, Square origin, Square target, List<Move> moves) {
    return Circe.super.createCapture(board, origin, target, moves);
  }

  @Override
  public void newDoubleStep(Board board, Square origin, Square target, Square stop,
      List<Move> moves) {
    AntiAndernach.super.newDoubleStep(board, origin, target, stop, moves);
  }

  @Override
  public boolean createEnPassant(Board board, Square origin, Square target, Square stop,
      List<Move> moves) {
    return Circe.super.createEnPassant(board, origin, target, stop, moves);
  }

  @Override
  public void newPromotion(Board board, Box box, Square origin, Square target, Section section,
      List<Move> moves) {
    AntiAndernach.super.newPromotion(board, box, origin, target, section, moves);
  }

  @Override
  public boolean createPromotionCapture(Board board, Box box, Square origin, Square target,
      Section section, List<Move> moves) {
    return Circe.super.createPromotionCapture(board, box, origin, target, section, moves);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", CirceAntiAndernachMoveFactory.class.getSimpleName() + "[",
        "]").toString();
  }
}
