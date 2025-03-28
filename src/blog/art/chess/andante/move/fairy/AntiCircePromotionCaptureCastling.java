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

import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.Section;
import blog.art.chess.andante.position.Square;
import java.util.StringJoiner;

public class AntiCircePromotionCaptureCastling extends AntiCircePromotionCapture {

  public AntiCircePromotionCaptureCastling(Square origin, Square target, Section section,
      Square rebirth) {
    super(origin, target, section, rebirth);
  }

  @Override
  protected void updateCastlings(Position position) {
    position.getState().removeCastling(origin);
    position.getState().removeCastling(target);
    position.getState().addCastling(rebirth);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AntiCircePromotionCaptureCastling.class.getSimpleName() + "[",
        "]").add("origin=" + origin).add("target=" + target).add("section=" + section)
        .add("rebirth=" + rebirth).toString();
  }
}
