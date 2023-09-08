package com.pinkstack.cel.sandbox

object Styles:
  val global: String =
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
      .replaceAll("\n", " ")
