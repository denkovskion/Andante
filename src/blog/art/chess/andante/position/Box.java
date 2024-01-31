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

import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.TreeMap;

public class Box {

  private final Map<Section, Stack<Piece>> pieces = new TreeMap<>();

  public Box() {
  }

  public Piece peek(Section section) {
    return pieces.get(section).peek();
  }

  public Piece pop(Section section) {
    return pieces.get(section).pop();
  }

  public void push(Section section, Piece piece) {
    pieces.computeIfAbsent(section, s -> new Stack<>()).push(piece);
  }

  public record Entry(Colour colour, int order, Piece piece) {

  }

  public void push(Entry entry) {
    push(new Section(entry.colour(), entry.order()), entry.piece());
  }

  public List<Section> getSections(Colour colour) {
    return pieces.keySet().stream().filter(entry -> entry.colour() == colour).toList();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Box.class.getSimpleName() + "[", "]").add("pieces=" + pieces)
        .toString();
  }
}
