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

import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Position {

  private final Board board;
  private final Box box;
  private final Table table;
  private Colour sideToMove;
  private State state;
  private final Memory memory;

  public Position(Board board, Box box, Table table, Colour sideToMove, State state,
      Memory memory) {
    this.board = board;
    this.box = box;
    this.table = table;
    this.sideToMove = sideToMove;
    this.state = state;
    this.memory = memory;
  }

  public Board getBoard() {
    return board;
  }

  public Box getBox() {
    return box;
  }

  public Table getTable() {
    return table;
  }

  public void toggleSideToMove() {
    if (sideToMove == Colour.WHITE) {
      sideToMove = Colour.BLACK;
    } else if (sideToMove == Colour.BLACK) {
      sideToMove = Colour.WHITE;
    }
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public Memory getMemory() {
    return memory;
  }

  public boolean isLegal(List<Move> pseudoLegalMoves) {
    return board.getOrigins().stream().noneMatch(origin -> {
      Piece piece = board.get(origin);
      return piece.getColour() == sideToMove && !piece.generateMoves(board, box, state, origin,
          pseudoLegalMoves);
    });
  }

  public int isCheck() {
    memory.push(new State(state));
    state.resetEnPassant();
    toggleSideToMove();
    int nChecks = board.getOrigins().stream().filter(origin -> {
      Piece piece = board.get(origin);
      return piece.getColour() == sideToMove && !piece.generateMoves(board, box, state, origin,
          null);
    }).map(result -> 1).reduce(0, Integer::sum);
    toggleSideToMove();
    state = memory.pop();
    return nChecks;
  }

  public boolean isTerminal(List<Move> generatedPseudoLegalMoves) {
    List<Move> pseudoLegalMoves =
        generatedPseudoLegalMoves != null ? generatedPseudoLegalMoves : new ArrayList<>();
    if (generatedPseudoLegalMoves == null) {
      board.getOrigins().forEach(origin -> {
        Piece piece = board.get(origin);
        if (piece.getColour() == sideToMove) {
          piece.generateMoves(board, box, state, origin, pseudoLegalMoves);
        }
      });
    }
    return pseudoLegalMoves.stream().noneMatch(move -> {
      boolean result = move.make(this, null, null, null);
      move.unmake(this);
      return result;
    });
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Position.class.getSimpleName() + "[", "]").add("board=" + board)
        .add("box=" + box).add("table=" + table).add("sideToMove=" + sideToMove)
        .add("state=" + state).add("memory=" + memory).toString();
  }
}
