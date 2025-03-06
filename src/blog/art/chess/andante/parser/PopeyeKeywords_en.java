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

public class PopeyeKeywords_en extends ListResourceBundle {

  @Override
  protected Object[][] getContents() {
    return new Object[][]{{Popeye.Colour.White.name(), "White"},
        {Popeye.Colour.Black.name(), "Black"},
        {Popeye.Directive.BeginProblem.name(), "BeginProblem"},
        {Popeye.Directive.EndProblem.name(), "EndProblem"},
        {Popeye.Directive.NextProblem.name(), "NextProblem"},
        {Popeye.Command.Remark.name(), "Remark"}, {Popeye.Command.Condition.name(), "Condition"},
        {Popeye.Command.Option.name(), "Option"},
        {Popeye.Command.Stipulation.name(), "Stipulation"},
        {Popeye.Command.Pieces.name(), "Pieces"}, {Popeye.Condition.Circe.name(), "Circe"},
        {Popeye.Condition.NoCapture.name(), "NoCapture"}, {Popeye.Option.Try.name(), "Try"},
        {Popeye.Option.Defence.name(), "Defence"}, {Popeye.Option.SetPlay.name(), "SetPlay"},
        {Popeye.Option.NullMoves.name(), "NullMoves"},
        {Popeye.Option.WhiteToPlay.name(), "WhiteToPlay"},
        {Popeye.Option.Variation.name(), "Variation"},
        {Popeye.Option.MoveNumbers.name(), "MoveNumbers"},
        {Popeye.Option.NoThreat.name(), "NoThreat"}, {Popeye.Option.EnPassant.name(), "EnPassant"},
        {Popeye.Option.NoBoard.name(), "NoBoard"},
        {Popeye.Option.NoShortVariations.name(), "NoShortVariations"},
        {Popeye.Option.HalfDuplex.name(), "HalfDuplex"},
        {Popeye.Option.NoCastling.name(), "NoCastling"}};
  }
}
