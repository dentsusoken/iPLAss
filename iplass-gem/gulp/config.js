module.exports = {
  sass: {
    flat: {
      src: {
        base: "src/main/sass/flat/base/base.scss",
        clear: "src/main/sass/flat/clear/clear.scss",
        module: "src/main/sass/flat/module/module.scss",
        structure: "src/main/sass/flat/structure/structure.scss",
        imagecolor: "src/main/sass/flat/structure/imagecolor",
        theme: "src/main/sass/flat/theme"
      },
      dest: "src/main/resources/META-INF/resources/styles/gem/skin/flat"
    },
    horizontal: {
      src: {
        base: "src/main/sass/horizontal/base/base.scss",
        clear: "src/main/sass/horizontal/clear/clear.scss",
        module: "src/main/sass/horizontal/module/module.scss",
        structure: "src/main/sass/horizontal/structure/structure.scss",
        imagecolor: "src/main/sass/horizontal/structure/imagecolor",
        theme: "src/main/sass/horizontal/theme"
      },
      dest: "src/main/resources/META-INF/resources/styles/gem/skin/horizontal"
    },
    vertical: {
      src: {
        base: "src/main/sass/vertical/base/base.scss",
        clear: "src/main/sass/vertical/clear/clear.scss",
        module: "src/main/sass/vertical/module/module.scss",
        structure: "src/main/sass/vertical/structure/structure.scss",
        imagecolor: "src/main/sass/vertical/structure/imagecolor",
        theme: "src/main/sass/vertical/theme"
      },
      dest: "src/main/resources/META-INF/resources/styles/gem/skin/vertical"
    },
    theme: ["black", "blue", "green", "lightblue", "lightgreen", "lightred", "orange", "red"],
    imageColor: ["blue", "green", "yellow", "red"],
    errorhtml: {
      base: {
        src: "src/main/sass/error/base/styles.scss",
        dest: "src/main/resources/META-INF/resources/errorhtml/styles/base"
      },
      flat: {
        src: "src/main/sass/error/flat/styles.scss",
        dest: "src/main/resources/META-INF/resources/errorhtml/styles/flat"
      }
    }
  }
};
