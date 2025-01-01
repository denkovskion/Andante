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

package blog.art.chess.andante.parser;

import java.util.ListResourceBundle;

public class PopeyeKeywords extends ListResourceBundle {

  @Override
  protected Object[][] getContents() {
    return new Object[][]{{Popeye.Colour.White.name(), Popeye.Colour.White.name()},
        {Popeye.Colour.Black.name(), Popeye.Colour.Black.name()},
        {Popeye.Directive.BeginProblem.name(), Popeye.Directive.BeginProblem.name()},
        {Popeye.Directive.EndProblem.name(), Popeye.Directive.EndProblem.name()},
        {Popeye.Directive.NextProblem.name(), Popeye.Directive.NextProblem.name()},
        {Popeye.Command.Remark.name(), Popeye.Command.Remark.name()},
        {Popeye.Command.Condition.name(), Popeye.Command.Condition.name()},
        {Popeye.Command.Option.name(), Popeye.Command.Option.name()},
        {Popeye.Command.Stipulation.name(), Popeye.Command.Stipulation.name()},
        {Popeye.Command.Pieces.name(), Popeye.Command.Pieces.name()},
        {Popeye.Condition.Circe.name(), Popeye.Condition.Circe.name()},
        {Popeye.Option.Try.name(), Popeye.Option.Try.name()},
        {Popeye.Option.Defence.name(), Popeye.Option.Defence.name()},
        {Popeye.Option.SetPlay.name(), Popeye.Option.SetPlay.name()},
        {Popeye.Option.NullMoves.name(), Popeye.Option.NullMoves.name()},
        {Popeye.Option.WhiteToPlay.name(), Popeye.Option.WhiteToPlay.name()},
        {Popeye.Option.Variation.name(), Popeye.Option.Variation.name()},
        {Popeye.Option.MoveNumbers.name(), Popeye.Option.MoveNumbers.name()},
        {Popeye.Option.NoThreat.name(), Popeye.Option.NoThreat.name()},
        {Popeye.Option.EnPassant.name(), Popeye.Option.EnPassant.name()},
        {Popeye.Option.NoBoard.name(), Popeye.Option.NoBoard.name()},
        {Popeye.Option.NoShortVariations.name(), Popeye.Option.NoShortVariations.name()},
        {Popeye.Option.HalfDuplex.name(), Popeye.Option.HalfDuplex.name()},
        {Popeye.Option.NoCastling.name(), Popeye.Option.NoCastling.name()}};
  }
}
