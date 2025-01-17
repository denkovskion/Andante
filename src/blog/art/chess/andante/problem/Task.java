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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;

public class Task {

  private final Problem problem;
  private final AnalysisOptions analysisOptions;
  private final DisplayOptions displayOptions;

  public Task(Problem problem, AnalysisOptions analysisOptions, DisplayOptions displayOptions) {
    this.problem = problem;
    this.analysisOptions = analysisOptions;
    this.displayOptions = displayOptions;
  }

  public void solve() {
    System.out.println("-".repeat(72));
    if (displayOptions.internalModel()) {
      System.err.println(Problem.logPrefix() + " task=" + this);
    }
    System.err.println(Problem.logPrefix() + " problem.solve(...)");
    Instant begin = Instant.now();
    problem.solve(analysisOptions, displayOptions);
    Instant end = Instant.now();
    System.err.println(Problem.logPrefix() + " duration=" + Duration.between(begin, end)
        .truncatedTo(ChronoUnit.MILLIS));
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Task.class.getSimpleName() + "[", "]").add("problem=" + problem)
        .add("analysisOptions=" + analysisOptions).add("displayOptions=" + displayOptions)
        .toString();
  }
}
