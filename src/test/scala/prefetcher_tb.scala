
package prefetchTB

import chisel3._
import chisel3.util._
import chiseltest._
//import connect._
import Prefetcher._
import org.scalatest.flatspec.AnyFlatSpec

class general extends AnyFlatSpec with ChiselScalatestTester {

"general_test" should "work" in {
  test(new prefetcher("src/test/programs/prefetchMem_test")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      //dut.clock.setTimeout(0)


      //
      //MULTI-WAY-PREFETCHER
      //
      //first fetch on all empty buffers, load buffer 3
      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(true.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.result.expect(19.U)//fetch in 1 cycle
      dut.io.valid.expect(true.B)

      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      //then wait until buffer half full and sequential fetch
      dut.clock.step(1)
      for(i<- 0 until 2){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      dut.io.missAddress.poke(1.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(1.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(1.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(4194451.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      //wait until buffer full and sequential fetch
      for(i<- 0 until 10){
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.io.valid.expect(false.B)
          dut.clock.step(1)
      }

      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(true.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.io.missAddress.poke(2.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(2.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(5243155.U)//is in buffer
      dut.clock.step(1)
      dut.io.valid.expect(false.B)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.clock.step(1)

      for(i<- 0 until 3){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      //non sequential  fetch to load buffer 2
      dut.io.missAddress.poke(5.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(5.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(5.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1118899.U)//fetch in 1 cycle

      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      //then wait until buffer half full and sequential fetch
      dut.clock.step(1)
      for(i<- 0 until 2){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      dut.io.missAddress.poke(6.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(6.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1123123.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)


      //wait until buffer full and sequential fetch
      dut.clock.step(1)
      for(i<- 0 until 10){
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.io.valid.expect(false.B)
          dut.clock.step(1)
      }

      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(true.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2139059.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 3){
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.io.valid.expect(false.B)
          dut.clock.step(1)
      }

      //hit on buffer 3
      dut.io.missAddress.poke(3.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(3.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2130355.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 3){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }




      //miss to load buffer 1
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(19.U)//fetch in 1 cycle

      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      //then wait until buffer half full and sequential fetch
      dut.clock.step(1)
      for(i<- 0 until 2){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      dut.io.missAddress.poke(1.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(1.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(4194451.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)


      //wait until buffer full and sequential fetch
      dut.clock.step(1)
      for(i<- 0 until 10){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      dut.io.missAddress.poke(2.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(2.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(5243155.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 4){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      //hit on buffer 3
      dut.io.missAddress.poke(4.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(4.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1074889267.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 3){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }



      //miss to load buffer 0
      dut.io.missAddress.poke(11.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(11.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.io.missAddress.poke(11.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2147763.U)//fetch in 1 cycle

      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      //then wait until buffer half full and sequential fetch
      dut.clock.step(1)
      for(i<- 0 until 2){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      dut.io.missAddress.poke(12.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(12.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1234483.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      //wait until buffer full and sequential fetch
      dut.clock.step(1)
      for(i<- 0 until 10){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      dut.io.missAddress.poke(13.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.io.missAddress.poke(13.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1081136819.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 2){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      //hit on buffer 3
      dut.io.missAddress.poke(5.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(5.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1118899.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 3){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }



      //lru  should be buffer 2, miss on all to flush buffer 2 and load new instructions
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)//flush
      dut.clock.step(1)

      dut.io.missAddress.poke(0.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(19.U)//fetch after flush

      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      for(i<- 0 until 10){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      //test if buffer 2 is loaded correctly
      dut.io.missAddress.poke(1.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(1.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(4194451.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 3){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }



      //new lru should be buffer 1, miss on all to flush buffer 1 and load new instructions
      dut.io.missAddress.poke(9.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(9.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)

      dut.io.missAddress.poke(9.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)//flush

      dut.clock.step(1)
      dut.io.missAddress.poke(9.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2143411.U)//fetch after flush

      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      for(i<- 0 until 10){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      //test if buffer 1 is loaded correctly
      dut.io.missAddress.poke(10.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(10.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1127731.U)//is in buffer
      dut.clock.step(1)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)

      dut.clock.step(1)
      for(i<- 0 until 10){
          dut.io.valid.expect(false.B)
          dut.io.cacheBusy.poke(false.B)
          dut.io.cacheValid.poke(false.B)
          dut.clock.step(1)
      }

      dut.io.valid.expect(false.B)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.clock.step(1)
      dut.io.valid.expect(false.B)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.clock.step(1)
      dut.io.valid.expect(false.B)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(true.B)
      dut.clock.step(1)
      dut.io.valid.expect(false.B)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.clock.step(1)
      dut.io.valid.expect(false.B)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.clock.step(1)

      //check every element in the buffers, no fetch inbetween
      //buffer 0
      dut.io.missAddress.poke(14.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(14.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1173299.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(15.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(15.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1144755.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(16.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(16.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(19.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(17.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(17.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(4194451.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(18.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(18.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(5243155.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(19.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(19.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2130355.U)//is in buffer
      dut.clock.step(1)

      //buffer 1
      dut.io.missAddress.poke(11.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(11.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2147763.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(12.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(12.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1234483.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(13.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(13.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1081136819.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(14.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(14.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1173299.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(15.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(15.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1144755.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(16.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(16.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(19.U)//is in buffer
      dut.clock.step(1)

      //buffer 2
      dut.io.missAddress.poke(2.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(2.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(5243155.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(3.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(3.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2130355.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(4.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(4.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1074889267.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(5.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(5.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1118899.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(6.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(6.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1123123.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2139059.U)//is in buffer
      dut.clock.step(1)

      //buffer 3
      dut.io.missAddress.poke(6.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(6.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1123123.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(7.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2139059.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(8.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(8.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2171955.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(9.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(9.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2143411.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(10.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(10.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(1127731.U)//is in buffer
      dut.clock.step(1)

      dut.io.missAddress.poke(11.U)
      dut.io.cacheBusy.poke(true.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(false.B)
      dut.clock.step(1)
      dut.io.missAddress.poke(11.U)
      dut.io.cacheBusy.poke(false.B)
      dut.io.cacheValid.poke(false.B)
      dut.io.valid.expect(true.B)
      dut.io.result.expect(2147763.U)//is in buffer
      dut.clock.step(1)

      /*
0: 00000013     19
1: 00400093     4194451
2: 00500113     5243155
3: 002081b3     2130355
4: 40118233     1074889267
5: 001112b3     1118899
6: 00112333     1123123
7: 0020a3b3     2139059
8: 00212433     2171955
9: 0020b4b3     2143411
10: 00113533    1127731
11: 0020c5b3    2147763
12: 0012d633    1234483
13: 4070d6b3    1081136819
14: 0011e733    1173299
15: 001177b3    1144755
16: 00000013     19
17: 00400093     4194451
18: 00500113     5243155
19: 002081b3     2130355
20: 40118233     1074889267
21: 001112b3     1118899
22: 00112333     1123123
23: 0020a3b3     2139059
24: 00212433     2171955
25: 0020b4b3     2143411
26: 00113533    1127731
27: 0020c5b3    2147763
28: 0012d633    1234483
29: 4070d6b3    1081136819
30: 0011e733    1173299
31: 001177b3    1144755
 */

    }
  }

}

class prefetchTestLinear extends AnyFlatSpec with ChiselScalatestTester {

    "linear_test" should "pass" in {
        test(new prefetcher("src/test/programs/prefetchMem_test")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
/*
            dut.clock.step(1)
            dut.io.missAddress.poke(0.U)
            dut.io.cacheBusy.poke(false.B)
            dut.io.cacheValid.poke(false.B)
            dut.io.result.expect(19.U)
            dut.io.valid.expect(true.B)

            dut.clock.step(1)
            for(i<- 0 until 2){

            }
 */

        }
    }
}


