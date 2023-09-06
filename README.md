# CEL Sandbox

This is just my sandbox for [CEL - Common Expression Language](https://github.com/google/cel-spec).

## Development

The easiest way to try this thing is to use [Nix Shell][nix-shell] and within it `sbt run`.

This will boot-up the service on [localhost:9099](http://localhost:9099)

```bash
nix-shell ./shell.nix --run "sbt run"
```

For faster development use [sbt-revolver](https://github.com/spray/sbt-revolver) with

```bash
nix-shell ./shell.nix --run "sbt ~reStart"
```

## Author

[Oto Brglez](https://github.com/otobrglez)

[nix-shell]: https://nixos.org/manual/nix/stable/command-ref/nix-shell

### P.s.:

This project has nothing to do with Google. All Google stuff is from Google. Use at your own will.

