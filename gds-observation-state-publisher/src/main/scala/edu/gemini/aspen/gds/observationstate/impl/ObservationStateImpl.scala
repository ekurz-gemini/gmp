package edu.gemini.aspen.gds.observationstate.impl

import org.apache.felix.ipojo.annotations.{Requires, Provides, Instantiate, Component}
import edu.gemini.aspen.giapi.data.{FitsKeyword, DataLabel}
import org.scala_tools.time.Imports._
import edu.gemini.aspen.gds.observationstate.{ObservationStatePublisher, ObservationStateProvider, ObservationStateRegistrar}
import java.util.concurrent.TimeUnit._
import scala.collection.JavaConversions._
import com.google.common.collect.MapMaker
import edu.gemini.aspen.gds.api.CollectionError
import collection.mutable.{SynchronizedStack, SynchronizedSet, HashSet, Set, ConcurrentMap}

@Component
@Instantiate
@Provides(specifications = Array(classOf[ObservationStateRegistrar], classOf[ObservationStateProvider]))
class ObservationStateImpl(@Requires obsStatePubl: ObservationStatePublisher) extends ObservationStateRegistrar with ObservationStateProvider {
  // expiration of 1 day by default but tests can override it
  def expirationMillis = 24 * 60 * 60 * 1000

  class ObservationInfo {
    val missingKeywords: Set[FitsKeyword] = new HashSet[FitsKeyword] with SynchronizedSet[FitsKeyword]
    val errorKeywords: Set[(FitsKeyword, CollectionError.CollectionError)] = new HashSet[(FitsKeyword, CollectionError.CollectionError)] with SynchronizedSet[(FitsKeyword, CollectionError.CollectionError)]
    val times: Set[(AnyRef, Option[Duration])] = new HashSet[(AnyRef, Option[Duration])] with SynchronizedSet[(AnyRef, Option[Duration])] //todo: think which is the correct type here
    var started = false
    var ended = false
    var inError = false
  }

  val obsInfoMap: ConcurrentMap[DataLabel, ObservationInfo] = new MapMaker().
    expireAfterWrite(expirationMillis, MILLISECONDS)
    .makeMap[DataLabel, ObservationInfo]()

  val lastDataLabels = new SynchronizedStack[DataLabel]()

  override def registerMissingKeyword(label: DataLabel, keywords: Traversable[FitsKeyword]) {
    obsInfoMap.getOrElseUpdate(label, new ObservationInfo).missingKeywords ++= keywords
    if (!keywords.isEmpty) {
      obsInfoMap.getOrElseUpdate(label, new ObservationInfo).inError = true
    }
  }

  //todo: use cause for something
  override def registerError(label: DataLabel, cause: String) {
    obsInfoMap.getOrElseUpdate(label, new ObservationInfo).inError = true
  }

  override def registerCollectionError(label: DataLabel, errors: Traversable[(FitsKeyword, CollectionError.CollectionError)]) {
    obsInfoMap.getOrElseUpdate(label, new ObservationInfo).errorKeywords ++= errors
    if (!errors.isEmpty) {
      obsInfoMap.getOrElseUpdate(label, new ObservationInfo).inError = true
    }
  }

  override def registerTimes(label: DataLabel, times: Traversable[(AnyRef, Option[Duration])]) {
    obsInfoMap.getOrElseUpdate(label, new ObservationInfo).times ++= times
  }

  override def endObservation(label: DataLabel) {
    obsInfoMap.getOrElseUpdate(label, new ObservationInfo).ended = true
    lastDataLabels.push(label)
    obsStatePubl.publishEndObservation(label, getMissingKeywords(label), getKeywordsInError(label))

  }

  override def startObservation(label: DataLabel) {
    obsInfoMap.getOrElseUpdate(label, new ObservationInfo).started = true
    obsStatePubl.publishStartObservation(label)
  }

  //-----------------------------------------------------------------------

  override def isInError(label: DataLabel): Boolean = {
    obsInfoMap.getOrElse(label, new ObservationInfo).inError
  }

  override def getLastDataLabel(n: Int): Traversable[DataLabel] = {
    lastDataLabels.take(n)
  }

  override def getTimes(label: DataLabel): Traversable[(AnyRef, Option[Duration])] = {
    obsInfoMap.getOrElse(label, new ObservationInfo).times
  }

  override def getMissingKeywords(label: DataLabel): Traversable[FitsKeyword] = {
    obsInfoMap.getOrElse(label, new ObservationInfo).missingKeywords
  }

  override def getKeywordsInError(label: DataLabel): Traversable[(FitsKeyword, CollectionError.CollectionError)] = {
    obsInfoMap.getOrElse(label, new ObservationInfo).errorKeywords
  }

  override def getObservationsInProgress: Traversable[DataLabel] = {
    obsInfoMap filter {
      case (key, value) => (value.started && !value.ended)
    } keySet
  }

  override def getLastDataLabel: Option[DataLabel] = {
    lastDataLabels.headOption
  }
}