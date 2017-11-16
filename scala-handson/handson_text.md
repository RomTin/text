# 学生向けScalaハンズオンテキスト

# プログラミング言語「Scala」

Scalaとは、JVM上で動くオブジェクト指向・関数型のハイブリッド言語である。

### JVM

OSとプログラムの間に立ってくれる存在の一種。
VMがないと、OS毎に別々のプログラムを書いたりコンパイルしたりしないといけないが、VMがその差異を吸収してくれると、1つプログラムを書くだけであらゆる環境で動かすことができる。

### ハイブリッド言語

言語を特徴付ける性質は様々あるが、Scalaは「オブジェクト指向言語」としての側面と「関数型言語」としての側面を持っている。
オブジェクト指向言語としては、メソッドを持つクラスを定義でき、継承とメソッドのオーバーライドを用いたポリモーフィズムを活用できる。
関数型言語としては、イミュータブルなオブジェクト、再代入不可能な変数、パターンマッチを活用でき、式指向である。

## Scalaの基本文法

Scalaの基本的な文法をざっと紹介する。

```scala
// 1行コメント

/*
 * 複数行コメント
 */

println("Hello, world!") // 標準出力
```

### 値とリテラル

基本的なデータ型として、以下のようなものがある。

```scala
 42 // Int型、整数
 3.14 // Double型、浮動小数点数
 "hogehoge" // String型、文字列
 true // Boolean型、真偽値
 () // Unit型、何もないことを表す特殊な値
```

```scala
Array(1, 2, 3) // 配列
List(1, 2, 3) // リスト
Map("a" -> 1, "b" -> 2) // マップ
Set(1, 2, 3) // 集合
```

```scala
1 :: 2 :: 3 :: Nil
// :: と Nil を使ってリストを作る。
// List(1, 2, 3) と同じ。
```

```scala
(42, "string", 3.14) // タプル

s"$value ${value2}" // $で変数を、${}で式を埋め込める。値は自動的に文字列に変換される。
```

### 変数定義

```scala
val i = 42 // val で変数を定義する。型は値から推論される。
// i = 99 // valで定義した変数には再代入できない。
val j: Int = 100 // 明示的に型を指定することもできる。
// k: String = 100 // 変数と値の型が合わないとコンパイルエラー

var x = 42 // 再代入可能な変数の宣言
x = 99 // 再代入可能
```

### 制御構文

Scalaには `if` `for` `match` `while` のような制御構文がある。
これらは全て式であり、値を返す。

#### 基本的な制御構文

```scala
// if (cond) のcondにはBoolean型しか置けない
if (cond) {
  ...
} else {
  ...
}
```

```scala
for (i <- seq) {
  ...
}
```

#### 文と式

```scala
// Scalaのifやforは全て式なので、値を返す。
val ret = if (false) {
  "aaa"
} else if (true) {
  "bbb"
} else {
  "ccc"
}
ret // => "bbb"
```

```scala
val list = 1 :: 2 :: 3 :: Nil

val ret = for (i <- list) { i * 2 }
ret //=> Unit
// for式は「何もない値」を返す。
// list.foreach { i => ... } の糖衣構文。

val list = for (i <- list) yield { i * 2 }
list //=> List(2, 4, 6)
// yieldを付けると、yieldに与えられた式で変換された値をまたリストに詰めて返す。
// list.map { i => ... } の糖衣構文。
```

```scala
val num = 42

// match式はガードとパターンマッチ（後述）を活用できる。
num match {
  case n if n > 40 => println("over 40.")
  case _ => println("less than or equal to 40.") // _ で何にでもマッチする条件（ワイルドカード）
}
```

#### パターンマッチ

```scala
val tuple = (42, "answer")

val (i, s) = tuple
i // 42
s // tuple

// match式とパターンマッチを活用して、複雑な条件を素直に書ける。
tuple match {
  case (40, s) => println(s"40 is $s")
  case (i, "universe") => println(s"$i is universe")
  case (i, s) => println(s"$i is $s")
}
```

```scala
val tuple = (42, "hogefuga", 3.14)

tuple match {
  case (40, "foobar", 2.718) => // リテラルとの比較
  case (Num, Str, Point) => // 定数との比較
  case (`num`, `str`, `p`) => // 変数と値を比較したい場合は、バッククオートで囲む
  case (n: Int, s: String, p: Int) => // 型比較
  case tup @ (n, s, p) => // アズパターン。分解した値だけでなく、分解前の値も変数に代入する
}
```

```scala
val list = 1 :: 2 :: 3 :: Nil

// リストの分解。headに先頭の値が、tailにそれ以外のリストが代入される。
val head :: tail = list
head // 1
tail // List(2, 3)

list match {
  case 42 :: tail => // 先頭が42のリストにマッチ
  case head :: Nil => // Nilは空リスト。1要素のリストにマッチ
  case e1 :: e2 :: _ => // 先頭と2番目の要素を変数に代入
  case Nil => // 空リストにのみマッチ
}
```

```scala
// リストもタプルもネストすることが可能
val tuple = (42, "answer", 1 :: 2 :: 3 :: Nil)

tuple match {
  case (40, str, head :: Nil) =>
  case (i, "answer", x :: xs) =>
  case (_, _, list @ (head :: tail)) => // listにはList全体が代入される
  case _ => // 全てにマッチ
}
```

