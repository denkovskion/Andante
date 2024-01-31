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

package blog.art.chess.andante.solution;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;

public class SolutionWriter {

  public record Branch(Play play, String move, List<Branch> branches) {

  }

  public static List<Branch> toGrouped(List<Branch> branches) {
    return branches.stream().collect(Collectors.groupingBy(Branch::play,
            Collectors.groupingBy(Branch::branches, LinkedHashMap::new,
                Collectors.mapping(Branch::move, Collectors.joining(","))))).entrySet().stream()
        .flatMap(playEntry -> playEntry.getValue().entrySet().stream().map(
            branchesEntry -> new Branch(playEntry.getKey(), branchesEntry.getValue(),
                toGrouped(branchesEntry.getKey())))).sorted(Comparator.comparing(Branch::play))
        .toList();
  }

  public static String toFormatted(List<Branch> branches) {
    StringBuilder stringBuilder = new StringBuilder();
    write(branches, 1, false, true, false, stringBuilder);
    return stringBuilder.toString();
  }

  private static void write(List<Branch> branches, int moveNo, boolean newline, boolean tab,
      boolean space, StringBuilder stringBuilder) {
    Spliterator<Branch> iBranch = branches.spliterator();
    if (iBranch.tryAdvance(branch -> write(branch, moveNo, newline, tab, space, stringBuilder))) {
      iBranch.forEachRemaining(branch -> write(branch, moveNo, true, true, false, stringBuilder));
    }
  }

  private static void write(Branch branch, int moveNo, boolean newline, boolean tab, boolean space,
      StringBuilder stringBuilder) {
    if (branch.play() == Play.SET) {
      write(branch.branches(), moveNo, newline, tab, space, stringBuilder);
    } else {
      if (newline) {
        stringBuilder.append(System.lineSeparator());
      }
      if (tab) {
        stringBuilder.append("\t".repeat(moveNo - 1));
      } else if (space) {
        stringBuilder.append(" ");
      }
      if (branch.play() == Play.ZUGZWANG || branch.play() == Play.THREAT) {
        stringBuilder.append("(");
        if (branch.play() == Play.ZUGZWANG) {
          stringBuilder.append("zz");
          write(branch.branches(), moveNo + 1, true, true, false, stringBuilder);
        } else {
          write(branch.branches(), moveNo + 1, false, false, false, stringBuilder);
        }
        stringBuilder.append(")");
      } else if (branch.play() == Play.VARIATION || branch.play() == Play.REFUTATION
          || branch.play() == Play.HELP_2ND || branch.play() == Play.TEMPO_2ND) {
        if (tab) {
          stringBuilder.append(moveNo);
          stringBuilder.append("...");
        }
        if (branch.play() == Play.TEMPO_2ND) {
          stringBuilder.append("??");
        } else {
          stringBuilder.append(branch.move());
          if (branch.play() == Play.REFUTATION) {
            stringBuilder.append("!");
          }
        }
        write(branch.branches(), moveNo + 1, false, false, true, stringBuilder);
      } else {
        stringBuilder.append(moveNo);
        stringBuilder.append(".");
        if (branch.play() == Play.TEMPO_1ST) {
          stringBuilder.append("??");
        } else {
          stringBuilder.append(branch.move());
          if (branch.play() == Play.TRY) {
            stringBuilder.append("?");
          } else if (branch.play() == Play.KEY) {
            stringBuilder.append("!");
          }
        }
        write(branch.branches(), moveNo, false, false, true, stringBuilder);
      }
    }
  }

  public record Point(String score, String move) {

  }

  public static String toOrderedAndFormatted(List<Point> points) {
    return points.stream().sorted(Comparator.comparing(Point::score).thenComparing(Point::move))
        .map(point -> point.score() + "\t" + point.move())
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
