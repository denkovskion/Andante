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

package blog.art.chess.andante.position;

import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

public class State {

  private final Set<Square> noCastling = new TreeSet<>();
  private Square enPassant;

  public State() {
  }

  public State(State state) {
    this.noCastling.addAll(state.noCastling);
    this.enPassant = state.enPassant;
  }

  public boolean isNoCastling(Square square) {
    return noCastling.contains(square);
  }

  public void addNoCastling(Square square) {
    noCastling.add(square);
  }

  public boolean isEnPassant(Square square) {
    return square.equals(enPassant);
  }

  public void setEnPassant(Square enPassant) {
    this.enPassant = enPassant;
  }

  public void resetEnPassant() {
    this.enPassant = null;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", State.class.getSimpleName() + "[", "]").add(
        "noCastling=" + noCastling).add("enPassant=" + enPassant).toString();
  }
}
