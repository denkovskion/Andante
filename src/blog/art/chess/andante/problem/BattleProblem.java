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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public abstract class BattleProblem extends Problem {

  protected final Aim aim;

  public BattleProblem(Position position, Aim aim, int nMoves) {
    super(position, nMoves);
    this.aim = aim;
  }

  @Override
  public void solve(AnalysisOptions analysisOptions, DisplayOptions displayOptions) {
    solve(position, aim, nMoves, analysisOptions.setPlay(), analysisOptions.nRefutations(),
        analysisOptions.variations(), analysisOptions.threats(), analysisOptions.shortVariations(),
        displayOptions.outputLanguage(), displayOptions.internalProgress());
  }

  protected void solve(Position position, Aim aim, int nMoves, boolean includeSetPlay,
      int includeTries, boolean includeVariations, boolean includeThreats,
      boolean includeShortVariations, Locale locale, boolean logMoves) {
    List<Move> pseudoLegalMoves = new ArrayList<>();
    boolean includeActualPlay = position.isLegal(pseudoLegalMoves);
    if (includeActualPlay || includeSetPlay) {
      List<SolutionWriter.Branch> branches = new ArrayList<>();
      analyseMax(position, aim, nMoves, pseudoLegalMoves, branches, locale, includeVariations,
          includeThreats, includeShortVariations, includeSetPlay, includeTries, includeActualPlay,
          includeActualPlay, logMoves);
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

  protected void analyseMax(Position position, Aim aim, int depth, List<Move> pseudoLegalMovesMax,
      List<SolutionWriter.Branch> branches, Locale locale, boolean includeVariations,
      boolean includeThreats, boolean includeShortVariations, boolean includeSetPlay,
      int includeTries, boolean includeActualPlay, boolean markKeys, boolean logMoves) {
    if (includeSetPlay && !(depth == getTerminalDepth())) {
      Move move = new NullMove();
      List<Move> pseudoLegalMovesMin = new ArrayList<>();
      if (move.make(position, pseudoLegalMovesMin, null, null)) {
        int score = searchMin(position, aim, depth, pseudoLegalMovesMin, 0);
        List<SolutionWriter.Branch> variations = new ArrayList<>();
        if (score > 0) {
          analyseMin(position, aim, depth - score + 1, pseudoLegalMovesMin, variations, locale,
              includeVariations, includeThreats, includeShortVariations, true);
        } else {
          analyseMin(position, aim, depth, pseudoLegalMovesMin, variations, locale,
              includeVariations, includeThreats, includeShortVariations, true);
        }
        branches.add(new SolutionWriter.Branch(Play.SET, null, variations));
        if (logMoves) {
          System.err.print(logPrefix() + " depth=" + depth + " move=" + move);
          if (score >= 0) {
            System.err.println(" score=" + score);
          } else {
            System.err.println(" score<0");
          }
        }
      } else {
        System.out.println("Illegal position in set play.");
      }
      move.unmake(position);
    }
    if (includeActualPlay) {
      for (Move move : pseudoLegalMovesMax) {
        List<Move> pseudoLegalMovesMin = new ArrayList<>();
        StringBuilder lanBuilder = new StringBuilder();
        if (move.make(position, pseudoLegalMovesMin, lanBuilder, locale)) {
          int score = searchMin(position, aim, depth, pseudoLegalMovesMin, includeTries);
          if (score > 0) {
            if (includeVariations && !(depth == getTerminalDepth())) {
              List<SolutionWriter.Branch> variations = new ArrayList<>();
              analyseMin(position, aim, depth - score + 1, pseudoLegalMovesMin, variations, locale,
                  true, includeThreats, includeShortVariations, false);
              if (markKeys) {
                branches.add(
                    new SolutionWriter.Branch(Play.KEY, lanBuilder.toString(), variations));
              } else {
                branches.add(new SolutionWriter.Branch(Play.CONTINUATION, lanBuilder.toString(),
                    variations));
              }
            } else {
              if (markKeys) {
                branches.add(new SolutionWriter.Branch(Play.KEY, lanBuilder.toString(),
                    Collections.emptyList()));
              } else {
                branches.add(new SolutionWriter.Branch(Play.CONTINUATION, lanBuilder.toString(),
                    Collections.emptyList()));
              }
            }
          } else if (score >= -includeTries) {
            List<SolutionWriter.Branch> variations = new ArrayList<>();
            analyseMin(position, aim, depth, pseudoLegalMovesMin, variations, locale,
                includeVariations, includeThreats, includeShortVariations, false);
            branches.add(new SolutionWriter.Branch(Play.TRY, lanBuilder.toString(), variations));
          }
          if (logMoves) {
            System.err.print(logPrefix() + " depth=" + depth + " move=" + move);
            if (score >= -includeTries) {
              System.err.println(" score=" + score);
            } else {
              System.err.println(" score<" + -includeTries);
            }
          }
        }
        move.unmake(position);
      }
    }
  }

  protected void analyseMin(Position position, Aim aim, int depth, List<Move> pseudoLegalMovesMin,
      List<SolutionWriter.Branch> branches, Locale locale, boolean includeVariations,
      boolean includeThreats, boolean includeShortVariations, boolean includeSetPlay) {
    if (depth == getTerminalDepth()) {
      for (Move move : pseudoLegalMovesMin) {
        StringBuilder lanBuilder = new StringBuilder();
        if (move.make(position, null, lanBuilder, locale)) {
          branches.add(new SolutionWriter.Branch(Play.REFUTATION, lanBuilder.toString(),
              Collections.emptyList()));
        }
        move.unmake(position);
      }
    } else {
      List<SolutionWriter.Branch> threats = null;
      if (depth > 1 && includeVariations && includeThreats && !includeSetPlay) {
        Move move = new NullMove();
        List<Move> pseudoLegalMovesMax = new ArrayList<>();
        if (move.make(position, pseudoLegalMovesMax, null, null)) {
          int score = searchMax(position, aim, depth - 1, pseudoLegalMovesMax);
          if (score > 0) {
            threats = new ArrayList<>();
            analyseMax(position, aim, depth - score, pseudoLegalMovesMax, threats, locale, true,
                true, includeShortVariations, false, 0, true, false, false);
            branches.add(new SolutionWriter.Branch(Play.THREAT, null, threats));
          } else {
            branches.add(new SolutionWriter.Branch(Play.ZUGZWANG, null, Collections.emptyList()));
          }
        }
        move.unmake(position);
      }
      for (Move move : pseudoLegalMovesMin) {
        List<Move> pseudoLegalMovesMax = new ArrayList<>();
        StringBuilder lanBuilder = new StringBuilder();
        if (move.make(position, pseudoLegalMovesMax, lanBuilder, locale)) {
          int score = searchMax(position, aim, depth - 1, pseudoLegalMovesMax);
          if (score > 0) {
            if ((includeVariations || includeSetPlay) && (score == 1 || includeShortVariations)) {
              List<SolutionWriter.Branch> continuations = new ArrayList<>();
              analyseMax(position, aim, depth - score, pseudoLegalMovesMax, continuations, locale,
                  includeVariations, includeThreats, includeShortVariations, false, 0, true, false,
                  false);
              if (threats == null || Collections.disjoint(continuations, threats)) {
                branches.add(new SolutionWriter.Branch(Play.VARIATION, lanBuilder.toString(),
                    continuations));
              }
            }
          } else if (!includeSetPlay) {
            branches.add(new SolutionWriter.Branch(Play.REFUTATION, lanBuilder.toString(),
                Collections.emptyList()));
          }
        }
        move.unmake(position);
      }
    }
  }

  protected abstract int searchMax(Position position, Aim aim, int depth,
      List<Move> pseudoLegalMovesMax);

  protected abstract int searchMin(Position position, Aim aim, int depth,
      List<Move> pseudoLegalMovesMin, int nRefutations);

  protected abstract int getTerminalDepth();

  @Override
  public String toString() {
    return new StringJoiner(", ", BattleProblem.class.getSimpleName() + "[", "]").add(
        "position=" + position).add("aim=" + aim).add("nMoves=" + nMoves).toString();
  }
}
