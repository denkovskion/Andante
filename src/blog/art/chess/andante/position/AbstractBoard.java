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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AbstractBoard implements Board {

  @Override
  public void put(Entry entry) {
    put(getSquare(entry.file(), entry.rank()), entry.piece());
  }

  protected abstract Map<Set<Direction>, List<Direction>> getAllDirections();

  @Override
  public List<Direction> getDirections(int baseFileOffset, int baseRankOffset) {
    return getAllDirections().computeIfAbsent(
        Set.of(new DefaultDirection(baseFileOffset, baseRankOffset)), this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int base1FileOffset, int base1RankOffset,
      int base2FileOffset, int base2RankOffset) {
    return getAllDirections().computeIfAbsent(
        Set.of(new DefaultDirection(base1FileOffset, base1RankOffset),
            new DefaultDirection(base2FileOffset, base2RankOffset)), this::computeDirections);
  }

  @Override
  public List<Direction> getDirections(int... baseOffsets) {
    return getAllDirections().computeIfAbsent(IntStream.range(0, baseOffsets.length / 2).mapToObj(
            halfNo -> new DefaultDirection(baseOffsets[halfNo * 2], baseOffsets[halfNo * 2 + 1]))
        .collect(Collectors.toUnmodifiableSet()), this::computeDirections);
  }

  private List<Direction> computeDirections(Set<Direction> bases) {
    return bases.stream().flatMap(
        direction -> Stream.of(-direction.fileOffset(), direction.fileOffset()).flatMap(
            fileOffset -> Stream.of(-direction.rankOffset(), direction.rankOffset()).flatMap(
                rankOffset -> Stream.of(getDirection(fileOffset, rankOffset),
                    getDirection(rankOffset, fileOffset))))).distinct().sorted().toList();
  }
}
