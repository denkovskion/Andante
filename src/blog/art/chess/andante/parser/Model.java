/*
 * MIT License
 *
 * Copyright (c) 2025 Ivan Denkovski
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

package blog.art.chess.andante.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class Model {

  enum Piece {WhiteKing, WhiteQueen, WhiteRook, WhiteBishop, WhiteKnight, WhitePawn, BlackKing, BlackQueen, BlackRook, BlackBishop, BlackKnight, BlackPawn}

  enum Colour {White, Black}

  enum Castling {WhiteShort, WhiteLong, BlackShort, BlackLong}

  record Square(int index) {

  }

  enum Opcode {ACD, DM}

  record Operation(Opcode opcode, int operand) {

  }

  static class Position {

    private final List<Piece> board = Arrays.asList(new Piece[64]);
    private Colour sideToMove;
    private final Set<Castling> castlings = new TreeSet<>();
    private Square enPassant;
    private Operation operation;

    List<Piece> getBoard() {
      return board;
    }

    Colour getSideToMove() {
      return sideToMove;
    }

    void setSideToMove(Colour sideToMove) {
      this.sideToMove = sideToMove;
    }

    Set<Castling> getCastlings() {
      return castlings;
    }

    Square getEnPassant() {
      return enPassant;
    }

    void setEnPassant(Square enPassant) {
      this.enPassant = enPassant;
    }

    Operation getOperation() {
      return operation;
    }

    void setOperation(Operation operation) {
      this.operation = operation;
    }
  }
}
