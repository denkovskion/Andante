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

import blog.art.chess.andante.piece.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MailboxBoard extends StandardBoard {

  private final List<Piece> pieces = new ArrayList<>(Collections.nCopies(120, null));

  private static final List<Square> squares = IntStream.range(0, 120).mapToObj(number -> {
    Square square = new MailboxSquare(number);
    if (square.file() < File.FIRST || square.file() > File.LAST || square.rank() < Rank.FIRST
        || square.rank() > Rank.LAST) {
      return null;
    }
    return square;
  }).collect(Collectors.toCollection(ArrayList::new));

  private static final int[] numbers = IntStream.range(0, 120).filter(number -> {
    Square square = new MailboxSquare(number);
    return !(square.file() < File.FIRST || square.file() > File.LAST || square.rank() < Rank.FIRST
        || square.rank() > Rank.LAST);
  }).toArray();

  @Override
  public Piece get(Square square) {
    return pieces.get(square.number());
  }

  @Override
  public void put(Square square, Piece piece) {
    pieces.set(square.number(), piece);
  }

  @Override
  public Piece remove(Square square) {
    return pieces.set(square.number(), null);
  }

  @Override
  public Square getSquare(int file, int rank) {
    if (file < File.FIRST || file > File.LAST || rank < Rank.FIRST || rank > Rank.LAST) {
      throw new IndexOutOfBoundsException();
    }
    return new MailboxSquare(file, rank);
  }

  @Override
  public List<Square> findOrigins() {
    List<Square> origins = new ArrayList<>();
    for (int number : numbers) {
      if (pieces.get(number) != null) {
        origins.add(squares.get(number));
      }
    }
    return origins;
  }

  @Override
  public Square findTarget(Square origin, Direction direction, int distance) {
    return squares.get(origin.number() + direction.offset() * distance);
  }

  @Override
  public Direction getDirection(int fileOffset, int rankOffset) {
    return new MailboxDirection(fileOffset, rankOffset);
  }

  private static final Map<Set<Direction>, List<Direction>> mailboxDirections = new HashMap<>();

  @Override
  protected Map<Set<Direction>, List<Direction>> getAllDirections() {
    return mailboxDirections;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MailboxBoard.class.getSimpleName() + "[", "]").add(
        "pieces=" + pieces).toString();
  }
}
