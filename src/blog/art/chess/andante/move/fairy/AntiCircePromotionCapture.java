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

package blog.art.chess.andante.move.fairy;

import blog.art.chess.andante.move.Promotion;
import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.Section;
import blog.art.chess.andante.position.Square;
import java.util.Locale;
import java.util.StringJoiner;

public class AntiCircePromotionCapture extends Promotion {

  protected final Square rebirth;

  public AntiCircePromotionCapture(Square origin, Square target, Section section, Square rebirth) {
    super(origin, target, section);
    this.rebirth = rebirth;
  }

  @Override
  protected void preWrite(Position position, StringBuilder lanBuilder, Locale locale) {
    lanBuilder.append(position.getBoard().get(origin).getCode(locale))
        .append(position.getBoard().toCode(origin)).append("x")
        .append(position.getBoard().toCode(target)).append("=")
        .append(position.getBox().peek(section).getCode(locale)).append("(")
        .append(position.getBox().peek(section).getCode(locale))
        .append(position.getBoard().toCode(rebirth)).append(")");
  }

  @Override
  protected void updatePieces(Position position) {
    position.getTable().push(position.getBoard().remove(target));
    position.getTable().push(position.getBoard().remove(origin));
    position.getBoard().put(rebirth, position.getBox().pop(section));
  }

  @Override
  protected void revertPieces(Position position) {
    position.getBox().push(section, position.getBoard().remove(rebirth));
    position.getBoard().put(origin, position.getTable().pop());
    position.getBoard().put(target, position.getTable().pop());
  }

  @Override
  protected void updateCastlings(Position position) {
    position.getState().removeCastling(origin);
    position.getState().removeCastling(target);
    position.getState().removeCastling(rebirth);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AntiCircePromotionCapture.class.getSimpleName() + "[", "]").add(
            "origin=" + origin).add("target=" + target).add("section=" + section)
        .add("rebirth=" + rebirth).toString();
  }
}
