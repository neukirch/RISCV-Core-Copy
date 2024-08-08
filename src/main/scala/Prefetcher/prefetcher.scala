package Prefetcher

import chisel3._
import chisel3.experimental.ChiselEnum
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

import ICache._
import DCache.Cache
import InstructionMemory._

class prefetcher (IMemFile: String) extends Module {
  val io = IO(new Bundle {
    val missAddress = Input(UInt(32.W)) // address to be fetched and saved
    val cacheBusy = Input(Bool())
    val cacheValid = Input(Bool())
    val result = Output(UInt(32.W))
    val valid = Output(Bool())
  })

  val IMem = Module(new InstructionMemory(IMemFile))
  IMem.io.instructionAddress := io.missAddress
  io.result := IMem.io.instruction
  IMem.testHarness.setupSignals.setup := 0.B
  IMem.testHarness.setupSignals.address := 0.U
  IMem.testHarness.setupSignals.instruction := 0.U



  val waitBusy :: waitValid :: direct :: fetch :: flush :: Nil = Enum(5)
  val state = RegInit(waitBusy)
  val nextAddress = RegInit(VecInit(Seq.fill(4)(0.U(32.W))))

  io.result := 0.U
  io.valid := false.B

  //multi-way prefetcher
  val buffer = VecInit(Seq.fill(4)(Module(new streamBuffer(6, 65)).io))
  for(i <- 0 until 4){
    buffer(i).flush := false.B//set flush to false so it doesnt go undefined or wrongly flushes

    // Connecting enqueue interface
    buffer(i).enq.valid := false.B // Set to true when data is available for enqueue
    buffer(i).enq.bits := 0.U // Data to be enqueued

    // Connecting dequeue interface
    buffer(i).deq.ready := false.B // Set to true when ready to receive data
  }

  val fetchBuf = RegInit(0.U(2.W))
  val fetchBufWire = Wire(UInt(2.W))
  fetchBufWire:= 0.U

  val flushCond1 = Wire(UInt(1.W)) // 0 flush, 1 fetch, 2 direct
  flushCond1 := 0.U
  val flushCond2 = Wire(UInt(1.W)) // 0 flush, 1 fetch, 2 direct
  flushCond2 := 0.U

  val leastU = Module(new lruModule)
  leastU.io.flush := false.B
  leastU.io.usedValid := false.B
  leastU.io.used := 0.U


  switch(state) {
    is(waitBusy) {
      when(io.cacheBusy === true.B) {
        state := waitValid
      }
    }
    is(waitValid) {
    //is(waitMiss) {
      when(io.cacheBusy === false.B) {
        //check if a buffer is empty
        for (i <- 0 until 4) {
          when(buffer(i).count === 0.U) {//if empty
            fetchBufWire := i.U// save which  one is empty
            flushCond2 := 1.U //condition to show a buffer is empty
          }
        }

        //check if missAddress is at top of a buffer
        for (i <- 0 until 4) {
          when(buffer(i).head === io.missAddress && buffer(i).count =/= 0.U) { //when hit and buffer not empty
            fetchBufWire := i.U// save which  one is a hit, can overwrite the empty one because hit is more important
            flushCond1 := 1.U //condition to show a buffer is hit
          }
        }

        fetchBuf := fetchBufWire//register to save which buffer to use

        when(flushCond1 === 0.U && flushCond2 === 0.U){ //no buffer empty and no hit -> go to flush
          leastU.io.flush := true.B //set signal to true to get which is lru to flush

          state := flush //go to flush state

        }.elsewhen(flushCond1 === 0.U && flushCond2 === 1.U){ //if a buffer is empty and we dont have a hit
          IMem.io.instructionAddress := io.missAddress << 2.U //set address for imem to missAddress
          nextAddress(fetchBufWire) := io.missAddress + 1.U
          state := direct//go to direct state

        }.otherwise{//if we have hit
          buffer(fetchBufWire).deq.ready := true.B //start dequeue
          io.result := buffer(fetchBufWire).deq.bits(31, 0) //output data
          io.valid := true.B //show that output is valid data
          IMem.io.instructionAddress := nextAddress(fetchBufWire) << 2.U //set next address to fetch
          state := fetch//go to fetch state
        }
      }.elsewhen(io.cacheValid === true.B){
        state := waitBusy
      }
    }
    is(direct) {
      //update lru
      leastU.io.usedValid := true.B
      leastU.io.used := fetchBufWire

      io.result := IMem.io.instruction //output result
      io.valid := true.B //show that output is valid data

      IMem.io.instructionAddress := (io.missAddress + 1.U)<< 2.U //set next address to fetch
      state := fetch //next state, prefetch state
    }
    is(fetch) { //prefetch state
      when(buffer(fetchBuf).count =/= 6.U && io.cacheBusy === false.B) { //until buffer is full or new cache miss occurs
        //update lru
        leastU.io.usedValid := true.B
        leastU.io.used := fetchBuf

        //enqueue
        buffer(fetchBuf).enq.valid := true.B
        buffer(fetchBuf).enq.bits := Cat(nextAddress(fetchBuf), 1.U, IMem.io.instruction)
        IMem.io.instructionAddress := (nextAddress(fetchBuf) + 1.U)<< 2.U//next address to fetch
        nextAddress(fetchBuf) := nextAddress(fetchBuf) + 1.U
      }.elsewhen(io.cacheBusy === true.B) {
        state := waitValid
      }.otherwise { //if no miss occurs and the buffer is fully  fetched
        state := waitBusy //idle state
      }
    }

    is(flush) { //flush state
      //update  lru
      buffer(leastU.io.out).flush := true.B
      nextAddress(leastU.io.out) := io.missAddress + 1.U
      fetchBuf := leastU.io.out//save which buffer was flushed to fetch into it
      IMem.io.instructionAddress := io.missAddress << 2.U
      state := direct
    }
  }

}