package com.pinkstack.cel.sandbox

import scalatags.Text
import scalatags.Text.all.*
import scalatags.Text.tags2.title as htitle
import scalatags.generic.{Attr, Namespace}

import java.time.LocalDateTime
import scala.jdk.CollectionConverters.*

object HTMX:
  enum hx(name: String, namespace: Option[Namespace] = None, raw: Boolean = false) extends Attr(name, namespace, raw):
    case post     extends hx("hx-post")
    case get      extends hx("hx-get")
    case select   extends hx("hx-select")
    case trigger  extends hx("hx-trigger")
    case swap     extends hx("hx-swap")
    case on       extends hx("hx-on")
    case preserve extends hx("hx-preserve")

object Templates:
  import HTMX.hx

  def homepage(celInput: String = "", celResultOpt: Option[CelResult] = None): Template =
    html(
      head(
        meta(charset := "utf-8"),
        meta(name    := "viewport", content := "width=device-width, initial-scale=1"),
        htitle("Google CEL Sandbox"),
        tag("style")(Styles.global),
        script(src   := "https://unpkg.com/htmx.org@1.9.5")
      ),
      body(
        div(`class`    := "wrapper")(
          div(`class`  := "title", "Google CEL Sandbox"),
          form(
            `id`       := "cel-form",
            hx.post    := "/",
            hx.select  := "#cel-form",
            hx.swap    := "outerHTML",
            hx.trigger := "submit",
            input(
              `class`     := "cel shadow",
              id          := "celInput",
              name        := "celInput",
              placeholder := "Write your CEL expression here and press <Enter>",
              autofocus   := "autofocus",
              required    := "required",
              hx.preserve := "true",
              value       := celInput
            ),
            input(`type`  := "hidden", name := "fromForm", value := "true"),
            div(
              `class`     := "output",
              celResultOpt.fold(pre("")) {
                case Left(value)  => pre(`class` := "shadow cel-result with-error", value.getMessage)
                case Right(value) =>
                  div(`class` := "shadow cel-result ok",
                    div(
                      `class` := "type-map",
                      h2("Type map"),
                      ul(
                        `class`   := "type-map",
                        for (celType <- value.ast.getTypeMap.values().asScala.toSeq)
                          yield li(
                            s"Name: ${celType.name()}, Kind: ${celType.kind()}"
                          )
                      ),
                      h2("Reference Map"),
                      ul(
                        `class`   := "reference-map",
                        for (celReference <- value.ast.getReferenceMap.values().asScala.toSeq)
                          yield li(
                            s"Name: ${celReference.name()}, Value: ${celReference.value()}"
                          )
                      ),
                      h2("Expression"),
                      div(`class` := "expr", value.ast.getExpr.toString)
                    )
                  )
              }
            ),
            div(`class`   := "render-time", p(LocalDateTime.now.toString))
          ),
          div(`class`  := "footer", p("Oto Brglez / @otobrglez"))
        )
      )
    )
