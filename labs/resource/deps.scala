import scala.util.Random
import chisel3.reflect.DataMirror

import scala.reflect.ClassTag
import chisel3.util.Cat
import chisel3.experimental.hierarchy.{Definition, Instance, instantiable, public}

implicit def bool2bigint(b: Boolean): BigInt = if (b) 1 else 0

  def setRandomInput(io: Bundle, r: Random) = {
    io.getElements
      .filter(data =>
        DataMirror.specifiedDirectionOf(data) == SpecifiedDirection.Input
      )
      .foreach {
        case bool: Bool =>
          bool.poke(r.nextBoolean().B)
        case uint: UInt =>
          uint.poke(BigInt(uint.getWidth, r).U)
        case sint: SInt =>
          sint.poke((BigInt(sint.getWidth, r)-(1 << (sint.getWidth-1))).S) //quick hack
        case _ =>
      }
  }


class HalfAdder extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    val sum = Output(UInt(1.W))
    val carry = Output(UInt(1.W))
  })

  val sum = io.a ^ io.b
  val carry = io.a & io.b

  io.sum := sum
  io.carry := carry
}

class FullAdder extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(1.W))
    val b = Input(UInt(1.W))
    val cin = Input(UInt(1.W))
    val sum = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  val halfAdder1 = Module(new HalfAdder)
  val halfAdder2 = Module(new HalfAdder)

  halfAdder1.io.a := io.a
  halfAdder1.io.b := io.b
  halfAdder2.io.a := halfAdder1.io.sum
  halfAdder2.io.b := io.cin

  io.sum := halfAdder2.io.sum
  io.cout := halfAdder1.io.carry | halfAdder2.io.carry
}

@instantiable
abstract class Adder(bitWidth: Int) extends Module {
  @public val io = IO(new Bundle {
    val a = Input(UInt(bitWidth.W))
    val b = Input(UInt(bitWidth.W))
    val cin = Input(Bool())
    val sum = Output(UInt((bitWidth+1).W))
    // val cout = Output(Bool())
  })
}

class RippleCarryAdder(bitWidth: Int) extends Adder(bitWidth) {
  val adders = Seq.fill(bitWidth)(Module(new FullAdder))

  adders(0).io.a := io.a(0)
  adders(0).io.b := io.b(0)
  adders(0).io.cin := io.cin

  for (i <- 1 until bitWidth) {
    adders(i).io.a := io.a(i)
    adders(i).io.b := io.b(i)
    adders(i).io.cin := adders(i - 1).io.cout
  }

  // io.sum := VecInit(adders.map(_.io.sum) :+ adders.last.io.cout).asUInt
  // io.sum := Cat(adders.last.io.cout, VecInit(adders.map(_.io.sum)).asUInt)
  io.sum := Cat(adders.last.io.cout, adders.map(_.io.sum).reverse.reduce(Cat(_, _)))
}


class ParallelPrefixSum[T <: Data](dataGen: => T, width: Int, associativeOperation: (T, T) => T) extends Module {
  val io = IO(new Bundle {
    val input = Input(Vec(width, dataGen))
    val output = Output(Vec(width, dataGen))
  })

  if (width == 1) {
    io.output := io.input
  } else {
    val subParallelPrefixSum = Module(new ParallelPrefixSum(dataGen, width / 2, associativeOperation))
    subParallelPrefixSum.io.input := VecInit((0 until width/2).map(i => associativeOperation(io.input(i*2+1), io.input(i*2))))
    val subOutput = subParallelPrefixSum.io.output
    val innerOutput = (0 until (width/2 - 1)).flatMap(i => Seq(subOutput(i), associativeOperation(io.input((i+1)*2), subOutput(i))))
    val subOutputLast = subOutput(width/2-1)
    if (width % 2 == 0) {
      io.output := VecInit(Seq(io.input(0)) ++ innerOutput ++ Seq(subOutputLast))
    } else {
      val extraOutput = associativeOperation(io.input(width - 1), subOutputLast)
      io.output := VecInit(Seq(io.input(0)) ++ innerOutput ++ Seq(subOutputLast, extraOutput))
    }
  }
}

class CarryLookaheadAdder(bitWidth: Int) extends Adder(bitWidth) {
  val associativeOperation = (ax: Vec[Bool], bx: Vec[Bool]) => {
    val g = ax(0)
    val p = ax(1)
    val g1 = bx(0)
    val p1 = bx(1)
    VecInit(g | (p & g1), p & p1)
  }
  val gp = VecInit((0 until bitWidth).map(i => VecInit(Seq(io.a(i) & io.b(i), io.a(i) ^ io.b(i)))))
  val parallelPrefixSum = Module(new ParallelPrefixSum(Vec(2, Bool()), bitWidth, associativeOperation))
  parallelPrefixSum.io.input := gp
  val prefixSum = parallelPrefixSum.io.output

  def cj(j: Int): Bool = {
    if (j == -1) {
      return io.cin
    }
    val gj0 = prefixSum(j)(0)
    val pj0 = prefixSum(j)(1)
    gj0 | (pj0 & io.cin)
  }

  io.sum := VecInit((0 until bitWidth).map(i => cj(i-1) ^ io.a(i) ^ io.b(i)) :+ cj(bitWidth-1)).asUInt
}