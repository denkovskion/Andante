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

package blog.art.chess.andante.piece;

import blog.art.chess.andante.piece.fairy.Amazon;
import blog.art.chess.andante.piece.fairy.Grasshopper;
import blog.art.chess.andante.piece.fairy.Nightrider;
import blog.art.chess.andante.piece.orthodox.Bishop;
import blog.art.chess.andante.piece.orthodox.King;
import blog.art.chess.andante.piece.orthodox.Knight;
import blog.art.chess.andante.piece.orthodox.Pawn;
import blog.art.chess.andante.piece.orthodox.Queen;
import blog.art.chess.andante.piece.orthodox.Rook;
import java.util.ListResourceBundle;

public class PieceCodes_fr extends ListResourceBundle {

  @Override
  protected Object[][] getContents() {
    return new Object[][]{{King.class.getSimpleName(), "R"}, {Queen.class.getSimpleName(), "D"},
        {Rook.class.getSimpleName(), "T"}, {Bishop.class.getSimpleName(), "F"},
        {Knight.class.getSimpleName(), "C"}, {Pawn.class.getSimpleName(), "P"},
        {Grasshopper.class.getSimpleName(), "S"}, {Nightrider.class.getSimpleName(), "N"},
        {Amazon.class.getSimpleName(), "AM"}};
  }
}
