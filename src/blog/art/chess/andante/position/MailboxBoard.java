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
import blog.art.chess.andante.piece.orthodox.Bishop;
import blog.art.chess.andante.piece.orthodox.King;
import blog.art.chess.andante.piece.orthodox.Knight;
import blog.art.chess.andante.piece.orthodox.Pawn;
import blog.art.chess.andante.piece.orthodox.Queen;
import blog.art.chess.andante.piece.orthodox.Rook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MailboxBoard implements Board {

  private final List<Piece> pieces = Stream.generate(() -> (Piece) null).limit(120)
      .collect(Collectors.toCollection(ArrayList::new));

  private static final List<Square> squares = IntStream.range(0, 120).mapToObj(
      number -> number < 20 || number > 100 || number % 10 == 0 || number % 10 == 9 ? null
          : new Square(0, number)).collect(Collectors.toCollection(ArrayList::new));

  @Override
  public Piece get(Square square) {
    return pieces.get(square.rank());
  }

  @Override
  public void put(Square square, Piece piece) {
    pieces.set(square.rank(), piece);
  }

  @Override
  public void put(Entry entry) {
    if (entry.file() < 1 || entry.file() > 8 || entry.rank() < 1 || entry.rank() > 8) {
      throw new IllegalArgumentException(entry.toString());
    }
    put(new Square(0, 10 * (entry.file() + 1) + entry.rank()), entry.piece());
  }

  @Override
  public Piece remove(Square square) {
    return pieces.set(square.rank(), null);
  }

  @Override
  public List<Square> getOrigins() {
    return IntStream.range(0, 120).filter(number -> pieces.get(number) != null)
        .mapToObj(squares::get).toList();
  }

  @Override
  public Square findTarget(Square origin, Direction direction, int distance) {
    return squares.get(origin.rank() + direction.rankOffset() * distance);
  }

  @Override
  public Square getSquare(int file, int rank) {
    if (file < 1 || file > 8 || rank < 1 || rank > 8) {
      throw new IllegalArgumentException(new Square(file, rank).toString());
    }
    return new Square(0, 10 * (file + 1) + rank);
  }

  @Override
  public Direction getDirection(int fileOffset, int rankOffset) {
    return new Direction(0, 10 * fileOffset + rankOffset);
  }

  private static final Map<Set<Direction>, List<Direction>> directions = new HashMap<>();

  @Override
  public List<Direction> getDirections(int baseFileOffset, int baseRankOffset) {
    return directions.computeIfAbsent(Set.of(new Direction(baseFileOffset, baseRankOffset)),
        this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int base1FileOffset, int base1RankOffset,
      int base2FileOffset, int base2RankOffset) {
    return directions.computeIfAbsent(Set.of(new Direction(base1FileOffset, base1RankOffset),
        new Direction(base2FileOffset, base2RankOffset)), this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int... baseOffsets) {
    return directions.computeIfAbsent(IntStream.range(0, baseOffsets.length / 2)
        .mapToObj(halfNo -> new Direction(baseOffsets[halfNo * 2], baseOffsets[halfNo * 2 + 1]))
        .collect(Collectors.toUnmodifiableSet()), this::computeDirections);
  }

  private List<Direction> computeDirections(Set<Direction> bases) {
    return bases.stream().flatMap(
        direction -> Stream.of(-direction.fileOffset(), direction.fileOffset()).flatMap(
            fileOffset -> Stream.of(-direction.rankOffset(), direction.rankOffset()).flatMap(
                rankOffset -> Stream.of(new Direction(0, 10 * fileOffset + rankOffset),
                    new Direction(0, 10 * rankOffset + fileOffset))))).distinct().sorted().toList();
  }

  @Override
  public boolean isRebirthSquare(Square square, Class<? extends Piece> pieceType, Colour colour) {
    int file = square.rank() / 10 - 1;
    int rank = square.rank() % 10;
    boolean officerType =
        pieceType.equals(King.class) || pieceType.equals(Queen.class) || pieceType.equals(
            Rook.class) || pieceType.equals(Bishop.class) || pieceType.equals(Knight.class);
    return switch (colour) {
      case WHITE -> officerType ? rank == 1 : pieceType.equals(Pawn.class) ? rank == 2 : rank == 8;
      case BLACK -> officerType ? rank == 8 : pieceType.equals(Pawn.class) ? rank == 7 : rank == 1;
    } && (pieceType.equals(King.class) ? file == 5 : pieceType.equals(Queen.class) ? file == 4
        : pieceType.equals(Rook.class) ? file == 1 || file == 8
            : pieceType.equals(Bishop.class) ? file == 3 || file == 6
                : pieceType.equals(Knight.class) ? file == 2 || file == 7 : true);
  }

  @Override
  public String toCode(Square square) {
    return "" + (char) ('a' + square.rank() / 10 - 2) + (char) ('1' + square.rank() % 10 - 1);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MailboxBoard.class.getSimpleName() + "[", "]").add(
        "pieces=" + pieces).toString();
  }
}
