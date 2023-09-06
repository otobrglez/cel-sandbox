with (import <nixpkgs> {});

mkShell {
  name = "cel-sandbox";
  buildInputs = [
    jdk17_headless
    sbt
  ];
  shellHook = ''
    export CEL_SANDBOX_HOME=`pwd`
    export JAVA_HOME="${jdk17_headless}"
  '';
}
