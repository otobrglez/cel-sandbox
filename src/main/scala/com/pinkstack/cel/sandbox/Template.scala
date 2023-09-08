package com.pinkstack.cel.sandbox

import cats.effect.IO
import org.http4s.{MediaType, Response}
import org.http4s.dsl.io.*
import org.http4s.headers.`Content-Type`
import scalatags.Text

type Template = Text.TypedTag[String]

extension (template: Template)
  def renderHtml: IO[Response[IO]] =
    IO(template.render).flatMap(raw => Ok("<!DOCTYPE html>" + raw, `Content-Type`(MediaType.text.html)))