#### メソッドと関数オブジェクト

Scalaはオブジェクト指向言語と関数型言語のハイブリッドだと述べた。
`def` で定義できるのは何らかのクラスに属する「メソッド」となる。

```scala
def func(x: Int, y: Int): Int = {
  x + y
}

// メソッドの返り値の型は省略可能
// 本文が式1つなら、波括弧は省略可能
def func2(x: Int, y: Int) = x + y
```

Scalaは関数を第一級オブジェクトとして取り扱うことができる。
これは、関数そのものを **値** として、変数に代入したり引数として渡したりできるということ。

```scala
// 関数オブジェクトを作る
val fun = (x: Int, y: Int) => x + y

// 使う
fun(1, 2) //=> 3
```

関数リテラル（ `=>` ）を使うと関数オブジェクトを作り出すことができる。
型推論を利用して変数に関数オブジェクトを代入する場合、型が明らかになるように型注釈が必要となる。
逆に、変数の型が明らかな場合は、型注釈は必要ない。

```scala
// xとyとは曖昧性なくInt型と推論できる。
val fun: (Int, Int) => Int = (x, y) => x + y
```

関数リテラルだけでなく、関数オブジェクトの型を表すのにも `=>` を利用している点に注意せよ。
これは同じ記号だが、出現位置によって意味するものが異なる。
また、match式で使った `=>` も、同じ記号で異なる意味である。
`=>` という記号が現れた時は、それがどういう文脈で現れたのかを注意深く確認してから、その意味を解釈する必要がある。

```scala
// 全て異なる意味の => である。
val fun: Int => String = (i: Int) => i match {
  case 42 => "correct"
}
```

関数リテラルをラムダ式、関数オブジェクトを無名関数とも呼ぶ。
ラムダ式や無名関数が活躍するのは、メソッドや他の関数の引数として渡す時である。

```scala
val fun = (x: Int, y: Int) = x + y

// 関数オブジェクトを引数に取るメソッド
def fortytwo(num: Int, f: (Int, Int) => Int) = f(num, 42)

// 引数として渡す
fortytwo(99, fun) // 141

// その場で関数オブジェクトを作って渡す
fortytwo(2, (x, y) => x * y) // 84
// fortytwoの仮引数の型から、実引数の無名関数の型が推論できるので、xとyとの型注釈が省略できる。
```

### map関数

関数を引数に取る関数を **高階関数** と呼ぶ。
典型的な高階関数として、map関数がある。
map関数は、コレクションの全ての値をそれぞれ関数オブジェクトに適用して、その返り値を集めて新たなコレクションを作る。

ScalaのListにはmapメソッドが存在するので、それを見てみよう。

```scala
val list = 1 :: 2 :: 3 :: Nil

// 数値を取り、2倍にして返す関数オブジェクト
val fun = i: Int => i * 2

// mapメソッドに関数オブジェクトを渡す
list.map(fun) //=> List(2, 4, 6)
// Listの全ての値に fun が適用されている

// その場で無名関数を作って渡す
list.map(i => i + 2) // List(3, 4, 5)
```

* * *

# 平均値を表示するCLIツールを作ってみよう

完成イメージ

```bash
$ scala cli.scala 74 76 81 89 92 87 79 85
mean: 82
```

コマンドに続いて数値を空白区切りで渡すことで、その平均値を算出してくれる。

## コマンドラインから使えるコードを書いてみる

```scala
object Main {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")
  }
}
```

これを `cli.scala` というファイル名で保存して、

```scala
$ scala cli.scala
```

と実行すると、 `Hello, world!` と出力される。
されただろうか？
おめでとう。あなたはScalaプログラミングの第一歩を踏み出した。

### sbtを利用する場合

※ sbtを使わない人は読み飛ばしても良い

Scalaのビルド・パッケージ管理ツール「sbt」を使ってソースをビルドし、実行することもできる。
コマンドラインから、ソースファイルの存在するディレクトリ内で、

```
$ sbt
```

と入力してsbtの対話環境を起動する。
そこで `compile` と入力すると、コンパイルを行う。
`run` と入力すると、コンパイルした後、main関数を探して実行してくれる。

なお、sbtを利用する場合は、専用のディレクトリを作った方が良いだろう。
sbtはディレクトリ内にtargetというディレクトリを作って環境を汚してしまう。

##### 課題 1-1
表示される文字列を変えてみる

## コマンドライン引数を取り扱う

コマンドラインで利用するプログラムは、コマンドライン引数を利用することができる。

Scalaのコマンドライン引数は、main関数に文字列の配列として渡される。
単純に出力してみよう。

```scala
object Main {
  def main(args: Array[String]): Unit = {
    for (arg <- args) {
      println(arg)
    }
  }
}
```

これを `args.scala` というファイル名で保存してみる。

```bash
$ scala args.scala hoge fuga
hoge
fuga

$ scala args.scala 42 99
42
99
```

渡ってきた引数を取り扱うことができた。

##### 課題 2-1
このプログラムを引数を渡さず実行したらどうなるだろう

### 標準入力から読み取る

※ 標準入力を使わない人は読み飛ばしても良い

