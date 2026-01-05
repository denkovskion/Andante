/*
 * MIT License
 *
 * Copyright (c) 2024-2026 Ivan Denkovski
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

  private final Set<Square> castlingOrigins = new TreeSet<>();
  private Square enPassantTarget;

  public DefaultState() {
  }

  private DefaultState(DefaultState state) {
    this.castlingOrigins.addAll(state.castlingOrigins);
    this.enPassantTarget = state.enPassantTarget;
  }

  @Override
  public State copy() {
    return new DefaultState(this);
  }

  @Override
  public boolean isCastlingOrigin(Square square) {
    return castlingOrigins.contains(square);
  }

  @Override
  public void addCastlingOrigin(Square square) {
    castlingOrigins.add(square);
  }

  @Override
  public void removeCastlingOrigin(Square square) {
    castlingOrigins.remove(square);
  }

  @Override
  public boolean isEnPassantTarget(Square square) {
    return square.equals(enPassantTarget);
  }

  @Override
  public void setEnPassantTarget(Square square) {
    this.enPassantTarget = square;
  }

  @Override
  public void resetEnPassantTarget() {
    this.enPassantTarget = null;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DefaultState.class.getSimpleName() + "[", "]").add(
        "castlingOrigins=" + castlingOrigins).add("enPassantTarget=" + enPassantTarget).toString();
  }
}
