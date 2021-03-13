# Stopship
Use a simple annotation to catch enabled debug flags before building

#### Why
Android Lint allows developers to catch forgotten issues or violations when
checking the project by throwing an error when `// STOPSHIP` is contained in
code blocks. This, however, requires the devleoper to run lint which is not always
the case (for better or worse) and always throws an error, regardless of where
it was applied. This is intended to be a more specific usage to catch enabled debug
flags for logging or other behaviors.

#### Usage
Include the jar as an annotation processor and enable the appropriate level.
In `build.gradle`:

```
buildTypes {
  debug {
    javaCompileOptions.annotationProcessorOptions.arguments.put("stopShip", name)
    ...
  }
  release {
    javaCompileOptions.annotationProcessorOptions.arguments.put("stopShip", name)
    ...
  }
}

dependencies {
  annotationProcessor "<packagenametbd>"
}
```
This enables warning logs for debug builds and errors for release builds.

Alternatively, to use it always:
```
javaCompileOptions {
  annotationProcessorOptions {
    arguments = ["stopShip", "release"]
  }
}
```

Flags can be annotated as follows:
```
@StopShip
private static final boolean DBG = false;
```

#### License
MIT
