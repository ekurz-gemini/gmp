package edu.gemini.aspen.gds.staticheaderreceiver

import edu.gemini.aspen.giapi.data.{DataLabel, FitsKeyword}
import actors.Actor
import java.util.logging.Logger
import edu.gemini.aspen.gds.keywords.database.{StoreProgramId, ProgramIdDatabase}

object RequestHandler extends Actor {
  private val LOG = Logger.getLogger(this.getClass.getName)

  private var keywordsDatabase: TemporarySeqexecKeywordsDatabase = _
  private var programIdDB: ProgramIdDatabase = _

  def setDatabases(keywordsDatabase: TemporarySeqexecKeywordsDatabase, programIdDB: ProgramIdDatabase) {
    this.keywordsDatabase = keywordsDatabase
    this.programIdDB = programIdDB
  }


  def act() {
    loop {
      react {
        case InitObservation(programId, dataLabel) => initObservation(programId, dataLabel)
        case StoreKeyword(dataLabel, keyword, value) => storeKeyword(dataLabel, keyword, value)
        case x: Any => throw new RuntimeException("Argument not known: " + x)
      }
    }
  }

  def initObservation(programId: String, dataLabel: DataLabel) {
    LOG.info("Program ID: "+programId+" Data label: "+dataLabel)
    programIdDB ! StoreProgramId(dataLabel, programId)
  }

  def storeKeyword(dataLabel: DataLabel, keyword: FitsKeyword, value: AnyRef) {
    LOG.info("Data label: "+dataLabel+" Keyword: "+keyword+" Value: "+value)
    keywordsDatabase ! StoreKeyword(dataLabel, keyword, value)
  }
}