競技プログラミングなどでは、コマンドライン引数ではなく標準入力から値を読み取るようなことも多い。
Scalaには2.11系から便利な `scala.io.StdIn` が入ったので、これを使ってみよう。

```scala
object Main {
  def main(args: Array[String]): Unit = {
    val input = scala.io.StdIn.readLine
    println(input)
  }
}
```

このコードを `stdin.scala` というファイル名で保存し、動かすと、コンソールが入力待ち状態になる。

```bash
$ scala stdin.scala

```

ここで何か文字列を入力し、エンターを押すと、同じ文字列が出力される。

```bash
$ scala stdin.scala
Hello, world!
Hello, world!
```

これを利用して、標準入力から値を受け取ることができる。
複数の値を渡したい場合は、複数行で渡すか、空白区切りで渡す。

例えば空白区切りの場合、splitメソッドを利用して、

```scala
object Main {
  def main(args: Array[String]): Unit = {
    val input = scala.io.StdIn.readLine.split(" ")
    for (word <- input) println(word)
  }
}
```

のように書くことができる。

```bash
$ scala stdin.scala
hoge fuga piyo
hoge
fuga
piyo
```

##### 課題 2-2
複数回標準入力から受け取るようなプログラムはどのように書けるだろうか。
また、任意の個数を標準入力から受け取るにはどうすればいいだろうか。
考えてみよう。

## 文字列を数値に変換する

コマンドライン引数で渡ってきた値は全て文字列だった。
Scalaは強い型付け言語なので、標準では文字列から数値へと暗黙に変換することはできない。
（なのだが、数値から文字列へは勝手に変換されてしまう……。）

```scala
42 + "99" // 暗黙的にtoStringが呼び出されて "4299" になってしまう！
```

参考までに、「強い型付け言語」と「弱い型付け言語」を、RubyとJavaScriptで比較してみよう。

同じ動的型付け言語でも、強い型付けを持つRubyでは基本的に暗黙の変換はなされない。

```ruby
irb(main):001:0> 42 + "99"
TypeError: String can't be coerced into Fixnum
        from (irb):1:in `+'
        from (irb):1
```

一方で弱い型付け言語であるJavaScriptは、

```js
> 42 + '99'
'4299'
> '7' * 9
63
> 42 * 'hoge'
NaN
```

足し算は勝手に文字列へと変換される点はScalaも同じだが、乗算は、なんと文字列から数値へと暗黙に変換される。
そしてそれが不可能な場合は、NaNになる。

上述したとおり、Scalaは文字列から数値へと勝手に変換することはしない（implicitな関数が定義されている場合を除く）。
文字列から数値（整数）へと変換するには、 `toInt` メソッドを利用する。
また、浮動小数点数に変換するには `toDouble` メソッドを使う。

```scala
42 + "99".toInt // 141

3 * 3 * "3.14".toDouble // 28.26
```

では、コマンドライン引数で渡ってきた数値を、全て2倍にするプログラムを書いてみよう。

```scala
object Main {
  def main(args: Array[String]): Unit = {
    for (arg <- args) {
      println(arg.toInt * 2)
    }
  }
}
```

`number.scala` で保存する。

```bash
$ scala number.scala 42 99 1
84
198
2
```

引数で渡ってきた「文字列扱いの数値」を「数値扱いの数値」にすることができた。

##### 課題 3-1
数値以外の文字列（"hoge"とか）に `toInt` や `toDouble` を使うとどうなるだろうか？

## Array[String] -> List[Double]

これから再帰関数を学ぶにあたって、Arrayは少し使い辛い。
なので、天下り的だが Array から List へ変換してしまう。
この際、上で使った `toDouble` メソッドを使って文字列から数値へ変換してしまう。

### toListメソッド

Array型に対して `toList` メソッドを使うと、 List 型に変換することができる。

```scala
args.toList
```

### mapメソッド

次に map メソッドを使う。
mapメソッドは、コレクションの各要素に関数を適用した結果できた要素からなる新たなコレクションを返す。
これを使って、引数リストの中身全てを Double 型に変換してしまう。

```scala
args.map(_.toDouble)
```

### 合わせる

```scala
args.map(_.toDouble).toList
```

これで、 Array[String] の引数リストを List[Double] に変換することができた。

##### 課題 4-1
実際に、 Array[String] を List[Double] に変換してみよう。
ArrayやListは以下のように作ることができる。

```scala
val arr = Array("1", "2", "3")

