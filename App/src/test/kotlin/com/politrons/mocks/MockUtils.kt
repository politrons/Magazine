package com.politrons.mocks

import arrow.fx.IO
import com.politrons.dao.CopyWriterDAO
import com.politrons.dao.EditorDAO
import com.politrons.dao.JournalistDAO
import com.politrons.events.ArticleDraftCreatedEvent
import com.politrons.events.MagazineCreatedEvent
import com.politrons.events.MagazineEvent
import com.politrons.events.SuggestionAddedEvent
import com.politrons.model.entities.CopyWriter
import com.politrons.model.entities.Editor
import com.politrons.model.entities.Journalist
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.SuggestionId
import com.politrons.reposirory.MagazineRepository

object MockUtils {


    /**
     * I found a nasty bug using Mockk and kotlin inline functions, that's why I implement my own mocks
     */

    class MagazineRepositoryMock(
        private val saveMagazineCreatedEventFunc: (MagazineCreatedEvent) -> IO<MagazineId> = { e -> IO.effect { e.magazineId } },
        private val saveArticleDraftCreatedEventFunc: (ArticleDraftCreatedEvent) -> IO<ArticleId> = { e -> IO.effect { e.article.id } },
        private val saveSuggestionAddedEventFunc: (SuggestionAddedEvent) -> IO<SuggestionId> = { e -> IO.effect { e.suggestion.id } },

        ) : MagazineRepository {

        override fun saveMagazineCreatedEvent(event: MagazineCreatedEvent): IO<MagazineId> {
            return saveMagazineCreatedEventFunc(event)
        }

        override fun saveArticleDraftCreatedEvent(event: ArticleDraftCreatedEvent): IO<ArticleId> {
            return saveArticleDraftCreatedEventFunc(event)
        }

        override fun saveSuggestionAddedEvent(event: SuggestionAddedEvent): IO<SuggestionId> {
            return saveSuggestionAddedEventFunc(event)
        }
    }

    class EditorDAOMock(
        val findFunc: (String) -> IO<Editor> = { id -> IO.effect { (Editor(id)) } }
    ) : EditorDAO {

        override fun findById(id: String): IO<Editor> {
            return findFunc(id)
        }
    }

    class JournalistDAOMock(
        val saveFunc: (Journalist) -> IO<Journalist> = { j -> IO.effect { j } },
        val findFunc: (String) -> IO<Journalist> = { id -> IO.effect { Journalist(id, emptyList()) } }
    ) : JournalistDAO {

        override fun findById(id: String): IO<Journalist> {
            return findFunc(id)
        }

        override fun save(journalist: Journalist): IO<Journalist> {
            return saveFunc(journalist)
        }
    }

    class CopyWriterDAOMock(
        val saveFunc: (CopyWriter) -> IO<CopyWriter> = { j -> IO.effect { j } },
        val findFunc: (String) -> IO<CopyWriter> = { id -> IO.effect { CopyWriter(id, emptyList()) } }
    ) : CopyWriterDAO {

        override fun findById(id: String): IO<CopyWriter> {
            return findFunc(id)
        }

        override fun save(copyWriter: CopyWriter): IO<CopyWriter> {
            return saveFunc(copyWriter)
        }
    }

}