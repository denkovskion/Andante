# Andante

Andante is an experimental program for solving chess problems.

## Usage

Java 17 or later is required. Andante reads problems from standard input or a file, and writes the
solutions to standard output.

```
java -jar Andante.jar [inputfile]
```

## EPD-based input

Andante
accepts [Extended Position Description](https://www.chessprogramming.org/Extended_Position_Description)
records with a single operation: direct mate fullmove count (opcode `dm`) for stipulating mate
search, or analysis count depth (opcode `acd`) for perft.

### Example

```
4K3/B6Q/1R5N/p3p1n1/rP3p2/5k2/2R2P2/1N2nB2 w - - dm 2;
```

Andante outputs the requested evaluation:

```
+M2 Rb6-g6
```

## Popeye-based input

The problems and the extent of analysis can be specified by using a subset of the input language
of [Popeye](https://github.com/thomas-maeder/popeye). Andante accepts the following specifications
and keywords:

- Directives: `BeginProblem`, `EndProblem`, `NextProblem`
- Commands: `Remark`, `Condition`, `Option`, `Stipulation`, `Pieces`
- Conditions: `Circe`, `NoCapture`, `AntiCirce`
- Anti-Circe types: `Calvet`(default), `Cheylan`
- Options: `Try`, `Defence`, `SetPlay`, `NullMoves`, `WhiteToPlay`, `Variation`, `MoveNumbers`,
  `NoThreat`, `EnPassant`, `NoBoard`, `NoShortVariations`, `HalfDuplex`, `NoCastling`
- Stipulation types: direct, help`h`, self`s`
- Goals: mate`#`, stalemate`=`
- Piece types: king`K`, queen`Q`, rook`R`, bishop`B`, knight`S`, pawn`P`, grasshopper`G`,
  nightrider`N`, amazon`AM`
- Piece colours: `White`, `Black`

The French or German variant of the input language can be used as well. The input is not
case-sensitive. Andante does not accept shortened forms of the keywords.

### Example

```
BeginProblem
Remark Ivan Denkovski, 3rd Comm. Belgrade Internet Tourney 2010
Pieces White Ke8 Qh7 Rb6c2 Ba7f1 Sh6b1 Pb4f2 Black Kf3 Ra4 Sg5e1 Pa5e5f4
Stipulation #2
Option Variation Try NoBoard
EndProblem
```

Andante outputs the requested analysis:

```
1.Sb1-d2+? Kf3xf2!
1.Rb6-a6,Rb6-b5,Rb6-b7,Rb6-b8,Rb6-c6,Rb6-d6,Rb6-e6,Rb6-f6? (2.Sb1-d2#)
1...Sg5-e4!
1.Rc2-e2? (2.Sb1-d2#)
1...Sg5-e4 2.Qh7xe4#
1...Ra4-a2!
1.Qh7-d3+? Se1xd3!
1.Rb6-g6! (2.Sb1-d2#)
1...Kf3-e4 2.Qh7-b7#
1...Sg5-e4 2.Bf1-e2#
```

## Author

[Ivan Denkovski](mailto:denkovski@hotmail.com) is the author of Andante.
