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

package blog.art.chess.andante.problem;

import blog.art.chess.andante.position.Position;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

public abstract class Problem {

  protected final Position position;
  protected final int nMoves;

  public Problem(Position position, int nMoves) {
    this.position = position;
    this.nMoves = nMoves;
  }

  public abstract void solve(AnalysisOptions analysisOptions, DisplayOptions displayOptions);

  protected boolean evaluateTerminalNode(Position position, Aim aim) {
    boolean result;
    if (position.isCheck() == 0) {
      result = aim == Aim.STALEMATE;
    } else {
      result = aim == Aim.MATE;
    }
    return result;
  }

  public static String logPrefix() {
    return "Andante@" + ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Problem.class.getSimpleName() + "[", "]").add(
        "position=" + position).add("nMoves=" + nMoves).toString();
  }
}
