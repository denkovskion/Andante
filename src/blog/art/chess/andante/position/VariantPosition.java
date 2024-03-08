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

public class VariantPosition extends Position {

  private final Variant variant;

  public VariantPosition(Board board, Box box, Table table, Colour sideToMove, State state,
      Memory memory, Variant variant) {
    super(board, box, table, sideToMove, state, memory);
    this.variant = variant;
  }

  @Override
  protected boolean generatePseudoLegalMoves(Piece piece, Square origin,
      List<Move> pseudoLegalMoves) {
    if (pseudoLegalMoves != null) {
      List<Move> baseMoves = new ArrayList<>();
      boolean legal = piece.generateMoves(board, box, state, origin, baseMoves);
      variant.decorateMoves(board, baseMoves, pseudoLegalMoves);
      return legal;
    } else {
      return piece.generateMoves(board, box, state, origin, null);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", VariantPosition.class.getSimpleName() + "[", "]").add(
            "board=" + board).add("box=" + box).add("table=" + table).add("sideToMove=" + sideToMove)
        .add("state=" + state).add("memory=" + memory).add("variant=" + variant).toString();
  }
}