val list = List(1.0, 2.0, 3.0)
```

## 平均値とは

さて、ここで最初の目的に立ち返ってみよう。
このCLIアプリの目的は **平均値を求める** ことだ。
平均値（相加平均）は「全ての値の合計 / 値の個数」で求めることができる。

関数型プログラミングの基本は、「単純な処理に分けて考えて、組み上げる」ことである。
なので、「全ての値の合計を出す」処理と、「値の個数を数える」処理に分けて考える。

## sum関数

「全ての値の合計を出す」処理として、sumという関数を作ってみよう。
Scalaのコレクション系オブジェクトにはsumメソッドが既にあるが、ここではそれを使わず、独自の関数を実装してみる。

### 再帰的に考える

さて、要素全ての合計を出す関数、sum関数について考えよう。
どうすれば要素の合計が出せるだろうか？

再帰的思考の基本は、「明らかな場所から考え始める」だ。

要素の合計で、明らかな場所は何だろうか。
それは空リストの場合だ。空リストの合計は明らかに0だろう。

Scalaでは、空リストは `Nil` であった。
なので、 Nil の時の合計 `sum(Nil)` は0である。

空リスト以外のリストは、1要素のリスト、2要素のリスト、3要素のリスト……と無限に存在する。
これを一般化して、n要素のリストとしよう。ここで、nは自然数だ。

nは自然数なので、n要素のリストがあれば、n-1要素のリストが存在することは明白である。
（ n = 1 の時、n-1要素のリストは空リストになる。）

さて、n要素のリストにはn個の要素が入っている。
この要素に 1..n までの番号を振ってみよう。
1..n 個の合計は、 1..n-1 個までの合計に、n番目の要素を足したものと等しい。

`1 + 2 + ... + n - 2 + n - 1 + n = (1 + 2 + ... n - 2 + n - 1) + n`

具体例を出そう。 `3 :: 2 :: 7 :: 5 :: Nil` というリストを考える。
1番目から4番目までの合計（17）は、1番目から3番目までの合計（12）に4番目の要素（5）を足したもの（17）と等しい。

n要素のリストに戻る。
これで、リストの要素を1つ減ずることができた。
もし `n - 1 = 0` なら、空リストの合計は0なので、計算はここで終わりである。
もし `n - 1 > 0` なら、さらに同じ処理を行って、リストの長さを短くしていくことができる。
リストの長さは有限なので、いずれは要素数は0になる。
0になったら、それを逆順に足し合わせていけば、リストの要素の合計を求めることができる。

つまり、sum関数の定義を書き下すと以下のようになる。

```
もし引数が空リストなら、返り値は「0」
もし引数がn要素リストなら、返り値は「n番目の要素」に「1..n-1要素のリストの合計」を加えたもの
```

処理の流れを見てみよう。
なお、Scalaのリストは連結リストなので、最後を取り出すより先頭を切り離す方がやりやすい。
なので、末尾ではなく先頭から処理を進めていく。

```
sum( 3 :: 2 :: 7 :: 5 :: Nil )
3 + sum( 2 :: 7 :: 5 :: Nil )
3 + 2 + sum( 7 :: 5 :: Nil)
3 + 2 + 7 + sum( 5 :: Nil)
3 + 2 + 7 + 5 + sum( Nil )
3 + 2 + 7 + 5 + 0
3 + 2 + 7 + 5
3 + 2 + 12
3 + 14
17
```

### Scalaで記述する

では、この処理をScalaで記述してみる。
関数のシグネチャは以下のような形になる。

```scala
def sum(list: List[Double]): Double = ???
```

引数としてList[Double]を取り、その合計であるDoubleを返す。

リストが空かそうでないかを確かめよう。
また、リストが空でない場合、先頭とそれ以外を切り離す必要がある。

リストが空かどうかの判断はif式でも可能だが、ここではmatch式でパターンマッチを利用する。
何故なら、パターンマッチを使うと「先頭とそれ以外との切り離し」が簡単に行えるからだ。

```scala
def sum(list: List[Double]): Double =
  list match {
    case Nil => ???
    case head :: tail => ???
  }
```

では関数を完成させよう。
まず空リストの場合だが、空リストの合計は0であった。なので、素直にそれを返す。

```scala
def sum(list: List[Double]): Double =
  list match {
    case Nil => 0
    case head :: tail => ???
  }
```

次に空リスト以外の場合。
この場合、「先頭の要素」と「先頭以外の要素の合計」を足し合わせたものが答えになる。
つまり、次のようになる。

```scala
def sum(list: List[Double]): Double =
  list match {
    case Nil => 0
    case head :: tail => head + sum(tail)
  }
```

これで、これでだけで関数は完成である。
望みどおりの挙動をするか、確かめてみよう。

##### 課題 5-1
パターンマッチでできる「分解」は先頭とそれ以外だけではない。
先頭、二番目、それ以外を分離することもできる。

```scala
1 :: 2 :: 3 :: Nil match {
  case a :: b :: rest =>
    println(a) // 1
    println(b) // 2
    println(rest) // List(3)
}
```

ではこの時、要素数が1つのリストを渡したら何が起こるだろうか？


## length関数

さて、sum関数を作ることができたならば、長さを求めるlength関数は難しくないだろう。
考えてみよう。

まず、「明らかな値」は何だろうか？
sum関数では、「空リストなら0」が明らかな値だった。
length関数の明らかな値とは？

実はこれは、sumと同じである。
空リストの長さは0である。

次にn要素のリストの長さを考える。
n要素のリストの長さを、n-1要素のリストの長さで表すには？
リストの長さは要素1個につき1ずつ長くなっていく。つまり、n-1要素のリストに1を足せば、n要素のリストの長さになる。

さて、空リストの際の結果と、n要素リストの結果をn-1要素リストの結果で表す方法がわかったので、これで関数が書けるはずだ。

この処理の流れを、sum関数と同じく `3 :: 2 :: 7 :: 5 :: Nil` で見てみよう。

```
length( 3 :: 2 :: 7 :: 5 :: Nil )
1 + length( 2 :: 7 :: 5 :: Nil )
1 + 1 + length( 7 :: 5 :: Nil )
1 + 1 + 1 + length( 5 :: Nil )
1 + 1 + 1 + 1 + length( Nil )
1 + 1 + 1 + 1 + 0
1 + 1 + 1 + 1
1 + 1 + 2
1 + 3
4
```

そして、これをScalaで書いてみる。

```scala
def length(list: List[Double]): Double =
  list match {
    case Nil => 0
    case _ :: tail => 1 + length(tail)
  }
