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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringJoiner;
import java.util.TreeMap;

public class DefaultBox implements Box {

  private final Map<Section, Stack<Piece>> pieces = new TreeMap<>();

  @Override
  public Piece peek(Section section) {
    return pieces.get(section).peek();
  }

  @Override
  public Piece pop(Section section) {
    return pieces.get(section).pop();
  }

  @Override
  public void push(Section section, Piece piece) {
    pieces.computeIfAbsent(section, s -> new Stack<>()).push(piece);
  }

  @Override
  public void push(Entry entry) {
    push(new DefaultSection(entry.colour(), entry.order()), entry.piece());
  }

  @Override
  public List<Section> findSections(Colour colour) {
    List<Section> sections = new ArrayList<>();
    for (Section section : pieces.keySet()) {
      if (section.colour() == colour) {
        sections.add(section);
      }
    }
    return sections;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DefaultBox.class.getSimpleName() + "[", "]").add(
        "pieces=" + pieces).toString();
  }
}
