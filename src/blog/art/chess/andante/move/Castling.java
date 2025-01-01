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

package blog.art.chess.andante.move;

import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.Square;
import java.util.StringJoiner;

public abstract class Castling extends QuietMove {

  protected final Square origin2;
  protected final Square target2;

  public Castling(Square origin, Square target, Square origin2, Square target2) {
    super(origin, target);
    this.origin2 = origin2;
    this.target2 = target2;
  }

  @Override
  protected boolean preMake(Position position) {
    Move nullMove = new NullMove();
    boolean result = nullMove.make(position, null, null, null);
    nullMove.unmake(position);
    Move stopMove = new QuietMove(origin, target2);
    result = stopMove.make(position, null, null, null) && result;
    stopMove.unmake(position);
    return result;
  }

  @Override
  protected void updatePieces(Position position) {
    position.getBoard().put(target, position.getBoard().remove(origin));
    position.getBoard().put(target2, position.getBoard().remove(origin2));
  }

  @Override
  protected void revertPieces(Position position) {
    position.getBoard().put(origin2, position.getBoard().remove(target2));
    position.getBoard().put(origin, position.getBoard().remove(target));
  }

  @Override
  protected void updateCastlings(Position position) {
    position.getState().removeCastling(origin);
    position.getState().removeCastling(target);
    position.getState().removeCastling(origin2);
    position.getState().removeCastling(target2);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Castling.class.getSimpleName() + "[", "]").add("origin=" + origin)
        .add("target=" + target).add("origin2=" + origin2).add("target2=" + target2).toString();
  }
}