```

`_` は、Scalaでは多くの意味を持つ記号だが、パターンマッチの中で使うと、「この値は何にでもマッチして、そして後では使わない」という意味を表す。
長さを調べる関数では、リストの要素が何であるかは気にしない。なので。 `_` にマッチさせている。

##### 課題 6-1
「使わない」値のパターンマッチに `_` を使った。
しかしこれは、別に普通の変数を置いても構わない（後で使わなければ良いだけ）。
`_` を使う利点は何か、考えてみよう。

## mean関数

では、平均値を求める関数を書いてみよう。
最初の定義で示した通り、平均値は「リストの要素の合計 / リストの長さ」で求めることができる。

詳しい説明は不要だろう。

```scala
def mean(list: List[Double]): Double = sum(list) / length(list)
```

定義をそのままコードに落とし込んでいるのが分かるだろうか。
関数型の書き方とはこういうものである。

## CLIツールとして仕上げる

では、今まで書いた関数を組み合わせて、実際にCLIツールとして使える形にしてみよう。

```scala
object Main {
  def sum(list: List[Double]): Double =
    list match {
      case Nil => 0
      case head :: tail => head + sum(tail)
    }

  def length(list: List[Double]): Double =
    list match {
      case Nil => 0
      case _ :: tail => 1 + length(tail)
    }

  def mean(list: List[Double]): Double = sum(list) / length(list)

  def main(args: Array[String]): Unit = {
    val list = args.map(_.toDouble).toList
    println(s"mean: ${mean(list)}")
  }
}
```

使ってみよう。

```bash
$ scala cli.scala
mean: 82.875
```

上手く動いただろうか？
もし挙動がおかしい場合、また例外が飛んだ場合は、原因を究明してみよう。


## 共通部分を括りだす

さて、先作った2つの関数を見てみよう。

```scala
def sum(list: List[Double]): Double =
  list match {
    case Nil => 0
    case head :: tail => head + sum(tail)
  }

def length(list: List[Double]): Double =
  list match {
    case Nil => 0
    case _ :: tail => 1 + length(tail)
  }
```

非常によく似ていることが分かるだろうか？
似ている部分がある場合、その共通部分を括りだすことができる。

```scala
def common(list: List[Double]): Double =
  list match {
    case Nil => 0
    case head :: tail => ??? + common(tail)
  }
```

違うのは `???` の部分だけだ。
この処理が、関数によって異なる部分、つまり関数の本質にあたる。
この部分をどのように書けば良いだろうか？

こういう場合、「関数を引数に取る関数」を作ることで、望む挙動を実現できる。

```scala
def common(list: List[Double], f: Double => Double): Double =
  list match {
    case Nil => 0
    case head :: tail => f(head) + common(tail, f)
  }
```

引数を1つ増やし、無名関数を受け取ろう。そう、高階関数である。
この受け取った無名関数に本質的な処理を委譲することにより、共通部分とそうでない部分を上手く分離できる。

これでsum関数とlength関数は、

```scala
def sum(list: List[Double]): Double = common(list, head => head)

def length(list: List[Double]): Double = common(list, _ => 1)
```

と書くことができた。

`head => head` や `_ => 1` は無名関数である。
`head => head` は「引数を1つ取って、それを返す」無名関数。
`_ => 1` は「引数を1つ取って、それを無視して1を返す」無名関数。

##### 課題 7-1
`_ => 1` という無名関数は、 `head => 1` と書くのと同じことである。
何故 `_` を使っているのか、考えてみよう。
また、match式の際に使用した `_` との関係を考えてみよう。

##### 課題 7-2

以下のコードはコンパイルを通る。

```scala
def test(fun: Double => Double) = fun(3.14)

test(_ => 1) // 1.0
```

だが次のように、関数を1度変数に代入してから渡そうとすると、コンパイルエラーとなる。

```scala
val fun = _: Double => 1

test(fun) // エラー！
```

何故だろうか。
また、コンパイルが通るよう修正するにはどうすればいいだろうか。
ヒント: エラーメッセージを注意深く読んでみよう。

##### 課題 7-3
「identity関数」について調べてみよう。
どのように活用できるだろうか？

### fold

おまけとして、fold関数を紹介しよう。
上で書いたcommon関数は、特殊化され過ぎていて汎用性が低いものになってしまっている。
リストに対する処理を一般化したものとしては、fold関数が有名である。
fold関数を使うことで、上で作ったsumやlengthは勿論、mapやfilterのようなよく使われるコレクション系メソッドを自由自在に作ることができる。
是非挑戦してみよう。

```scala
def foldr[A, B](f: (A, B) => B, ini: B, list: List[A]): B =
  list match {
    case Nil => ini
    case head :: tail => f(head, foldr(f, ini, tail))
  }
