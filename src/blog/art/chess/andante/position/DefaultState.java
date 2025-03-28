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

package blog.art.chess.andante.position;

import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

public class DefaultState implements State {

  private final Set<Square> castlings = new TreeSet<>();
  private Square enPassant;

  public DefaultState() {
  }

  private DefaultState(DefaultState state) {
    this.castlings.addAll(state.castlings);
    this.enPassant = state.enPassant;
  }

  @Override
  public State copy() {
    return new DefaultState(this);
  }

  @Override
  public boolean isCastling(Square square) {
    return castlings.contains(square);
  }

  @Override
  public void addCastling(Square square) {
    castlings.add(square);
  }

  @Override
  public void removeCastling(Square square) {
    castlings.remove(square);
  }

  @Override
  public boolean isEnPassant(Square square) {
    return square.equals(enPassant);
  }

  @Override
  public void setEnPassant(Square enPassant) {
    this.enPassant = enPassant;
  }

  @Override
  public void resetEnPassant() {
    this.enPassant = null;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DefaultState.class.getSimpleName() + "[", "]").add(
        "castlings=" + castlings).add("enPassant=" + enPassant).toString();
  }
}
