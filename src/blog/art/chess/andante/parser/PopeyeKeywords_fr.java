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

public class PopeyeKeywords_fr extends ListResourceBundle {

  @Override
  protected Object[][] getContents() {
    return new Object[][]{{Popeye.Colour.White.name(), "Blanc"},
        {Popeye.Colour.Black.name(), "Noir"},
        {Popeye.Directive.BeginProblem.name(), "DebutProbleme"},
        {Popeye.Directive.EndProblem.name(), "FinProbleme"},
        {Popeye.Directive.NextProblem.name(), "ASuivre"},
        {Popeye.Command.Remark.name(), "Remarque"}, {Popeye.Command.Condition.name(), "Condition"},
        {Popeye.Command.Option.name(), "Option"}, {Popeye.Command.Stipulation.name(), "Enonce"},
        {Popeye.Command.Pieces.name(), "Pieces"}, {Popeye.Condition.Circe.name(), "Circe"},
        {Popeye.Condition.NoCapture.name(), "SansPrises"}, {Popeye.Option.Try.name(), "Essais"},
        {Popeye.Option.Defence.name(), "Defense"}, {Popeye.Option.SetPlay.name(), "Apparent"},
        {Popeye.Option.NullMoves.name(), "CoupsVides"},
        {Popeye.Option.WhiteToPlay.name(), "ApparentSeul"},
        {Popeye.Option.Variation.name(), "Variantes"}, {Popeye.Option.MoveNumbers.name(), "Trace"},
        {Popeye.Option.NoThreat.name(), "SansMenace"},
        {Popeye.Option.EnPassant.name(), "EnPassant"},
        {Popeye.Option.NoBoard.name(), "SansEchiquier"},
        {Popeye.Option.NoShortVariations.name(), "SansVariantesCourtes"},
        {Popeye.Option.HalfDuplex.name(), "DemiDuplex"},
        {Popeye.Option.NoCastling.name(), "SansRoquer"}};
  }
}