```

汎用性の為に型引数を取っているが、今回に限っては、Double型に限定してしまってもいいだろう。
参考として、lengthとsumをfoldrで記述した例を下に挙げておく。

```scala
def length(list: List[Double]): Double =
  foldr[Double, Double]((_, i) => i + 1, 0, list)

def sum(list: List[Double]): Double =
  foldr[Double, Double]((a, i) => a + i, 0, list)
```

##### 課題 7-4
foldr関数を利用して、もっと多様な関数を作ってみよう。
例えば以下のような関数がある。

- max関数（リストを取り、リスト内の要素の最大値を返す）
- min関数（リストを取り、リスト内の要素の最小値を返す）
- reverse関数（リストを取り、そのリストの順序を反対にした新たなリストを返す）
- filter関数（真偽値を返す関数と、リストを取り、リストの要素で、その関数に適用した時真を返す要素のみを集めた新たなリストを返す）
- map関数（関数と、リストを取り、リストの全ての要素をその関数に適用し、その返り値を集めた新たなリストを返す）

* * *

## パラメータ化された型

一般的な引数は、実行時に可変な値を受け取り、その値を使って決められた処理を行う。
静的型付言語だと、この引数の静的な型は決められていて、関数に決められた型以外の引数を渡すことはできない。
この制約によって、実行時エラーを起こす可能性のあるコードを事前に排除することができる。

静的型の制約は確かに便利なのだが、汎用的な処理を書いていると、型の決まっていない関数を作りたくなることもある。
そういう時に利用するのが、Javaでは **ジェネリクス** と呼ばれ、Scalaでは **パラメータ化された型** と呼ばれる機能である。
また、パラメータ化された型（型引数）を取る関数を **多相（的）関数** と呼ぶ。

C++などでは典型例としてswap関数を持ち出すが、Scalaでは難しいので、先に作ったlength関数とsum関数を見てみよう。

### length関数

length関数は以下のような実装であった。

```scala
def length(list: List[Double]): Double =
  list match {
    case Nil => 0
    case _ :: tail => 1 + length(tail)
  }
```

（共通部分を括りだした実装もあるが、個別に実装した方を見てみよう。）

この関数はリストの長さを調べることができる。

```scala
val list = List(1.0, 3.14, 2.71)

length(list) // 3
```

では、次のようなリストの長さを測ってみよう。

```scala
val list = List(42, 99, 666)
```

この値をlength関数に与えると何が起こるだろうか。

```
 error: type mismatch;
 found   : List[Int]
 required: List[Double]
       length(list)
```

コンパイルエラーになってしまった。
エラーメッセージにその理由が書いてある。曰く、 **型が合わない** とのことである。
List[Double]型を期待した場所に、List[Int]型の引数を渡してしまっている。

なるほど、確かにlength関数の定義は `List[Double] => Double` なので、型が合っていない。
だが、このlength関数にとってリストの要素の型は、果たして重要であろうか？
リストの要素がDouble型であろうとInt型であろうと、String型であろうとAny型であろうと、要素数を数えるという処理は同じではないだろうか。

こういう要望に答えるのが、パラメータ化された型であり、型引数である。
型引数を使うと、 **型自体をあたかも引数のように受け渡しすることができる** のだ。
実際に使ってみよう。

```scala
def length[A](list: List[A]): Double =
  list match {
    case Nil => 0
    case _ :: tail => 1 + length(tail)
  }
```

修正したlength関数を見てみよう。
まず、関数名の後ろに `[A]` という記述が増えている。これが型引数だ。

通常の引数は丸括弧で囲み、引数名とその型を書くが、型引数は角括弧で囲んで引数名を書く。複数個の型引数を取る時はカンマで区切る。
型引数は慣例としてパスカルケースで書き、わかりやすい名前を充てる。今回のように、汎用的な使い方をする場合は1文字という場合も多い。
引数に取った型変数は、関数内で一般的な型と同じように使える。ただし、new演算子を使ってインスタンスを作ることはできない。

```scala
def func[A, B, C](arga: A, argb: B): C = ...
```

さて、length関数に戻ろう。
パラメータ化された型を持つlength関数は、型引数を取っている（ `[A]` の部分）以外、元の関数実装と違いがない。
これは、length関数が元から、リストの要素の型を気にしていない関数であったからだ。

この型引数版length関数を使ってみよう。
実型引数も、仮型引数と同じように角括弧を使う。

```scala
val listDouble = List(1.0, 3.14, 2.71)
length[Double](listDouble) // 3

val listInt = List(1, 2, 3, 4, 5)
length[Int](listInt) // 5

val listString = List("hoge", "fuga", "piyo")
length[String](listString) // 3
```

同じ実装で、異なる型の値を渡すことができている。
もし型引数が無ければ、似たような実装の関数を幾つも定義しなければならなかったところだ。

またScalaには型推論があるので、実引数からその型が明らかな場合は、敢えて実型引数を書く必要はない。

```scala
val listDouble = List(1.0, 3.14, 2.71)
length(listDouble) // 3

