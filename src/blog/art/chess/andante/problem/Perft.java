/*
 * MIT License
 *
 * Copyright (c) 2024-2025 Ivan Denkovski
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

import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.position.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Perft extends Problem {

  private final boolean halfMove;

  public Perft(Position position, int nMoves, boolean halfMove) {
    super(position, nMoves);
    this.halfMove = halfMove;
  }

  @Override
  public void solve(AnalysisOptions analysisOptions, DisplayOptions displayOptions) {
    solve(position, nMoves, halfMove);
  }

  private void solve(Position position, int nMoves, boolean halfMove) {
    List<Move> pseudoLegalMoves = new ArrayList<>();
    if (position.isLegal(pseudoLegalMoves)) {
      long nNodes;
      if (halfMove) {
        nNodes = analyse(position, nMoves * 2 + 1, pseudoLegalMoves);
      } else {
        nNodes = analyse(position, nMoves * 2, pseudoLegalMoves);
      }
      System.out.println(nNodes);
    } else {
      System.out.println("Illegal position.");
    }
  }

  private long analyse(Position position, int depth, List<Move> pseudoLegalMoves) {
    if (depth == 0) {
      return 1;
    }
    long nNodes = 0;
    for (Move move : pseudoLegalMoves) {
      List<Move> pseudoLegalMovesNext = new ArrayList<>();
      if (move.make(position, pseudoLegalMovesNext, null, null)) {
        nNodes += analyse(position, depth - 1, pseudoLegalMovesNext);
      }
      move.unmake(position);
    }
    return nNodes;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Perft.class.getSimpleName() + "[", "]").add(
        "position=" + position).add("nMoves=" + nMoves).add("halfMove=" + halfMove).toString();
  }
}
