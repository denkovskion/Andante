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

public interface Board {

  Piece get(Square square);

  void put(Square square, Piece piece);

  record Entry(int file, int rank, Piece piece) {

  }

  void put(Entry entry);

  Piece remove(Square square);

  List<Square> findOrigins();

  Square findTarget(Square origin, Direction direction, int distance);

  Square getSquare(int file, int rank);

  Direction getDirection(int fileOffset, int rankOffset);

  List<Direction> getDirections(int baseFileOffset, int baseRankOffset);

  List<Direction> getDirections(int base1FileOffset, int base1RankOffset, int base2FileOffset,
      int base2RankOffset);

  List<Direction> getDirections(int... baseOffsets);

  boolean isRebirthSquare(Square square, Class<? extends Piece> pieceType, Colour colour);

  String toCode(Square square);
}