val listInt = List(1, 2, 3, 4, 5)
length(listInt) // 5
```

### 存在型

length関数を、型引数を取る関数に書き換えることができた。
これで、同じ実装を複数の型の間で使いまわすことができる。

しかし、もう一度length関数の実装を見て欲しい。

```scala
def length[A](list: List[A]): Double =
  list match {
    case Nil => 0
    case _ :: tail => 1 + length(tail)
  }
```

型引数として `A` を取っているが、このAは **関数本体の中で使っていない** のだ。
使っていないのなら、こういう引数は無くしてしまうのが好ましい。

好ましいのだが、しかしこの型引数を取らないと、リストの要素の型を書くことができない。
なので仕方なく、使いもしない型引数を取っているのである。

この問題は、単に「コード数が増える」という程度では収まらない。
例えば、「リスト2つと、リストに対する畳込み関数を取って、2つのリストにそれを適用した結果をタプルで返す関数」を考えてみる。
文字に起こすとややこしいが、以下のようになる。

```scala
def twoList[A, B, C](list1: List[A], list2: List[B], func: List[???] => C): (C, C) =
  (func(list1), func(list2))
```

この時、3つめの引数の型はどのようになるだろうか？
この関数はList[A]に対してもList[B]に対しても適用できないといけないのだが、そんな型は表せない。
とはいえ、たとえばlength関数ならリストの要素の型が何であろうと利用できるので、この関数に渡せる筈である。

こういう場合に利用できるのが、存在型である。

```scala
def length(list: List[_]): Double =
  list match {
    case Nil => 0
    case _ :: tail => 1 + length(tail)
  }
```

存在型を使ったlength関数の実装は上のようになる。
型引数が消えている。代わりにListには `[_]` という型引数が与えられている。
これは文字通り、「ある型Aが存在すること」という制約を意味する。
今回は型Aに何の制約も加えられていないので、どんな型でも取ることができるが、何らかの制約を加えることもできる。

```scala
def func(list: List[_ <: AnyVal]) = ...
```

`A <: B` は「AはBのサブ型」という制約。この制約は型引数に書くこともできる。

これで、異なる要素の型を持つリストに対して使うことができる。

```scala
twoList(List(1,2,3,4,5), List("hoge","fuga","piyo"), length) // (5.0, 3.0)
```

##### 課題 8-1
リストのリストを取り、要素のリストの長さを全て合計する関数を作ってみよう。

### sum関数

次はsum関数だ。以下のような実装であった。

```scala
def sum(list: List[Double]): Double =
  list match {
    case Nil => 0
    case head :: tail => head + sum(tail)
  }
```

これはDouble型のListの要素を全て足し合わせる関数だった。

```scala
val list = List(1.0, 3.14, 2.71)

sum(list) // 6.85
```

`List(42, 99, 666)`　というリストを、この関数に適用できるだろうか？

```scala
val list = List(42, 99, 666)

sum(list)
// error: type mismatch;
// found   : List[Int]
// required: List[Double]
//       sum(list)
```

できない。length関数の時と同じように、型が合わないと言われてしまった。
しかし、 **数を足し合わせる** という処理は、DoubleであろうとIntであろうと変わらないはずだ。

では、lengthの時のようにパラメータ化された型を利用して、sum関数をIntでもDoubleでも引数に取るように修正できるであろうか。

```scala
def sum[A](list: List[A]): A =
  list match {
    case Nil => 0
    case head :: tail => head + sum(tail)
  }
```

コンパイルしてみよう。
……コンパイルエラーが出てしまった。

```
  type mismatch;
  found   : Int(0)
  required: A
       case Nil => 0
                   ^
  type mismatch;
  found   : List[A]
  required: List[String]
       case head :: tail => head + sum(tail)
                                       ^
```

何がいけなかったのだろうか。

### 数値とモノイド

一般的な引数は実行時に渡されるが、型引数はコンパイル時に解決される。
コンパイラはパラメータ化された型も含めて型検査を行い、コードの正しさを検証する。

というわけで、sum関数を渡されたScalaコンパイラは型を解決しようとする。
まず、関数のヘッドの部分を見て、

```scala
def sum[A](list: List[A]): A =
```

「型引数としてAを受け取り、要素がA型のリストを引数として受け取り、A型の値を返す」関数であると確認する。
次に実装を確認するのだが、

```scala
  case Nil => 0
```

この部分で、型の条件の違反を見つける。
というのも、関数の返り値はA型であるはずなのに、ここではInt型を返しているからだ。
（Scalaの数値リテラルは、明示的に片付けされた変数に入れる時はその変数に応じて適切な型に自動で変換されるのだが、パラメータ化された型の場合はその仕組みが働かない。）

次の行でも、

```scala
case head :: tail => head + sum(tail)
```

`+` という演算子（実体はメソッド）が使われているが、この時型引数として取っているA型に `+` 演算子が定義されている保証は無い。

以上の理由から、単純に型引数を取るだけではsum関数を多相関数にすることはできない。
sum関数が取ることができる型には条件があるのだ。

条件は以下の2つ

- その型には0があること
- その型は足し合わせることができること

このような性質（単位元、結合律）を持つ集合を一般的に **モノイド** と呼ぶ。
（ちなみに単位元を条件の中に入れない場合は半群となり、逆元を要求すれば群となる。）

### 多相と型クラス

型引数に条件を付ける方法は2つある。

- 型境界を指定する
- 型クラスを用いる

型境界と言うのは、上で触れた `<:` といった記号を使う方法で、引数型がある型のサブ型であるといった条件を付すことができる。
たとえば、

```scala
trait Addable {
  def +(other: Addable): Addable
}

