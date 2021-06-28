package com.politrons.resources

import arrow.core.Either
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.handleError
import com.fasterxml.jackson.databind.SerializationFeature
import com.politrons.command.impl.AddDraftCommand
import com.politrons.command.impl.AddSuggestionCommand
import com.politrons.command.impl.CreateMagazineCommand
import com.politrons.dao.impl.CopyWriterDAOImpl
import com.politrons.dao.impl.EditorDAOImpl
import com.politrons.dao.impl.JournalistDAOImpl
import com.politrons.events.MagazineEvent
import com.politrons.handler.AddDraftCommandHandler
import com.politrons.handler.AddSuggestionCommandHandler
import com.politrons.handler.CreateMagazineCommandHandler
import com.politrons.handler.impl.AddDraftCommandHandlerImpl
import com.politrons.handler.impl.AddSuggestionCommandHandlerImpl
import com.politrons.handler.impl.CreateMagazineCommandHandlerImpl
import com.politrons.model.Magazine
import com.politrons.model.valueObjects.ArticleId
import com.politrons.model.valueObjects.MagazineId
import com.politrons.model.valueObjects.SuggestionId
import com.politrons.reposirory.impl.MagazineRepositoryImpl
import com.politrons.query.MagazineEventHandler
import com.politrons.query.impl.MagazineEventHandlerImpl
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.channels.Channel
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class MagazineServer(
    private val magazineEventHandler: MagazineEventHandler,
    private val createMagazineCommandHandler: CreateMagazineCommandHandler,
    private val addDraftCommandHandler: AddDraftCommandHandler,
    private val addSuggestionCommandHandler: AddSuggestionCommandHandler
) {

    private val logger: Logger = LoggerFactory.getLogger(MagazineServer::class.java)

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val eventBus: Channel<MagazineEvent> = Channel()
            val eventHandler = MagazineEventHandlerImpl(eventBus)
            val magazineRepository = MagazineRepositoryImpl(eventBus)

            val createMagazineCommandHandler =
                CreateMagazineCommandHandlerImpl(magazineRepository, EditorDAOImpl())

            val addDraftCommandHandler =
                AddDraftCommandHandlerImpl(magazineRepository, JournalistDAOImpl(), CopyWriterDAOImpl())

            val addSuggestionCommandHandler =
                AddSuggestionCommandHandlerImpl(magazineRepository, CopyWriterDAOImpl())

            MagazineServer(
                eventHandler,
                createMagazineCommandHandler,
                addDraftCommandHandler,
                addSuggestionCommandHandler
            ).initServer()
        }
    }

    private fun initServer() {
        val server: NettyApplicationEngine = embeddedServer(Netty, port = 8080) {
            install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }
            loadRouting()
        }
        server.start(wait = true)
    }

    @OptIn(KtorExperimentalAPI::class)
    private fun Application.loadRouting() {
        routing {
            get("/magazine/{magazineId}") {
                logger.debug("Get magazine request.")
                val magazineId = call.parameters.getOrFail("magazineId")
                    ?: throw InternalError("Id for Magazine is not include in request")
                val getMagazineProgram: IO<Either<Throwable, Magazine>> =
                    IO.fx {
                        Either.right(!magazineEventHandler.get(magazineId))
                    }.handleError { t ->
                        logger.error("Error obtaining magazine. Caused by ${ExceptionUtils.getStackTrace(t)}")
                        Either.left(t)
                    }
                renderResponse(getMagazineProgram)
            }
            post("/magazine/") {
                logger.debug("Create magazine request")
                val createMagazineCommand = kotlin.runCatching { call.receive<CreateMagazineCommand>() }
                val createMagazineProgram = IO.fx {
                    val magazineId: MagazineId = !createMagazineCommandHandler.createMagazine(createMagazineCommand.getOrThrow())
                    Either.right(magazineId.value)
                }.handleError { t ->
                    logger.error("Error creating magazine. Caused by ${ExceptionUtils.getStackTrace(t)}")
                    Either.left(t)
                }
                renderResponse(createMagazineProgram)
            }
            post("/magazine/article/") {
                logger.debug("Create Article request")
                val addDraftCommand = kotlin.runCatching { call.receive<AddDraftCommand>() }
                val addDraftProgram = IO.fx {
                    val articleId: ArticleId = !addDraftCommandHandler.addDraft(addDraftCommand.getOrThrow())
                    Either.right(articleId.value)
                }.handleError { t ->
                    logger.error("Error adding article. Caused by ${ExceptionUtils.getStackTrace(t)}")
                    Either.left(t)
                }
                renderResponse(addDraftProgram)
            }
            post("/magazine/article/suggestion") {
                logger.debug("Create Suggestion request")
                val addSuggestionCommand = kotlin.runCatching { call.receive<AddSuggestionCommand>() }
                val addSuggestionProgram = IO.fx {
                    val suggestionId: SuggestionId = !addSuggestionCommandHandler.addSuggestion(addSuggestionCommand.getOrThrow())
                    Either.right(suggestionId.value)
                }.handleError { t ->
                    logger.error("Error adding suggestion. Caused by ${ExceptionUtils.getStackTrace(t)}")
                    Either.left(t)
                }
                renderResponse(addSuggestionProgram)
            }
        }
    }

    private suspend fun <T : Any> PipelineContext<Unit, ApplicationCall>.renderResponse(
        program: IO<Either<Throwable, T>>
    ) {
        when (val response: Either<Throwable, T> = program.unsafeRunSync()) {
            is Either.Left -> call.respond(
                HttpStatusCode.InternalServerError,
                response.a.message ?: "Internal server error"
            )
            is Either.Right -> call.respond(response.b)
        }
    }
}