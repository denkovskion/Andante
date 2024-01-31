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

import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.State;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class NullMove extends Move {

  @Override
  protected boolean preMake(Position position, StringBuilder lanBuilder, Locale locale) {
    return true;
  }

  @Override
  public void preWrite(Position position, StringBuilder lanBuilder, Locale locale) {
    lanBuilder.append((String) null);
  }

  @Override
  public void postWrite(Position position, List<Move> generatedPseudoLegalMoves,
      StringBuilder lanBuilder) {
  }

  @Override
  protected void updatePieces(Position position) {
  }

  @Override
  protected void revertPieces(Position position) {
  }

  @Override
  protected void updateState(Position position) {
    position.getMemory().push(new State(position.getState()));
    position.getState().resetEnPassant();
    position.toggleSideToMove();
  }

  @Override
  protected void revertState(Position position) {
    position.toggleSideToMove();
    position.setState(position.getMemory().pop());
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", NullMove.class.getSimpleName() + "[", "]").toString();
  }
}
