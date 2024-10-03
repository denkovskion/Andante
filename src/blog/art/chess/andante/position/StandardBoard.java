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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

public abstract class StandardBoard implements Board {

  protected static class File {

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

  protected static class Rank {

    static final int FIRST = 1;
    static final int LAST = 8;
  }

  @Override
  public void put(Entry entry) {
    put(getSquare(entry.file(), entry.rank()), entry.piece());
  }

  protected abstract Map<Set<Direction>, List<Direction>> getAllDirections();

  @Override
  public List<Direction> getDirections(int baseFileOffset, int baseRankOffset) {
    return getAllDirections().computeIfAbsent(Set.of(getDirection(baseFileOffset, baseRankOffset)),
        this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int base1FileOffset, int base1RankOffset,
      int base2FileOffset, int base2RankOffset) {
    return getAllDirections().computeIfAbsent(Set.of(getDirection(base1FileOffset, base1RankOffset),
        getDirection(base2FileOffset, base2RankOffset)), this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int... baseOffsets) {
    List<Direction> bases = new ArrayList<>();
    for (int halfNo = 0; halfNo < baseOffsets.length / 2; halfNo++) {
      bases.add(getDirection(baseOffsets[halfNo * 2], baseOffsets[halfNo * 2 + 1]));
    }
    return getAllDirections().computeIfAbsent(Set.copyOf(bases), this::computeDirections);
  }

  private List<Direction> computeDirections(Set<Direction> bases) {
    SortedSet<Direction> directions = new TreeSet<>();
    for (Direction base : bases) {
      for (int fileOffset : new int[]{-base.fileOffset(), base.fileOffset()}) {
        for (int rankOffset : new int[]{-base.rankOffset(), base.rankOffset()}) {
          directions.add(getDirection(fileOffset, rankOffset));
          directions.add(getDirection(rankOffset, fileOffset));
        }
      }
    }
    return List.copyOf(directions);
  }

  @Override
  public boolean isRebirthSquare(Square square, Class<? extends Piece> pieceType, Colour colour) {
    return switch (colour) {
      case WHITE ->
          pieceType.equals(King.class) ? square.file() == File.KING && square.rank() == Rank.FIRST
              : pieceType.equals(Queen.class) ? square.file() == File.QUEEN
                  && square.rank() == Rank.FIRST : pieceType.equals(Rook.class) ?
                  (square.file() == File.QUEEN_ROOK || square.file() == File.KING_ROOK)
                      && square.rank() == Rank.FIRST : pieceType.equals(Bishop.class) ?
                  (square.file() == File.QUEEN_BISHOP || square.file() == File.KING_BISHOP)
                      && square.rank() == Rank.FIRST : pieceType.equals(Knight.class) ?
                  (square.file() == File.QUEEN_KNIGHT || square.file() == File.KING_KNIGHT)
                      && square.rank() == Rank.FIRST
                  : pieceType.equals(Pawn.class) ? square.rank() == Rank.FIRST + 1
                      : square.rank() == Rank.LAST;
      case BLACK ->
          pieceType.equals(King.class) ? square.file() == File.KING && square.rank() == Rank.LAST
              : pieceType.equals(Queen.class) ? square.file() == File.QUEEN
                  && square.rank() == Rank.LAST : pieceType.equals(Rook.class) ?
                  (square.file() == File.QUEEN_ROOK || square.file() == File.KING_ROOK)
                      && square.rank() == Rank.LAST : pieceType.equals(Bishop.class) ?
                  (square.file() == File.QUEEN_BISHOP || square.file() == File.KING_BISHOP)
                      && square.rank() == Rank.LAST : pieceType.equals(Knight.class) ?
                  (square.file() == File.QUEEN_KNIGHT || square.file() == File.KING_KNIGHT)
                      && square.rank() == Rank.LAST
                  : pieceType.equals(Pawn.class) ? square.rank() == Rank.LAST - 1
                      : square.rank() == Rank.FIRST;
    };
  }

  @Override
  public String toCode(Square square) {
    return "" + (char) ('a' + square.file() - 1) + (char) ('1' + square.rank() - 1);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", StandardBoard.class.getSimpleName() + "[", "]").toString();
  }
}
