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

import blog.art.chess.andante.move.Move;
import blog.art.chess.andante.move.NullMove;
import blog.art.chess.andante.position.Position;
import blog.art.chess.andante.solution.Play;
import blog.art.chess.andante.solution.SolutionWriter;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class HelpProblem extends Problem {

  private final boolean halfMove;
  private final Aim aim;

  public HelpProblem(Position position, Aim aim, int nMoves, boolean halfMove) {
    super(position, nMoves);
    this.halfMove = halfMove;
    this.aim = aim;
  }

  @Override
  public void solve(AnalysisOptions analysisOptions, DisplayOptions displayOptions) {
    solve(position, aim, nMoves, halfMove, analysisOptions.setPlay(), analysisOptions.tempoTries(),
        displayOptions.outputLanguage(), displayOptions.internalProgress());
  }

  private void solve(Position position, Aim aim, int nMoves, boolean halfMove,
      boolean includeSetPlay, boolean includeTempoTries, Locale locale, boolean logMoves) {
    List<Move> pseudoLegalMoves = new ArrayList<>();
    boolean includeActualPlay = position.isLegal(pseudoLegalMoves);
    if (includeActualPlay || includeSetPlay) {
      List<SolutionWriter.Branch> branches = new ArrayList<>();
      if (halfMove) {
        analyseMax(position, aim, nMoves + 1, pseudoLegalMoves, branches, locale, includeTempoTries,
            includeSetPlay, includeActualPlay, logMoves);
      } else {
        analyseMin(position, aim, nMoves, pseudoLegalMoves, branches, locale, includeTempoTries,
            includeSetPlay, includeActualPlay, logMoves);
      }
      System.out.println(SolutionWriter.toFormatted(SolutionWriter.toGrouped(branches)));
    }
    if (!includeActualPlay) {
      if (includeSetPlay) {
        System.out.println("Illegal position in actual play.");
      } else {
        System.out.println("Illegal position.");
      }
    }
  }

  private int analyseMax(Position position, Aim aim, int depth, List<Move> pseudoLegalMovesMax,
      List<SolutionWriter.Branch> branchesMax, Locale locale, boolean includeTempoTries,
      boolean includeSetPlay, boolean includeActualPlay, boolean logMoves) {
    int max = 0;
    if (includeSetPlay || includeTempoTries) {
      Move move = new NullMove();
      List<Move> pseudoLegalMovesMin = new ArrayList<>();
      if (move.make(position, pseudoLegalMovesMin, null, null)) {
        List<SolutionWriter.Branch> branchesMin = new ArrayList<>();
        if (analyseMin(position, aim, depth - 1, pseudoLegalMovesMin, branchesMin, locale,
            includeTempoTries, false, true, false) != 0) {
          max++;
          if (includeSetPlay) {
            branchesMax.add(new SolutionWriter.Branch(Play.SET, null, branchesMin));
          } else {
            branchesMax.add(new SolutionWriter.Branch(Play.TEMPO_2ND, null, branchesMin));
          }
        }
        if (logMoves) {
          System.err.println(
              "Andante@" + ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS) + " depth=" + depth
                  + " move=" + move + " branches.size()=" + branchesMax.size());
        }
      } else {
        if (includeSetPlay) {
          System.out.println("Illegal position in set play.");
        }
      }
      move.unmake(position);
    }
    if (includeActualPlay) {
      for (Move move : pseudoLegalMovesMax) {
        List<Move> pseudoLegalMovesMin = new ArrayList<>();
        StringBuilder lanBuilder = new StringBuilder();
        move.preWrite(position, lanBuilder, locale);
        if (move.make(position, pseudoLegalMovesMin, null, null)) {
          List<SolutionWriter.Branch> branchesMin = new ArrayList<>();
          if (analyseMin(position, aim, depth - 1, pseudoLegalMovesMin, branchesMin, locale,
              includeTempoTries, false, true, false) != 0) {
            max++;
            move.postWrite(position, pseudoLegalMovesMin, lanBuilder);
            branchesMax.add(
                new SolutionWriter.Branch(Play.HELP_2ND, lanBuilder.toString(), branchesMin));
          }
          if (logMoves) {
            System.err.println(
                "Andante@" + ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS) + " depth=" + depth
                    + " move=" + move + " branches.size()=" + branchesMax.size());
          }
        }
        move.unmake(position);
      }
    }
    return max;
  }

  private int analyseMin(Position position, Aim aim, int depth, List<Move> pseudoLegalMovesMin,
      List<SolutionWriter.Branch> branchesMin, Locale locale, boolean includeTempoTries,
      boolean includeSetPlay, boolean includeActualPlay, boolean logMoves) {
    int min = 0;
    int nLegalMoves = 0;
    if (depth == 0) {
      for (Move move : pseudoLegalMovesMin) {
        if (move.make(position, null, null, null)) {
          nLegalMoves++;
        }
        move.unmake(position);
        if (nLegalMoves != 0) {
          break;
        }
      }
    } else {
      if (includeSetPlay || includeTempoTries) {
        Move move = new NullMove();
        List<Move> pseudoLegalMovesMax = new ArrayList<>();
        if (move.make(position, pseudoLegalMovesMax, null, null)) {
          List<SolutionWriter.Branch> branchesMax = new ArrayList<>();
          if (analyseMax(position, aim, depth, pseudoLegalMovesMax, branchesMax, locale,
              includeTempoTries, false, true, false) != 0) {
            min++;
            if (includeSetPlay) {
              branchesMin.add(new SolutionWriter.Branch(Play.SET, null, branchesMax));
            } else {
              branchesMin.add(new SolutionWriter.Branch(Play.TEMPO_1ST, null, branchesMax));
            }
          }
          if (logMoves) {
            System.err.println(
                "Andante@" + ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS) + " depth=" + depth
                    + " move=" + move + " branches.size()=" + branchesMin.size());
          }
        } else {
          if (includeSetPlay) {
            System.out.println("Illegal position in set play.");
          }
        }
        move.unmake(position);
      }
      if (includeActualPlay) {
        for (Move move : pseudoLegalMovesMin) {
          List<Move> pseudoLegalMovesMax = new ArrayList<>();
          StringBuilder lanBuilder = new StringBuilder();
          move.preWrite(position, lanBuilder, locale);
          if (move.make(position, pseudoLegalMovesMax, null, null)) {
            nLegalMoves++;
            List<SolutionWriter.Branch> branchesMax = new ArrayList<>();
            if (analyseMax(position, aim, depth, pseudoLegalMovesMax, branchesMax, locale,
                includeTempoTries, false, true, false) != 0) {
              min++;
              move.postWrite(position, pseudoLegalMovesMax, lanBuilder);
              branchesMin.add(
                  new SolutionWriter.Branch(Play.HELP_1ST, lanBuilder.toString(), branchesMax));
            }
            if (logMoves) {
              System.err.println(
                  "Andante@" + ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS) + " depth="
                      + depth + " move=" + move + " branches.size()=" + branchesMin.size());
            }
          }
          move.unmake(position);
        }
      }
    }
    if (nLegalMoves == 0) {
      if (evaluateTerminalNode(position, aim)) {
        min = 1;
      } else {
        min = 0;
      }
    }
    return min;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", HelpProblem.class.getSimpleName() + "[", "]").add(
            "position=" + position).add("aim=" + aim).add("nMoves=" + nMoves)
        .add("halfMove=" + halfMove).toString();
  }
}
