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
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public abstract class Move {

  public final boolean make(Position position, List<Move> pseudoLegalMoves,
      StringBuilder lanBuilder, Locale locale) {
    if (lanBuilder != null) {
      preWrite(position, lanBuilder, locale);
    }
    boolean result = preMake(position);
    updatePieces(position);
    updateState(position);
    result = position.isLegal(pseudoLegalMoves) && result;
    if (lanBuilder != null) {
      postWrite(position, pseudoLegalMoves, lanBuilder);
    }
    return result;
  }

  public final void unmake(Position position) {
    revertState(position);
    revertPieces(position);
  }

  public abstract void preWrite(Position position, StringBuilder lanBuilder, Locale locale);

  public abstract void postWrite(Position position, List<Move> generatedPseudoLegalMoves,
      StringBuilder lanBuilder);

  protected abstract boolean preMake(Position position);

  protected abstract void updatePieces(Position position);

  protected abstract void revertPieces(Position position);

  protected abstract void updateState(Position position);

  protected abstract void revertState(Position position);

  @Override
  public String toString() {
    return new StringJoiner(", ", Move.class.getSimpleName() + "[", "]").toString();
  }
}
