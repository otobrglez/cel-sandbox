package com.pinkstack.cel.sandbox

import cats.*
import cats.effect.*
import cats.implicits.*
import cats.syntax.all.*
import dev.cel.common.CelVarDecl
import dev.cel.common.types.{SimpleType, StructTypeReference}
import dev.cel.compiler.{CelCompiler, CelCompilerFactory}
import dev.cel.parser.CelStandardMacro
import dev.cel.runtime.{CelRuntime, CelRuntimeFactory}

object CEL:
  import Resource.eval
  private def mkCelCompiler: Resource[IO, CelCompiler] =
    eval(
      IO(
        CelCompilerFactory
          .standardCelCompilerBuilder()
          .setStandardMacros(CelStandardMacro.ALL)
          .addVar("hello", SimpleType.STRING)
          .addVar("name", SimpleType.STRING)
          .addVar("is_true", SimpleType.BOOL)
          .addVar("x", StructTypeReference.create("x"))
          .build()
      )
    )

  private def mkCelRuntime: Resource[IO, CelRuntime] =
    eval(IO(CelRuntimeFactory.standardCelRuntimeBuilder().build()))

  def resource: Resource[IO, (CelCompiler, CelRuntime)] = (mkCelCompiler, mkCelRuntime).parTupled
