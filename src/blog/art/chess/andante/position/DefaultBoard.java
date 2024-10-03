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

import blog.art.chess.andante.piece.Piece;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;

public class DefaultBoard extends StandardBoard {

  private final Map<Square, Piece> pieces = new TreeMap<>();

  @Override
  public Piece get(Square square) {
    return pieces.get(square);
  }

  @Override
  public void put(Square square, Piece piece) {
    pieces.put(square, piece);
  }

  @Override
  public Piece remove(Square square) {
    return pieces.remove(square);
  }

  @Override
  public List<Square> findOrigins() {
    return new ArrayList<>(pieces.keySet());
  }

  @Override
  public Square findTarget(Square origin, Direction direction, int distance) {
    Square target = new DefaultSquare(origin.file() + direction.fileOffset() * distance,
        origin.rank() + direction.rankOffset() * distance);
    if (target.file() < File.FIRST || target.file() > File.LAST || target.rank() < Rank.FIRST
        || target.rank() > Rank.LAST) {
      return null;
    }
    return target;
  }

  @Override
  public Square getSquare(int file, int rank) {
    if (file < File.FIRST || file > File.LAST || rank < Rank.FIRST || rank > Rank.LAST) {
      throw new IndexOutOfBoundsException();
    }
    return new DefaultSquare(file, rank);
  }

  @Override
  public Direction getDirection(int fileOffset, int rankOffset) {
    return new DefaultDirection(fileOffset, rankOffset);
  }

  private static final Map<Set<Direction>, List<Direction>> defaultDirections = new HashMap<>();

  @Override
  protected Map<Set<Direction>, List<Direction>> getAllDirections() {
    return defaultDirections;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DefaultBoard.class.getSimpleName() + "[", "]").add(
        "pieces=" + pieces).toString();
  }
}
