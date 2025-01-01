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

public class Directmate extends BattleProblem {

  public Directmate(Position position, Aim aim, int nMoves) {
    super(position, aim, nMoves);
  }

  @Override
  protected int searchMax(Position position, Aim aim, int depth, List<Move> pseudoLegalMovesMax) {
    int max = Integer.MIN_VALUE;
    for (Move move : pseudoLegalMovesMax) {
      List<Move> pseudoLegalMovesMin = new ArrayList<>();
      if (move.make(position, pseudoLegalMovesMin, null, null)) {
        int score = searchMin(position, aim, depth, pseudoLegalMovesMin, 0);
        if (score > max) {
          max = score;
        }
      }
      move.unmake(position);
      if (max == depth) {
        break;
      }
    }
    return max;
  }

  @Override
  protected int searchMin(Position position, Aim aim, int depth, List<Move> pseudoLegalMovesMin,
      int nRefutations) {
    int min = 0;
    if (depth == 1) {
      for (Move move : pseudoLegalMovesMin) {
        if (move.make(position, null, null, null)) {
          min--;
        }
        move.unmake(position);
        if (min < -nRefutations) {
          min = Integer.MIN_VALUE;
          break;
        }
      }
    } else {
      for (Move move : pseudoLegalMovesMin) {
        List<Move> pseudoLegalMovesMax = new ArrayList<>();
        if (move.make(position, pseudoLegalMovesMax, null, null)) {
          int score = searchMax(position, aim, depth - 1, pseudoLegalMovesMax);
          if (min == 0) {
            if (score < 0) {
              min = -1;
            } else {
              min = score;
            }
          } else if (min > 0) {
            if (score < 0) {
              min = -1;
            } else {
              if (score < min) {
                min = score;
              }
            }
          } else {
            if (score < 0) {
              min--;
            }
          }
        }
        move.unmake(position);
        if (min < -nRefutations) {
          min = Integer.MIN_VALUE;
          break;
        }
      }
    }
    if (min == 0) {
      if (evaluateTerminalNode(position, aim)) {
        min = depth;
      } else {
        min = Integer.MIN_VALUE;
      }
    }
    return min;
  }

  @Override
  protected int getTerminalDepth() {
    return 1;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Directmate.class.getSimpleName() + "[", "]").add(
        "position=" + position).add("aim=" + aim).add("nMoves=" + nMoves).toString();
  }
}
