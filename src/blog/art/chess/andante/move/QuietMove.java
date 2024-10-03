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

package blog.art.chess.andante.move;

import blog.art.chess.andante.piece.Colour;
import blog.art.chess.andante.piece.orthodox.King;
import blog.art.chess.andante.piece.orthodox.Rook;
import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.Square;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class QuietMove extends NullMove {

  protected final Square origin;
  protected final Square target;

  public QuietMove(Square origin, Square target) {
    this.origin = origin;
    this.target = target;
  }

  @Override
  public void preWrite(Position position, StringBuilder lanBuilder, Locale locale) {
    lanBuilder.append(position.getBoard().get(origin).getCode(locale))
        .append(position.getBoard().toCode(origin)).append("-")
        .append(position.getBoard().toCode(target));
  }

  @Override
  public void postWrite(Position position, List<Move> generatedPseudoLegalMoves,
      StringBuilder lanBuilder) {
    int nChecks = position.isCheck();
    boolean terminal = position.isTerminal(generatedPseudoLegalMoves);
    if (terminal) {
      if (nChecks > 0) {
        if (nChecks > 1) {
          lanBuilder.append("+".repeat(nChecks));
        }
        lanBuilder.append("#");
      } else {
        lanBuilder.append("=");
      }
    } else {
      if (nChecks > 0) {
        lanBuilder.append("+".repeat(nChecks));
      }
    }
  }

  @Override
  protected void updatePieces(Position position) {
    position.getBoard().put(target, position.getBoard().remove(origin));
  }

  @Override
  protected void revertPieces(Position position) {
    position.getBoard().put(origin, position.getBoard().remove(target));
  }

  @Override
  protected void updateState(Position position) {
    position.getMemory().push(position.getState().copy());
    addNoCastling(position);
    setEnPassant(position);
    position.toggleSideToMove();
  }

  protected void addNoCastling(Position position) {
    for (Square square : new Square[]{origin, target}) {
      if (position.getBoard().isRebirthSquare(square, King.class, Colour.WHITE)
          || position.getBoard().isRebirthSquare(square, Rook.class, Colour.WHITE)
          || position.getBoard().isRebirthSquare(square, King.class, Colour.BLACK)
          || position.getBoard().isRebirthSquare(square, Rook.class, Colour.BLACK)) {
        position.getState().addNoCastling(square);
      }
    }
  }

  protected void setEnPassant(Position position) {
    position.getState().resetEnPassant();
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", QuietMove.class.getSimpleName() + "[", "]").add(
        "origin=" + origin).add("target=" + target).toString();
  }
}
