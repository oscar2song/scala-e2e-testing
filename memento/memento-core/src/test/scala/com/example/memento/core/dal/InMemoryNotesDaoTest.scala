package com.example.memento.core.dal

import com.example.memento.core.model.Note
import com.example.memento.testkit.FutureTestingSupport
import com.example.memento.testkit.matchers.NoteMatchers
import java.util.UUID
import org.specs2.mutable.Specification
import org.specs2.specification.Scope

class InMemoryNotesDaoTest extends Specification with FutureTestingSupport with NoteMatchers {

  trait Context extends Scope {
    val dao = new InMemoryNotesDao()
  }

  "In-memory notes dao" should {
    "return None for a non-existing note" in new Context {
      val note: Option[Note] = dao.get(UUID.randomUUID)
      note must beNone
    }

    "add and return a note" in new Context {
      val id: UUID = dao.add("Hello world")
      val note: Option[Note] = dao.get(id)
      note must beSome[Note]
      note must haveText("Hello world")
    }
  }
}
