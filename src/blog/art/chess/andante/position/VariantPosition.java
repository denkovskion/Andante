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

import blog.art.chess.andante.condition.Condition;
import blog.art.chess.andante.move.Action;
import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.VariantMove;
import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

public class VariantPosition extends Position {

  private final SortedSet<Condition> conditions;

  public VariantPosition(Board board, Box box, Table table, Colour sideToMove, State state,
      Memory memory, Condition... conditions) {
    super(board, box, table, sideToMove, state, memory);
    this.conditions = new TreeSet<>(List.of(conditions));
  }

  @Override
  public boolean isLegal(List<Move> pseudoLegalMoves) {
    for (Square origin : board.getOrigins()) {
      Piece piece = board.get(origin);
      if (piece.getColour() == sideToMove) {
        if (!piece.generateMoves(board, box, state, origin, pseudoLegalMoves)) {
          return false;
        }
      }
    }
    if (pseudoLegalMoves != null) {
      replaceMoves(pseudoLegalMoves);
    }
    return true;
  }

  @Override
  public boolean isTerminal(List<Move> generatedPseudoLegalMoves) {
    List<Move> pseudoLegalMoves =
        generatedPseudoLegalMoves != null ? generatedPseudoLegalMoves : new ArrayList<>();
    if (generatedPseudoLegalMoves == null) {
      for (Square origin : board.getOrigins()) {
        Piece piece = board.get(origin);
        if (piece.getColour() == sideToMove) {
          piece.generateMoves(board, box, state, origin, pseudoLegalMoves);
        }
      }
      replaceMoves(pseudoLegalMoves);
    }
    for (Move move : pseudoLegalMoves) {
      boolean result = move.make(this, null, null, null);
      move.unmake(this);
      if (result) {
        return false;
      }
    }
    return true;
  }

  private void replaceMoves(List<Move> moves) {
    for (ListIterator<Move> iMove = moves.listIterator(); iMove.hasNext(); ) {
      Move move = iMove.next();
      List<Action> actions = new ArrayList<>();
      for (Condition condition : conditions) {
        condition.generateAction(board, move, actions);
      }
      if (!actions.isEmpty()) {
        iMove.set(new VariantMove(move, actions));
      }
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", VariantPosition.class.getSimpleName() + "[", "]").add(
            "board=" + board).add("box=" + box).add("table=" + table).add("sideToMove=" + sideToMove)
        .add("state=" + state).add("memory=" + memory).add("conditions=" + conditions).toString();
  }
}
