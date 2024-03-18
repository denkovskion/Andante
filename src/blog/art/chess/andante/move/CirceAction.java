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

package blog.art.chess.andante.move;

import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.orthodox.Rook;
import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.Square;
import java.util.Locale;
import java.util.StringJoiner;

public class CirceAction implements Action {

  private final Square capture;
  private final Square rebirth;

  public CirceAction(Square capture, Square rebirth) {
    this.capture = capture;
    this.rebirth = rebirth;
  }

  @Override
  public void preWrite(Position position, StringBuilder lanBuilder, Locale locale) {
    lanBuilder.append("(").append(position.getBoard().get(capture).getCode(locale))
        .append(position.getBoard().toCode(rebirth)).append(")");
  }

  @Override
  public void updatePieces(Position position) {
    position.getBoard().put(rebirth, position.getTable().pop());
  }

  @Override
  public void revertPieces(Position position) {
    position.getTable().push(position.getBoard().remove(rebirth));
  }

  @Override
  public void updateState(Position position) {
    if (position.getBoard().isRebirthSquare(rebirth, Rook.class, Colour.WHITE)
        || position.getBoard().isRebirthSquare(rebirth, Rook.class, Colour.BLACK)) {
      position.getState().removeNoCastling(rebirth);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", CirceAction.class.getSimpleName() + "[", "]").add(
        "capture=" + capture).add("rebirth=" + rebirth).toString();
  }
}
