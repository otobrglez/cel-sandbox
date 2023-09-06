package com.pinkstack.cel.sandbox

import cats.effect.IO
import org.http4s.{MediaType, Response}
import org.http4s.dsl.io.*
import org.http4s.headers.`Content-Type`
import scalatags.Text
import scalatags.Text.tags2.samp
import scala.jdk.CollectionConverters.*

import java.time.LocalDateTime
type Template = Text.TypedTag[String]

extension (template: Template)
  def renderHtml: IO[Response[IO]] =
    for
      rawHtml  <- IO(template.render)
      response <- Ok("<!DOCTYPE html>" + rawHtml, `Content-Type`(MediaType.text.html))
    yield response

object Templates:
  import scalatags.Text.all._
  import scalatags.Text.tags2.{title => htitle}

  private val globalCss: String =
    """
      |*, *:before, *:after { box-sizing: border-box; }
      |html, body { margin: 0; padding: 10px;}
      |body,td,th,p { font-family: sans-serif; font-size: 12pt; line-height: 14pt; }
      |input, textarea { font-family:inherit; font-size: inherit;}
      |.title { font-size: 18pt; line-height: 20pt; margin-bottom: 15px}
      |.wrapper { max-width: 960px; display: block; margin: 0 auto; }
      |form { margin-top: 5px; margin-bottom: 5px;}
      |input.cel { padding: 10px; width: 99%; margin: 0 auto; display: block; clear: both; float:none;}
      |.shadow { box-shadow: 2px 2px 2px #e2e2e2; }
      |input.cel { border:1px solid #CCCCCC; border-radius: 4px; margin-bottom: 10px;}
      |button.submit { text-align: right; }
      |.footer { color: #333; margin-top: 20px;}
      |.cel-result { padding: 10px; border: 1px solid #EEE;
      |border-radius: 4px; width: 99%; margin: 0 auto; display: block; clear: both; float:none;}
      |.cel-result.with-error { background-color: lightpink; font-size: 10pt}
      |.render-time { font-size: small; text-align: right; color: #CCC;}""".stripMargin

  def homepage(celInput: String = "", celResultOpt: Option[CelResult] = None): Template =
    html(
      head(
        meta(charset := "utf-8"),
        meta(name    := "viewport", content := "width=device-width, initial-scale=1"),
        htitle("Google CEL Sandbox"),
        tag("style")(globalCss),
        script(src   := "https://unpkg.com/htmx.org@1.9.5")
      ),
      body(
        div(`class`            := "wrapper")(
          div(`class`          := "title", "Google CEL Sandbox"),
          form(
            `id`               := "cel-form",
            attr("hx-post")    := "/",
            attr("hx-select")  := "#cel-form",
            /* attr("hx-target") := "find .cel-form", */
            attr("hx-swap")    := "outerHTML",
            attr("hx-trigger") := "submit",
            attr("tx-on")      := "htmx:afterRequest: console.log('after request')",
            input(
              `class`             := "cel shadow",
              id                  := "celInput",
              name                := "celInput",
              placeholder         := "Write your CEL expression here and press <Enter>",
              autofocus           := "autofocus",
              required            := "required",
              attr("hx-preserve") := "true",
              value               := celInput
            ),
            input(`type`          := "hidden", name := "fromForm", value := "true"),
            div(
              `class`             := "output",
              celResultOpt.fold(pre("")) {
                case Left(value)  =>
                  pre(`class` := "shadow cel-result with-error", value.getMessage)
                case Right(value) =>
                  div(
                    `class` := "shadow cel-result ok",
                    div(
                      `class` := "type-map",
                      h2("Type map"),
                      ul(
                        `class` := "type-map",
                        for (celType <- value.ast.getTypeMap.values().asScala.toSeq)
                          yield li(
                            s"Name: ${celType.name()}, Kind: ${celType.kind()}"
                          )
                      ),
                      h2("Reference Map"),
                      ul(
                        `class` := "reference-map",
                        for (celReference <- value.ast.getReferenceMap.values().asScala.toSeq)
                          yield li(
                            s"Name: ${celReference.name()}, Value: ${celReference.value()}"
                          )
                      ),
                      h2("Expression"),
                      div(
                        `class` := "expr",
                        value.ast.getExpr.toString
                      )
                    )
                  )
              }
            ),
            div(`class`           := "render-time", p(LocalDateTime.now.toString))
          ),
          div(`class`          := "footer", p("Oto Brglez / @otobrglez"))
        )
      )
    )
