package com.pinkstack.cel.sandbox

import cats.*
import cats.effect.*
import cats.implicits.*
import com.comcast.ip4s.*
import dev.cel.common.CelAbstractSyntaxTree
import org.http4s.FormDataDecoder.*
import org.http4s.dsl.io.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.{FormDataDecoder, HttpRoutes, Response}
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger}

final case class CelForm(celInput: String, fromForm: String = "true")
object CelForm:
  given mapper: FormDataDecoder[CelForm] = (
    field[String]("celInput"),
    field[String]("fromForm")
  ).mapN(CelForm.apply)

final case class CelOutput(ast: CelAbstractSyntaxTree, debugString: String)
type CelResult = Either[Throwable, CelOutput]

object SandboxServerApp extends IOApp:
  implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]
  private val logger                            = Slf4jLogger.getLogger[IO]

  private val celService = HttpRoutes
    .of[IO] {
      case GET -> Root        => Templates.homepage().renderHtml
      case req @ POST -> Root =>
        for
          celForm   <- req.as[CelForm]
          _         <- logger.info(s"Received cel expression ${celForm.celInput}")
          celResult <- CEL.resource.use { case (compiler, runtime) =>
            for
              result      <- IO(compiler.compile(celForm.celInput))
              ast         <- IO(result.getAst)
              debugString <- IO(result.getDebugString)
            yield Right(CelOutput(ast, debugString))
          }.recoverWith { th =>
            logger.error(s"Failed processing CEL: ${th}") *> IO.pure(Left(th))
          }
          _         <- logger.info(s"Evaluated as $celResult")
          response  <- Templates.homepage(celForm.celInput, Some(celResult)).renderHtml
        yield response
    }
    .orNotFound

  def mkServer(): IO[Unit] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"9099")
      .withHttpApp(celService)
      .build
      .use(_ => IO.never)
      .as(IO.unit)

  def run(args: List[String]): IO[ExitCode] = mkServer().as(ExitCode.Success)