def add[T <: Addable](a: T, b: T) = a + b
```

とすれば、型引数で受け取ったTが間違いなく `+` メソッドを持っているという保証があるので、 `+` メソッドを使うことができる。
この関数に渡せるのは、Addableを実装した型だけである。

しかしScalaの型には、モノイドどころか、IntやDoubleといった数値系の型に対しても、共通の先祖クラスは用意されていない。
なので、今回は型境界ではなく型クラスを使って多相を実現しよう。
というのも、Scalaには数値系のクラスを表すNumeric型クラスが標準で用意されているからだ。

型クラスというのは、Haskellで用いられている多相の手段で、Scalaでは（言語機能ではなく）一種のデザインパターンとして導入されている。
型クラスの利用にはimplicitの理解が欠かせないが、ここでは使い方のみを簡単に記す。

まず、ある型が、何らかの型クラスのインスタンスであることの制約を書かなければならない。
（型クラスとオブジェクト指向言語で言うクラス、また型クラスのインスタンスとオブジェクト指向言語で言うインスタンスは、 **全くの別物** であることに注意。）
この制約は、Scalaでは次のように書く。

```scala
def sum[A](list: List[A])(implicit ev: Numeric[A]): A =
```

カリー化された2つめのimplicitな引数を取り、その型を `型クラス[A]` とする。
これで、Aがその型クラスのインスタンスであること、という制約を付けることができる（evはevidenceのこと）。

型クラスにはメソッドが用意されている。型クラスの制約を付けると、関数内でその型クラスのメソッドを使うことができる。
（ここでいうメソッドも、Rubyなどのオブジェクト指向言語でいうメソッドとは異なるものである。）
Numeric型クラスにはどのようなメソッドが用意されているだろうか。
ソースを見てみよう。

```scala
trait Numeric[T] extends Ordering[T] {
  def plus(x: T, y: T): T
  def minus(x: T, y: T): T
  def times(x: T, y: T): T
  def negate(x: T): T
  def fromInt(x: Int): T
  def toInt(x: T): Int
  def toLong(x: T): Long
  def toFloat(x: T): Float
  def toDouble(x: T): Double

  def zero = fromInt(0)
  def one = fromInt(1)

  def abs(x: T): T = if (lt(x, zero)) negate(x) else x

  ...
```

さて、sum関数の型引数の条件は何だったろうか。
それは「0があり、足し合わせることができる」ことだった。
上のメソッド群を見ると、plusメソッドやzeroメソッドが見えるので、Numeric型クラスはこの条件を満たしていることがわかる。
（実際は、sum関数に対してNumeric型クラスを要求するのは **厳しすぎる** と言って良い。上で触れたように、モノイドで充分なのだ。が、今回は話をややこしくしないためにScalaで標準ライブラリに入っているNumeric型クラスを利用する。）

では、Numeric型クラスを利用していこう。

```scala
def sum[A](list: List[A])(implicit ev: Numeric[A]): A =
  list match {
    case Nil => ev.zero
    case head :: tail => ev.plus(head, sum(tail))
  }
```

これが、型クラスを使った多相版のsum関数である。

型クラスのメソッドを使う場合、制約にも利用した型クラスの引数（ここでは `ev` ）を利用する。
このevのメソッド（このメソッドは、いわゆるオブジェクト指向言語のメソッド）として、型クラスのメソッドを呼び出すことができる。

使ってみよう。

```scala
val listDouble = List(1.0, 3.14, 2.71)
sum(listDouble) // 6.85

val listInt = List(1, 2, 3, 4, 5)
sum(listInt) // 15
```

異なる型に対して、同じように動作している。
多相的なsum関数を作ることができた。

##### 課題 9-1
モノイドとなる型にはどのようなものがあるだろうか。思いつくものを挙げてみよう。単位元と結合演算子はそれぞれ何だろうか。
また、半群であってモノイドではないのはどのようなものだろうか。

##### 課題 9-2
多相版sum関数は、次のように書くこともできる。

```scala
def sum[A: Numeric](list: List[A]): A =
  list match {
    case Nil => implicitly[Numeric[A]].zero
    case head :: tail => implicitly[Numeric[A]].plus(head, sum(tail))
  }
```

ここで使われている `[A: Numeric]` という書き方は、 `context bound` という機能である。
これは型クラスを使いやすくする為の糖衣構文として用意されている。
どのような挙動なのか、なぜ上のように書けるのか、調べて、考えてみよう。

なお、プログラムリスト中で出てきているimplicitly関数の定義は以下の通りである。

```scala
def implicitly[T](implicit e: T) = e
```

##### 課題 9-3
Scalaは型引数に制約を加える方法として、上限・下限境界を用いる方法と、型クラスを利用する方法がある。
それぞれどのような利点と欠点があるだろうか。考えてみよう。
