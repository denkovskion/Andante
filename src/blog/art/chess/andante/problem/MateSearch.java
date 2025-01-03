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
import blog.art.chess.andante.solution.SolutionWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class MateSearch extends Problem {

  public MateSearch(Position position, int nMoves) {
    super(position, nMoves);
  }

  @Override
  public void solve(AnalysisOptions analysisOptions, DisplayOptions displayOptions) {
    solve(position, nMoves, displayOptions.outputLanguage());
  }

  private void solve(Position position, int nMoves, Locale locale) {
    List<Move> pseudoLegalMovesMax = new ArrayList<>();
    if (position.isLegal(pseudoLegalMovesMax)) {
      List<SolutionWriter.Point> points = new ArrayList<>();
      for (Move move : pseudoLegalMovesMax) {
        List<Move> pseudoLegalMovesMin = new ArrayList<>();
        StringBuilder lanBuilder = new StringBuilder();
        if (move.make(position, pseudoLegalMovesMin, lanBuilder, locale)) {
          for (int depth = 1; depth <= nMoves; depth++) {
            int score = searchMin(position, depth, pseudoLegalMovesMin);
            if (score > 0) {
              move.postWrite(position, pseudoLegalMovesMin, lanBuilder);
              points.add(new SolutionWriter.Point("+M" + depth, lanBuilder.toString()));
              break;
            }
          }
        }
        move.unmake(position);
      }
      System.out.println(SolutionWriter.toOrderedAndFormatted(points));
    } else {
      System.out.println("Illegal position.");
    }
  }

  private int searchMax(Position position, int depth, List<Move> pseudoLegalMovesMax) {
    int max = -1;
    for (Move move : pseudoLegalMovesMax) {
      List<Move> pseudoLegalMovesMin = new ArrayList<>();
      if (move.make(position, pseudoLegalMovesMin, null, null)) {
        max = searchMin(position, depth, pseudoLegalMovesMin);
      }
      move.unmake(position);
      if (max > 0) {
        break;
      }
    }
    return max;
  }

  private int searchMin(Position position, int depth, List<Move> pseudoLegalMovesMin) {
    int min = 0;
    if (depth == 1) {
      for (Move move : pseudoLegalMovesMin) {
        if (move.make(position, null, null, null)) {
          min = -1;
        }
        move.unmake(position);
        if (min < 0) {
          break;
        }
      }
    } else {
      for (Move move : pseudoLegalMovesMin) {
        List<Move> pseudoLegalMovesMax = new ArrayList<>();
        if (move.make(position, pseudoLegalMovesMax, null, null)) {
          min = searchMax(position, depth - 1, pseudoLegalMovesMax);
        }
        move.unmake(position);
        if (min < 0) {
          break;
        }
      }
    }
    if (min == 0) {
      if (evaluateTerminalNode(position, Aim.MATE)) {
        min = 1;
      } else {
        min = -1;
      }
    }
    return min;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MateSearch.class.getSimpleName() + "[", "]").add(
        "position=" + position).add("nMoves=" + nMoves).toString();
  }
}
