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
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.StringJoiner;
import java.util.TreeSet;

public class DefaultVariant implements Variant {

  private final SortedSet<Condition> conditions;

  public DefaultVariant(Condition... conditions) {
    this.conditions = new TreeSet<>(List.of(conditions));
  }

  @Override
  public void replaceMoves(Board board, List<Move> moves) {
    moves.replaceAll(move -> {
      List<Action> actions = conditions.stream()
          .map(condition -> condition.generateAction(board, move)).filter(Objects::nonNull)
          .toList();
      return actions.isEmpty() ? move : new VariantMove(move, actions);
    });
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DefaultVariant.class.getSimpleName() + "[", "]").add(
        "conditions=" + conditions).toString();
  }
}
