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
import blog.art.chess.andante.piece.orthodox.Rook;
import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.position.Square;
import java.util.Locale;
import java.util.StringJoiner;

public class AndernachMove extends VariantMove {

  private final Square change;
  private final Colour origin;
  private final Colour target;

  public AndernachMove(Move move, Square change, Colour origin, Colour target) {
    super(move);
    this.change = change;
    this.origin = origin;
    this.target = target;
  }

  @Override
  public void preWrite(Position position, StringBuilder lanBuilder, Locale locale) {
    move.preWrite(position, lanBuilder, locale);
    lanBuilder.append("(").append(target.getCode(locale)).append(")");
  }

  @Override
  protected void updatePieces(Position position) {
    move.updatePieces(position);
    position.getBoard().get(change).setColour(target);
  }

  @Override
  protected void revertPieces(Position position) {
    position.getBoard().get(change).setColour(origin);
    move.revertPieces(position);
  }

  @Override
  protected void updateState(Position position) {
    move.updateState(position);
    if ((position.getBoard().isRebirthSquare(change, Rook.class, Colour.WHITE)
        || position.getBoard().isRebirthSquare(change, Rook.class, Colour.BLACK))
        && position.getBoard().isRebirthSquare(change, position.getBoard().get(change).getClass(),
        position.getBoard().get(change).getColour())) {
      position.getState().removeNoCastling(change);
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", AndernachMove.class.getSimpleName() + "[", "]").add(
            "move=" + move).add("change=" + change).add("origin=" + origin).add("target=" + target)
        .toString();
  }
}
