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

package blog.art.chess.andante.parser;

import java.util.ListResourceBundle;

public class PopeyeKeywords_de extends ListResourceBundle {

  @Override
  protected Object[][] getContents() {
    return new Object[][]{{Popeye.Colour.White.name(), "Weiss"},
        {Popeye.Colour.Black.name(), "Schwarz"},
        {Popeye.Directive.BeginProblem.name(), "Anfangproblem"},
        {Popeye.Directive.EndProblem.name(), "Endeproblem"},
        {Popeye.Directive.NextProblem.name(), "WeiteresProblem"},
        {Popeye.Command.Remark.name(), "Bemerkung"}, {Popeye.Command.Condition.name(), "Bedingung"},
        {Popeye.Command.Option.name(), "Option"}, {Popeye.Command.Stipulation.name(), "Forderung"},
        {Popeye.Command.Pieces.name(), "Steine"}, {Popeye.Condition.Circe.name(), "Circe"},
        {Popeye.Option.Try.name(), "Verfuehrung"}, {Popeye.Option.Defence.name(), "Widerlegung"},
        {Popeye.Option.SetPlay.name(), "Satzspiel"}, {Popeye.Option.NullMoves.name(), "NullZuege"},
        {Popeye.Option.WhiteToPlay.name(), "WeissBeginnt"},
        {Popeye.Option.Variation.name(), "Varianten"},
        {Popeye.Option.MoveNumbers.name(), "Zugnummern"},
        {Popeye.Option.NoThreat.name(), "OhneDrohung"},
        {Popeye.Option.EnPassant.name(), "EnPassant"}, {Popeye.Option.NoBoard.name(), "OhneBrett"},
        {Popeye.Option.NoShortVariations.name(), "OhneKurzVarianten"},
        {Popeye.Option.HalfDuplex.name(), "HalbDuplex"},
        {Popeye.Option.NoCastling.name(), "KeineRochade"}};
  }
}
