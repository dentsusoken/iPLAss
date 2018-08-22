/**
 * sass関連
 */

var gulp = require("gulp");
var del = require("del");
var sass = require("gulp-sass");
var plumber = require("gulp-plumber");
var concat = require("gulp-concat");
var runSequence = require("run-sequence");
var config = require("../config").sass;

var flat = config.flat;
var horizontal = config.horizontal;
var vertical = config.vertical;
var errorhtml = config.errorhtml;
var imageColor = config.imageColor;

// sassコンパイル前のクリーン処理
gulp.task("clean_sass", function(callback) {
  return del([
    flat.dest + "/base.css",
    flat.dest + "/clear.css",
    flat.dest + "/module.css",
    flat.dest + "/structure.css",
    flat.dest + "/theme/**/*.css",
    horizontal.dest + "/base.css",
    horizontal.dest + "/clear.css",
    horizontal.dest + "/module.css",
    horizontal.dest + "/structure.css",
    horizontal.dest + "/theme/**/*.css",
    vertical.dest + "/base.css",
    vertical.dest + "/clear.css",
    vertical.dest + "/module.css",
    vertical.dest + "/structure.css",
    vertical.dest + "/theme/**/*.css",
    errorhtml.base.dest + "/styles.css",
    errorhtml.flat.dest + "/styles.css"
  ], callback);
});

// sassのコンパイル
gulp.task("compile_sassImageColor", function() {
  buildImageColor(flat);
  buildImageColor(horizontal);
  buildImageColor(vertical);

  function buildImageColor(skin) {
    imageColor.map(function(color) {
      var src = skin.src.imagecolor + "/" + color + ".scss";
      var fileName = "imagecolor/" + color + ".css"
      var dest = skin.dest;
      gulp.src(src)
        .pipe(plumber())
        .pipe(sass())
        .pipe(concat(fileName))
        .pipe(gulp.dest(dest));
    });
  }
});

gulp.task("compile_sass", function() {
  buildSkin(flat);
  buildSkin(horizontal);
  buildSkin(vertical);

  buildErrorSkin(errorhtml.base);
  buildErrorSkin(errorhtml.flat);

  function buildSkin(skin) {
    buildBase(skin.src.base, skin.dest);
    buildClear(skin.src.clear, skin.dest);
    buildModule(skin.src.module, skin.dest);
    buildStructure(skin.src.structure, skin.dest);
    config.theme.map(function(color){
      buildTheme(skin.src.theme, skin.dest, color);
    });
  }

  //base.css
  function buildBase(src, dest) {
    buildSass(src, dest, "base.css");
  }
  //clear.css
  function buildClear(src, dest) {
    buildSass(src, dest, "clear.css");
  }
  //module.css
  function buildModule(src, dest) {
    buildSass(src, dest, "module.css");
  }
  //structure.css
  function buildStructure(src, dest) {
    buildSass(src, dest, "structure.css");
  }
  //color.css
  function buildTheme(src, dest, color) {
    buildSass(src + "/" + color + ".scss", dest, "theme/" + color + "/color.css")
  }

  function buildErrorSkin(skin) {
    buildSass(skin.src, skin.dest, "styles.css");
  }

  function buildSass(src, dest, fileName) {
    gulp.src(src)
      .pipe(plumber())
      .pipe(sass())
      .pipe(concat(fileName))
      .pipe(gulp.dest(dest));
  }
});

// sassのビルド処理
gulp.task("build_sass", function() {
  runSequence("clean_sass", "compile_sassImageColor", "compile_sass");
});

gulp.task("build_sassImageColor", function() {
  runSequence("compile_sassImageColor");
});
