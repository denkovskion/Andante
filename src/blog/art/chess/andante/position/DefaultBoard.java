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

public class DefaultBoard implements Board {

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

  @Override
  public Piece get(Square square) {
    return pieces.get(square);
  }

  @Override
  public void put(Square square, Piece piece) {
    pieces.put(square, piece);
  }

  @Override
  public void put(Entry entry) {
    if (entry.file() < File.FIRST || entry.file() > File.LAST || entry.rank() < Rank.FIRST
        || entry.rank() > Rank.LAST) {
      throw new IllegalArgumentException(entry.toString());
    }
    put(new DefaultSquare(entry.file(), entry.rank()), entry.piece());
  }

  @Override
  public Piece remove(Square square) {
    return pieces.remove(square);
  }

  @Override
  public List<Square> getOrigins() {
    return List.copyOf(pieces.keySet());
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
      throw new IllegalArgumentException(new DefaultSquare(file, rank).toString());
    }
    return new DefaultSquare(file, rank);
  }

  @Override
  public Direction getDirection(int fileOffset, int rankOffset) {
    return new DefaultDirection(fileOffset, rankOffset);
  }

  private static final Map<Set<Direction>, List<Direction>> directions = new HashMap<>();

  @Override
  public List<Direction> getDirections(int baseFileOffset, int baseRankOffset) {
    return directions.computeIfAbsent(Set.of(new DefaultDirection(baseFileOffset, baseRankOffset)),
        this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int base1FileOffset, int base1RankOffset,
      int base2FileOffset, int base2RankOffset) {
    return directions.computeIfAbsent(Set.of(new DefaultDirection(base1FileOffset, base1RankOffset),
        new DefaultDirection(base2FileOffset, base2RankOffset)), this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int... baseOffsets) {
    return directions.computeIfAbsent(IntStream.range(0, baseOffsets.length / 2).mapToObj(
            halfNo -> new DefaultDirection(baseOffsets[halfNo * 2], baseOffsets[halfNo * 2 + 1]))
        .collect(Collectors.toUnmodifiableSet()), this::computeDirections);
  }

  private List<Direction> computeDirections(Set<Direction> bases) {
    return bases.stream().flatMap(
            direction -> Stream.of(-direction.fileOffset(), direction.fileOffset()).flatMap(
                fileOffset -> Stream.of(-direction.rankOffset(), direction.rankOffset()).flatMap(
                    rankOffset -> Stream.of(new DefaultDirection(fileOffset, rankOffset),
                        new DefaultDirection(rankOffset, fileOffset))))).distinct().sorted()
        .map(direction -> (Direction) direction).toList();
  }

  @Override
  public boolean isRebirthSquare(Square square, Class<? extends Piece> pieceType, Colour colour) {
    return ((pieceType.equals(King.class) || pieceType.equals(Queen.class) || pieceType.equals(
        Rook.class) || pieceType.equals(Bishop.class) || pieceType.equals(Knight.class)) && (
        colour == Colour.WHITE && square.rank() == Rank.FIRST
            || colour == Colour.BLACK && square.rank() == Rank.LAST)
        || pieceType.equals(Pawn.class) && (
        colour == Colour.WHITE && square.rank() == Rank.FIRST + 1
            || colour == Colour.BLACK && square.rank() == Rank.LAST - 1) ||
        !(pieceType.equals(King.class) || pieceType.equals(Queen.class) || pieceType.equals(
            Rook.class) || pieceType.equals(Bishop.class) || pieceType.equals(Knight.class)
            || pieceType.equals(Pawn.class)) && (
            colour == Colour.WHITE && square.rank() == Rank.LAST
                || colour == Colour.BLACK && square.rank() == Rank.FIRST)) && (
        pieceType.equals(King.class) && square.file() == File.KING
            || pieceType.equals(Queen.class) && square.file() == File.QUEEN
            || pieceType.equals(Rook.class) && (square.file() == File.QUEEN_ROOK
            || square.file() == File.KING_ROOK) || pieceType.equals(Bishop.class) && (
            square.file() == File.QUEEN_BISHOP || square.file() == File.KING_BISHOP)
            || pieceType.equals(Knight.class) && (square.file() == File.QUEEN_KNIGHT
            || square.file() == File.KING_KNIGHT) || !(pieceType.equals(King.class)
            || pieceType.equals(Queen.class) || pieceType.equals(Rook.class) || pieceType.equals(
            Bishop.class) || pieceType.equals(Knight.class)));
  }

  @Override
  public String toCode(Square square) {
    return "" + (char) ('a' + square.file() - 1) + (char) ('1' + square.rank() - 1);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DefaultBoard.class.getSimpleName() + "[", "]").add(
        "pieces=" + pieces).toString();
  }
}
