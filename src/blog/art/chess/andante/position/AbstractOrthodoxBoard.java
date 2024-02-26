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

public abstract class AbstractOrthodoxBoard extends AbstractBoard {

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
  public boolean isRebirthSquare(Square square, Class<? extends Piece> pieceType, Colour colour) {
    return switch (colour) {
      case WHITE ->
          pieceType.equals(King.class) ? square.rank() == Rank.FIRST && square.file() == File.KING
              : pieceType.equals(Queen.class) ? square.rank() == Rank.FIRST
                  && square.file() == File.QUEEN
                  : pieceType.equals(Rook.class) ? square.rank() == Rank.FIRST && (
                      square.file() == File.QUEEN_ROOK || square.file() == File.KING_ROOK)
                      : pieceType.equals(Bishop.class) ? square.rank() == Rank.FIRST && (
                          square.file() == File.QUEEN_BISHOP || square.file() == File.KING_BISHOP)
                          : pieceType.equals(Knight.class) ? square.rank() == Rank.FIRST && (
                              square.file() == File.QUEEN_KNIGHT
                                  || square.file() == File.KING_KNIGHT)
                              : pieceType.equals(Pawn.class) ? square.rank() == Rank.FIRST + 1
                                  : square.rank() == Rank.LAST;
      case BLACK ->
          pieceType.equals(King.class) ? square.rank() == Rank.LAST && square.file() == File.KING
              : pieceType.equals(Queen.class) ? square.rank() == Rank.LAST
                  && square.file() == File.QUEEN
                  : pieceType.equals(Rook.class) ? square.rank() == Rank.LAST && (
                      square.file() == File.QUEEN_ROOK || square.file() == File.KING_ROOK)
                      : pieceType.equals(Bishop.class) ? square.rank() == Rank.LAST && (
                          square.file() == File.QUEEN_BISHOP || square.file() == File.KING_BISHOP)
                          : pieceType.equals(Knight.class) ? square.rank() == Rank.LAST && (
                              square.file() == File.QUEEN_KNIGHT
                                  || square.file() == File.KING_KNIGHT)
                              : pieceType.equals(Pawn.class) ? square.rank() == Rank.LAST - 1
                                  : square.rank() == Rank.FIRST;
    };
  }

  @Override
  public String toCode(Square square) {
    return "" + (char) ('a' + square.file() - 1) + (char) ('1' + square.rank() - 1);
  }
}
