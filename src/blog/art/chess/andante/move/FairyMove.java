/*
 * MIT License
 *
 * Copyright (c) 2025-2026 Ivan Denkovski
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
import java.util.Locale;
import java.util.StringJoiner;

public abstract class FairyMove extends NullMove {

  protected final NullMove baseMove;

  protected FairyMove(NullMove baseMove) {
    this.baseMove = baseMove;
  }

  @Override
  protected boolean preMake(Position position) {
    return baseMove.preMake(position);
  }

  @Override
  protected void preWrite(Position position, StringBuilder lanBuilder, Locale locale) {
    baseMove.preWrite(position, lanBuilder, locale);
  }

  @Override
  protected void updatePieces(Position position) {
    baseMove.updatePieces(position);
  }

  @Override
  protected void revertPieces(Position position) {
    baseMove.revertPieces(position);
  }

  @Override
  protected void updateCastlings(Position position) {
    baseMove.updateCastlings(position);
  }

  @Override
  protected void updateEnPassant(Position position) {
    baseMove.updateEnPassant(position);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", FairyMove.class.getSimpleName() + "[", "]").add(
        "baseMove=" + baseMove).toString();
  }
}
