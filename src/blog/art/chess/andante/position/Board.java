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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Board {

  private static class File {

    static final int FIRST = 1;
    static final int LAST = 8;
    static final int QUEEN_ROOK = 1;
    static final int QUEEN_KNIGHT = 2;
    static final int QUEEN_BISHOP = 3;
    static final int QUEEN = 4;
    static final int KING = 5;
    static final int KING_BISHOP = 6;
    static final int KING_KNIGHT = 7;
    static final int KING_ROOK = 8;
  }

  private static class Rank {

    static final int FIRST = 1;
    static final int LAST = 8;
  }

  private final Map<Square, Piece> pieces = new TreeMap<>();

  public Board() {
  }

  public Piece get(Square square) {
    return pieces.get(square);
  }

  public void put(Square square, Piece piece) {
    pieces.put(square, piece);
  }

  public record Entry(int file, int rank, Piece piece) {

  }

  public void put(Entry entry) {
    if (entry.file() < File.FIRST || entry.file() > File.LAST || entry.rank() < Rank.FIRST
        || entry.rank() > Rank.LAST) {
      throw new IllegalArgumentException(entry.toString());
    }
    put(new Square(entry.file(), entry.rank()), entry.piece());
  }

  public Piece remove(Square square) {
    return pieces.remove(square);
  }

  public List<Square> getOrigins() {
    return List.copyOf(pieces.keySet());
  }

  public Square findTarget(Square origin, Direction direction, int distance) {
    Square target = new Square(origin.file() + direction.fileOffset() * distance,
        origin.rank() + direction.rankOffset() * distance);
    if (target.file() < File.FIRST || target.file() > File.LAST || target.rank() < Rank.FIRST
        || target.rank() > Rank.LAST) {
      return null;
    }
    return target;
  }

  public Square getSquare(int file, int rank) {
    if (file < File.FIRST || file > File.LAST || rank < Rank.FIRST || rank > Rank.LAST) {
      throw new IllegalArgumentException(new Square(file, rank).toString());
    }
    return new Square(file, rank);
  }

  public Direction getDirection(int fileOffset, int rankOffset) {
    return new Direction(fileOffset, rankOffset);
  }

  private static final Map<Set<Direction>, List<Direction>> directions = new HashMap<>();

  public List<Direction> getDirections(int baseFileOffset, int baseRankOffset) {
    return directions.computeIfAbsent(Set.of(new Direction(baseFileOffset, baseRankOffset)),
        this::computeDirections);
  }

  public List<Direction> getDirections(int base1FileOffset, int base1RankOffset,
      int base2FileOffset, int base2RankOffset) {
    return directions.computeIfAbsent(Set.of(new Direction(base1FileOffset, base1RankOffset),
        new Direction(base2FileOffset, base2RankOffset)), this::computeDirections);
  }

  public List<Direction> getDirections(int... baseOffsets) {
    return directions.computeIfAbsent(IntStream.range(0, baseOffsets.length / 2)
        .mapToObj(halfNo -> new Direction(baseOffsets[halfNo * 2], baseOffsets[halfNo * 2 + 1]))
        .collect(Collectors.toUnmodifiableSet()), this::computeDirections);
  }

  private List<Direction> computeDirections(Set<Direction> bases) {
    return bases.stream().flatMap(
        direction -> Stream.of(-direction.fileOffset(), direction.fileOffset()).flatMap(
            fileOffset -> Stream.of(-direction.rankOffset(), direction.rankOffset()).flatMap(
                rankOffset -> Stream.of(new Direction(fileOffset, rankOffset),
                    new Direction(rankOffset, fileOffset))))).distinct().sorted().toList();
  }

  public boolean isRebirthSquare(Square square, Class<? extends Piece> pieceType, Colour colour) {
    return ((pieceType.equals(King.class) || pieceType.equals(Queen.class) || pieceType.equals(
        Rook.class) || pieceType.equals(Bishop.class) || pieceType.equals(Knight.class)) && (
        colour == Colour.WHITE && square.rank() == Rank.FIRST
            || colour == Colour.BLACK && square.rank() == Rank.LAST)
        || pieceType.equals(Pawn.class) && (
        colour == Colour.WHITE && square.rank() == Rank.FIRST + 1
            || colour == Colour.BLACK && square.rank() == Rank.LAST - 1)
        || pieceType.equals(Piece.class) && (colour == Colour.WHITE && square.rank() == Rank.LAST
        || colour == Colour.BLACK && square.rank() == Rank.FIRST)) && (
        pieceType.equals(King.class) && square.file() == File.KING
            || pieceType.equals(Queen.class) && square.file() == File.QUEEN
            || pieceType.equals(Rook.class) && (square.file() == File.QUEEN_ROOK
            || square.file() == File.KING_ROOK) || pieceType.equals(Bishop.class) && (
            square.file() == File.QUEEN_BISHOP || square.file() == File.KING_BISHOP)
            || pieceType.equals(Knight.class) && (square.file() == File.QUEEN_KNIGHT
            || square.file() == File.KING_KNIGHT) || pieceType.equals(Pawn.class)
            || pieceType.equals(Piece.class));
  }

  public String toCode(Square square) {
    return "" + (char) ('a' + square.file() - 1) + (char) ('1' + square.rank() - 1);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Board.class.getSimpleName() + "[", "]").add("pieces=" + pieces)
        .toString();
  }
}
